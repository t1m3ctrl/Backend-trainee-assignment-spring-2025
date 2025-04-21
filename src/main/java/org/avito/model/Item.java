package org.avito.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "items", schema = "pvz")
public class Item {
    @Id
    private UUID id;
    private UUID receptionId;
    private LocalDateTime receivedAt;
    private ItemType itemType;
    private Integer sequence;

    public enum ItemType {
        electronics, clothing, footwear
    }
}