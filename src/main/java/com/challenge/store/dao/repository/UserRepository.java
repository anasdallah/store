package com.challenge.store.dao.repository;

import java.util.Optional;

import com.challenge.store.dao.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

}
