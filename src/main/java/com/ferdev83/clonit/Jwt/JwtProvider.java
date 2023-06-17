package com.ferdev83.clonit.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class JwtProvider {
    @Autowired
    private JwtEncoder jwtEncoder; //this class firm the token
    @Value("${jwt.expiration.time}")
    //@Value("90000")
    private Long jwtTokenExpirationTime;

    public String generateToken(Authentication authentication) {
        User principal= (User) authentication.getPrincipal();
        return generateTokenWithUsername(principal.getUsername());
    }

    private String generateTokenWithUsername(String username) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtTokenExpirationTime))
                .subject(username)
                .claim("scope", "ROLE_USER")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Long getJwtTokenExpirationTime() {
        return jwtTokenExpirationTime;
    }
}
