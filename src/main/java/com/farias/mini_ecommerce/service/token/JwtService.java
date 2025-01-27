package com.farias.mini_ecommerce.service.token;

import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${security.token.secret}")
    private String secretKey;

    public String generateToken(UUID userId, Map<String, Object> claims) {
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
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

        return jwtBuilder.sign(algorithm);
    }
}

