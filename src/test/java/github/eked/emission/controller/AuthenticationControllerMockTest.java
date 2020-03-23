package github.eked.emission.controller;

import github.eked.emission.service.TokenService;
import github.eked.emission.service.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthenticationController.class)
public class AuthenticationControllerMockTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private TokenService tokenService;
    @Mock
    private UserDetails userDetails;

    @Test
    public void loginRightCredentials() throws Exception {
        given(tokenService.generateToken(Mockito.any(UserDetails.class)))
                .willReturn("thetoken");
        when(userDetails.getUsername()).thenReturn("bla");
        when(userDetails.getPassword()).thenReturn("bb");
        when(userDetails.isAccountNonLocked()).thenReturn(true);
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isEnabled()).thenReturn(true);
        when(userDetails.isCredentialsNonExpired()).thenReturn(true);
        given(userDetailsService.loadUserByUsername("bla")).willReturn(userDetails);
        this.mockMvc.perform(post("/emissions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"bla\",\"password\":\"bb\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":thetoken}"));
    }

    @Test
    public void loginAccountLockedReturnsHttp_401() throws Exception {
       /* given(tokenService.generateToken(Mockito.any(UserDetails.class)))
                .willReturn("thetoken");*/
        when(userDetails.getUsername()).thenReturn("bla");
        when(userDetails.getPassword()).thenReturn("bb");
        when(userDetails.isAccountNonLocked()).thenReturn(false);
        when(userDetails.isAccountNonExpired()).thenReturn(true);
        when(userDetails.isEnabled()).thenReturn(true);
        when(userDetails.isCredentialsNonExpired()).thenReturn(true);
        given(userDetailsService.loadUserByUsername("bla")).willReturn(userDetails);
        this.mockMvc.perform(post("/emissions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"bla\",\"password\":\"bb\"}"))
                .andExpect(status().isUnauthorized());

    }
    @Test
    public void userDetailsNotFoundReturnsHttp_401() throws Exception {
       /* given(tokenService.generateToken(Mockito.any(UserDetails.class)))
                .willReturn("thetoken");*/

        given(userDetailsService.loadUserByUsername("bla")).willThrow(UsernameNotFoundException.class);
        this.mockMvc.perform(post("/emissions/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"bla\",\"password\":\"bb\"}"))
                .andExpect(status().isUnauthorized());

    }
}
