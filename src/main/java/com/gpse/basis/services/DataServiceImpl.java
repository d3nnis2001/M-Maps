package com.gpse.basis.services;

import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import com.gpse.basis.domain.GleisLageRange;
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

@Service
public class DataServiceImpl implements DataService{
    private static final Lock lock = new ReentrantLock();

    private final MongoTemplate template;

    private List<GleisLageRange> grd;

    @Autowired
    DataServiceImpl(
        MongoTemplate tmp
    ) {
        template = tmp;
        grd = template.findAll(GleisLageRange.class);
    }
    @Override
    public List<Map.Entry<Colors, String>> getNewestColorsforGeoData(List<GeoData> lst) {
        List<Map.Entry<Colors, String>> l = new ArrayList<>();
        int no_threads = 30;
        int sts = lst.size() / no_threads;
        List<Thread> workers = new ArrayList<>(no_threads);
        int index = 0;
        for(int i = 0; i < no_threads - 1; ++i) {
            workers.add(i, new Thread(new Worker(lst.subList(index, index + sts), l, null, null)));
            index += sts;
            workers.get(i).start();
        }
        workers.add(no_threads-1, new Thread(new Worker(lst.subList(index, lst.size()), l, null, null)));
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

    private List<Map.Entry<Colors, String>> getColorForDateRange(List<GeoData> lst, LocalDateTime from, LocalDateTime till) {
        List<Map.Entry<Colors, String>> l = new ArrayList<>();
        int no_threads = 30;
        int sts = lst.size() / no_threads;
        List<Thread> workers = new ArrayList<>(no_threads);
        int index = 0;
        for(int i = 0; i < no_threads - 1; ++i) {
            workers.add(i, new Thread(new Worker(lst.subList(index, index + sts), l, from, till)));
            index += sts;
            workers.get(i).start();
        }
        workers.add(no_threads-1, new Thread(new Worker(lst.subList(index, lst.size()), l, from, till)));
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

    private class Worker implements Runnable {
        List<Map.Entry<Colors, String>> l;
        private List<GeoData> lst;

        private LocalDateTime from = null;
        private LocalDateTime till = null;

        private final MongoTemplate tmpl = new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/project_12"));

        Worker(List<GeoData> lst, List<Map.Entry<Colors, String>> l, LocalDateTime from, LocalDateTime till) {
            this.lst = lst;
            this.l = l;
            this.from = from;
            this.till = till;
        }

        @Override
        public void run() {
            List<Thread> mySubWorkers = new ArrayList<>();
            MatchOperation matchOperation = Aggregation.match(Criteria.where("iDlocation").in(lst.parallelStream().map(GeoData::getId).toList()));
            Aggregation aggregation = Aggregation.newAggregation(matchOperation);
            List<GleisLageDatenpunkt> results = tmpl.aggregate(aggregation, "GleisLageDaten", GleisLageDatenpunkt.class).getMappedResults();
            if(results.isEmpty())
                return;


            lst.forEach(geoDat -> {
                var res = results.parallelStream().filter(w -> Objects.equals(w.getLocation(), geoDat.getId())).toList();
                if(!res.isEmpty()) {
                    List<GleisLageDatenpunkt> v_res = null;
                    if(from != null && till != null) {
                        v_res = res.parallelStream().filter(obj -> {
                            LocalDateTime time = Instant.ofEpochSecond((long) obj.getTime_unix()).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                            return time.isAfter(from) && time.isBefore(till);
                        }).toList();
                        if(v_res.isEmpty())
                            return;
                    }
                    else {
                        double newestTime = res.parallelStream().mapToDouble(GleisLageDatenpunkt::getTime_unix).max().getAsDouble();
                        LocalDateTime maxDate = Instant.ofEpochSecond((long) newestTime).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                        v_res = res.parallelStream().filter(obj -> {
                            LocalDateTime time = Instant.ofEpochSecond((long) obj.getTime_unix()).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
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
                if(!value.isEmpty()) {
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

                if(rslim <= value)
                    c.set(3);
                else if(rs100 <= value)
                    c.set(Math.max(2, c.get()));
                else if(rsa <= value)
                    c.set(Math.max(1, c.get()));
                else
                    c.set(Math.max(0, c.get()));
            });

            if(c.get() == 0) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.NORMAL, g.getId()));
                lock.unlock();
            }
            else if(c.get() == 1) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.LOW, g.getId()));
                lock.unlock();
            }
            else if(c.get() == 2) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.MEDIUM, g.getId()));
                lock.unlock();
            }
            else if(c.get() == 3) {
                lock.lock();
                l.add(new AbstractMap.SimpleEntry<>(Colors.HIGH, g.getId()));
                lock.unlock();
            }


        }
    }

    /**
     * Gets the Colours and ids for each existing GeoPoint
     **/

    public List<Map.Entry<Colors, String>> getGeoDatabyTrackId(int track_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("strecken_id").is(track_id));
        List<GeoData> gd = template.find(query, GeoData.class);
        if(gd.isEmpty())
            return null;
        List<Map.Entry<Colors, String>> col = getNewestColorsforGeoData(gd);
        return col;
    }

    /**
     * Gets the Colours and ids for each existing GeoPoint in a
     * timeframe.
     **/
    public List<Map.Entry<Colors, String>> getGeoDataByDate(int track_id, LocalDateTime from, LocalDateTime till) {
        Query query = new Query();
        query.addCriteria(Criteria.where("strecken_id").is(track_id));
        List<GeoData> gd = template.find(query, GeoData.class);
        if(gd.isEmpty())
            return null;
        System.out.println(gd.getFirst().getId());
        List<Map.Entry<Colors, String>> col = getColorForDateRange(gd, from, till);
        System.out.println("Wir sind im Dataservice: "+col.size());
        return col;
    }
}
