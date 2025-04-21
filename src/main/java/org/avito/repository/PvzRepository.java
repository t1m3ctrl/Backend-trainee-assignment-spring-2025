package org.avito.repository;

import org.avito.model.Pvz;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PvzRepository extends CrudRepository<Pvz, UUID> {
    @Query("""
            SELECT DISTINCT p.*
            FROM pvz.pvz p
            JOIN pvz.receptions r ON p.id = r.pvz_id
            WHERE r.started_at BETWEEN :start AND :end
            LIMIT :limit OFFSET :offset
           """)
    List<Pvz> findAllWithReceptionsInDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query("""
            SELECT COUNT(DISTINCT p.id)
            FROM pvz.pvz p
            JOIN pvz.receptions r ON p.id = r.pvz_id
            WHERE r.started_at BETWEEN :start AND :end
           """)
    int countAllWithReceptionsInDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
