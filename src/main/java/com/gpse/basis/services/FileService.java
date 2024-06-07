package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import com.gpse.basis.domain.GeoData;
import org.apache.avro.util.MapEntry;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

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

}
