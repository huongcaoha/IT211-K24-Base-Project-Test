package org.example.project_base_test.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterDTO {
    @NotBlank
    private String fullName;
    @NotBlank
    private String password;
    @NotBlank
    @Email
    private String email;
}
