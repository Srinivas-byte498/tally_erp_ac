package com.get.erpintegration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.get.erpintegration.model.Company;
import java.util.Optional;

public interface CompanyRepository extends MongoRepository<Company, String> {
    Optional<Company> findByName(String name);
}
