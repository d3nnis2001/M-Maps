package com.gpse.basis.services;

import com.gpse.basis.domain.GleisLageRange;
import com.gpse.basis.repositories.GleisLageRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Range service.
 */
@Service
public class RangeServiceImpl implements RangeService {

    private final GleisLageRangeRepository rpr;

    @Autowired
    RangeServiceImpl(GleisLageRangeRepository pr) {
        rpr = pr;
    }

    @Override
    public List<GleisLageRange> getGleisLageRange() {
        List<GleisLageRange> lst = new ArrayList<>();
        rpr.findAll().forEach(lst::add);
        return lst;
    }

    @Override
    public void saveGleisLageRange(List<GleisLageRange> lst) {
        rpr.deleteAll();
        rpr.saveAll(lst);
    }

}
