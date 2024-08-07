package com.challenge.store.dao.repository;

import java.util.List;

import com.challenge.store.dao.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findAllById(Iterable<String> ids);
}
