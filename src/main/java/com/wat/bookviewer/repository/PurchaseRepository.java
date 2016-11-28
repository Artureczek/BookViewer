package com.wat.bookviewer.repository;
import com.wat.bookviewer.domain.Purchase;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Purchase entity.
 */

public interface PurchaseRepository extends MongoRepository<Purchase,Integer> {

    @Override
    void delete(Purchase t);

    List<Purchase> findAllByUserId(String userId);


}
