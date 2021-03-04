package pl.firmy90.controller.testrest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import pl.firmy90.model.Book;
import pl.firmy90.model.Lending;
import pl.firmy90.model.LendingRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LendingControllerTestRestIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void shouldLendBook() {
        //given
        int bookId = 1001;
        String username = "oliviajones";
        Book book = Book
                .builder()
                .id(1001)
                .isbn("978832463176")
                .title("Thinking in Java")
                .author("Bruce Eckel")
                .type("programming")
                .build();
        LendingRequest lendingRequest = new LendingRequest();
        Lending lending = Lending.builder().book(book).lenderUsername(username).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        lendingRequest.setLenderUsername(username);
        HttpEntity<LendingRequest> request = new HttpEntity<>(lendingRequest, headers);

        //when
        ResponseEntity<Lending> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", request, Lending.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(lending);
    }


    @Test
    public void shouldReturnBookNotFoundWhenTryToLendUnavailableBook(){
        //given
        int bookId = 1001;
        String username = "oliviajones";
        Book book = Book
                .builder()
                .id(1001)
                .isbn("978832463176")
                .title("Thinking in Java")
                .author("Bruce Eckel")
                .type("programming")
                .build();
        LendingRequest lendingRequest = new LendingRequest();
        Lending lending = Lending.builder().book(book).lenderUsername(username).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        lendingRequest.setLenderUsername(username);
        HttpEntity<LendingRequest> request = new HttpEntity<>(lendingRequest, headers);

        //when
        this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", request, Lending.class);
        ResponseEntity<Lending> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", request, Lending.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldGetLending() {
        //given
        int bookId = 1002;
        String username = "oliviajones";
        Book book = Book
                .builder()
                .id(1001)
                .isbn("978832463176")
                .title("Thinking in Java")
                .author("Bruce Eckel")
                .type("programming")
                .build();
        LendingRequest lendingRequest = new LendingRequest();
        Lending lending = Lending.builder().book(book).lenderUsername(username).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        lendingRequest.setLenderUsername(username);
        HttpEntity<LendingRequest> request = new HttpEntity<>(lendingRequest, headers);

        //when
        this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", request, Lending.class);
        ResponseEntity<Lending> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", Lending.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(lending);

    }

    @Test
    public void shouldReturnNotFoundWhenTryToGetNotExistingLending() {
        //given
        int bookId = 1002;

        //when
        ResponseEntity<Lending> response = this.restTemplate.getForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", Lending.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void shouldDeleteLendingWhenReturnBook() {
        //given
        int bookId = 1002;
        String username = "oliviajones";
        Book book = Book
                .builder()
                .id(1001)
                .isbn("978832463176")
                .title("Thinking in Java")
                .author("Bruce Eckel")
                .type("programming")
                .build();
        LendingRequest lendingRequest = new LendingRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        lendingRequest.setLenderUsername(username);
        HttpEntity<LendingRequest> request = new HttpEntity<>(lendingRequest, headers);

        //when
        this.restTemplate.postForEntity("http://localhost:" + port + "/api/books/" + bookId + "/lendings", request, Lending.class);
        ResponseEntity<Lending> response  = this.restTemplate.exchange("http://localhost:" + port + "/api/books/" + bookId + "/lendings", HttpMethod.DELETE, null, Lending.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void shouldReturnNotFoundWhenTryToDeleteNotExistingLending() {
        //given
        int bookId = 1002;
        String username = "oliviajones";

        //when
        ResponseEntity<Lending> response  = this.restTemplate.exchange("http://localhost:" + port + "/api/books/" + bookId + "/lendings", HttpMethod.DELETE, null, Lending.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}