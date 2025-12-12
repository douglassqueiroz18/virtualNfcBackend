package com.virtualnfc.backendproject.controller;

import com.virtualnfc.backendproject.dto.LoginRequestDTO;
import com.virtualnfc.backendproject.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {

        boolean success = service.login(dto);

        if (success) {
            return ResponseEntity.ok().body("Login OK");
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
