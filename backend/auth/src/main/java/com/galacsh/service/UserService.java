package com.galacsh.service;

import com.galacsh.config.OAuth2CommonAttributeNames;
import com.galacsh.dto.CompatibleUser;
import com.galacsh.entity.UserEntity;
import com.galacsh.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OAuth2CommonAttributeNames attributeNames;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, OAuth2CommonAttributeNames attributeNames, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.attributeNames = attributeNames;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        String clientId = userRequest.getClientRegistration().getRegistrationId();
        String username = clientId + "::" + user.getName();

        Optional<UserEntity> found = userRepository.findByUsername(username);
        UserEntity actualUser;

        if (found.isEmpty()) {
            String emailAttribute = attributeNames.emailOf(clientId);
            String email = user.getAttribute(emailAttribute);
            actualUser = createOAuth2User(username, email);
        } else {
            actualUser = found.get();
        }

        return CompatibleUser.of(actualUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getAuthorities()
        );
    }

    public void createUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        var password = passwordEncoder.encode(rawPassword);
        userRepository.save(new UserEntity(
                null,
                username,
                email,
                password,
                Collections.emptyList()));
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    private UserEntity createOAuth2User(String username, String email) {
        return userRepository.save(new UserEntity(
                null,
                username,
                email,
                null,
                Collections.emptyList()));
    }
}
