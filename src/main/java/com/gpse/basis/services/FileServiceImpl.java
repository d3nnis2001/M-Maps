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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;


/**
 * The type File service.
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * The constant LLH_DAT.
     */
    public static final String LLH_DAT = ".LLH.dat";
    /**
     * The constant BAG.
     */
    public static final String BAG = ".bag";
    /**
     * The constant MISSING.
     */
    public static final String MISSING = "missing";
    /**
     * The constant MISSING_TRACK_ID.
     */
    public static final String MISSING_TRACK_ID = "Missing Track_ID";
    /**
     * The constant CAMERA.
     */
    public static final String CAMERA = "camera";
    /**
     * The constant FILE_NAME.
     */
    public static final String FILE_NAME = "fileName";
    /**
     * The constant CSV.
     */
    public static final String CSV = ".csv";
    /**
     * The constant MONGODB_LOCALHOST_27017_PROJECT_12.
     */
    public static final String MONGODB_LOCALHOST_27017_PROJECT_12 = "mongodb://localhost:27017/project_12";
    /**
     * The constant ALL.
     */
    public static final String ALL = "all";
    /**
     * The constant I_DLOCATION.
     */
    public static final String I_DLOCATION = "iDlocation";
    /**
     * The constant ID.
     */
    public static final String ID = "_id";
    /**
     * The constant DATA_SETID.
     */
    public static final String DATA_SETID = "dataSetid";
    /**
     * The constant GLEISLAGEDATEN.
     */
    public static final String GLEISLAGEDATEN = "GleisLageDaten";
    private final DataSetRepository datasetRepro;

    private final GleisLageDatenRepository glDatenRepro;

    private final GeoTrackData geoTrack;

    private final GleisVDataRepository gleisV;

    private MongoTemplate template;

    private final DataService dService;

    private final RosBagService rosService;

    /**
     * Instantiates a new File service.
     *
     * @param repro the repro
     * @param gt    the gt
     * @param rpr   the rpr
     * @param tmp   the tmp
     * @param rprr  the rprr
     * @param dstr  the dstr
     * @param ros   the ros
     */
    @Autowired
    FileServiceImpl(DataSetRepository repro, GeoTrackData gt, GleisLageDatenRepository rpr,
                    MongoTemplate tmp, GleisVDataRepository rprr, DataService dstr, RosBagService ros) {
        datasetRepro = repro;
        glDatenRepro = rpr;
        geoTrack = gt;
        template = tmp;
        gleisV = rprr;
        dService = dstr;
        rosService = ros;
    }

    @Override
    public List<FileUploadResponse> handleImport(List<String> paths, List<String> streckenIds) {
        Iterator<String> itr1 = paths.iterator();
        Iterator<String> itr2 = streckenIds.iterator();

        List<FileUploadResponse> rsp = new ArrayList<>();

        while (itr1.hasNext() && itr2.hasNext()) {
            String filepath = itr1.next();
            String streckenId = itr2.next();

            File file = new File(filepath);

            if (file.getName().endsWith(LLH_DAT)) {
                try {
                    saveLHHFile(file);
                } catch (IndexOutOfBoundsException | IOException e) {
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!!"));
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
                continue;
            }

            if (file.getName().endsWith(BAG)) {

                    if (Objects.equals(streckenId, MISSING)) {
                        rsp.add(new FileUploadResponse(file.getName(), false, MISSING_TRACK_ID));
                    } else {
                        if (file.getName().contains(CAMERA)) {
                            var k = rosService
                                .saveCameraImagesForTrack(Integer.parseInt(streckenId), file.getAbsolutePath());
                            var kk = rosService
                                .saveInfraRedImagesForTrack(Integer.parseInt(streckenId), file.getAbsolutePath());

                            Date uploadDate = new Date();
                            DataSet st = new DataSet();
                            st.setFileName(file.getAbsolutePath());
                            st.setStreckenId(Integer.parseInt(streckenId));
                            st.setUploadDate(uploadDate);
                            datasetRepro.save(st);

                            st = template.find(new Query().addCriteria(Criteria.where(FILE_NAME)
                                .is(file.getAbsolutePath())), DataSet.class).getFirst();

                            if (!k.isEmpty()) {
                                for (var image : k) {
                                    image.setDataSetid(st.getId());
                                    template.save(image);
                                }
                            }
                            if (!kk.isEmpty()) {
                                for (var image : kk) {
                                   image.setDataSetid(st.getId());
                                    template.save(image);
                                }
                            }
                        } else {
                            Date uploadDate = new Date();
                            DataSet st = new DataSet();
                            st.setFileName(file.getAbsolutePath());
                            st.setStreckenId(Integer.parseInt(streckenId));
                            st.setUploadDate(uploadDate);
                            datasetRepro.save(st);
                        }
                        rsp.add(new FileUploadResponse(file.getName(), true, ""));
                    }

                continue;
            }

            if (file.getName().endsWith(CSV)) {
                try {
                    saveCsvFile(file);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    rsp.add(new FileUploadResponse(file.getName(), false, "Fehlerhafte Datei!!!"));
                }
                rsp.add(new FileUploadResponse(file.getName(), true, ""));
                continue;
            }

            if (!checkFileName(file.getName())) {
                if (Objects.equals(streckenId, "missing!")) {
                    rsp.add(new FileUploadResponse(file.getName(), false, MISSING_TRACK_ID));
                } else {
                    try {
                        String str = saveFile(file, Integer.parseInt(streckenId));
                        if (str != null) {
                            rsp.add(new FileUploadResponse(file.getName(), false, str));
                            continue;
                        }

                    } catch (IndexOutOfBoundsException e) {
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
            } else {
                try {
                    String str = saveFile(file, Objects.equals(streckenId, MISSING)
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
        while ((line = reader.readLine()) != null) {
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
                columns[6],
                ""
            ));
        }
        gleisV.saveAll(lst);
        Date uploadDate = new Date();

        DataSet st = new DataSet();
        st.setFileName(file.getName());
        st.setStreckenId(lst.getFirst().getStr_Nr());
        st.setUploadDate(uploadDate);
        datasetRepro.save(st);
        st = template.find(new Query()
            .addCriteria(Criteria.where(FILE_NAME).is(file.getName())), DataSet.class).getFirst();
        for (var v : lst) {
            v.setDatasetId(st.getId());
        }
        gleisV.saveAll(lst);
    }

    private int extractStreckeId(String fileName) {
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
    /**
     * Saves LLH-file in MongoDB.
     * @param file file to store.
     */
    public void saveLHHFile(File file) throws IOException, IndexOutOfBoundsException {
        List<GeoData> lst = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            lst.add(new GeoData(Integer.parseInt(parts[0]),
                                Double.parseDouble(parts[1]),
                                Double.parseDouble(parts[2]),
                                Integer.parseInt(parts[4]) / 1000.00,
                ""));
        }
        geoTrack.saveAll(lst);
        Date uploadDate = new Date();

        DataSet st = new DataSet();
        st.setFileName(file.getName());
        st.setStreckenId(lst.get(0).getStrecken_id());
        st.setUploadDate(uploadDate);
        datasetRepro.save(st);
        st = template.find(new Query()
            .addCriteria(Criteria.where(FILE_NAME).is(file.getName())), DataSet.class).getFirst();
        for (var i : lst) {
            i.setDataSetid(st.getId());
        }
        geoTrack.saveAll(lst);
    }

    /**
     * Save file string.
     *
     * @param file       the file
     * @param streckenId the strecken id
     * @return the string
     * @throws IOException               the io exception
     * @throws IndexOutOfBoundsException the index out of bounds exception
     * @throws RuntimeException          the runtime exception
     */
    public String saveFile(File file, int streckenId) throws IOException, IndexOutOfBoundsException, RuntimeException {
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
                lst.add(new GleisLageDatenpunkt((Double) values.get(0),
                    (Double) values.get(1), (Double) values.get(2), (Double) values.get(3),
                    (Double) values.get(4), st.getId(), null, -1));
                row = reader.read();
            }
            //glDatenRepro.saveAll(lst);
            System.out.println(lst.size());
            int no_threads = 30;
            int sts = lst.size() / no_threads;
            int currentMax = sts;
            int count = 0;
            List<List<GleisLageDatenpunkt>> l = new ArrayList<>();
            l.add(new ArrayList<>());
            int currentIndex = 0;
            while (currentIndex < lst.size()) {
                if (currentIndex < currentMax) {
                    l.get(count).add(lst.get(currentIndex++));
                } else {
                    l.add(new ArrayList<>());
                    count++;
                    currentMax += sts;
                }
            }
            List<Thread> threads = new ArrayList<>();
            Query query = new Query();
            query.addCriteria(Criteria.where("strecken_id").is(st.getStreckenId()));
            List<GeoData> k = template.find(query, GeoData.class);
            if (k.isEmpty()) {
                return "Geo-Daten (LLH) fehlen!";
            }
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("Str_Nr").is(st.getStreckenId()));
            List<GleisVData> kk = template.find(query2, GleisVData.class);
            if (kk.isEmpty()) {
                return "v-zul fehlen!";
            }
            for (int i = 0; i < no_threads; i++) {
                threads.add(new Thread(new Thr(l.get(i), k, kk)));
                threads.get(i).start();
            }


            try {
                for (int i = 0; i < no_threads; i++) {
                    threads.get(i).join();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            st = datasetRepro.save(st);
            return null;
    }

    private static class Thr implements Runnable {
        /**
         * The Batch size.
         */
        final int batchSize = 40000;

        private final List<GleisLageDatenpunkt> lst;

        private final MongoTemplate tmpl1 = new
            MongoTemplate(new SimpleMongoClientDatabaseFactory(MONGODB_LOCALHOST_27017_PROJECT_12));


        private BulkOperations bulkInsertion = tmpl1
            .bulkOps(BulkOperations.BulkMode.UNORDERED, GleisLageDatenpunkt.class);

       private final List<GeoData> l;

       private final List<GleisVData> lst3;

        /**
         * Instantiates a new Thr.
         *
         * @param lt  the lt
         * @param k   the k
         * @param trk the trk
         */
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
        if (searchString.toLowerCase().equals(ALL)) {
            return s;
        } else {
            return s.stream().filter(xt -> xt.getId().contains(searchString)).collect(toList());
        }
    }

    @Override
    public void deleteDataSetsById(List<String> ids) {
        for (var id : ids) {
            Query query = new Query();
            query.addCriteria(Criteria.where(ID).is(new ObjectId(id)));
            var dts = template.find(query, DataSet.class).getFirst();
            if (dts.getFileName().endsWith(".parquet")) {
                Query q = new Query();
                q.addCriteria(Criteria.where(DATA_SETID).is(id));
                template.remove(q, GleisLageDatenpunkt.class);
            } else if (dts.getFileName().endsWith(CSV)) {
                Query q = new Query();
                q.addCriteria(Criteria.where(DATA_SETID).is(id));
                template.remove(q, GleisVData.class);
            } else if (dts.getFileName().endsWith(LLH_DAT)) {
                Query q = new Query();
                q.addCriteria(Criteria.where(DATA_SETID).is(id));
                var lst = template.find(q, GeoData.class);
                q = new Query();
                q.addCriteria(Criteria.where(DATA_SETID).is(id));
                template.remove(q, GeoData.class);
                q = new Query();
                System.out.println("Größe: " + lst.size());
                q.addCriteria(Criteria.where(I_DLOCATION).in(lst.stream().map(GeoData::getId).toList()));
                template.remove(q, GleisLageDatenpunkt.class);
            } else if (dts.getFileName().endsWith(BAG) && dts.getFileName().contains(CAMERA)) {
                Query q = new Query();
                q.addCriteria(Criteria.where(DATA_SETID).is(id));
                var lst = template.find(q, CameraImage.class);
                for (var image : lst) {
                    String str = image.getPath();
                    Pattern pattern = Pattern.compile(Pattern.quote("rosbagPictures") + ".*");
                    Matcher matcher = pattern.matcher(str);
                    String result = matcher.find() ? matcher.group() : "";
                    String path = "../gp-se-ss-2024-team1-2/" + result;

                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                q = new Query();
                q.addCriteria(Criteria.where(DATA_SETID).is(id));
                template.remove(q, CameraImage.class);
            }
            query = new Query();
            query.addCriteria(Criteria.where(ID).is(new ObjectId(id)));
            template.remove(query, DataSet.class);
        }

    }


    /**
     * Gets part heatmap.
     *
     * @param strecke the strecke
     * @param from    the from
     * @param till    the till
     * @return the part heatmap
     */
//return null, if from > till
    public List<Map.Entry<DataService.Colors, String>> getPartHeatmap(int strecke, LocalDateTime from,
                                                                      LocalDateTime till) {
        if (from.isAfter(till)) {
            return null;
        }
         return dService.getGeoDataByDate(strecke, from, till);
    }
    /**
     * Gets data for trackid.
     * @param trackId is track id
     * @return Geo data of track id.
     */
    public ArrayList<String> getDataforId(int trackId) {
        List<DataSet> sets = getDataSets(ALL);
        ArrayList<String> dataIds = new ArrayList<>();
        for (DataSet set : sets) {
            if (set.getStreckenId() == trackId) {
                dataIds.add(set.getId());
            }
        }
        return dataIds;
    }
    /**
     * Gets all Gleislagedaten.
     * @return All Gleislagedaten.
     */
    public ArrayList<GleisLageDatenpunkt> getAllTrackData() {
        Iterable<GleisLageDatenpunkt> iterable = glDatenRepro.findAll();
        ArrayList<GleisLageDatenpunkt> dataPoints = new ArrayList<>();
        Iterator<GleisLageDatenpunkt> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            GleisLageDatenpunkt gld = iterator.next();
            dataPoints.add(gld);
        }
        return dataPoints;
    }


    /**
     * Gets data points for track.
     *
     * @param trackId the track id
     * @return the data points for track
     */
    public List<GleisLageDatenpunkt> getDataPointsForTrack(int trackId) {
        MongoTemplate tmpl = new MongoTemplate(
            new SimpleMongoClientDatabaseFactory(MONGODB_LOCALHOST_27017_PROJECT_12));
        List<GeoData> lst = getTrackGeoData(trackId);
        System.out.println(lst.size() + " Geodata fertig");
        MatchOperation matchOperation = Aggregation.match(Criteria.where(I_DLOCATION)
            .in(lst.parallelStream().map(GeoData::getId).toList()));
        System.out.println("matchoperation fertig");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        System.out.println("aggregation fertig");
        List<GleisLageDatenpunkt> results = tmpl.aggregate(aggregation, GLEISLAGEDATEN,
            GleisLageDatenpunkt.class).getMappedResults();
        System.out.println(results.size());
        return results;
    }
    /**
     * Gets Gleislagedaten for a trackid.
     * @param trackId the track id
     * @return Gleislagedaten for trackid.
     */
    public ArrayList<GleisLageDatenpunkt> getData(int trackId) {
        List<GleisLageDatenpunkt> lst = getDataPointsForTrack(trackId);
        System.out.println(lst.size());
        ArrayList<GleisLageDatenpunkt> dataPoints = new ArrayList<>();
        int i = 0;
        for (GleisLageDatenpunkt gld : lst) {
            GleisLageDatenpunkt gld2 = gld;
            BigDecimal bd = new BigDecimal(Double.toString(gld2.getStr_km()));
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            gld2.setStr_km(bd.doubleValue());
            dataPoints.add(gld2);
            i += 1;
            if (i % 1000 == 0) {
                System.out.println(i);
            }
        }
        return dataPoints;
    }
    /**
     * Gets all Information about Geopoint.
     * @param pointId the point id
     * @return All relevant information for that geopoint.
     */
    public GeoData getPointInformation(String pointId) {
        Iterable<GeoData> iterable = geoTrack.findAll();
        GeoData geoData = null;
        ArrayList<GleisLageDatenpunkt> dataPoints = new ArrayList<>();
        Iterator<GeoData> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            GeoData geo = iterator.next();
            if (geo.getId().equals(pointId)) {
                return geo;
            }
        }
        return null;
    }
    /**
     * Gets all Information about Geopoint.
     * @param pointId the point id
     * @return All relevant information for that geopoint.
     */
    public ArrayList<GleisLageDatenpunkt> getPointData(String pointId) {
        ArrayList<GleisLageDatenpunkt> dataPoints = new ArrayList<>();
        MongoTemplate template1 = new MongoTemplate(
            new SimpleMongoClientDatabaseFactory(MONGODB_LOCALHOST_27017_PROJECT_12)
        );
        MatchOperation matchOperation = Aggregation.match(
            Criteria.where(I_DLOCATION).is(pointId)
        );
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        List<GleisLageDatenpunkt> lst = template1.aggregate(
            aggregation,
            GLEISLAGEDATEN,
            GleisLageDatenpunkt.class).getMappedResults();
        dataPoints.addAll(lst);
        return dataPoints;
    }

    //--------------------------------------------------------------
    /**
     * Returns the all available Geodata from a track.
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
        files.forEach(f -> lst.add(new ArrayList<String>() {
            {
                add(f.getPath());
                add(f.getName());
            }
        }));

        return lst;
    }
}
