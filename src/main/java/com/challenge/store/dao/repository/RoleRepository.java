package com.challenge.store.dao.repository;

import java.util.Optional;

import com.challenge.store.constant.RoleEnum;
import com.challenge.store.dao.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleEnum name);
}
