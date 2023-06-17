package com.ferdev83.clonit.Services;

import com.ferdev83.clonit.Dtos.AuthenticationResponse;
import com.ferdev83.clonit.Dtos.LoginRequest;
import com.ferdev83.clonit.Dtos.RefreshTokenRequest;
import com.ferdev83.clonit.Dtos.RegisterRequest;
import com.ferdev83.clonit.Entities.NotificationEmail;
import com.ferdev83.clonit.Entities.User;
import com.ferdev83.clonit.Entities.VerificationToken;
import com.ferdev83.clonit.Jwt.JwtProvider;
import com.ferdev83.clonit.Repositories.UserRepository;
import com.ferdev83.clonit.Repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    public void signup(RegisterRequest registerRequest) {
        User user= new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token= generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("-- Account Activation --", user.getEmail(), "Thank you for signing up to Clonit Platform, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/authentication/account-verification/" + token));
    }
    private String generateVerificationToken(User user) {
        String token= UUID.randomUUID().toString();
        VerificationToken verificationToken= new VerificationToken(token,user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new RuntimeException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username= verificationToken.getUser().getUsername();
        User user= userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate); //
        String token= jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtTokenExpirationTime()))
                .username(loginRequest.getUsername())
                .build();
    }

    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new RuntimeException("User name not found - " + principal.getSubject()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
