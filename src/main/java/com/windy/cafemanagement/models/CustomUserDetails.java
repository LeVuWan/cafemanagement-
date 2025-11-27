package com.windy.cafemanagement.models;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Custom User Details class
 *
 * Version 1.0
 *
 * Date: 12-11-2013
 *
 * Copyright
 *
 * Modification Logs:
 * DATE AUTHOR DESCRIPTION
 * -----------------------------------------------------------------------
 * 10-11-2025 VuLQ Create
 */

public class CustomUserDetails extends User {
    private final String avatar;
    private final Long employyeeId;

    public CustomUserDetails(final Long employyeeId, final String username, final String password,
            final Collection<? extends GrantedAuthority> authorities,
            final String avatar) {
        super(username, password, authorities);
        this.employyeeId = employyeeId;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public Long getEmployyeeId() {
        return employyeeId;
    }

}
