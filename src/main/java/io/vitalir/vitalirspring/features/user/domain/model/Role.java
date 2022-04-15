package io.vitalir.vitalirspring.features.user.domain.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public enum Role {
    ADMIN,
    USER;

    public List<? extends GrantedAuthority> getAuthorities() {
        var authorityPrefix = "ROLE_";
        switch (this) {
            case USER -> {
                return List.of(new SimpleGrantedAuthority(authorityPrefix + this.name()));
            }
            case ADMIN -> {
                return List.of(
                        new SimpleGrantedAuthority(authorityPrefix + this.name()),
                        new SimpleGrantedAuthority(authorityPrefix + "USER")
                );
            }
        }
        return Collections.emptyList();
    }
}
