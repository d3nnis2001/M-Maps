package com.gpse.basis;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
        initUsers();
        initInspectionOrder();
    }
    public void initUsers() {
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        usRepo.save(user);
    }

    public void initInspectionOrder() {
        InspectionOrder inspec = new InspectionOrder("1234", "1000", "Bielefeld",
            "Hannover", "08-05-2024", "09-05-2024",
            " ", " ", "archiviert", "hallo :)", true);
    }

}
