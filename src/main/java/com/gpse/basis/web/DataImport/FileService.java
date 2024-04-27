package com.gpse.basis.web.DataImport;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public List<FileUploadResponse> handleImport(List<MultipartFile> files, List<String> streckenIds);
}
