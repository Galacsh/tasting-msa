package com.galacsh;

import java.util.Collection;

public interface ProfileRepository {
    ProfileEntity save(ProfileEntity profile);

    Collection<ProfileEntity> findAllByUsername(String username);

    void deleteAllByUsername(String username);
}
