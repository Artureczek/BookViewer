package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.Publisher;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Publisher entity.
 */

public interface PublisherRepository extends MongoRepository<Publisher,Integer> {

}
