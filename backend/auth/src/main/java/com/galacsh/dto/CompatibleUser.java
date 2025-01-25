package com.galacsh.dto;

import com.galacsh.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CompatibleUser extends User implements OAuth2User {

    private CompatibleUser(String name, Collection<? extends GrantedAuthority> authorities) {
        super(name, null, authorities);
    }

    public static CompatibleUser of(UserEntity actualUser) {
        return new CompatibleUser(actualUser.getUsername(), actualUser.getAuthorities());
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}
