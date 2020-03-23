package github.eked.emission.controller;

import github.eked.emission.bean.AverageEmission;
import github.eked.emission.service.EmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//http://localhost:8081/emissions/
//http://localhost:8081/emissions?department=Police&sourceType=Propane
//http://localhost:8081/emissions?department=Status of Women&sourceType=Natural Gas
// curl  -u admin:admin 'http://localhost:8081/emissions?sourceType=Propane&department=Police'
//if you don't put it in quotes it loses the query param after the &
//but quotes don't help you with blanks: gives you http version not supported
//curl  -G -v  -u admin:admin http://localhost:8081/emissions --data-urlencode "department=Status of Women" --data-urlencode "sourceType=Natural Gas"
//
//-v means verbose
//-G is necessary to add the query params

@RestController
@RequestMapping(path = "/emissions")
@Slf4j
public class EmissionController {

    private EmissionService emissionService;

    public EmissionController(EmissionService emissionService) {
        this.emissionService = emissionService;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AverageEmission> getEmissions(
            @RequestParam(required = false, value = "department") String department,
            @RequestParam(required = false, value = "sourceType") String sourceType
    ) {

        log.info("sourcetype " + sourceType + " department " + department);
        List<AverageEmission> emissions = emissionService.getEmissions(department, sourceType);
        log.info("EmissionController returning {} items for {} and {} ",emissions.size(),department,sourceType);
        return emissions;
    }
}
