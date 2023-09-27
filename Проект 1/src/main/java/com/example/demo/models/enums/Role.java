package com.example.demo.models.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_OPERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
