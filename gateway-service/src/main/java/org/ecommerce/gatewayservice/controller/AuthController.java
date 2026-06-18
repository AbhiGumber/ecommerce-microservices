package org.ecommerce.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.gatewayservice.records.LoginRequest;
import org.ecommerce.gatewayservice.records.LoginResponse;
import org.ecommerce.gatewayservice.security.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    public AuthController(JwtService jwtService) {

        this.jwtService = jwtService;

    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        if (!request.username().equals("admin")
                || !request.password().equals("password")) {

            throw new RuntimeException("Invalid credentials");
        }

        String token =
                jwtService.generateToken(
                        request.username()
                );

        return new LoginResponse(token);
    }
}
