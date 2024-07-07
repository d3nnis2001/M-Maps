package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface File service.
 */
public interface FileService {
    List<FileUploadResponse> handleImport(List<String> paths, List<String> streckenIds);
    List<DataSet> getDataSets(String searchString);

    void deleteDataSetsById(List<String> ids);
    void saveLHHFile(File file) throws IOException, IndexOutOfBoundsException;
    List<List<String>> readFoler(String path);


    ArrayList<GeoData> getTrackGeoData(int trackId);
    ArrayList<GleisLageDatenpunkt> getAllTrackData();
    ArrayList<GleisLageDatenpunkt> getTrackData(int trackId);
    ArrayList<String> getDataforId(int trackId);
    ArrayList<GleisLageDatenpunkt> getData(int trackId);
    GeoData getPointInformation(String pointId);
    ArrayList<GleisLageDatenpunkt> getPointData(String pointId);
}
