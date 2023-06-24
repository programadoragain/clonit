package com.ferdev83.clonit.Services;

import com.ferdev83.clonit.Entities.RefreshToken;
import com.ferdev83.clonit.Repositories.RefreshTokenRepository;
import org.antlr.v4.runtime.Lexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken= new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
