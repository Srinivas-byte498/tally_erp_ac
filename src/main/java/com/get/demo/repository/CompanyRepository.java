package com.get.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.get.demo.model.Company;
import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Optional<Company> findByName(String name);
}
