package org.avito.service;

import org.avito.dto.PvzPage;
import org.avito.model.Pvz;
import org.avito.repository.PvzRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PvzServiceTest {

    @Mock
    private PvzRepository pvzRepository;

    @InjectMocks
    private PvzService pvzService;

    @Test
    void createPvz_shouldCreatePvz() {
        when(pvzRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Pvz result = pvzService.createPvz("Москва");

        assertEquals("Москва", result.getCity());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void createPvz_shouldThrowIfInvalidCity() {
        assertThrows(IllegalArgumentException.class, () -> pvzService.createPvz("Новосибирск"));
    }

    @Test
    void getPvzWithReceptionInRange_shouldReturnPage() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<Pvz> pvzList = List.of(new Pvz(), new Pvz());

        when(pvzRepository.findAllWithReceptionsInDateRange(start, end, 2, 0)).thenReturn(pvzList);
        when(pvzRepository.countAllWithReceptionsInDateRange(start, end)).thenReturn(10);

        PvzPage result = pvzService.getPvzWithReceptionInRange(start, end, 1, 2);

        assertEquals(1, result.pageNumber());
        assertEquals(2, result.pageSize());
        assertEquals(5, result.totalPages());
        assertEquals(pvzList, result.content());
    }
}