package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Created by jhcue on 10/04/2021
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void whenPostRequestThenBookIsCreated() {
        var book = new Book("1234567890", "Title", "Author", Year.of(2021), 9.90);

        ResponseEntity<Book> response = restTemplate.postForEntity("/api/v1/books", book, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIsbn()).isEqualTo(book.getIsbn());
    }


}
