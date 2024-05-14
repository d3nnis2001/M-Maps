package com.gpse.basis.services;

import com.gpse.basis.domain.DataSet;
import com.gpse.basis.domain.GeoData;
import com.gpse.basis.domain.GleisLageDatenpunkt;
import com.gpse.basis.repositories.GeoTrackData;
import com.gpse.basis.repositories.GleisLageDatenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeoDataServiceImpl implements GeoDataService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public GeoDataServiceImpl( final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public int getGleisColour(int id, double start, double end) {
        Criteria criteria = Criteria.where("streckenId").is(Integer.toString(id));
        Query query = new Query(criteria);
        List<DataSet> result = mongoTemplate.find(query, DataSet.class);
        List<String> dataSetIds = result.stream().map(DataSet::getId).collect(Collectors.toList());

        Criteria gleisCriteria = Criteria.where("dataSetid").in(dataSetIds).and("str_km").gte(start).lte(end);
        Query gleisQuery = new Query(gleisCriteria);
        List<GleisLageDatenpunkt> gleisLageDatenpunkte = mongoTemplate.find(gleisQuery, GleisLageDatenpunkt.class);
        int colour = 0;
        for (GleisLageDatenpunkt punkt : gleisLageDatenpunkte) {
            double p1 = punkt.getZ_links_railab_3p();
            double p2 = punkt.getZ_rechts_railab_3p();
            if (p1 >= 21 || p2 >= 21){
                return 2;
            }
            else if (p1 >= 12 || p2 >= 12){
                colour = 1;
            }
        }

        return colour;
    }

    @Override
    public ArrayList<GeoData> getAllGeoPoints() {
        System.out.println("Hier!");
        List<GeoData> lst =  mongoTemplate.findAll(GeoData.class);
        System.out.println("Hallo!");
        System.out.println(lst.size());
        return new ArrayList<>(lst);
    }
}
