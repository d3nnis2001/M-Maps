package com.gpse.basis;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class InitializeDatabase implements InitializingBean {
    private final UserRepository usRepo;

    @Autowired
    public InitializeDatabase(final UserRepository usRepo) {
        this.usRepo = usRepo;
    }
    @Override
    public void afterPropertiesSet() {
        usRepo.deleteAll();
        UserModel user = new UserModel("123@gmail.com", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        usRepo.save(user);
    }
}
