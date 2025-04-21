package org.avito.repository;

import org.avito.model.Item;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {
    @Query("""
            select * from pvz.items
            where reception_id = :receptionId
            order by sequence desc limit 1
            """)
    Optional<Item> findLastByReceptionId(UUID receptionId);

    @Query("select count(*) from pvz.items where reception_id = :receptionId")
    int countByReceptionId(UUID receptionId);
}
