package org.avito.service;

import org.avito.model.Reception;
import org.avito.repository.ReceptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceptionServiceTest {

    @Mock
    private ReceptionRepository receptionRepository;

    @InjectMocks
    private ReceptionService receptionService;

    @Test
    void createReception_shouldCreateIfNoActiveReception() {
        UUID pvzId = UUID.randomUUID();

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.empty());

        when(receptionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Reception result = receptionService.createReception(pvzId);

        assertEquals(pvzId, result.getPvzId());
        assertEquals(Reception.AcceptanceStatus.in_progress, result.getStatus());
        assertNotNull(result.getStartedAt());
    }

    @Test
    void closeReception_shouldUpdateStatus() {
        UUID pvzId = UUID.randomUUID();
        Reception reception = new Reception();
        reception.setStatus(Reception.AcceptanceStatus.in_progress);
        reception.setPvzId(pvzId);

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.of(reception));
        when(receptionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reception result = receptionService.closeReception(pvzId);

        assertEquals(Reception.AcceptanceStatus.closed, result.getStatus());
        assertNotNull(result.getClosedAt());
    }
}