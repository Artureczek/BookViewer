package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.Rate;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Rate entity.
 */
@SuppressWarnings("unused")
public interface RateRepository extends MongoRepository<Rate,Integer> {

}
