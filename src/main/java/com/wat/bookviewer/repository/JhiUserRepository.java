package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.JhiUser;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the JhiUser entity.
 */
@SuppressWarnings("unused")
public interface JhiUserRepository extends MongoRepository<JhiUser,String> {

}
