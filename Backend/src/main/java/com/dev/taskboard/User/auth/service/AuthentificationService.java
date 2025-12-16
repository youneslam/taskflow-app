package com.dev.taskboard.User.auth.service;

import com.dev.taskboard.User.auth.dtos.AuthentificationRequest;
import com.dev.taskboard.User.auth.dtos.AuthentificationResponse;
import com.dev.taskboard.User.auth.dtos.RegisterRequest;
import com.dev.taskboard.User.config.JwtService;
import com.dev.taskboard.User.entity.User;
import com.dev.taskboard.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthentificationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthentificationResponse register(RegisterRequest request){
        var user= User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthentificationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthentificationResponse authenticate(AuthentificationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
                var user=userRepository.findByEmail(request.getEmail())
                        .orElseThrow();
                var jwtToken=jwtService.generateToken(user);
                return AuthentificationResponse.builder()
                        .token(jwtToken)
                        .build();
    }
}
