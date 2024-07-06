package com.gpse.basis.repositories;

import com.gpse.basis.domain.CameraImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * CameraImageRepository.
 */
@Repository
public interface CameraImageRepository extends CrudRepository<CameraImage, String> {
}
