package org.example.project_base_test.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    // Lấy Client ID từ Google Cloud Console
    private static final String CLIENT_ID = "146681352133-61lacepudts5f3f7celip0vm7p1qv3ea.apps.googleusercontent.com";

    public String verifyAndGetEmail(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            // Hàm verify sẽ tự động gọi lên Google để lấy Public Key về kiểm tra chữ ký
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                // Token hợp lệ, trả về email của người dùng
                return payload.getEmail();
                // Thầy có thể lấy thêm payload.get("name"), payload.get("picture")
            } else {
                throw new RuntimeException("Google Token không hợp lệ!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác thực Google: " + e.getMessage());
        }
    }
}