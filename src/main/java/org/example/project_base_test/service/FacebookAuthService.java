package org.example.project_base_test.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class FacebookAuthService {

    public String verifyAndGetEmail(String accessToken) {
        try {
            // Gọi lên Graph API của Facebook để lấy thông tin
            String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                // Nếu lấy được email tức là Token chuẩn
                return (String) body.get("email");
            } else {
                throw new RuntimeException("Facebook Token không hợp lệ!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác thực Facebook: " + e.getMessage());
        }
    }
}
