package org.avito.service;

import lombok.RequiredArgsConstructor;
import org.avito.model.Role;
import org.avito.model.User;
import org.avito.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email.equals("dummy_ROLE_CLIENT@pvz.com") || email.equals("dummy_ROLE_MODERATOR@pvz.com")) {
            String role = email.substring("dummy_".length(), email.indexOf("@"));
            return new org.springframework.security.core.userdetails.User(
                    email,
                    "dummy",
                    List.of(new SimpleGrantedAuthority(role)));
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                buildAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> buildAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
