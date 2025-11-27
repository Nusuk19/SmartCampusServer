package com.smartcampus.dto;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserData user;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, UserData user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    // Getters and Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public UserData getUser() { return user; }
    public void setUser(UserData user) { this.user = user; }

    public static class UserData {
        private long id;
        private String email;
        private String name;
        private String role;
        private String photoUrl;

        // Getters and Setters
        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getPhotoUrl() { return photoUrl; }
        public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    }
}