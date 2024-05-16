package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import com.gpse.basis.domain.GeoData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FileService {
    List<FileUploadResponse> handleImport(List<MultipartFile> files, List<String> streckenIds);
    List<DataSet> getDataSets(String searchString);

    void deleteDataSetsById(List<String> ids);
    void saveLHHFile(MultipartFile file) throws IOException, IndexOutOfBoundsException;
    ArrayList<GeoData> getGeoData();

}
