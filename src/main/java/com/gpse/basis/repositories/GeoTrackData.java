package com.gpse.basis.repositories;

import com.gpse.basis.domain.GeoData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoTrackData extends CrudRepository<GeoData, String> {
}
