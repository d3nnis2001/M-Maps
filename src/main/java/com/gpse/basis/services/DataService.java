package com.gpse.basis.services;

import com.gpse.basis.domain.GeoData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The interface Data service.
 */
public interface DataService {
    /**
     * The enum Colors.
     */
    public enum Colors {
        /**
         * Normal colors.
         */
        NORMAL,
        /**
         * Low colors.
         */
        LOW,
        /**
         * Medium colors.
         */
        MEDIUM,
        /**
         * High colors.
         */
        HIGH,


    }
    List<Map.Entry<Colors, String>> getNewestColorsforGeoData(List<GeoData> lst);

    List<Map.Entry<Colors, String>> getHeatmap();

    List<Map.Entry<Colors, String>> getGeoDataByDate(int track_id, LocalDateTime from, LocalDateTime till);

    ArrayList<GeoData> getGeoData();

    Double[] getDataForGeoPart(String id);


}
