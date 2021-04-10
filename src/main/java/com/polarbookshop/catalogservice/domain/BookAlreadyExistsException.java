package com.polarbookshop.catalogservice.domain;

/*
 * Created by jhcue on 09/04/2021
 */
public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("Book with ISBN " + isbn + " already exists.");
    }
}
