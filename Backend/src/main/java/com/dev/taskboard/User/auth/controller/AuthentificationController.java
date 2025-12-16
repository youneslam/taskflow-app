package com.dev.taskboard.User.auth.controller;

import com.dev.taskboard.User.auth.dtos.AuthentificationRequest;
import com.dev.taskboard.User.auth.dtos.AuthentificationResponse;
import com.dev.taskboard.User.auth.dtos.RegisterRequest;
import com.dev.taskboard.User.auth.service.AuthentificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")

public class AuthentificationController {
    //Calling the service for authentification
    public final AuthentificationService authentificationService;

    @PostMapping("/register")
    public ResponseEntity<AuthentificationResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authentificationService.register(registerRequest));



    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthentificationResponse> authenticate(@RequestBody AuthentificationRequest authentificationRequest){
        return ResponseEntity.ok(authentificationService.authenticate(authentificationRequest));
    }


}
