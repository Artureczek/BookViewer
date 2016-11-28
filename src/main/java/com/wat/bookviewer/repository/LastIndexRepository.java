package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.LastIndex;

import com.wat.bookviewer.domain.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the LastIndex entity.
 */
public interface LastIndexRepository extends MongoRepository<LastIndex,String> {

    LastIndex findOneByTable(String table);

}
