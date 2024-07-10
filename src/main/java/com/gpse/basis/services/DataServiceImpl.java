package com.gpse.basis.services;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import com.gpse.basis.domain.GleisLageRange;
import com.gpse.basis.repositories.GeoTrackData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * The type Data service.
 */
@Service
public class DataServiceImpl implements DataService {
    /**
     * The constant I_DLOCATION.
     */
    public static final String I_DLOCATION = "iDlocation";
    /**
     * The constant GLEIS_LAGE_DATEN.
     */
    public static final String GLEIS_LAGE_DATEN = "GleisLageDaten";
    private static final Lock lock = new ReentrantLock();
    private final MongoTemplate template;
    private final GeoTrackData geoTrack;

    private List<GleisLageRange> grd;

    /**
     * Instantiates a new Data service.
     *
     * @param tmp      the tmp
     * @param geoTrack the geo track
     */
    @Autowired
    DataServiceImpl(
        MongoTemplate tmp,
        GeoTrackData geoTrack
    ) {
        template = tmp;
        grd = template.findAll(GleisLageRange.class);
        this.geoTrack = geoTrack;
    }

    private class Worker implements Runnable {
        /**
         * The L.
         */
        List<Map.Entry<Colors, String>> l;
        private List<GeoData> lst;

        private LocalDateTime from = null;
        private LocalDateTime till = null;

        private final MongoTemplate tmpl =
            new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/project_12"));

        /**
         * Instantiates a new Worker.
         *
         * @param lst  the lst
         * @param l    the l
         * @param from the from
         * @param till the till
         */
        Worker(List<GeoData> lst, List<Map.Entry<Colors, String>> l, LocalDateTime from, LocalDateTime till) {
            this.lst = lst;
            this.l = l;
            this.from = from;
            this.till = till;
        }

        @Override
        public void run() {
            List<Thread> mySubWorkers = new ArrayList<>();
            MatchOperation matchOperation = Aggregation.match(Criteria.where(I_DLOCATION)
                .in(lst.parallelStream().map(GeoData::getId).toList()));
            Aggregation aggregation = Aggregation.newAggregation(matchOperation);
            List<GleisLageDatenpunkt> results = tmpl.aggregate(aggregation, GLEIS_LAGE_DATEN,
                GleisLageDatenpunkt.class).getMappedResults();
            if (results.isEmpty()) {
                return;
            }


            lst.forEach(geoDat -> {
                var res = results.parallelStream()
                    .filter(w -> Objects.equals(w.getLocation(), geoDat.getId())).toList();
                if (!res.isEmpty()) {
                    List<GleisLageDatenpunkt> v_res = null;
                    if (from != null && till != null) {
                        v_res = res.parallelStream().filter(obj -> {
                            LocalDateTime time = Instant.ofEpochSecond((long) obj.getTime_unix())
                                .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                            return time.isAfter(from) && time.isBefore(till);
                        }).toList();
                        if (v_res.isEmpty()) {
                            return;
                        }
                    } else {
                        double newestTime = res.parallelStream()
                            .mapToDouble(GleisLageDatenpunkt::getTime_unix).max().getAsDouble();
                        LocalDateTime maxDate = Instant.ofEpochSecond((long) newestTime)
                            .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                        v_res = res.parallelStream().filter(obj -> {
                            LocalDateTime time = Instant.ofEpochSecond((long) obj.getTime_unix())
                                .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                            return time.isEqual(maxDate);
                        }).toList();
                    }
                    System.out.println("Debug Size v_res: " + v_res.size());
                    mySubWorkers.add(new Thread(new SubWorker(v_res, geoDat, l)));
                    mySubWorkers.getLast().start();
                }
            });

            mySubWorkers.forEach(w -> {
                try {
                    w.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });


        }
    }

    private class SubWorker implements Runnable {

        private List<GleisLageDatenpunkt> k;

        private List<Map.Entry<Colors, String>> l;

        private GeoData g;


        /**
         * Instantiates a new Sub worker.
         *
         * @param l   the l
         * @param g   the g
         * @param col the col
         */
        SubWorker(List<GleisLageDatenpunkt> l, GeoData g, List<Map.Entry<Colors, String>> col) {
            this.k = l;
            this.g = g;
            this.l = col;
        }
        @Override
        public void run() {
            var max_values = new ConcurrentHashMap<Integer, Double>();
            var grouped = k.parallelStream().collect(Collectors.groupingBy(GleisLageDatenpunkt::getV_zul));
            grouped.entrySet().parallelStream().forEach(entry -> {
                var key = entry.getKey();
                var value = entry.getValue();
                if (!value.isEmpty()) {
                    var max_links = value.parallelStream()
                        .mapToDouble(GleisLageDatenpunkt::getZ_links_railab_3p)
                        .max();
                    var max_rechts = value.parallelStream()
                        .mapToDouble(GleisLageDatenpunkt::getZ_rechts_railab_3p)
                        .max();
                    max_values.put(key, Double.max(max_links.getAsDouble(), max_rechts.getAsDouble()));
                }
            });

            AtomicInteger c = new AtomicInteger();

            /*
            for (ConcurrentHashMap.Entry<Integer, Double> entry : max_values.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            */

            max_values.entrySet().parallelStream().forEach(entry -> {
                var key = entry.getKey();
                var value = entry.getValue();

                double rsa = grd.get(0).getValueToVelocity(key);
                double rs100 = grd.get(1).getValueToVelocity(key);
                double rslim = grd.get(2).getValueToVelocity(key);

                if (rslim <= value) {
                    c.set(3);
                } else if (rs100 <= value) {
                    c.set(Math.max(2, c.get()));
                } else if (rsa <= value) {
                    c.set(Math.max(1, c.get()));
                } else {
                    c.set(Math.max(0, c.get()));
                }
            });

            if (c.get() == 0) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.NORMAL, g.getId()));
                lock.unlock();
            } else if (c.get() == 1) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.LOW, g.getId()));
                lock.unlock();
            } else if (c.get() == 2) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.MEDIUM, g.getId()));
                lock.unlock();
            } else if (c.get() == 3) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.HIGH, g.getId()));
                lock.unlock();
            }


        }
    }

    @Override
    public List<Map.Entry<Colors, String>> getNewestColorsforGeoData(List<GeoData> lst) {
        List<Map.Entry<Colors, String>> l = new ArrayList<>();
        int no_threads = 30;
        int sts = lst.size() / no_threads;
        List<Thread> workers = new ArrayList<>(no_threads);
        int index = 0;
        for (int i = 0; i < no_threads - 1; ++i) {
            workers.add(i, new Thread(new Worker(lst.subList(index, index + sts), l, null, null)));
            index += sts;
            workers.get(i).start();
        }
        workers.add(no_threads - 1, new Thread(new Worker(lst.subList(index, lst.size()), l, null, null)));
        workers.getLast().start();

        workers.forEach(w -> {
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return l;
    }

    private List<Map.Entry<Colors, String>> getColorForDateRange(List<GeoData> lst,
                                                                 LocalDateTime from, LocalDateTime till) {
        List<Map.Entry<Colors, String>> l = new ArrayList<>();
        int no_threads = 30;
        int sts = lst.size() / no_threads;
        List<Thread> workers = new ArrayList<>(no_threads);
        int index = 0;
        for (int i = 0; i < no_threads - 1; ++i) {
            workers.add(i, new Thread(new Worker(lst.subList(index, index + sts), l, from, till)));
            index += sts;
            workers.get(i).start();
        }
        workers.add(no_threads - 1, new Thread(new Worker(lst.subList(index, lst.size()), l, from, till)));
        workers.getLast().start();

        workers.forEach(w -> {
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return l;
    }
    /**
     * Gets Heatmap Data with Color.
     * @return Color with Datapoint.
     */
    public List<Map.Entry<Colors, String>> getHeatmap() {
        List<GeoData> gd = template.findAll(GeoData.class);
        if (gd.isEmpty()) {
            return null;
        }
        List<Map.Entry<Colors, String>> col = getNewestColorsforGeoData(gd);
        return col;
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
    /**
     * Gets Heatmap Data with Color with filter on time.
     * @param track_id is track id.
     * @param from is start time.
     * @param till is end time.
     * @return Heatmap data.
     */
    public List<Map.Entry<Colors, String>> getGeoDataByDate(int track_id, LocalDateTime from, LocalDateTime till) {
        if (from.isAfter(till)) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("strecken_id").is(track_id));
        List<GeoData> gd = template.find(query, GeoData.class);
        if (gd.isEmpty()) {
            return null;
        }
        System.out.println(gd.getFirst().getId());
        List<Map.Entry<Colors, String>> col = getColorForDateRange(gd, from, till);
        return col;
    }
    /**
     * Gets Geo data with track id.
     * @param id is track id.
     * @return Geo data of track id.
     */
    public Double[] getDataForGeoPart(String id) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where(I_DLOCATION).is(id));
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        List<GleisLageDatenpunkt> results = template
            .aggregate(aggregation, GLEIS_LAGE_DATEN, GleisLageDatenpunkt.class).getMappedResults();
        if (!results.isEmpty()) {
            double max_left = Math.round(results.parallelStream()
                .mapToDouble(GleisLageDatenpunkt::getZ_links_railab_3p).max().getAsDouble() * 100.0) / 100.0;
            double max_right = Math.round(results.parallelStream()
                .mapToDouble(GleisLageDatenpunkt::getZ_rechts_railab_3p).max().getAsDouble() * 100.0) / 100.0;
            double vul_zul = Math.round(results.parallelStream()
                .mapToDouble(GleisLageDatenpunkt::getV_zul).average().getAsDouble() * 100.0) / 100.0;
            double vul = Math.round(results.parallelStream()
                .mapToDouble(GleisLageDatenpunkt::getGeschwindigkeit).average().getAsDouble() * 100.0) / 100.0;
            return new Double[] {max_left, max_right, vul_zul, vul};
        }
        return null;
    }
}
