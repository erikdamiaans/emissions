package github.eked.emission.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Slf4j
@Service
public class TokenService {

    public static final String HEADER_TOKEN = "x-access-token";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(HMAC512(secret.getBytes()));
        log.info("doGenerateToken " + token);
        return token;
    }
    public Boolean validateToken(String token, UserDetails userDetails) {

        return userDetails!=null && getUsernameFromToken(token).equals(userDetails.getUsername())
                && !getExpirationDateFromToken(token).before(new Date());
    }
    public String getUsernameFromToken(String token) {
        return decodedToken(token).getSubject();
    }
    public Date getExpirationDateFromToken(String token) {
        return decodedToken(token).getExpiresAt();
    }


    private DecodedJWT decodedToken(String token) {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token);
    }



}
