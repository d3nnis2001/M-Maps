package com.gpse.basis.services;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

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

    public boolean checkCredentials(String email, String password) {
        Optional<UserModel> userOpt = userRepo.findById(email);
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            boolean equal = password.equals(user.getPassword());
            return equal;
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
        Iterator<UserModel> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            UserModel userModel = iterator.next();
            users.add(userModel);
            System.out.println(userModel.getLastname());
        }
        return users;
    }
    @Override
    public boolean updateRoles(String email, ArrayList<String> roles) {
        UserModel us = loadUserByUsername(email);
        ArrayList<String> oldRoles = us.getRoles();
        for (String role : oldRoles) {
            if (!roles.contains(role)){
                us.deleteRole(role);
            }
        }
        for (String role : roles) {
            if (!oldRoles.contains(role)){
                us.addRole(role);
            }
        }
        return true;
    }

    @Override
    public UserModel getUserByUsername(final String username) {
        Optional<UserModel> user = userRepo.findById(username);
        return user.orElse(null);
    }
}
