package com.wat.bookviewer.repository;

import com.wat.bookviewer.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Book entity.
 */
@Repository
public interface BookRepository extends MongoRepository<Book,Integer> {

    Book findById(Integer id);

    Page<Book> findAllByAuthor(String author, Pageable pageable);

    @Query("{$limit:100}")
    List<Book> findTop100();

    @Override
    void delete(Book t);

}
