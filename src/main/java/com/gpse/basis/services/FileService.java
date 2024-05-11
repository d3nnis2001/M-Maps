package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import com.gpse.basis.domain.GeoData;
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

    List<List<String>> readFoler(String path);

}
