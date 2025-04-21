package org.avito.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.avito.service.PvzService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pvz")
@Tag(name = "PVZ", description = "Операции с ПВЗ")
public class PvzController {
    private final PvzService pvzService;

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PostMapping
    public ResponseEntity<?> createPvz(@RequestParam("city") String city) {
        return ResponseEntity.ok(pvzService.createPvz(city));
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_MODERATOR')")
    @GetMapping
    public ResponseEntity<?> listPvzByReceptionDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return ResponseEntity.ok(pvzService.getPvzWithReceptionInRange(start, end, page, size));
    }
}
