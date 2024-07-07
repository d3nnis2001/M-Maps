package com.gpse.basis.repositories;

import com.gpse.basis.domain.DataSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * DataSetRepository.
 */
@Repository
public interface DataSetRepository extends CrudRepository<DataSet, String> {
}
