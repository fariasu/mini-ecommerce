package com.farias.mini_ecommerce.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtProvider {

    @Value("${security.token.secret}")
    private String secretKey;

    public String validateToken(String token) {
        token = token.replace("Bearer ", "");

        var algorithm = Algorithm.HMAC256(secretKey);

        try{
            return JWT.require(algorithm).build().verify(token).getSubject();

        } catch (JWTVerificationException e) {
            return "";
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
