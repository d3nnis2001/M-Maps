package com.gpse.basis.repositories;

import com.gpse.basis.domain.InspectionOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionOrderRepository extends CrudRepository<InspectionOrder, String> {
}
