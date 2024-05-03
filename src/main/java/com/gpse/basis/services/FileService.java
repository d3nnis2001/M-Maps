package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public List<FileUploadResponse> handleImport(List<MultipartFile> files, List<String> streckenIds);
    public List<DataSet> getDataSets(String searchString);

    public void deleteDataSetsById(List<String> ids);
}
