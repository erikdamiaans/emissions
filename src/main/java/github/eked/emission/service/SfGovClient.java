package github.eked.emission.service;

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


    private String urlSvGof;

    public SfGovClient(@Value("${emission.backend.endpoint}") String endPoint,
                       @Value("${emission.default.whereclause}") String whereClause) {
        this.urlSvGof =  Optional.ofNullable(whereClause).filter(s -> !s.isEmpty())
                .map(where-> String.format("%s?$where=%s", endPoint,where)).orElse(endPoint);
        log.info(" url {} ", urlSvGof);
    }



    public ResponseEntity<List<Emission>> getEmissions(String department, String sourceType) {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url(department,sourceType),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
    }

    private String url(String department,String sourcetype){
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Optional.ofNullable(department).filter(s->!s.isEmpty()).ifPresent(s->params.add("department",s));
        Optional.ofNullable(sourcetype).filter(s->!s.isEmpty()).ifPresent(s->params.add("source_type",s));

        String uri = UriComponentsBuilder.fromHttpUrl(urlSvGof)
                .queryParams(params)
                .build()
                .toUriString();
        log.info(" UriComponentsBuilder uri {} ", uri);
        return uri;
    }




}
