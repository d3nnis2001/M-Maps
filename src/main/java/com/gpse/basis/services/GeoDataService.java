package com.gpse.basis.services;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;

import java.util.ArrayList;
import java.util.List;

public interface GeoDataService {
    int getGleisColour(int id, double start, double end);

    ArrayList<GeoData> getAllGeoPoints();
}
