package com.gpse.basis;

import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Database for testing.
 */
@Service
@Profile("Name1")
public class InitializeDatabase implements InitializingBean {
    /**
     * BERLIN_OSTBAHNHOF.
     */
    public static final String BERLIN_OSTBAHNHOF = "Berlin Ostbahnhof";
    /**
     * DB_REGIO_SCHIENE_NORD_OST_NO.
     */
    public static final String DB_REGIO_SCHIENE_NORD_OST_NO = "DB Regio Schiene Nord-Ost (NO)";
    /**
     * GLEISLAGEDATEN.
     */
    public static final String GLEISLAGEDATEN = "Gleislagedaten";
    /**
     * HOCH.
     */
    public static final String HOCH = "hoch";
    private static final String PRIMARY_AND_DARK_COLOR = "#282D37";
    private final InspectionOrderRepository ioRepo;
    private final ChecklistRepository checkRepo;
    private final GleisLageRangeRepository glrRepo;
    private final SettingsRepository settingsRepository;

    private final GeoTrackData geoTrackRepository;
    private final UserRepository usRepo;

    /**
     * InitializeDatabase.
     */
    @SuppressWarnings("checkstyle:JavadocMethod")
    @Autowired
    public InitializeDatabase(final UserRepository usRepo, final InspectionOrderRepository ioRepo,
                              final ChecklistRepository checkRepo,
                              final GleisLageRangeRepository r, final GeoTrackData gTD,
                              final SettingsRepository settingsRepository) {

        this.usRepo = usRepo;
        this.ioRepo = ioRepo;
        this.checkRepo = checkRepo;
        this.glrRepo = r;
        this.geoTrackRepository = gTD;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void afterPropertiesSet() {
        initUsers();
        initChecklists();
        initRanges();
        initGeoTrack();
        initSettings();
        initInspectionOrder();
        initChecklistTemplates();
    }

    /**
     * Initialize users for testing.
     */
    public void initUsers() {
        // Test User 1
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        user.addRole("Administrator");
        UserModel user2 = new UserModel("mauricemeise@gmx.net", "asdf", "Jochen", "Bau");
        user2.addRole("Bearbeiter");
        UserModel user3 = new UserModel("affe@web.de", "affe", "Charlie", "Monkey");
        user3.addRole("Datenverwalter");
        usRepo.save(user);
        usRepo.save(user2);
        usRepo.save(user3);
    }

    /**
     * Initialize checklists for testing.
     */
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

    /**
     * Initialize ranges for testing.
     */
    public void initRanges() {
        glrRepo.deleteAll();
        GleisLageRange range1 = new GleisLageRange(GleisLageRange.Level.SRA, 12, 10, 8, 6, 5);
        GleisLageRange range2 = new GleisLageRange(GleisLageRange.Level.SR100, 15, 13, 11, 9, 7);
        GleisLageRange range3 = new GleisLageRange(GleisLageRange.Level.SRLIM, 21, 17, 14, 11, 9);
        glrRepo.save(range1);
        glrRepo.save(range2);
        glrRepo.save(range3);
    }

    /**
     * Initialize inspection orders for testing.
     */
    public void initInspectionOrder() {
        InspectionOrder inspec1 = new InspectionOrder("p-1717767131183662",
            "1010", "", "Bielefeld Hbf", BERLIN_OSTBAHNHOF,
            "2024/07/12", "2024/07/17", DB_REGIO_SCHIENE_NORD_OST_NO,
            GLEISLAGEDATEN, "in Bearbeitung", "Dringend!", false, HOCH);
        InspectionOrder inspec2 = new InspectionOrder("p-1718015853290597",
            "1020", "", "Hamburg Hbf", BERLIN_OSTBAHNHOF,
            "2024/09/12", "2024/09/17", DB_REGIO_SCHIENE_NORD_OST_NO,
            GLEISLAGEDATEN, "beauftrage", "", false, HOCH);
        ioRepo.save(inspec1);
        ioRepo.save(inspec2);
    }

    /**
     * Initialize settings for testing.
     */
    private void initSettings() {
        Settings settings = new Settings("", new Colors(PRIMARY_AND_DARK_COLOR, "#ec0016",
            "#1e7f5e", "#e21437", "#fec705", PRIMARY_AND_DARK_COLOR, "#31CCEC"),
            new byte[0]);
        settingsRepository.save(settings);
    }

    /**
     * Initialize geotrack for testing.
     */
    public void initGeoTrack() {
        Iterable<GeoData> lst = geoTrackRepository.findAll();
        AtomicBoolean found = new AtomicBoolean(false);
        lst.forEach(w -> {
            if (w.getStrecken_id() == 1) {
                found.set(true);
            }
        });
        if (!found.get()) {
            geoTrackRepository.save(new GeoData(1, 52.17027, 9.08446, 0, "1"));
        }
    }

    /**
     * Initialize checklisttemplates for testing.
     */
    public void initChecklistTemplates() {
        // Test Checklist Template 1
        LinkedList<String> tasks = new LinkedList<>(Arrays.asList("Punkt 1", "Punkt 2", "Punkt 3"));
        LinkedList<String> material = new LinkedList<>(Arrays.asList("Material 1", "Material 2", "Material 3"));
        Checklist example = new Checklist("Template 1", tasks);
        example.setMaterial(material);
        checkRepo.save(example);
    }
}
