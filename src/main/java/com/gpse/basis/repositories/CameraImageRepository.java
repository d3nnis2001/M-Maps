package com.gpse.basis.repositories;

import com.gpse.basis.domain.CameraImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Camera image repository.
 */
@Repository
public interface CameraImageRepository extends CrudRepository<CameraImage, String> {
}
