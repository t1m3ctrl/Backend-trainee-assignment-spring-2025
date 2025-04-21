package org.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.avito.dto.AuthResponse;
import org.avito.model.Role;
import org.avito.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dummy", description = "Получение тестового токена (используется для отладки)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class DummyLoginController {

    private final JwtService jwtService;

    @Operation(summary = "Получение тестового токена", description = "Генерация JWT токена на основе указанной роли (employee или moderator)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/dummyLogin")
    public ResponseEntity<AuthResponse> dummyLogin(@RequestParam Role role) {
        String email = "dummy_" + role.name() + "@pvz.com";

        var authorities = List.of(new SimpleGrantedAuthority(role.name()));
        UserDetails user = new org.springframework.security.core.userdetails.User(email, "dummy", authorities);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}