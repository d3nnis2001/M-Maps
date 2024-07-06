package com.gpse.basis;

import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Profile("Name1")
public class InitializeDatabase implements InitializingBean {
    private final UserRepository usRepo;
    private final InspectionOrderRepository ioRepo;
    private final ReperaturRepository reRepo;
    private final ChecklistRepository checkRepo;
    private final GleisLageRangeRepository glrRepo;

    private final GeoTrackData geoTrackRepository;

    @Autowired
    public InitializeDatabase(final UserRepository usRepo, final InspectionOrderRepository ioRepo, final ReperaturRepository reRepo,
        final ChecklistRepository checkRepo, final GleisLageRangeRepository r, final GeoTrackData gTD) {
        this.usRepo = usRepo;
        this.ioRepo = ioRepo;
        this.reRepo = reRepo;
        this.checkRepo = checkRepo;
        this.glrRepo = r;
        this.geoTrackRepository = gTD;
    }

    @Override
    public void afterPropertiesSet() {
        initUsers();
        initChecklists();
        initRanges();
        initGeoTrack();
        initInspectionOrder();;
    }
    public void initUsers() {
        // Test User 1
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        user.addRole("Admin");
        UserModel user2 = new UserModel("mauricemeise@gmx.net", "asdf", "Jochen", "Bauer");
        user2.addRole("Admin");
        UserModel user3 = new UserModel("affe@web.de", "affe", "Charlie", "Monkey");
        user3.addRole("Prüfer");
        user3.addRole("Datenverwalter");
        UserModel user4 = new UserModel("test", "abc", "Hi", "Du");
        user4.addRole("Bearbeiter");
        usRepo.save(user);
        usRepo.save(user2);
        usRepo.save(user3);
        usRepo.save(user4);
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
        InspectionOrder inspec1 = new InspectionOrder("p-1717767131183662", "1010", "", "Bielefeld Hbf", "Berlin Ostbahnhof", "2024/07/12", "2024/07/17", "DB Regio Schiene Nord-Ost (NO)", "Gleislagedaten", "in Bearbeitung", "Dringend!", false, "hoch");
        InspectionOrder inspec2 = new InspectionOrder("p-1718015853290597", "1020", "", "Hamburg Hbf", "Berlin Ostbahnhof", "2024/09/12", "2024/09/17", "DB Regio Schiene Nord-Ost (NO)", "Gleislagedaten", "beauftrage", "", false, "hoch");
        ioRepo.save(inspec1);
        ioRepo.save(inspec2);
    }


    public void initGeoTrack() {
        Iterable<GeoData> lst = geoTrackRepository.findAll();
        AtomicBoolean found = new AtomicBoolean(false);
        lst.forEach(w -> {
            if(w.getStrecken_id() == 1) {
                found.set(true);
            }
        });
        if(!found.get())
            geoTrackRepository.save(new GeoData(1, 52.17027,  9.08446,0, "1"));
    }
}
