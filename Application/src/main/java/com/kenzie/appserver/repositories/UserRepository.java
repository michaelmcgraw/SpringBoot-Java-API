package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.UserRecord;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserRecord, String> {
}
