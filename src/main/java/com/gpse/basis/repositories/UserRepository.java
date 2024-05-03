package com.gpse.basis.repositories;

import com.gpse.basis.domain.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModel, String> {
}
