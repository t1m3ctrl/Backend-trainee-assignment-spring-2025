package org.avito.service;

import org.avito.dto.AuthRequest;
import org.avito.dto.AuthResponse;
import org.avito.dto.RegisterRequest;
import org.avito.exception.UserAlreadyExistsException;
import org.avito.model.Role;
import org.avito.repository.UserRepository;
import org.avito.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authManager;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldSaveUserAndReturnSuccessMessage() {
        RegisterRequest request = new RegisterRequest("user@example.com", "password");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded");

        String result = authService.register(request, Role.ROLE_CLIENT);

        verify(userRepository).save(argThat(user ->
                user.getEmail().equals(request.email()) &&
                        user.getPassword().equals("encoded") &&
                        user.getRole() == Role.ROLE_CLIENT
        ));
        assertEquals("User registered successfully", result);
    }

    @Test
    void register_shouldThrowExceptionIfEmailExists() {
        when(userRepository.existsByEmail("exists@example.com")).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () ->
                authService.register(new RegisterRequest("exists@example.com", "1234"), Role.ROLE_CLIENT));
    }

    @Test
    void login_shouldReturnJwtToken() {
        AuthRequest request = new AuthRequest("user@example.com", "pass");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.token());
    }
}