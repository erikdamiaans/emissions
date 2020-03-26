package github.eked.emission.service;

import github.eked.emission.bean.AverageEmission;
import github.eked.emission.bean.Department;
import github.eked.emission.bean.Emission;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;

/**
 * Business logic
 */

@Slf4j
@Service
public class EmissionService {

    private SfGovClient sfGovClient;

    public EmissionService(@Autowired SfGovClient sfGovClient) {
        this.sfGovClient = sfGovClient;
    }

    @Cacheable(value = "departments", unless = "#result==null or #result.isEmpty()")
    public List<Department> getDepartments() {
        ResponseEntity<List<Department>> responseEntity = sfGovClient.getDepartments();
        log.info("getDepartments status code " + responseEntity.getStatusCode());
        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            return Collections.emptyList();//don't change this, is referenced in the unless condition of the @Cacheable
        }
        return responseEntity.getBody();
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    private static class CompositeKey {
        private String department;
        private String sourceType;
    }


    @Cacheable(value = "emissionsDepartmentSourceType", unless = "#result==null or #result.isEmpty()")
    public List<AverageEmission> getEmissions(String department, String sourceType) {
        ResponseEntity<List<Emission>> responseEntity = sfGovClient.getEmissions("emissions_mtco2e!=0.0",department, sourceType);
        log.info("getEmissions status code " + responseEntity.getStatusCode());
        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            return Collections.emptyList();//don't change this, is referenced in the unless condition of the @Cacheable
        }

        return responseEntity.getBody()
                .stream()
                .collect(groupingBy(e -> new CompositeKey(e.getDepartment(), e.getSource()),
                        averagingDouble(Emission::getCo2EmissionDouble)))
                .entrySet().stream()
                .map(e -> new AverageEmission(e.getKey().department, e.getKey().sourceType, roundthis(e.getValue(),2)))
                .collect(Collectors.toList());

    }

    private double roundthis(double value,int places){
            double scale = Math.pow(10, places);
            return Math.round(value * scale) / scale;
    }
}
