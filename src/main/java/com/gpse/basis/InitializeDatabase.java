package com.gpse.basis;

import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Profile("Name1")
public class InitializeDatabase implements InitializingBean {
    private final UserRepository usRepo;
    private final InspectionOrderRepository ioRepo;
    private final ReperaturRepository reRepo;
    private final ChecklistRepository checkRepo;
    private final GleisLageRangeRepository glrRepo;

    @Autowired
    public InitializeDatabase(final UserRepository usRepo, final InspectionOrderRepository ioRepo, final ReperaturRepository reRepo,
        final ChecklistRepository checkRepo, final GleisLageRangeRepository r) {
        this.usRepo = usRepo;
        this.ioRepo = ioRepo;
        this.reRepo = reRepo;
        this.checkRepo = checkRepo;
        this.glrRepo = r;
    }

    @Override
    public void afterPropertiesSet() {
        initUsers();
        initInspectionOrder();
        initChecklists();
        initRanges();
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
        items.add("Checker 5");
        items.add("Checker 6");
        items.add("Checker 7");
        items.add("Checker 8");
        items.add("Checker 9");
        items.add("Checker 10");

        Checklist check1 = new Checklist("Abarbeitung1", items);
        Checklist check2 = new Checklist("Abarbeitung2", items);

        checkRepo.save(check2);
        checkRepo.save(check1);
    }

    public void initRanges() {
        glrRepo.deleteAll();
        GleisLageRange range1 = new GleisLageRange(GleisLageRange.Level.SRA, 12, 10, 8, 6, 5);
        GleisLageRange range2 = new GleisLageRange(GleisLageRange.Level.SR100, 15, 13, 11, 9, 7);
        GleisLageRange range3 = new GleisLageRange(GleisLageRange.Level.SRLIM, 21, 17, 14, 11, 9);
        glrRepo.save(range1);
        glrRepo.save(range2);
        glrRepo.save(range3);
    }

    public void initInspectionOrder() {
        InspectionOrder inspec = new InspectionOrder("1716728251294","1234", "1000", "Bielefeld",
            "Hannover", "2024/01/01", "2024/01/02",
            " ", " ", "archiviert", "", true, "niedrig");
        ioRepo.save(inspec);
    }


}
