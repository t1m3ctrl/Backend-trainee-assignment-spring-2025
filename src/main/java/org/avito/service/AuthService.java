package org.avito.service;

import lombok.RequiredArgsConstructor;
import org.avito.dto.AuthRequest;
import org.avito.dto.AuthResponse;
import org.avito.dto.RegisterRequest;
import org.avito.exception.UserAlreadyExistsException;
import org.avito.model.Role;
import org.avito.model.User;
import org.avito.repository.UserRepository;
import org.avito.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public String register(RegisterRequest request, Role role) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponse(jwt);
    }
}