package com.smartcampus.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * 🆕 Сервіс для верифікації Google ID Token
 */
@Service
public class GoogleOAuthService {

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * Верифікувати Google ID Token та отримати дані користувача
     */
    public GoogleUserInfo verifyIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                GoogleUserInfo userInfo = new GoogleUserInfo();
                userInfo.setGoogleId(payload.getSubject());
                userInfo.setEmail(payload.getEmail());
                userInfo.setName((String) payload.get("name"));
                userInfo.setPhotoUrl((String) payload.get("picture"));

                return userInfo;
            }

            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Google ID Token", e);
        }
    }

    /**
     * DTO для даних користувача з Google
     */
    public static class GoogleUserInfo {
        private String googleId;
        private String email;
        private String name;
        private String photoUrl;

        // Getters and Setters
        public String getGoogleId() { return googleId; }
        public void setGoogleId(String googleId) { this.googleId = googleId; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPhotoUrl() { return photoUrl; }
        public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    }
}