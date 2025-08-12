package com.kozitskiy.dorm4.sys.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    ADMIN,
    STUDENT,
    PLUMBER,
    ELECTRICIAN,
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
