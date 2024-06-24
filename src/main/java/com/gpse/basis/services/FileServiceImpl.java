package com.gpse.basis.services;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;
import com.gpse.basis.domain.*;
import com.gpse.basis.repositories.DataSetRepository;
import com.gpse.basis.repositories.GeoTrackData;
import com.gpse.basis.repositories.GleisLageDatenRepository;
import com.gpse.basis.repositories.GleisVDataRepository;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;


@Service
public class FileServiceImpl implements FileService {

    private final DataSetRepository datasetRepro;


    private final GeoTrackData geoTrack;

    private final GleisVDataRepository gleisV;

    private MongoTemplate template;

    private final DataService dService;

    Lock lock = new ReentrantLock();
    @Autowired
    FileServiceImpl(DataSetRepository repro, GeoTrackData gt,
                    MongoTemplate tmp, GleisVDataRepository rprr, DataService dstr) {
        datasetRepro = repro;
        geoTrack = gt;
        template = tmp;
        gleisV = rprr;
        dService = dstr;
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
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!!"));
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
                continue;
            }

            if(file.getName().endsWith(".csv")) {
                try {
                    saveCsvFile(file);
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!!!"));
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
                continue;
            }

            if(!checkFileName(file.getName())) {
                if(Objects.equals(streckenId, "missing!")) {
                    rsp.add(new FileUploadResponse(file.getName(), false, "Missing Track_ID"));
                }
                else {
                    try {
                        String str = saveFile(file, Integer.parseInt(streckenId));
                        if(str != null){
                            rsp.add(new FileUploadResponse(file.getName(), false, str));
                            continue;
                        }

                    } catch(IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                        rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Parquet-Format!"));
                        continue;
                    } catch (IOException | RuntimeException e) {
                        System.out.println(e.getMessage());
                        rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei"));
                        continue;
                    }
                    rsp.add(new FileUploadResponse(file.getName(), true, ""));
                }
            }
            else {
                try {
                    String str = saveFile(file, Objects.equals(streckenId, "missing")
                        ? extractStreckeId(file.getName()) : Integer.parseInt(streckenId));
                    if (str != null) {
                        rsp.add(new FileUploadResponse(file.getName(), false, str));
                        continue;
                    }
                } catch (IndexOutOfBoundsException e) {
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Parquet-Format"));
                    continue;
                } catch (IOException | RuntimeException e) {
                    System.out.println(e.getMessage());
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!"));
                    continue;
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
            }
        }
        return rsp;
    }

    private void saveCsvFile(File file) throws Exception {
        List<GleisVData> lst = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        //read header and skip
        String line = reader.readLine();
        String zeichen = "\\+";
        String comma = ",";
        String dot = ".";
        while((line = reader.readLine()) != null){
            String[] columns = line.split(";");
            String[] von = columns[2].split(zeichen);
            von[0] = von[0].trim();
            von[1] = von[1].trim();
            String[] bis = columns[3].split(zeichen);
            bis[0] = bis[0].trim();
            bis[1] = bis[1].trim();
            lst.add(new GleisVData(
                Integer.parseInt(columns[0]),
                Integer.parseInt(columns[1]),
                Double.parseDouble(von[0].replace(comma, dot)),
                Double.parseDouble((von[1].replace(comma, dot))),
                Double.parseDouble((bis[0].replace(comma, dot))),
                Double.parseDouble((bis[1].replace(comma, dot))),
                Integer.parseInt(columns[4]),
                Integer.parseInt(columns[5]),
                columns[6]
            ));
        }
        gleisV.saveAll(lst);
        Date uploadDate = new Date();

        DataSet st = new DataSet();
        st.setFileName(file.getName());
        st.setStreckenId(lst.getFirst().getStr_Nr());
        st.setUploadDate(uploadDate);
        datasetRepro.save(st);
    }

    private int extractStreckeId(String fileName){
        Pattern pattern = Pattern.compile("Str_\\d+");
        Matcher matcher = pattern.matcher(fileName);
        matcher.find();
        return Integer.parseInt(matcher.group(0).substring(4));
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
                                Integer.parseInt(parts[4]) / 1000.00));
        }
        geoTrack.saveAll(lst);
        Date uploadDate = new Date();

        DataSet st = new DataSet();
        st.setFileName(file.getName());
        st.setStreckenId(lst.get(0).getStrecken_id());
        st.setUploadDate(uploadDate);
        datasetRepro.save(st);
    }

    public String saveFile(File file, int streckenId) throws IOException,IndexOutOfBoundsException,RuntimeException {
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
                lst.add(new GleisLageDatenpunkt((Double) values.get(0), (Double) values.get(1), (Double) values.get(2), (Double) values.get(3), (Double) values.get(4), st.getId(), null, -1));
                row = reader.read();
            }
            System.out.println(lst.size());
            int no_threads = 30;
            int sts = lst.size() / no_threads;
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
            Query query = new Query();
            query.addCriteria(Criteria.where("strecken_id").is(st.getStreckenId()));
            List<GeoData> k = template.find(query, GeoData.class);
            if(k.isEmpty())
                return "Geo-Daten (LLH) fehlen!";
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("Str_Nr").is(st.getStreckenId()));
            List<GleisVData> kk = template.find(query2, GleisVData.class);
            if(kk.isEmpty())
                return "v-zul fehlen!";
            for(int i = 0; i < no_threads; i++){
                threads.add(new Thread(new Thr(l.get(i), k, kk)));
                threads.get(i).start();
            }


            try {
                for (int i = 0; i < no_threads; i++)
                    threads.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            st = datasetRepro.save(st);
            return null;
    }

    private static class Thr implements Runnable {
        final int batchSize = 40000;

        private final List<GleisLageDatenpunkt> lst;

        private final MongoTemplate tmpl1 = new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/project_12"));


        private BulkOperations bulkInsertion = tmpl1.bulkOps(BulkOperations.BulkMode.UNORDERED, GleisLageDatenpunkt.class);

       private final List<GeoData> l;

       private final List<GleisVData> lst3;

        Thr(List<GleisLageDatenpunkt> lt, List<GeoData> k, List<GleisVData> trk) {
            lst = lt;
            l = k;
            lst3 = trk;
        }

        @Override
        public void run() {
            findGeoDataIfExistent();
            save();
        }

        private void save() {
            int idx = 0;
            while (idx < lst.size()) {
                bulkInsertion.insert(lst.get(idx++));
                if (idx % batchSize == 0) {
                    bulkInsertion.execute();
                    bulkInsertion = tmpl1.bulkOps(BulkOperations.BulkMode.UNORDERED, GleisLageDatenpunkt.class);
                }
            }
            bulkInsertion.execute();
        }
        private void findGeoDataIfExistent() {
            lst.parallelStream()
                .forEach(gld -> {
                    GeoData closestElements = l.parallelStream()
                        .min((geo1, geo2) -> {
                            double distance1 = Math.abs(geo1.getTrack_km() - gld.getStr_km());
                            double distance2 = Math.abs(geo2.getTrack_km() - gld.getStr_km());
                            return Double.compare(distance1, distance2);
                        })
                        .orElse(null);
                    gld.setLocation(closestElements.getId());
                    Optional<GleisVData> result = lst3.stream()
                        .filter(obj -> obj.getVon_km() <= gld.getStr_km() && gld.getStr_km() <= obj.getBis_km())
                        .findFirst();
                    gld.setV_zul(result.get().getV_zul());
                });
        }

    }


    @Override
    public List<DataSet> getDataSets(String searchString) {
        List<DataSet> s = new ArrayList<>();
        datasetRepro.findAll().forEach(s::add);
        if (searchString.toLowerCase().equals("all")) {
            return s;
        } else {
            return s.stream().filter(xt -> xt.getId().contains(searchString)).collect(toList());
        }
    }

    @Override
    public void deleteDataSetsById(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        template.remove(query, "dataSets");

        query = new Query();
        query.addCriteria(Criteria.where("dataSetid").in(ids));
        template.remove(query, "GleisLageDaten");
    }
    /**
     * Returns the all available Geodata
     **/
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

    /**
     * Returns the all available Geodata from a track
     **/
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
    public List<Map.Entry<DataService.Colors, String>> getPartGeoData(int from, int till) {
        Iterable<GeoData> iterable = geoTrack.findAll();
        boolean isright = true;
        if (from > till) {
            isright = false;
        }
        ArrayList<GeoData> geoArr = new ArrayList<>();
        Iterator<GeoData> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            GeoData geo = iterator.next();
            double currKm = geo.getTrack_km();
            if (isright) {
                if (currKm >= from && currKm <= till) {
                    geoArr.add(geo);
                }
            } else {
                if (currKm <= from && currKm >= till) {
                    geoArr.add(geo);
                }
            }
        }
        System.out.println(geoArr.size());
        List<Map.Entry<DataService.Colors, String>> ltg = dService.getNewestColorsforGeoData(geoArr);
        return ltg;
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

        //todo: löschen
        dService.getGeoDatabyTrackId(6100);

        return lst;
    }

    //return null, if from > till
    public List<Map.Entry<DataService.Colors, String>> getPartHeatmap(int strecke, LocalDateTime from, LocalDateTime till) {
        if (from.isAfter(till)) {
            return null;
        }
         return dService.getGeoDataByDate(strecke, from, till);
    }
}
