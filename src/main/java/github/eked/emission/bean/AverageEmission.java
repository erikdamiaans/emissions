package github.eked.emission.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AverageEmission {
    private String department;
    private String sourceType;
    private Double co2Emission;

}
