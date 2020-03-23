package github.eked.emission.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG)
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginRightCredentials() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/emissions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"another\",\"password\":\"anotherpw\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("contentAsString = " + contentAsString);
        //.andExpect(content().json("{\"token\":thetoken}"));
    }
    @Test
    public void wrongPasswordReturnsHttp_401() throws Exception {

        this.mockMvc.perform(post("/emissions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"another\",\"password\":\"bb\"}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void userDetailsNotFoundReturnsHttp_401() throws Exception {

        this.mockMvc.perform(post("/emissions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"bla\",\"password\":\"bb\"}"))
                .andExpect(status().isUnauthorized());

    }
}
