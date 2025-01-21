package com.galacsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITELIST = {
            "/static/**",
            "/signup",
            "/login",
            "/logout",
            "/reset-password"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(customizer -> customizer
                .requestMatchers(WHITELIST).permitAll()
                .anyRequest().authenticated());

        http.formLogin(customizer -> customizer
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .successHandler(formLoginSuccessHandler())
                .failureHandler(formLoginFailureHandler()));
        http.logout(customizer -> customizer.logoutUrl("/logout"));

        http.exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(authenticationEntryPoint()));

        // Disable "Basic" authentication (RFC 7617)
        http.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


    /**
     * When the form login fails, redirect to
     * the login page with an error query parameter.
     * @return Authentication failure handler
     */
    @Bean
    public AuthenticationFailureHandler formLoginFailureHandler() {
        RedirectStrategy redirectStrategy = redirectPrependsContextPath();
        return (request, response, exception) -> {
            String uri = UriComponentsBuilder
                    .fromPath("/login")
                    .query(request.getQueryString())
                    .replaceQueryParam("error")
                    .queryParam("error")
                    .encode().build().toUriString();
            redirectStrategy.sendRedirect(request, response, uri);
        };
    }

    /**
     * When the user is not authenticated,
     * redirect to the login page with the original request URI.
     * @return Authentication entry point
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        RedirectStrategy redirectStrategy = redirectPrependsContextPath();
        return (request, response, authException) -> {
            String uri = UriComponentsBuilder
                    .fromPath("/login")
                    .replaceQueryParam("redirect", request.getRequestURI())
                    .encode().build().toUriString();
            redirectStrategy.sendRedirect(request, response, uri);
        };
    }

    /**
     * When the user logs in successfully,
     * redirect to the original request URI.
     * @return Authentication success handler
     */
    @Bean
    public AuthenticationSuccessHandler formLoginSuccessHandler() {
        var handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setTargetUrlParameter("redirect");
        handler.setRedirectStrategy(redirectWithoutContextPath());
        return handler;
    }

    /**
     * Redirect strategy that does not prepend the context path to the URL.
     * <p>
     *     In RedirectStrategy, contextRelative = true means that
     *     "given URL is already relative to the context path".
     * </p>
     * @return Redirect strategy
     */
    public RedirectStrategy redirectWithoutContextPath() {
        var redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.setContextRelative(true);
        return redirectStrategy;
    }

    /**
     * Redirect strategy that prepends the context path to the URL if necessary.
     * @return Redirect strategy
     */
    public RedirectStrategy redirectPrependsContextPath() {
        return new DefaultRedirectStrategy();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
