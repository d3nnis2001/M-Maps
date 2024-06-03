package com.gpse.basis.services;

import com.gpse.basis.domain.GeoData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DataService {
    public enum Colors{
        NORMAL,
        LOW,
        MEDIUM,
        HIGH,


    }
    List<Map.Entry<Colors, String>> getNewestColorsforGeoData(List<GeoData> lst);

    List<Map.Entry<Colors, String>> getGeoDatabyTrackId(int track_id);

    List<Map.Entry<Colors, String>> getGeoDataByDate(int track_id, LocalDateTime from, LocalDateTime till);
}
