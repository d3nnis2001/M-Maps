package com.gpse.basis.services;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * UserServiceImpl for all functions for the UserModel.
 */
@Service
public class UserServicesImpl implements UserServices {
    private final UserRepository userRepo;

    @Autowired
    public UserServicesImpl(final UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserModel loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("ExampleUser name " + username + " not found."));
    }

    @Override
    public boolean checkExistanceEmail(String email) {
        return userRepo.existsById(email);
    }
    @Override
    public boolean addUser(UserModel us) {
        userRepo.save(us);
        return true;
    }
    @Override
    public boolean deleteUser(String username) {
        try {
            Optional<UserModel> user = userRepo.findById(username);
            user.ifPresent(userRepo::delete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Credentials.
     * @param email Email
     * @param password Password
     * @return boolean
     */
    public boolean checkCredentials(String email, String password) {
        Optional<UserModel> userOpt = userRepo.findById(email);
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            return password.equals(user.getPassword());
        }
        return false;
    }
    @Override
    public String getToken(String email) {
        if (checkExistanceEmail(email)) {
            UserModel us = loadUserByUsername(email);
            userRepo.delete(us);
            String token = us.setTokenPassword();
            userRepo.save(us);
            return token;
        }
        return "";
    }
    @Override
    public boolean setPasswordNew(String email, String password, String token) {
        System.out.println("Service");
        UserModel us = loadUserByUsername(email);
        System.out.println(us.getTokenPassword() + "service");
        userRepo.delete(us);
        us.setPassword(password);
        userRepo.save(us);
        return true;
    }
    @Override
    public ArrayList<UserModel> getAllUsers() {
        Iterable<UserModel> iterable = userRepo.findAll();
        ArrayList<UserModel> users = new ArrayList<>();
        for (UserModel userModel : iterable) {
            users.add(userModel);
            System.out.println(userModel.getLastname());
        }
        return users;
    }
    @Override
    public boolean updateRoles(String username, ArrayList<String> newRoles) {
        UserModel us = getUserByUsername(username);
        ArrayList<String> oldRoles = us.getRoles();
        ArrayList<String> toDelete = new ArrayList<>();
        if (oldRoles != null) {
            if (oldRoles.contains("") && oldRoles.size() == 1) {
                oldRoles.remove("");
                newRoles.remove("");
                us.deleteRole("");
                userRepo.save(us);
            } else {
                for (String oldRole : oldRoles) {
                    if (!newRoles.contains(oldRole)) {
                        toDelete.add(oldRole);
                    }
                }
                for (String role : toDelete) {
                    us.deleteRole(role);
                }
            }
        }
        for (String newRole : newRoles) {
            us.addRole(newRole);
        }
        System.out.println(us.getRoles());
        userRepo.save(us);
        return true;
    }

    @Override
    public UserModel getUserByUsername(final String username) {
        Optional<UserModel> user = userRepo.findById(username);
        return user.orElse(null);
    }

    /**
     * Sets the variable "unlocked" to true, so the user can use tha app.
     * @param username
     * @return boolean
     */
    public boolean unlockUser(final String username) {
        UserModel us = getUserByUsername(username);
        us.setUnlocked(true);
        System.out.println(us.getUnlocked());
        userRepo.save(us);
        return true;
    }

    public String getToken(String email, String password) {
        UserModel us = getUserByUsername(email);
        System.out.println("TEST-service-passwort" + us.getPassword());
        String token = us.setUserToken();
        userRepo.save(us);
        System.out.println("TEST-service: " + token);
        return token;
    }

    public ArrayList<String> getRoles (String email, String token) {
        UserModel us = getUserByUsername(email);
        if (us.getUserToken().equals(token)) {
            ArrayList<String> roles = us.getRoles();
            return roles;
        }
        return null;
    }

    public String getUserByToken (String token) {
        Iterable<UserModel> al = userRepo.findAll();
        ArrayList<UserModel> user = new ArrayList<>();
        for (UserModel userModel : al) {
            user.add(userModel);
        }
        for (int i = 0; i < user.size(); i++) {
            if (user.get(i).getUserToken().equals(token)) {
                return user.get(i).getUsername();
            }
        }
        return null;
    }
}
