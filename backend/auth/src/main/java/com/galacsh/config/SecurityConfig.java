package com.galacsh.config;

import com.galacsh.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    private static final String[] WHITELIST = {
            "/static/**",
            "/signup",
            "/login/**",
            "/logout",
            "/reset-password"
    };

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(customizer -> customizer
                .requestMatchers(WHITELIST).permitAll()
                .anyRequest().authenticated());

        http.formLogin(customizer -> customizer
                .loginPage("/login")
                .successHandler(onSuccessPatternBasedRedirect()));

        // Notice that login(authorization) endpoint (e.g. /oauth2/authorization/google) is
        // used to initiate login and perform a redirect to the third party authorization server (e.g. Google).
        // And, the redirection endpoint (e.g. /login/oauth2/code/google) is
        // used by the authorization server (e.g. Google) to redirect back to this application.
        http.oauth2Login(customize -> customize
                .loginPage("/login")
                .successHandler(onSuccessPatternBasedRedirect())
                .userInfoEndpoint(userInfo -> userInfo.userService(userService)));

        http.logout(customizer -> customizer.logoutUrl("/logout"));

        // Disable "Basic" authentication (RFC 7617)
        http.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * When the user logs in successfully,
     * redirect to the original request URI.
     * @return Authentication success handler
     */
    @Bean
    @Scope(value = "prototype")
    public AuthenticationSuccessHandler onSuccessPatternBasedRedirect() {
        var handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setRedirectStrategy(new PatternBasedRedirectStrategy());
        return handler;
    }
}
