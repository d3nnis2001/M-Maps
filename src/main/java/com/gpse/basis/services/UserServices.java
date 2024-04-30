package com.gpse.basis.services;

import com.gpse.basis.domain.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService {
    boolean checkExistanceEmail(String email);
    boolean addUser(UserModel us);
    boolean deleteUser(String email);
    boolean checkCredentials(String email, String password);
}
