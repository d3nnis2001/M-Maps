package com.gpse.basis.web.DataImport;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;
import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.DataSetRepository;
import com.gpse.basis.domain.GleisLageDatenRepository;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final DataSetRepository datasetRepro;

    private final GleisLageDatenRepository glDatenRepro;

    @Autowired
    FileServiceImpl(DataSetRepository repro, GleisLageDatenRepository rpr) {
        datasetRepro = repro;
        glDatenRepro = rpr;
    }

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
                    try {
                        saveFile(file, streckenId);
                    } catch (IOException e) {
                        rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Datei!"));
                        continue;
                    } catch(IndexOutOfBoundsException e) {
                        rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Parquet-Format"));
                        continue;
                    }
                    rsp.add(new FileUploadResponse(file.getOriginalFilename(), true, ""));
                }
            }
            else {
                try {
                    saveFile(file, Objects.equals(streckenId, "missing") ? extractStreckeId(file.getOriginalFilename()) : streckenId);
                } catch (IOException e) {
                    rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Datei!"));
                    continue;
                } catch (IndexOutOfBoundsException e) {
                    rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Parquet-Format"));
                    continue;
                }
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

    public void saveFile(MultipartFile file, String streckenId) throws IOException,IndexOutOfBoundsException {
            Date uploadDate = new Date();

            DataSet st = new DataSet();
            st.setFileName(file.getOriginalFilename());
            st.setStreckenId(streckenId);
            st.setUploadDate(uploadDate);

            System.out.println(st);
            st = datasetRepro.save(st);

            List<GleisLageDatenpunkt> lst = new ArrayList<>();
            File tempFile = File.createTempFile("temporary", ".temp");
            file.transferTo(tempFile);
            final Path path = new Path(tempFile.getPath());
            final Configuration config = new Configuration();
            final ParquetReader<Row> reader = RowParquetReader.builder(HadoopInputFile.fromPath(path, config)).build();
            Row row = reader.read();
            while (row != null) {
                List<Object> values = row.getValues();
                lst.add(new GleisLageDatenpunkt((Double) values.get(0), (Double) values.get(1), (Double) values.get(2), (Double) values.get(3), (Double) values.get(4), st.getId()));
                row = reader.read();
            }
            // todo: effizienz????
            glDatenRepro.saveAll(lst);
            tempFile.delete();
    }

    @Override
    public List<DataSet> getDataSets(String searchString) {
        List<DataSet> s = new ArrayList<>();
        datasetRepro.findAll().forEach(s::add);
        if(searchString.toLowerCase().equals("all")) {
            return s;
        }
        else{
            return s.stream().filter(xt -> xt.getId().contains(searchString)).collect(Collectors.toList());
        }
    }

    @Override
    public void deleteDataSetsById(List<String> ids) {
        ids.forEach(datasetRepro::deleteById);
        // todo: zugehörige Daten löschen
    }
}
