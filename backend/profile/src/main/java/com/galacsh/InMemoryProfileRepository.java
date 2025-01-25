package com.galacsh;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class InMemoryProfileRepository implements ProfileRepository {

    private final Deque<ProfileEntity> profiles = new ConcurrentLinkedDeque<>();

    @Override
    public ProfileEntity save(ProfileEntity profile) {
        var entity = new ProfileEntity(
                profiles.size() + 1L,
                profile.getName(),
                profile.getUsername()
        );
        profiles.add(entity);
        return entity;
    }

    @Override
    public Collection<ProfileEntity> findAllByUsername(String username) {
        return profiles.stream()
                .filter(profile -> profile.getUsername().equals(username))
                .toList();
    }
}
