package github.eked.emission.bean;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Emission {
    private String department;
    private String source;
    private String co2Emission;


    public String getDepartment() {
        return department;
    }


    @JsonGetter(value = "source_type")
    public String getSource() {
      //  log.info("getSource {} ", source);
        return source;
    }


    @JsonGetter(value = "emissions_mtco2e")
    public String getCo2Emission() {
        return co2Emission;
    }

    public Double getCo2EmissionDouble() {
        return co2Emission == null ? null : Double.parseDouble(co2Emission);
    }
}
