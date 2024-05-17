package com.gpse.basis;

import com.gpse.basis.domain.Checklist;
import com.gpse.basis.domain.ChecklistTemplate;
import com.gpse.basis.domain.Reparatur;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.ChecklistRepository;
import com.gpse.basis.repositories.ChecklistTemplateRepository;
import com.gpse.basis.repositories.ReperaturRepository;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InitializeDatabase implements InitializingBean {
    private final UserRepository usRepo;
    private final ReperaturRepository reRepo;
    private final ChecklistTemplateRepository checklistTemplateRepository;
    private final ChecklistRepository checklistRepository;

    @Autowired
    public InitializeDatabase(final UserRepository usRepo,
                              final ReperaturRepository reRepo, final ChecklistTemplateRepository checkRepo,
                              final ChecklistRepository checklistRepository) {
        this.usRepo = usRepo;
        this.reRepo = reRepo;
        this.checklistTemplateRepository = checkRepo;
        this.checklistRepository = checklistRepository;
    }
    @Override
    public void afterPropertiesSet() {
        initUsers();
        initRepair();
        initChecklistTemplates();
        initRepair();
    }
    public void initUsers() {
        // Test User 1
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        usRepo.save(user);
    }
    public void initChecklistTemplates() {
        // Test Checklist Template 1
        LinkedList<String> tasks = new LinkedList<>(Arrays.asList("Punkt 1", "Punkt 2", "Punkt 3"));
        LinkedList<String> material = new LinkedList<>(Arrays.asList("Material 1", "Material 2", "Material 3"));
        ChecklistTemplate example = new ChecklistTemplate("Template 1", tasks, material);
        checklistTemplateRepository.save(example);
    }

    public void initRepair() {
        // Reparaturauftrag 1
        Date datefrom = new Date(2024, 02, 10, 10, 10, 10);
        Date datetill = new Date(2024, 03, 10, 10, 10, 10);
        ArrayList<String> items = new ArrayList<>();
        items.add("Checker 1");
        items.add("Checker 2");
        Checklist check1 = new Checklist("Abarbeitung1", items, items);
        Reparatur rep = new Reparatur("1", 6200, datefrom, datetill, check1,
            "Alles gut hier", "storniert", "Müller");

        // Reparaturauftrag 2
        Date datefrom2 = new Date(2023, 02, 10, 10, 10, 10);
        Date datetill2 = new Date(2023, 03, 10, 10, 10, 10);
        ArrayList<String> items2 = new ArrayList<>();
        items2.add("Checker 1");
        items2.add("Checker 2");

        Checklist check2 = new Checklist("Abarbeitung2", items, items2);
        checklistRepository.save(check2);
        checklistRepository.save(check1);
        Reparatur rep2 = new Reparatur("2", 6300, datefrom2, datetill2, check2,
            "Auch alles gut hier", "neu beauftragt", "Heinz");
        reRepo.save(rep);
        reRepo.save(rep2);
    }
}
