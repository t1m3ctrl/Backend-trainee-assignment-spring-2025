package org.avito.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.avito.service.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.avito.model.Item;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@Tag(name = "Товары", description = "Добавление товаров в приемку")
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/{pvzId}")
    public Item addItem(@PathVariable UUID pvzId, @RequestParam Item.ItemType item) {
        return itemService.addItemToReception(pvzId, item);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @DeleteMapping("/{pvzId}")
    public void deleteLastItem(@PathVariable UUID pvzId) {
        itemService.deleteLastItemFromReception(pvzId);
    }
}