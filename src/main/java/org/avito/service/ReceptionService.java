package org.avito.service;

import lombok.RequiredArgsConstructor;
import org.avito.model.Reception;
import org.avito.repository.ReceptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReceptionService {
    private final ReceptionRepository receptionRepository;

    public Reception createReception(UUID pvzId) {
        receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress).ifPresent(
            r -> {
                throw new IllegalStateException("It is not possible to create a new reception " +
                "because the previous one has not been closed yet");
        });

        var reception = Reception.builder()
                .pvzId(pvzId)
                .status(Reception.AcceptanceStatus.in_progress)
                .startedAt(LocalDateTime.now())
                .build();

        return receptionRepository.save(reception);
    }

    public Reception closeReception(UUID pvzId) {
        var reception = receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress).orElseThrow(
                () -> new IllegalArgumentException("In progress reception in this pvz not found")
        );

        reception.setStatus(Reception.AcceptanceStatus.closed);
        reception.setClosedAt(LocalDateTime.now());
        return receptionRepository.save(reception);
    }
}
