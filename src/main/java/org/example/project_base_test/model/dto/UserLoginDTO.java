package org.example.project_base_test.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
