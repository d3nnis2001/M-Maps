package com.gpse.basis.domain;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetRepository extends CrudRepository<DataSet, String> {
}
