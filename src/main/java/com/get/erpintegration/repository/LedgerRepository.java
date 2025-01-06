package com.get.erpintegration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.get.erpintegration.model.Ledger;
import java.util.Optional;

public interface LedgerRepository extends MongoRepository<Ledger, String> {
    Optional<Ledger> findByName(String name);
}
