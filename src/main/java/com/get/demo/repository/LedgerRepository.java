package com.get.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.get.demo.model.Ledger;
import java.util.Optional;

public interface LedgerRepository extends MongoRepository<Ledger, String> {
    Optional<Ledger> findByName(String name);
}
