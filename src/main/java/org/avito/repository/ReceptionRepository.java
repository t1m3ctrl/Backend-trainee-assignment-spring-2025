package org.avito.repository;

import org.avito.model.Reception;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReceptionRepository extends CrudRepository<Reception, UUID> {
    Optional<Reception> findByPvzIdAndStatus(UUID pvzId, Reception.AcceptanceStatus status);
}
