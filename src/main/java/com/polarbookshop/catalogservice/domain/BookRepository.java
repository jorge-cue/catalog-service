package com.polarbookshop.catalogservice.domain;

import java.util.Collection;
import java.util.Optional;

/*
 * Created by jhcue on 09/04/2021
 */
public interface BookRepository {
    Collection<Book> findAll();
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Book save(Book book);
    void deleteByIsbn(String isbn);
    Book update(String isbn, Book bookToUpdate);
}
