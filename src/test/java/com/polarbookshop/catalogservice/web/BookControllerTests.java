package com.polarbookshop.catalogservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Created by jhcue on 09/04/2021
 */
@WebMvcTest(BookController.class)
class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGet() throws Exception {
        Book book = new Book("1234567891", "Test Book", "Test Case", Year.of(2021), 24.99);
        given(bookService.viewBookList()).willReturn(List.of(book));
        mockMvc.perform(get("/api/v1/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].isbn").value("1234567891"))
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[0].author").value("Test Case"))
                .andExpect(jsonPath("$[0].publishingYear").value("2021"))
                .andExpect(jsonPath("$[0].price").value("24.99"));
    }

    @Test
    void testGetByIsbn_Found_ReportsStatus200() throws Exception {
        final String isbn = "1234567891";
        Book book = new Book(isbn, "Test Book", "Test Case", Year.of(2021), 24.99);
        given(bookService.viewBookDetails(isbn)).willReturn(Optional.of(book));
        mockMvc.perform(get("/api/v1/books/{isbn}", isbn))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value("1234567891"))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Case"))
                .andExpect(jsonPath("$.publishingYear").value("2021"))
                .andExpect(jsonPath("$.price").value("24.99"));
    }

    @Test
    void testGetByIsbn_NotFound_ReportsStatus404() throws Exception {
        final String isbn = "1234567891";
        given(bookService.viewBookDetails(isbn)).willReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/books/{isbn}", isbn))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book with ISBN " + isbn + " not found."));
    }

    @Test
    void testPost_withValidationError_ReportStatusBadRequest() throws Exception {
        var book = new Book("123ABC456Z", "", "Jon Snow", Year.of(1394), 9.90);
        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(book);
        var result = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        var responseContent = result.getResponse().getContentAsString();
        assertAll(
                () -> assertTrue(responseContent.contains("The ISBN format must follow standards ISBN-10 or ISBN-13."),
                        "The ISBN format must follow standards ISBN-10 or ISBN-13."),
                () -> assertTrue(responseContent.contains("The book title must be defined."),
                        "The book title must be defined.")
        );
    }

    @Test
    void testPost_withValidRequest_RespondsCreated() throws Exception {
        final String isbn = "1234567891";
        Book book = new Book(isbn, "Test Book", "Test Case", Year.of(2021), 24.99);
        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(book);
        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(json));
        verify(bookService).addBookToCatalog(eq(book));
    }
}