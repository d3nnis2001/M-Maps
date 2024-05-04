package com.gpse.basis;

import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.domain.UserModel;
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

    @Autowired
    public InitializeDatabase(final UserRepository usRepo, final ReperaturRepository reRepo) {
        this.usRepo = usRepo;
        this.reRepo = reRepo;
    }
    @Override
    public void afterPropertiesSet() {
        initUsers();
        initRepair();
    }
    public void initUsers() {
        // Test User 1
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        usRepo.save(user);
    }
    public void initRepair() {
        // Reparaturauftrag 1
        Date datefrom = new Date(2024, 02, 10, 10, 10, 10);
        Date datetill = new Date(2024, 03, 10, 10, 10, 10);
        ArrayList<String> checklist = new ArrayList<>();
        checklist.add("ErsteAufgabe");
        checklist.add("ZweiteAufgabe");
        Reparatur rep = new Reparatur("1", 6200, datefrom, datetill, checklist,
            "Alles gut hier", "storniert", "Müller");

        // Reparaturauftrag 2
        Date datefrom2 = new Date(2023, 02, 10, 10, 10, 10);
        Date datetill2 = new Date(2023, 03, 10, 10, 10, 10);
        ArrayList<String> checklist2 = new ArrayList<>();
        checklist.add("ErsteAufgabeWieder");
        checklist.add("ZweiteAufgabeWieder");
        Reparatur rep2 = new Reparatur("2", 6300, datefrom2, datetill2, checklist2,
            "Auch alles gut hier", "neu beauftragt", "Heinz");
        reRepo.save(rep);
        reRepo.save(rep2);



    }
}
