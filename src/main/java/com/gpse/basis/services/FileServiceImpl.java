package com.gpse.basis.services;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;
import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.DataSetRepository;
import com.gpse.basis.repositories.GeoTrackData;
import com.gpse.basis.repositories.GleisLageDatenRepository;
import com.gpse.basis.services.FileService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.model.WriteModel;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final DataSetRepository datasetRepro;

    private final GleisLageDatenRepository glDatenRepro;

    private final GeoTrackData geoTrack;

    private MongoTemplate template;

    Lock lock = new ReentrantLock();
    @Autowired
    FileServiceImpl(DataSetRepository repro, GleisLageDatenRepository rpr, GeoTrackData gt, MongoTemplate tmp) {
        datasetRepro = repro;
        glDatenRepro = rpr;
        geoTrack = gt;
        template = tmp;
    }

    @Override
    public List<FileUploadResponse> handleImport(List<String> paths, List<String> streckenIds) {
        Iterator<String> itr1 = paths.iterator();
        Iterator<String> itr2 = streckenIds.iterator();

        List<FileUploadResponse> rsp = new ArrayList<>();

        while(itr1.hasNext() && itr2.hasNext()) {
            String filepath = itr1.next();
            String streckenId = itr2.next();

            File file = new File(filepath);

            if(file.getName().endsWith(".LLH.dat")) {
                try {
                    saveLHHFile(file);
                } catch (IndexOutOfBoundsException | IOException e) {
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!"));
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
                continue;
            }

            if(!checkFileName(file.getName())) {
                if(Objects.equals(streckenId, "missing")) {
                    rsp.add(new FileUploadResponse(file.getName(), false, "Missing Track_ID"));
                }
                else {
                    try {
                        saveFile(file, streckenId);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!"));
                        continue;
                    } catch(IndexOutOfBoundsException e) {
                        rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Parquet-Format"));
                        continue;
                    } catch(RuntimeException e) {
                        System.out.println(e.getMessage());
                        rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!"));
                        continue;
                    }
                    rsp.add(new FileUploadResponse(file.getName(), true, ""));
                }
            }
            else {
                try {
                    saveFile(file, Objects.equals(streckenId, "missing") ? extractStreckeId(file.getName()) : streckenId);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!"));
                    continue;
                } catch (IndexOutOfBoundsException e) {
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Parquet-Format"));
                    continue;
                } catch(RuntimeException e) {
                    System.out.println(e.getMessage());
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!"));
                    continue;
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
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

    public void saveLHHFile(File file) throws IOException, IndexOutOfBoundsException {
        List<GeoData> lst = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            lst.add(new GeoData(Integer.parseInt(parts[0]),
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Integer.parseInt(parts[4])));
        }
        geoTrack.saveAll(lst);
        Date uploadDate = new Date();

        DataSet st = new DataSet();
        st.setFileName(file.getName());
        st.setStreckenId(Integer.toString(lst.get(0).getStrecken_id()));
        st.setUploadDate(uploadDate);
        datasetRepro.save(st);
    }

    public void saveFile(File file, String streckenId) throws IOException,IndexOutOfBoundsException,RuntimeException {
            Date uploadDate = new Date();

            DataSet st = new DataSet();
            st.setFileName(file.getName());
            st.setStreckenId(streckenId);
            st.setUploadDate(uploadDate);

            System.out.println(st);


            List<GleisLageDatenpunkt> lst = new ArrayList<>();
            final Path path = new Path(file.getPath());
            final Configuration config = new Configuration();
            final ParquetReader<Row> reader = RowParquetReader.builder(HadoopInputFile.fromPath(path, config)).build();
            Row row = reader.read();
            while (row != null) {
                List<Object> values = row.getValues();
                lst.add(new GleisLageDatenpunkt((Double) values.get(0), (Double) values.get(1), (Double) values.get(2), (Double) values.get(3), (Double) values.get(4), st.getId()));
                row = reader.read();
            }
            //glDatenRepro.saveAll(lst);
            System.out.println(lst.size());
            int no_threads = 8;
            int sts = lst.size() / 8;
            int currentMax = sts;
            int count = 0;
            List<List<GleisLageDatenpunkt>> l = new ArrayList<>();
            l.add(new ArrayList<>());
            int currentIndex = 0;
            while(currentIndex < lst.size()) {
                if(currentIndex < currentMax)
                    l.get(count).add(lst.get(currentIndex++));
                else {
                    l.add(new ArrayList<>());
                    count++;
                    currentMax += sts;
                }
            }
            List<Thread> threads = new ArrayList<>();
            for(int i = 0; i < no_threads; i++){
                threads.add(new Thread(new Thr(l.get(i))));
                threads.get(i).run();
                try {
                    threads.get(i).join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            st = datasetRepro.save(st);
    }

    private class Thr implements Runnable {
        int batchSize = 40000;
        private List<GleisLageDatenpunkt> lst;
        private  BulkOperations bulkInsertion = template.bulkOps(BulkOperations.BulkMode.UNORDERED, GleisLageDatenpunkt.class);
        Thr(List<GleisLageDatenpunkt> lt) {lst = lt;}
        @Override
        public void run() {
            int idx = 0;
            while(idx < lst.size()) {
                bulkInsertion.insert(lst.get(idx++));
                if(idx % batchSize == 0) {
                    bulkInsertion.execute();
                    lock.lock();
                    bulkInsertion = template.bulkOps(BulkOperations.BulkMode.UNORDERED, GleisLageDatenpunkt.class);
                    lock.unlock();
                }
            }
            bulkInsertion.execute();
        }
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



    private List<File> getAllFiles(File folder) {
        File[] files = folder.listFiles();
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                fileList.addAll(getAllFiles(file));
            } else {
                fileList.add(file);
            }
        }
        return fileList;
    }
    @Override
    public List<List<String>> readFoler(String path) throws NullPointerException {
        File folder = new File(path);
        List<List<String>> lst = new ArrayList<>();
        List<File> files = getAllFiles(folder);
        files.forEach(f -> lst.add(new ArrayList<String>() {{add(f.getPath()); add(f.getName());}}));
        return lst;
    }
}
