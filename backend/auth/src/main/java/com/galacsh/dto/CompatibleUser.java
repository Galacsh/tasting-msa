package com.galacsh.dto;

import com.galacsh.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CompatibleUser implements OAuth2User, UserDetails {

    private final String username;
    private final Set<GrantedAuthority> authorities;

    private CompatibleUser(String name, Collection<? extends GrantedAuthority> authorities) {
        this.username = name;
        this.authorities = Set.copyOf(authorities);
    }

    public static CompatibleUser of(UserEntity actualUser) {
        return new CompatibleUser(actualUser.getUsername(), actualUser.getAuthorities());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
