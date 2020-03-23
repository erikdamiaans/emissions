package github.eked.emission.controller;

import github.eked.emission.bean.AverageEmission;
import github.eked.emission.repo.ApplicationUserRepoImpl;
import github.eked.emission.service.EmissionService;
import github.eked.emission.service.TokenService;
import github.eked.emission.service.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmissionController.class)
@Import(value={UserDetailsServiceImpl.class,ApplicationUserRepoImpl.class, TokenService.class})

public class EmissionControllerTest {
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpYm1fdXNlciIsImV4cCI6MTU4NTU4N" +
            "Tc0M30.57-kDKJR2udVpMJVPcZt--3Xt1oA4Y17-q3gqo0xkzl1k_vggsQEvlp7v1tUdzrR-FoTKj9Tc5ZfwY8bnzU1uw";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmissionService emissionService;

    @Test
    @WithMockUser(username = "ester", roles = {"ADMIN"})
    public void averageTestAuhtenticated() throws Exception {

        List<AverageEmission> averageEmissions = Arrays.asList(new AverageEmission("police", "petrol",465.54));
        given(emissionService.getEmissions("police", "petrol")).willReturn(averageEmissions);

        // when + then
        this.mockMvc.perform(get("/emissions?department=police&sourceType=petrol"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[{'department': police, 'sourceType': petrol, 'co2Emission': 465.54}]"));
    }

}
