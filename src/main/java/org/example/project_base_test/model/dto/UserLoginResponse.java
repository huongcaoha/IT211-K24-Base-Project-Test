package org.example.project_base_test.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginResponse {
    private String fullName;
    private String email;
    private String accessToken;
    private String RefreshToken;
}
