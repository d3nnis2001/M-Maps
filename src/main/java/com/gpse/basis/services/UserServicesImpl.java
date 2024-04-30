package com.gpse.basis.services;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServicesImpl implements UserServices {
    private final UserRepository userRepo;

    @Autowired
    public UserServicesImpl(final UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
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
    public boolean deleteUser(String email) {
        return true;
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
}
