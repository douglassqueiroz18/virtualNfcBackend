package com.virtualnfc.backendproject.service;

import com.virtualnfc.backendproject.dto.LoginRequestDTO;
import com.virtualnfc.backendproject.model.User;
import com.virtualnfc.backendproject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    public boolean login(LoginRequestDTO dto) {
        User user = repo.findByEmail(dto.getEmail())
                .orElse(null);

        if (user == null) return false;

        return BCrypt.checkpw(dto.getPassword(), user.getPasswordHash());
    }
}
