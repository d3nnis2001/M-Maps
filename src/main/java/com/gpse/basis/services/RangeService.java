package com.gpse.basis.services;

import com.gpse.basis.domain.GleisLageRange;

import java.util.List;

public interface RangeService {

    public List<GleisLageRange> getGleisLageRange();

    public void saveGleisLageRange(List<GleisLageRange> lst);
}
