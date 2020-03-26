package github.eked.emission.service;

import github.eked.emission.bean.AverageEmission;
import github.eked.emission.bean.Department;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EmissionServiceCacheTest.TestConfig.class)

public class EmissionServiceCacheTest {
    private static String DEPARTMENT = "dep1";
    private static String SOURCE = "s1";


    @Autowired
    EmissionService emissionService;
    @MockBean
    SfGovClient sfGovClient;

    @Test
    public void nullOrEmptyDepartmentsAreNotCached() {
        ResponseEntity<List<Department>> responseOk = new ResponseEntity<>(
                Arrays.asList(
                        new Department(DEPARTMENT),
                        new Department(DEPARTMENT+1)),
                null, HttpStatus.OK);
        ResponseEntity<List<Department>> responseFailure = new ResponseEntity<>(null,
                null, HttpStatus.FORBIDDEN);
        ResponseEntity<List<Department>> responseFailreEmpty = new ResponseEntity<>(emptyList(),
                null, HttpStatus.BAD_REQUEST);
        given(sfGovClient.getDepartments())
                .willReturn(responseFailure, responseOk, responseOk, responseFailreEmpty, responseOk);

        System.out.println("...........first: failure");
        List<Department> departments = emissionService.getDepartments();
        System.out.println("...........second: ok ");
        Assert.assertEquals(0, departments.size());
        departments = emissionService.getDepartments();
        System.out.println("...........third: ok");
        Assert.assertEquals(2, departments.size());
        departments = emissionService.getDepartments();
        System.out.println("...........fourth: failure");
        Assert.assertEquals(2, departments.size());
        departments = emissionService.getDepartments();
        System.out.println("...........fifth: ok");
        Assert.assertEquals(2, departments.size());

        //2 webservice calls: first one that failed, second one that was ok, all the others should use cache
        verify(sfGovClient, Mockito.times(2)).getDepartments();
    }

    @Test
    public void nullOrEmptyEmissionsAreNotCached() {
          ResponseEntity<List<Emission>> emResponseOk = new ResponseEntity<>(
                Arrays.asList(
                        new Emission(DEPARTMENT, SOURCE, "45.0"),
                        new Emission(DEPARTMENT, SOURCE, "10.0")),
                null, HttpStatus.OK);
   ResponseEntity<List<Emission>> responseFailure = new ResponseEntity<>(null,
                null, HttpStatus.FORBIDDEN);
         ResponseEntity<List<Emission>> responseFailureEmpty = new ResponseEntity<>(emptyList(),
                null, HttpStatus.BAD_REQUEST);
        given(sfGovClient.getEmissions(Mockito.anyString(),eq(DEPARTMENT), eq(SOURCE)))
                .willReturn(responseFailure, emResponseOk, emResponseOk, responseFailureEmpty, emResponseOk);

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
        verify(sfGovClient, Mockito.times(2)).getEmissions(Mockito.anyString(),eq(DEPARTMENT), eq(SOURCE));

    }

    @TestConfiguration
    @EnableCaching
    @Import(EmissionService.class)
    @AutoConfigureCache
    protected static class TestConfig {

    }
}
