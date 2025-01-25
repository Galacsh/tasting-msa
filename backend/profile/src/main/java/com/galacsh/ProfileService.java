package com.galacsh;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Random;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Collection<Profile> getAllProfiles(String username) {
        Collection<ProfileEntity> profiles = profileRepository.findAllByUsername(username);
        return profiles.stream()
                .map(entity -> new Profile(entity.getId(), entity.getName()))
                .toList();
    }

    public Profile createRandomProfile(String username) {
        ProfileEntity randomProfile = profileRepository.save(
                new ProfileEntity(null, randomProfileName(10), username)
        );

        return new Profile(randomProfile.getId(), randomProfile.getName());
    }

    private String randomProfileName(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            result.append(alphabet.charAt(index));
        }

        return result.toString();
    }
}
