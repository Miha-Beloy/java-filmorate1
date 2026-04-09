package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Модульные тесты модели User")
class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Создание корректного пользователя")
    void shouldCreateValidUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("good_login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Email не может быть пустым")
    void shouldFailWhenEmailIsBlank() {
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Email должен содержать @")
    void shouldFailWhenEmailWithoutAt() {
        User user = new User();
        user.setEmail("wrong.email");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email должен содержать символ @ и быть корректным", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Логин не может быть пустым")
    void shouldFailWhenLoginIsBlank() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("");
        user.setName("Name");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Логин не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Логин не должен содержать пробелы")
    void shouldFailWhenLoginHasSpaces() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("bad login");
        user.setName("Name");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Логин не должен содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Имя может быть пустым")
    void shouldAllowNameToBeBlank() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("good_login");
        user.setName("");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Дата рождения не в будущем")
    void shouldFailWhenBirthdayInFuture() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Дата рождения не null")
    void shouldFailWhenBirthdayIsNull() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Дата рождения не может быть пустой", violations.iterator().next().getMessage());
    }
}