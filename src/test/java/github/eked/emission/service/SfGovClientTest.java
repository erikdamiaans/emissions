package github.eked.emission.service;

import github.eked.emission.bean.Emission;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class SfGovClientTest {
    private static final String BASE_URL = "https://data.sfgov.org/resource/pxac-sadh.json";


    private SfGovClient sfGovClient(String whereClause) {
        return new SfGovClient(BASE_URL, whereClause);
    }

    @Test
    public void sourceTypeNull() {
        SfGovClient sfGovClientWhereEmissionNotZero = sfGovClient("emissions_mtco2e!=0.0");

        ResponseEntity<List<Emission>> emissions = sfGovClientWhereEmissionNotZero
                .getEmissions("Police", null);
        Assert.assertFalse(emissions.getBody().isEmpty());
        long differentSourceTypesCount = emissions.getBody().stream().map(Emission::getSource).distinct().count();
        Assert.assertTrue(differentSourceTypesCount > 1);
    }

    @Test
    public void emissionsWhereClauseIsUsed() {
        String department = "SF Unified School District";
        String sourceType = "Natural Gas";

        SfGovClient sfGovClientWhereEmissionNotZero = sfGovClient("emissions_mtco2e!=0.0");

        ResponseEntity<List<Emission>> emissions = sfGovClientWhereEmissionNotZero
                .getEmissions(department, sourceType);
        System.out.println("emissions Natural Gas size= " + emissions.getBody().size());
        emissions.getBody().stream().map(Emission::getCo2EmissionDouble).forEach(e -> Assert.assertNotEquals(0.0, e, 0.0));

        SfGovClient sfGovClientNoWhereClause = sfGovClient(null);
        emissions = sfGovClientNoWhereClause
                .getEmissions(department, sourceType);
        System.out.println("emissions Natural Gas size= " + emissions.getBody().size());
        Assert.assertTrue(
                emissions.getBody().stream().map(Emission::getCo2EmissionDouble).anyMatch(e -> e == 0.0));
    }
}
