package com.example.demo.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    Default, Student, Parent, Teacher, Admin;

    @Override
    public String getAuthority() {
        return name();
    }
}
