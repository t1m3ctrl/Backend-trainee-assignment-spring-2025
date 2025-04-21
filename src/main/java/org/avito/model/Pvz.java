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
@Table(name = "pvz", schema = "pvz")
public class Pvz {
    @Id
    private UUID id;
    private String city;
    private LocalDateTime createdAt;
}
