package com.gpse.basis.services;

import com.gpse.basis.domain.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

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
    UserModel getUserByUsername(String username);
    boolean unlockUser(String username);
    String getToken(String email, String password);
    ArrayList<String> getRoles(String email, String token);
    String getUserByToken(String token);
}
