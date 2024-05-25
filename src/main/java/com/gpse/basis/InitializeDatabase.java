package com.gpse.basis;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.GleisLageRange;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.ChecklistRepository;
import com.gpse.basis.repositories.GleisLageRangeRepository;
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

    private final GleisLageRangeRepository glrRepo;

    @Autowired
    public InitializeDatabase(final UserRepository usRepo,
                              final ReperaturRepository reRepo, final ChecklistRepository checkRepo, final GleisLageRangeRepository r) {
        this.usRepo = usRepo;
        this.reRepo = reRepo;
        this.checkRepo = checkRepo;
        this.glrRepo = r;
    }

    @Override
    public void afterPropertiesSet() {
        initUsers();
        initChecklists();
        initRanges();
    }

    public void initUsers() {
        // Test User 1
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        UserModel user2 = new UserModel("mauricemeise@gmx.net", "asdf", "Jochen", "Bauer");
        user.addRole("Admin");
        UserModel user3 = new UserModel("affe@web.de", "affe", "Charlie", "Monkey");
        user.addRole("Prüfer");
        usRepo.save(user);
        usRepo.save(user2);
        usRepo.save(user3);
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

    public void initRanges() {
        glrRepo.deleteAll();
        GleisLageRange range1 = new GleisLageRange(GleisLageRange.Level.SRA, 12, 10, 8, 6, 5);
        GleisLageRange range2 = new GleisLageRange(GleisLageRange.Level.SR100, 15, 13, 11, 9, 7);
        GleisLageRange range3 = new GleisLageRange(GleisLageRange.Level.SRLIM, 21, 17, 14, 11, 9);
        glrRepo.save(range1);
        glrRepo.save(range2);
        glrRepo.save(range3);
    }
}
