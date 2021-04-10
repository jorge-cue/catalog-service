package com.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Year;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Created by jhcue on 10/04/2021
 */
public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void whenAllFieldsAreCorrectThenValidationSucceeds() {
        Book book = new Book("1234567890", "Title", "Author", Year.of(2021), 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = new Book("a234567890", "Title", "Author", Year.of(2021), 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must follow the standards ISBN-10 or ISBN-13.");
    }
}
