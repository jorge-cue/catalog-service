package com.polarbookshop.catalogservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonbTester;

/*
 * Created by jhcue on 10/04/2021
 */
@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;



}
