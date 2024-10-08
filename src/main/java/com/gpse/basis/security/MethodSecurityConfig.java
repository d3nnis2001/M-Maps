package com.gpse.basis.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * MethodSecurityConfig.
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {
}

