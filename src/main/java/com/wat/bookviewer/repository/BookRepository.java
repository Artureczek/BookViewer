package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.Book;

import com.wat.bookviewer.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data MongoDB repository for the Book entity.
 */
@Repository
public interface BookRepository extends MongoRepository<Book,Integer> {


    List<Book> findByYear(Long year, Pageable pageable);

    Book findById(Integer id);

    @Query("{$limit:100}")
    List<Book> findTop100();

    @Override
    void delete(Book t);

}
