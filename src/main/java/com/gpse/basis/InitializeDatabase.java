package com.gpse.basis;

import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.ChecklistRepository;
import com.gpse.basis.repositories.ReperaturRepository;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class InitializeDatabase implements InitializingBean {
    private final UserRepository usRepo;
    private final ReperaturRepository reRepo;
    private final ChecklistRepository checkRepo;
    @Autowired
    public InitializeDatabase(final UserRepository usRepo,
                              final ReperaturRepository reRepo, final ChecklistRepository checkRepo) {
        this.usRepo = usRepo;
        this.reRepo = reRepo;
        this.checkRepo = checkRepo;
    }

    @Override
    public void afterPropertiesSet() {
        initUsers();
        initChecklists();
    }

    public void initUsers() {
        // Test User 1
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        UserModel user2 = new UserModel("mauricemeise@gmx.net", "asdf", "Jochen", "Bauer");
        user.addRole("Admin");
        usRepo.save(user);
        usRepo.save(user2);
    }
    public void initChecklists() {
        ArrayList<String> items = new ArrayList<>();
        items.add("Checker 1");
        items.add("Checker 2");
        items.add("Checker 3");
        items.add("Checker 4");

        ArrayList<String> items2 = new ArrayList<>();
        items2.add("Checker 1");
        items2.add("Checker 2");
        items2.add("Checker 3");
        items2.add("Checker 4");

        Checklist check1 = new Checklist("Abarbeitung1", items);
        Checklist check2 = new Checklist("Abarbeitung2", items);

        checkRepo.save(check2);
        checkRepo.save(check1);
    }
}
