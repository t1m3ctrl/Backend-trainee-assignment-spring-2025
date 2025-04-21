package org.avito.service;

import org.avito.model.Item;
import org.avito.model.Reception;
import org.avito.repository.ItemRepository;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock private ItemRepository itemRepository;
    @Mock private ReceptionRepository receptionRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void addItemToReception_shouldCreateItem() {
        UUID pvzId = UUID.randomUUID();
        Reception reception = new Reception();
        reception.setId(UUID.randomUUID());

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.of(reception));
        when(itemRepository.countByReceptionId(reception.getId())).thenReturn(3);

        Item savedItem = Item.builder().id(UUID.randomUUID()).build();
        when(itemRepository.save(any())).thenReturn(savedItem);

        Item result = itemService.addItemToReception(pvzId, Item.ItemType.clothing);

        assertNotNull(result);
        verify(itemRepository).save(argThat(item ->
                item.getReceptionId().equals(reception.getId()) &&
                        item.getSequence() == 4 &&
                        item.getItemType() == Item.ItemType.clothing
        ));
    }

    @Test
    void deleteLastItemFromReception_shouldDeleteItem() {
        UUID pvzId = UUID.randomUUID();
        Reception reception = new Reception();
        reception.setId(UUID.randomUUID());

        Item item = mock(Item.class);

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.of(reception));
        when(itemRepository.findLastByReceptionId(reception.getId()))
                .thenReturn(Optional.of(item));

        itemService.deleteLastItemFromReception(pvzId);

        verify(itemRepository).delete(item);
    }

    @Test
    void addItemToReception_shouldThrowExceptionIfReceptionNotFound() {
        UUID pvzId = UUID.randomUUID();

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                itemService.addItemToReception(pvzId, Item.ItemType.electronics));

        assertEquals("In progress reception not found", exception.getMessage());
    }

    @Test
    void deleteLastItemFromReception_shouldThrowExceptionIfReceptionNotFound() {
        UUID pvzId = UUID.randomUUID();

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                itemService.deleteLastItemFromReception(pvzId));

        assertEquals("In progress reception not found", exception.getMessage());
    }

    @Test
    void deleteLastItemFromReception_shouldThrowExceptionIfNoItemsFound() {
        UUID pvzId = UUID.randomUUID();
        Reception reception = new Reception();
        reception.setId(UUID.randomUUID());

        when(receptionRepository.findByPvzIdAndStatus(pvzId, Reception.AcceptanceStatus.in_progress))
                .thenReturn(Optional.of(reception));
        when(itemRepository.findLastByReceptionId(reception.getId()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                itemService.deleteLastItemFromReception(pvzId));

        assertEquals("Items not found", exception.getMessage());
    }
}