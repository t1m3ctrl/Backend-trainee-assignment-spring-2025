package org.avito.service;

import lombok.RequiredArgsConstructor;
import org.avito.model.Item;
import org.avito.model.Reception;
import org.avito.repository.ItemRepository;
import org.avito.repository.ReceptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ReceptionRepository receptionRepository;

    public Item addItemToReception(UUID pvzId, Item.ItemType type) {
        Reception reception = receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress)
                .orElseThrow(() -> new IllegalArgumentException("In progress reception not found"));

        Item item = Item.builder()
                .receptionId(reception.getId())
                .receivedAt(LocalDateTime.now())
                .itemType(type)
                .sequence(itemRepository.countByReceptionId(reception.getId()) + 1)
                .build();

        return itemRepository.save(item);
    }

    public void deleteLastItemFromReception(UUID pvzId) {
        Reception reception = receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress)
                .orElseThrow(() -> new IllegalArgumentException("In progress reception not found"));

        Item lastItem = itemRepository.findLastByReceptionId(reception.getId())
                .orElseThrow(() -> new IllegalStateException("Items not found"));

        itemRepository.delete(lastItem);
    }
}