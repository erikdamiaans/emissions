package github.eked.emission.service;

import github.eked.emission.bean.Department;
import github.eked.emission.bean.Emission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
/**
 * Rest client of sfgov web service
 * Make it implement an interface so you can swap it for a db
 */
//https://www.baeldung.com/restclienttest-in-spring-boot
public class SfGovClient {


    private String baseUrl;

    public SfGovClient(@Value("${emission.backend.endpoint}") String endPoint) {
        this.baseUrl = endPoint;

    }


    public ResponseEntity<List<Emission>> getEmissions(String whereClause, String department, String sourceType) {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(urlEmissions(whereClause, department, sourceType),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    public ResponseEntity<List<Department>> getDepartments() {
        return new RestTemplate().exchange(urlDepartments(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
    }

    private String urlDepartments() {
        String uri = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
                .queryParam("$select", "distinct department")
                .queryParam("$order", "department")
                .build()
                .toUriString();
        log.info("urlDepartments {} ", uri);
        return uri;
    }

    private String urlEmissions(String whereClause, String department, String sourcetype) {
        String urlSvGof = Optional.ofNullable(whereClause).filter(s -> !s.isEmpty())
                .map(where -> String.format("%s?$where=%s", baseUrl, where)).orElse(baseUrl);
        log.info(" url {} ", urlSvGof);
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("$select", "department,source_type, emissions_mtco2e");//don't add distinct, it will mess up the average
        Optional.ofNullable(department).filter(s -> !s.isEmpty()).ifPresent(s -> params.add("department", s));
        Optional.ofNullable(sourcetype).filter(s -> !s.isEmpty()).ifPresent(s -> params.add("source_type", s));

        String uri = UriComponentsBuilder.fromHttpUrl(urlSvGof)
                .queryParams(params)
                .build()
                .toUriString();
        log.info(" UriComponentsBuilder uri {} ", uri);
        return uri;
    }


}
