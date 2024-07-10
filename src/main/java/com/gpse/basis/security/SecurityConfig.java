package com.gpse.basis.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * The type Security config.
 */
@Configuration
@EnableConfigurationProperties(SecurityConstants.class)
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final SecurityConstants securityConstants;
    private final AuthenticationConfiguration configuration;

    /**
     * Instantiates a new Security config.
     *
     * @param userDetailsService the user details service
     * @param securityConstants  the security constants
     * @param configuration      the configuration
     */
    @Autowired
    public SecurityConfig(final UserDetailsService userDetailsService, final SecurityConstants securityConstants,
                          final AuthenticationConfiguration configuration) {
        super();
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
        this.configuration = configuration;
    }

    /**
     * Filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers("/api/**").permitAll())
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityConstants))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, securityConstants))
            .build();

    }

    /**
     * Authentication manager authentication manager.
     *
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Cors configuration source cors configuration source.
     *
     * @return the cors configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "DELETE"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
