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
@Table(name = "receptions", schema = "pvz")
public class Reception {
    @Id
    private UUID id;
    private UUID pvzId;
    private AcceptanceStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime closedAt;

    public enum AcceptanceStatus {
        in_progress, closed
    }
}
