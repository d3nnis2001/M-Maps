package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FileService {
    List<FileUploadResponse> handleImport(List<String> paths, List<String> streckenIds);
    List<DataSet> getDataSets(String searchString);

    void deleteDataSetsById(List<String> ids);
    void saveLHHFile(File file) throws IOException, IndexOutOfBoundsException;
    ArrayList<GeoData> getGeoData();
    ArrayList<GeoData> getTrackGeoData(int trackID);
    List<Map.Entry<DataService.Colors, String>> getPartGeoData(int from, int till);

    List<List<String>> readFoler(String path);

    List<Map.Entry<DataService.Colors, String>> getPartHeatmap(int strecke, LocalDateTime from, LocalDateTime till);

    ArrayList<GleisLageDatenpunkt> getAllTrackData();
    ArrayList<GleisLageDatenpunkt> getTrackData(int trackId);
    ArrayList<String> getDataforId(int trackId);
    ArrayList<GleisLageDatenpunkt> getData(int trackId);
    ArrayList<GeoData> getPointData(String pointId);
}
