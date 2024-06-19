package com.gpse.basis.services;

import com.gpse.basis.domain.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

public interface UserServices extends UserDetailsService {
    boolean checkExistanceEmail(String email);
    boolean addUser(UserModel us);
    boolean deleteUser(String email);
    boolean checkCredentials(String email, String password);
    String getToken(String email);
    boolean setPasswordNew(String email, String password, String token);
    ArrayList<UserModel> getAllUsers();
    UserModel loadUserByUsername(String email);
    boolean updateRoles(String email, ArrayList<String> roles);
}
