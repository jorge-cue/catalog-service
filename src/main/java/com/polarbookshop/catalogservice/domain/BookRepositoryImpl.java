package com.polarbookshop.catalogservice.domain;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Created by jhcue on 09/04/2021
 */
@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final Map<String, Book> books = new ConcurrentHashMap<>();

    @Override
    public Collection<Book> findAll() {
        return books.values();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    @Override
    public Book save(Book book) {
        books.put(book.getIsbn(), book);
        return book;
    }

    @Override
    public void deleteByIsbn(String isbn) {
        books.remove(isbn);
    }

    @Override
    public Book update(String isbn, Book bookToUpdate) {
        return null;
    }
}
