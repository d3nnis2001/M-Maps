package com.gpse.basis.services;

import com.gpse.basis.domain.GeoData;

import java.util.List;
import java.util.Map;

public interface DataService {
    public enum Colors{
        NORMAL,
        LOW,
        MEDIUM,
        HIGH,


    }
    public List<Map.Entry<Colors, String>> getNewestColorsforGeoData(List<GeoData> lst);

    public List<Map.Entry<Colors, String>> getGeoDatabyTrackId(int track_id);
}
