package com.challenge.store.dao.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @MongoId
    private String id;

    private String username;

    private String password;

    private Set<Role> roles = new HashSet<>();

    @CreatedDate
    private Instant createdDate;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
