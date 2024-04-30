package com.gpse.basis.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GleisLageDatenRepository extends CrudRepository<GleisLageDatenpunkt, String> {
}
