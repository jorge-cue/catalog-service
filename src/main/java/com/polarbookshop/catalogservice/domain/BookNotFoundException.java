package com.polarbookshop.catalogservice.domain;

/*
 * Created by jhcue on 09/04/2021
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Book with ISBN " + isbn + " not found.");
    }
}
