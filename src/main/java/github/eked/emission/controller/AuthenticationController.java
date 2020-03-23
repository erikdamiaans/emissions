package github.eked.emission.controller;

import github.eked.emission.service.TokenService;
import github.eked.emission.service.UserDetailsServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(path = "/emissions")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

 /*   @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        log.info(" login reached " + jwtRequest);
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

        JwtResponse body = new JwtResponse(tokenService
                .generateToken(userDetailsService
                        .loadUserByUsername(jwtRequest.getUsername())));
        return ResponseEntity.ok().header("x-access-token",body.jwttoken).body("{\"name\":\"yougotit\"}");
    }*/

    /**
     * token is supplied in the response body
     * @param jwtRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        log.info(" login reached " + jwtRequest);
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

        return ResponseEntity.ok(new JwtResponse(tokenService
                .generateToken(userDetailsService
                        .loadUserByUsername(jwtRequest.getUsername()))));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }

    public static class JwtResponse {
        private final String jwttoken;

        public JwtResponse(String jwttoken) {
            this.jwttoken = jwttoken;
        }

        public String getToken() {
            return this.jwttoken;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor//need default constructor for JSON Parsing
    static class JwtRequest {
        private String username;
        private String password;
    }
}
