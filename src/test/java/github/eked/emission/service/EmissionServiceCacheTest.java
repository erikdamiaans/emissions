package github.eked.emission.service;

import github.eked.emission.bean.AverageEmission;
import github.eked.emission.bean.Emission;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EmissionServiceCacheTest.TestConfig.class)

public class EmissionServiceCacheTest {
    private static String DEPARTMENT = "dep1";
    private static String SOURCE = "s1";
    private static ResponseEntity<List<Emission>> RESPONSE_OK = new ResponseEntity<>(
            Arrays.asList(
                    new Emission(DEPARTMENT, SOURCE, "45.0"),
                    new Emission(DEPARTMENT, SOURCE, "10.0")),
            null, HttpStatus.OK);
    private static ResponseEntity<List<Emission>> RESPONSE_FAILURE = new ResponseEntity<>(null,
            null, HttpStatus.FORBIDDEN);
    private static ResponseEntity<List<Emission>> RESPONSE_FAILURE_EMPTY = new ResponseEntity<>(emptyList(),
            null, HttpStatus.BAD_REQUEST);

    @Autowired
    EmissionService emissionService;
    @MockBean
    SfGovClient sfGovClient;





    @Test
    public void nullOrEmptyValuesAreNotCached() {

        given(sfGovClient.getEmissions(DEPARTMENT, SOURCE))
                .willReturn(RESPONSE_FAILURE, RESPONSE_OK, RESPONSE_OK, RESPONSE_FAILURE_EMPTY, RESPONSE_OK);

        System.out.println("...........first: failure");
        List<AverageEmission> emissions = emissionService.getEmissions(DEPARTMENT, SOURCE);
        System.out.println("...........second: ok ");
        Assert.assertEquals(0, emissions.size());
        emissions = emissionService.getEmissions(DEPARTMENT, SOURCE);
        System.out.println("...........third: ok");
        Assert.assertEquals(1, emissions.size());
        emissions = emissionService.getEmissions(DEPARTMENT, SOURCE);
        System.out.println("...........fourth: failure");
        Assert.assertEquals(1, emissions.size());
        emissions = emissionService.getEmissions(DEPARTMENT, SOURCE);
        System.out.println("...........fifth: ok");
        Assert.assertEquals(1, emissions.size());

        //2 webservice calls: first one that failed, second one that was ok, all the others should use cache
        verify(sfGovClient, Mockito.times(2)).getEmissions(DEPARTMENT, SOURCE);

    }

    @TestConfiguration
    @EnableCaching
    @Import(EmissionService.class)
    @AutoConfigureCache
    protected static class TestConfig {

    }
}
