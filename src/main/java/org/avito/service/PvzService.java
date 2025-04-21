package org.avito.service;

import lombok.RequiredArgsConstructor;
import org.avito.dto.PvzPage;
import org.avito.model.Pvz;
import org.avito.repository.PvzRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PvzService {
    private static final List<String> PVZ_LIST = List.of("Москва", "Санкт-Петербург", "Казань");
    private final PvzRepository pvzRepository;

    public Pvz createPvz(String city) {
        if (!PVZ_LIST.contains(city)) {
            throw new IllegalArgumentException("It is impossible to create a PVZ in this city");
        }

        Pvz pvz = Pvz.builder()
                .city(city)
                .createdAt(LocalDateTime.now())
                .build();

        return pvzRepository.save(pvz);
    }

    public PvzPage getPvzWithReceptionInRange(LocalDateTime start, LocalDateTime end, int page, int size) {
        if (page < 1) throw new IllegalArgumentException("page must be greater than 0");
        if (size < 1 || size > 30) throw new IllegalArgumentException("page size must be between 1 and 30");
        page = page - 1;
        int offset = page * size;
        List<Pvz> content = pvzRepository.findAllWithReceptionsInDateRange(start, end, size, offset);
        int total = pvzRepository.countAllWithReceptionsInDateRange(start, end);
        return new PvzPage(content, page + 1, size, total);
    }
}
