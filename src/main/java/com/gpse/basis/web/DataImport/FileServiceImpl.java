package com.gpse.basis.web.DataImport;

import com.gpse.basis.domain.DataSet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileUploadResponse> handleImport(List<MultipartFile> files, List<String> streckenIds) {
        Iterator<MultipartFile> itr1 = files.iterator();
        Iterator<String> itr2 = streckenIds.iterator();

        List<FileUploadResponse> rsp = new ArrayList<>();

        while(itr1.hasNext() && itr2.hasNext()) {
            MultipartFile file = itr1.next();
            String streckenId = itr2.next();

            if(!checkFileName(file.getOriginalFilename())) {
                if(Objects.equals(streckenId, "missing")) {
                    rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Missing Track_ID"));
                }
                else {
                   saveFile(file, streckenId);
                   rsp.add(new FileUploadResponse(file.getOriginalFilename(), true, ""));
                }
            }
            else {
                saveFile(file, Objects.equals(streckenId, "missing") ? extractStreckeId(file.getOriginalFilename()) : streckenId);
                rsp.add(new FileUploadResponse(file.getOriginalFilename(), true, ""));
            }
        }
        return rsp;
    }

    private String extractStreckeId(String fileName){
        Pattern pattern = Pattern.compile("Str_\\d+");
        Matcher matcher = pattern.matcher(fileName);
        matcher.find();
        return matcher.group(0);
    }

    //Prüft ob Strecken ID im Dateinamen enthalten ist
    private boolean checkFileName(String fileName) {
        Pattern pattern = Pattern.compile("_Str_\\d+_");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find();
    }

    public void saveFile(MultipartFile file, String streckenId) {
            Date uploadDate = new Date();

            DataSet st = new DataSet();
            st.setFileName(file.getOriginalFilename());
            st.setStreckenId(streckenId);
            st.setUploadDate(uploadDate);

            System.out.println(st);
            //todo: in mongo db einfügen (ID automatisch einfügen!!!)
    }
}
