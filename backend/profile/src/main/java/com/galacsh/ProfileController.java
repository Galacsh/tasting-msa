package com.galacsh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    public String allProfiles(Passport passport) {
        Collection<Profile> profiles = profileService.getAllProfiles(passport.username());

        if (profiles.isEmpty()) {
            return "No profile found for: " + passport.username();
        }

        StringBuilder result = new StringBuilder();

        int num = 1;
        for (Profile profile : profiles) {
            result.append("#")
                    .append(num)
                    .append(": ")
                    .append(profile.name());
            if (num < profiles.size()) result.append("\n");
            num++;
        }

        return result.toString();
    }

    @GetMapping("/random")
    public String randomProfileGenerator(Passport passport) {
        Profile randomProfile = profileService.createRandomProfile(passport.username());
        return "Successfully generated a random profile: " + randomProfile.name();
    }
}
