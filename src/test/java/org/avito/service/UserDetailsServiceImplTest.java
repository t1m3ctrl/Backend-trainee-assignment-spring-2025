package org.avito.service;

import org.avito.model.Role;
import org.avito.model.User;
import org.avito.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnDummyUser() {
        UserDetails user = userDetailsService.loadUserByUsername("dummy_ROLE_CLIENT@pvz.com");

        assertEquals("dummy_ROLE_CLIENT@pvz.com", user.getUsername());
        assertEquals("dummy", user.getPassword());
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }

    @Test
    void loadUserByUsername_shouldLoadFromRepository() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encoded");
        user.setRole(Role.ROLE_MODERATOR);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encoded", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR")));
    }

    @Test
    void loadUserByUsername_shouldThrowIfNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing@example.com"));
    }
}