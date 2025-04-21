package org.avito.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.avito.service.ReceptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reception")
@Tag(name = "Приемки", description = "Управление приемками товаров")
public class ReceptionController {
    private final ReceptionService receptionService;

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/{pvzId}")
    public ResponseEntity<?> startReception(@PathVariable UUID pvzId) {
        return ResponseEntity.ok(receptionService.createReception(pvzId));
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/close/{pvzId}")
    public ResponseEntity<?> closeReception(@PathVariable UUID pvzId) {
        return ResponseEntity.ok(receptionService.closeReception(pvzId));
    }
}