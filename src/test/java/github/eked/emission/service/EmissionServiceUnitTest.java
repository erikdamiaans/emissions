package github.eked.emission.service;

import github.eked.emission.bean.AverageEmission;
import github.eked.emission.bean.Emission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class EmissionServiceUnitTest {
    public static final String WHERE_CLAUSE = "emissions_mtco2e!=0.0";
    private static String DEPARTMENT = "dep1";
    private static String SOURCE = "s1";
    private static String SOURCE_2 = "s2";
    private static ResponseEntity<List<Emission>> SAME_DEPARTMENT_AND_SOURCETYPE = new ResponseEntity<>(
            Arrays.asList(
                    new Emission(DEPARTMENT, SOURCE, "45.0"),
                    new Emission(DEPARTMENT, SOURCE, "10.0")),
            null, HttpStatus.OK);

    private static ResponseEntity<List<Emission>> SAME_DEPARTMENT_DIFFERENT_SOURCETYPES = new ResponseEntity<>(
            Arrays.asList(
                    new Emission(DEPARTMENT, SOURCE, "45.0"),
                    new Emission(DEPARTMENT, SOURCE, "10.0"),
                    new Emission(DEPARTMENT, SOURCE_2, "900.0")),
            null, HttpStatus.OK);
    EmissionService emissionService;
    @Mock
    SfGovClient sfGovClient;

    @Before
    public void setUp()  {
        initMocks(this);
        emissionService = new EmissionService(sfGovClient);
    }

    @Test
    public void averageSameSourceTypes() {

        when(sfGovClient.getEmissions(WHERE_CLAUSE,DEPARTMENT, SOURCE))
                .thenReturn(SAME_DEPARTMENT_AND_SOURCETYPE);

        List<AverageEmission> emissions = emissionService.getEmissions(DEPARTMENT, SOURCE);

        Assert.assertEquals(1, emissions.size());
        Assert.assertEquals(27.5, emissions.get(0).getCo2Emission(), 0.01);
        Assert.assertEquals(DEPARTMENT, emissions.get(0).getDepartment());
        Assert.assertEquals(SOURCE, emissions.get(0).getSourceType());
    }

    @Test
    public void averageDifferentSourceTypes() {

        when(sfGovClient.getEmissions(WHERE_CLAUSE,DEPARTMENT, null))
                .thenReturn(SAME_DEPARTMENT_DIFFERENT_SOURCETYPES);

        List<AverageEmission> emissions = emissionService.getEmissions(DEPARTMENT, null);

        Assert.assertEquals(2, emissions.size());

        AverageEmission averageEmission = emissions.stream().filter(e -> e.getSourceType().equals(SOURCE)).findAny().get();
        Assert.assertEquals(27.5, averageEmission.getCo2Emission(), 0.01);
        Assert.assertEquals(DEPARTMENT, averageEmission.getDepartment());
        Assert.assertEquals(SOURCE, averageEmission.getSourceType());

        averageEmission = emissions.stream().filter(e -> e.getSourceType().equals(SOURCE_2)).findAny().get();
        Assert.assertEquals(900.0, averageEmission.getCo2Emission(), 0.01);
        Assert.assertEquals(DEPARTMENT, averageEmission.getDepartment());
        Assert.assertEquals(SOURCE_2, averageEmission.getSourceType());

    }
}
