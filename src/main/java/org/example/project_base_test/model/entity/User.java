package org.example.project_base_test.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements Serializable {

    private Long id;
    private String fullName;
    @Column(unique = true)
    private String username;
    private String role ;
    private String password;
    private String secret;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user",joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
