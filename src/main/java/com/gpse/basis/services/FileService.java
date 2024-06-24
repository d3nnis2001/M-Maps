package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FileService {
    List<FileUploadResponse> handleImport(List<String> paths, List<String> streckenIds);
    List<DataSet> getDataSets(String searchString);

    void deleteDataSetsById(List<String> ids);
    void saveLHHFile(File file) throws IOException, IndexOutOfBoundsException;
    ArrayList<GeoData> getGeoData();
    ArrayList<GeoData> getTrackGeoData(int trackID);
    ArrayList<GeoData> getPartGeoData(int from, int till);

    List<List<String>> readFoler(String path);

    ArrayList<GleisLageDatenpunkt> getAllTrackData();
    ArrayList<GleisLageDatenpunkt> getTrackData(int trackId);
    ArrayList<String> getDataforId(int trackId);
    ArrayList<GleisLageDatenpunkt> getData(int trackId);
    ArrayList<GeoData> getPointData(double lat, double lo);
}
