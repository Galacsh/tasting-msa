package com.galacsh.repository;

import com.galacsh.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Deque<UserEntity> users = new ConcurrentLinkedDeque<>();

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    @Override
    public UserEntity save(UserEntity user) {
        var newUser = new UserEntity(
                users.size() + 1L,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
        users.add(newUser);
        return newUser;
    }

    @Override
    public void deleteByUsername(String username) {
        users.stream().filter(user -> user.getUsername().equals(username))
                .findFirst()
                .ifPresent(users::remove);
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }
}
