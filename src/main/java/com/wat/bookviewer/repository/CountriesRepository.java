package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.Countries;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Countries entity.
 */
@SuppressWarnings("unused")
public interface CountriesRepository extends MongoRepository<Countries,Integer> {

}
