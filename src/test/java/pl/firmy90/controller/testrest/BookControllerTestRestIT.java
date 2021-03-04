package pl.firmy90.controller.testrest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import pl.firmy90.model.Book;
import pl.firmy90.service.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTestRestIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnListOfAvailableBooks() {
        //given
        List<Book> bookList = new ArrayList<>();
        Book book = Book
                .builder()
                .id(1000)
                .isbn("9780132350884")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .author("Robert C. Martin")
                .type("programming")
                .build();

        //when
        ResponseEntity<BookService> response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/api/books/", BookService.class);

        //then
        assertThat(response).isNotNull();
        List<Book> availableBooks = Objects.requireNonNull(response.getBody()).getAvailableBooks();
        assertThat(availableBooks).isNotNull();
        assertThat(availableBooks).contains(book);

    }

    @Test
    public void shouldAddNewBook() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Book book = Book.builder()
                .id(1003)
                .isbn("9780134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .type("programming")
                .build();
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        //when
        ResponseEntity<Book> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/", request, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(book);

    }

    @Test
    public void shouldReturnBookExistsExceptionWhenTryAddBookWithExistedBookId() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Book book = Book.builder()
                .id(1003)
                .isbn("9780134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .type("programming")
                .build();
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        //when
        this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/", request, Book.class);
        ResponseEntity<Book> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/", request, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBook() {
        //given
        int bookId = 1002;
        Book book = Book.builder()
                .id(1002)
                .isbn("9788324627738")
                .title("Rusz glowa Java")
                .author("Sierra Kathy, Bates Bert")
                .type("programming")
                .build();

        //when
        ResponseEntity<Book> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/api/books/" + bookId, Book.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(book);

    }

    @Test
    public void shouldReturnBookNotFoundExceptionWhenBookDoesntExist() {
        //given
        int bookId = 999;

        //when
        ResponseEntity<Book> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/books/" + bookId, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void shouldReturnBookNotFoundWhenTryToEditNotExistedBook() {
        //given
        int bookId = 999;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Book book = Book.builder()
                .id(999)
                .isbn("9788324627738")
                .title("Rusz glowa Java Druga Edycja")
                .author("Sierra Kathy, Bates Bert")
                .type("programming")
                .build();
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        //when
        ResponseEntity<Book> response = this.restTemplate.exchange("http://localhost:" + port + "/api/books/" + bookId, HttpMethod.PUT, request, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void shouldEditBook() {
        //given
        int bookId = 1002;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Book book = Book.builder()
                .id(1002)
                .isbn("9788324627738")
                .title("Rusz glowa Java Druga Edycja")
                .author("Sierra Kathy, Bates Bert")
                .type("programming")
                .build();
        HttpEntity<Book> request = new HttpEntity<>(book, headers);

        //when
        ResponseEntity<Book> response = this.restTemplate.exchange("http://localhost:" + port + "/api/books/" + bookId, HttpMethod.PUT, request, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(book);
    }

    @Test
    public void shouldDeleteBook() {
        //given
        int bookId = 1001;

        //when
        ResponseEntity<Book> response = this.restTemplate.exchange("http://localhost:" + port + "/api/books/" + bookId, HttpMethod.DELETE, null, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

    }

    @Test
    public void shouldReturnBookNotFoundWhenTryToDeleteNotExistedBook() {
        //given
        int bookId = 999;

        //when
        ResponseEntity<Book> response = this.restTemplate.exchange("http://localhost:" + port + "/api/books/" + bookId, HttpMethod.DELETE, null, Book.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}