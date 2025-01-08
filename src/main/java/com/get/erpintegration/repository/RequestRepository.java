package com.get.erpintegration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.get.erpintegration.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByStatus(String status);
}