package com.smartcampus.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

/**
 * 🆕 Клас для зберігання поточного користувача в Security Context
 */
public class CurrentUser extends User {

    private Long id;
    private String email;
    private String name;
    private String role;

    public CurrentUser(Long id, String email, String name, String role,
                       Collection<? extends GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
}