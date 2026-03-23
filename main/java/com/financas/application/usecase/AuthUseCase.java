package com.financas.application.usecase;

import com.financas.application.dto.request.LoginRequest;
import com.financas.application.dto.request.RegisterRequest;
import com.financas.application.dto.response.AuthResponse;
import com.financas.domain.exception.BusinessException;
import com.financas.domain.model.User;
import com.financas.domain.repository.UserRepository;
import com.financas.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        User user = User.create(request.getName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getId().toString(), saved.getEmail());
        return AuthResponse.builder().token(token).type("Bearer")
                .name(saved.getName()).email(saved.getEmail()).build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getId().toString(), user.getEmail());
        return AuthResponse.builder().token(token).type("Bearer")
                .name(user.getName()).email(user.getEmail()).build();
    }
}
