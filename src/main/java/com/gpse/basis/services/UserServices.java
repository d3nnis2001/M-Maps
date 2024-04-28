package com.gpse.basis.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService {
    boolean checkExistanceEmail(String email);
}
