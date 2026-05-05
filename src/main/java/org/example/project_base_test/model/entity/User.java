package org.example.project_base_test.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    private Long id;
    private String fullName;
    @Column(unique = true)
    private String username;
    private String role ;
    private String password;
}
