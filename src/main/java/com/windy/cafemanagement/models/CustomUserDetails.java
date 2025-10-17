package com.windy.cafemanagement.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
    private final String avatar;

    public CustomUserDetails(String username, String password,
            Collection<? extends GrantedAuthority> authorities,
            String avatar) {
        super(username, password, authorities);
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }
}
