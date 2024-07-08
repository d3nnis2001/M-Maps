package com.gpse.basis.services;

import com.gpse.basis.domain.GleisLageRange;

import java.util.List;

/**
 * The interface Range service.
 */
public interface RangeService {

    public List<GleisLageRange> getGleisLageRange();

    public void saveGleisLageRange(List<GleisLageRange> lst);
}
