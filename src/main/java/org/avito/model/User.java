package org.avito.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", schema = "pvz")
public class User {
    @Id
    private UUID id;
    private String email;
    private String password;
    private Role role;
}
