package com.farias.mini_ecommerce.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtTokenProvider {

    @Value("${security.token.secret}")
    private String secretKey;

    public TokenResponse generateToken(UUID userId, Map<String, Object> claims) {
        var expiresIn = Instant.now().plus(Duration.ofMinutes(60));
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String issuer = "ECOMMERCE";
        var jwtBuilder = JWT.create()
                .withIssuer(issuer)
                .withSubject(userId.toString())
                .withExpiresAt(expiresIn);

        claims.forEach((key, value) -> {
            if (value instanceof String[]) {
                jwtBuilder.withArrayClaim(key, (String[]) value);
            } else {
                jwtBuilder.withClaim(key, value.toString());
            }
        });

        return new TokenResponse(jwtBuilder.sign(algorithm), expiresIn);
    }

    public String validateToken(String token) {
        token = token.replace("Bearer ", "");

        var algorithm = Algorithm.HMAC256(secretKey);

        try{
            return JWT.require(algorithm).build().verify(token).getSubject();

        } catch (JWTVerificationException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        DecodedJWT decodedToken = JWT.decode(token);
        List<GrantedAuthority> authorities = new ArrayList<>();

        var rolesClaim = decodedToken.getClaim("roles");

        if (!rolesClaim.isNull()) {
            List<String> roles = rolesClaim.asList(String.class);
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }

        return authorities;
    }
}
