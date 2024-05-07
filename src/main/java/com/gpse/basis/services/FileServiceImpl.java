package com.gpse.basis.services;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;
import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.DataSetRepository;
import com.gpse.basis.repositories.GeoTrackData;
import com.gpse.basis.repositories.GleisLageDatenRepository;
import com.gpse.basis.services.FileService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final DataSetRepository datasetRepro;

    private final GleisLageDatenRepository glDatenRepro;

    private final GeoTrackData geoTrack;

    @Autowired
    FileServiceImpl(DataSetRepository repro, GleisLageDatenRepository rpr, GeoTrackData gt) {
        datasetRepro = repro;
        glDatenRepro = rpr;
        geoTrack = gt;
    }

    @Override
    public List<FileUploadResponse> handleImport(List<MultipartFile> files, List<String> streckenIds) {
        Iterator<MultipartFile> itr1 = files.iterator();
        Iterator<String> itr2 = streckenIds.iterator();

        List<FileUploadResponse> rsp = new ArrayList<>();

        while(itr1.hasNext() && itr2.hasNext()) {
            MultipartFile file = itr1.next();
            String streckenId = itr2.next();

            if(file.getOriginalFilename().endsWith(".LLH.dat")) {
                try {
                    saveLHHFile(file);
                } catch (IndexOutOfBoundsException | IOException e) {
                    rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Datei!"));
                }
                rsp.add(new FileUploadResponse(file.getOriginalFilename(), true, ""));
                continue;
            }

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
                    } catch(RuntimeException e) {
                        rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Datei!"));
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
                } catch(RuntimeException e) {
                    rsp.add(new FileUploadResponse(file.getOriginalFilename(), false, "Fehlerhafte Datei!"));
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

    public void saveLHHFile(MultipartFile file) throws IOException, IndexOutOfBoundsException {
        List<GeoData> lst = new ArrayList<>();
        InputStream stream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        while((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            lst.add(new GeoData(Integer.parseInt(parts[0]),
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Integer.parseInt(parts[4])));
        }
        geoTrack.saveAll(lst);
    }

    public void saveFile(MultipartFile file, String streckenId) throws IOException,IndexOutOfBoundsException,RuntimeException {
            Date uploadDate = new Date();

            DataSet st = new DataSet();
            st.setFileName(file.getOriginalFilename());
            st.setStreckenId(streckenId);
            st.setUploadDate(uploadDate);

            System.out.println(st);


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
            glDatenRepro.saveAll(lst);
            st = datasetRepro.save(st);
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
        List<GleisLageDatenpunkt> gld = new ArrayList<>();
        glDatenRepro.findAll().forEach(gld::add);
        gld = gld.stream().filter(gl -> ids.contains(gl.getDataSetid())).collect(Collectors.toList());
        glDatenRepro.deleteAll(gld);
    }

    @Override
    public ArrayList<GeoData> getGeoData() {
        Iterable<GeoData> iterable = geoTrack.findAll();
        ArrayList<GeoData> geoArr = new ArrayList<>();
        Iterator<GeoData> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            GeoData geo = iterator.next();
            geoArr.add(geo);
        }
        return geoArr;
    }

    @Override
    public ArrayList<GeoData> getTrackGeoData(int trackID) {
        Iterable<GeoData> iterable = geoTrack.findAll();
        ArrayList<GeoData> geoArr = new ArrayList<>();
        Iterator<GeoData> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            GeoData geo = iterator.next();
            if (geo.getStrecken_id() == trackID) {
                geoArr.add(geo);
            }
        }
        System.out.println(geoArr.size());
        return geoArr;
    }

    @Override
    public ArrayList<GeoData> getPartGeoData(int from, int till) {
        Iterable<GeoData> iterable = geoTrack.findAll();
        boolean isright = true;
        if (from > till) {
            isright = false;
        }
        ArrayList<GeoData> geoArr = new ArrayList<>();
        Iterator<GeoData> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            GeoData geo = iterator.next();
            int currKm = geo.getTrack_km();
            if (isright) {
                if (currKm > from && currKm < till) {
                    geoArr.add(geo);
                }
            } else {
                if (currKm < from && currKm > till) {
                    geoArr.add(geo);
                }
            }
        }
        System.out.println(geoArr.size());
        return geoArr;
    }
}
