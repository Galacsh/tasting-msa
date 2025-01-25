package com.galacsh.repository;

import com.galacsh.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByUsername(String username);

    UserEntity save(UserEntity user);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);
}
