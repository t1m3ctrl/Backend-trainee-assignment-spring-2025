package org.avito.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.avito.dto.AuthRequest;
import org.avito.dto.AuthResponse;
import org.avito.dto.RegisterRequest;

import org.avito.model.Role;
import org.avito.model.User;
import org.avito.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Аутентификация", description = "Регистрация и вход пользователей")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя с указанной ролью (employee или moderator)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, @RequestParam(defaultValue = "ROLE_CLIENT") Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", authService.register(request, role)));
    }

    @Operation(summary = "Авторизация пользователя", description = "Вход пользователя по email и паролю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        AuthResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}