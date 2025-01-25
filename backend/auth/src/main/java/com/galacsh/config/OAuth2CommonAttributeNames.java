package com.galacsh.config;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
public class OAuth2CommonAttributeNames {
    private final ClientRegistrationRepository clients;

    public OAuth2CommonAttributeNames(ClientRegistrationRepository clients) {
        this.clients = clients;
    }

    public String emailOf(String clientId) {
        ClientRegistration client = clients.findByRegistrationId(clientId);
        if (client == null) throw new IllegalArgumentException("Not supported: " + clientId);

        if ("google".equals(clientId)) {
            return "email";
        }

        throw new IllegalArgumentException("Cannot find email attribute for: " + clientId);
    }
}
