package github.eked.emission.service;

import github.eked.emission.bean.Department;
import github.eked.emission.bean.Emission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class SfGovClientTest {
    private static final String BASE_URL = "https://data.sfgov.org/resource/pxac-sadh.json";
    private SfGovClient sfGovClient;
    @Before
    public void setUp() throws Exception {
        this.sfGovClient=new SfGovClient(BASE_URL);
    }


    @Test
    public void departments() {
        ResponseEntity<List<Department>> departments = sfGovClient.getDepartments();
        Assert.assertFalse(departments.getBody().isEmpty());
        System.out.println(" departments.getBody().size() "+departments.getBody().size());
        departments.getBody().stream().map(Department::getDepartment).forEach(Assert::assertNotNull);
    }

    @Test
    public void sourceTypeNull() {


        ResponseEntity<List<Emission>> emissions = sfGovClient
                .getEmissions("emissions_mtco2e!=0.0","Police", null);
        Assert.assertFalse(emissions.getBody().isEmpty());
        long differentSourceTypesCount = emissions.getBody().stream().map(Emission::getSource).distinct().count();
        Assert.assertTrue(differentSourceTypesCount > 1);
    }

    @Test
    public void emissionsWhereClauseIsUsed() {
        String department = "SF Unified School District";
        String sourceType = "Natural Gas";

        ResponseEntity<List<Emission>> emissions = sfGovClient
                .getEmissions("emissions_mtco2e!=0.0",department, sourceType);
        System.out.println("emissions Natural Gas size= " + emissions.getBody().size());
        emissions.getBody().stream().map(Emission::getCo2EmissionDouble).forEach(e -> Assert.assertNotEquals(0.0, e, 0.0));


        emissions = sfGovClient
                .getEmissions(null,department, sourceType);
        System.out.println("emissions Natural Gas size= " + emissions.getBody().size());
        Assert.assertTrue(
                emissions.getBody().stream().map(Emission::getCo2EmissionDouble).anyMatch(e -> e == 0.0));
    }
}
