package com.gpse.basis;

import com.gpse.basis.domain.GleisLageRange;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.repositories.GleisLageRangeRepository;
import com.gpse.basis.repositories.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitializeDatabase implements InitializingBean {
    private final UserRepository usRepo;

    private final GleisLageRangeRepository glrRepo;

    @Autowired
    public InitializeDatabase(final UserRepository usRepo, final GleisLageRangeRepository r) {
        this.usRepo = usRepo;
        this.glrRepo = r;
    }
    @Override
    public void afterPropertiesSet() {
        initUsers();
        initRanges();
    }
    public void initUsers() {
        UserModel user = new UserModel("d3nnis.s@web.de", "hello", "Georg", "Bauer");
        user.addRole("Prüfer");
        usRepo.save(user);
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
