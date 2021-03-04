package pl.firmy90.controller.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerMockIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnListOfAvailableBooks() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("_embedded").exists())
                .andExpect(jsonPath("_embedded.bookList[0].id").value(1000))
                .andExpect(jsonPath("_embedded.bookList[0].isbn").value("9780132350884"))
                .andExpect(jsonPath("_embedded.bookList[0].title").value("Clean Code: A Handbook of Agile Software Craftsmanship"))
                .andExpect(jsonPath("_embedded.bookList[0].author").value("Robert C. Martin"))
                .andExpect(jsonPath("_embedded.bookList[0].type").value("programming"));
    }

    @Test
    public void shouldAddNewBook() throws Exception {
        String nb = "{\"id\":1003,\"isbn\":\" 9780134685991\",\"title\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"type\":\"programming\"}";

        mockMvc.perform(post("/api/books")
                .content(nb)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldReturnBookExistsExceptionWhenTryAddBookWithExistedBookId() throws Exception {
        String book = "{\"id\":1003,\"isbn\":\" 9780134685991\",\"title\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"type\":\"programming\"}";

        mockMvc.perform(post("/api/books")
                .content(book)
                .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/api/books")
                .content(book)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldReturnBook() throws Exception {
        int bookId = 1002;
        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(bookId))
                .andExpect(jsonPath("isbn").value("9788324627738"))
                .andExpect(jsonPath("title").value("Rusz glowa Java"))
                .andExpect(jsonPath("author").value("Sierra Kathy, Bates Bert"))
                .andExpect(jsonPath("type").value("programming"))
                .andExpect(jsonPath("_links.self.href").value("http://localhost/api/books/1002"));
    }

    @Test
    public void shouldReturnBookNotFoundExceptionWhenBookDoesntExist() throws Exception {
        int bookId = 999;
        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBookNotFoundWhenTryToEditNotExistedBook() throws Exception {
        int bookId = 999;
        String newBook = "{\"id\":" + bookId + ",\"isbn\":\" 9780134685991\",\"title\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"type\":\"programming\"}";
        mockMvc.perform(put("/api/books/" + bookId)
                .content(newBook)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldEditBook() throws Exception {
        int bookId = 1002;
        String editBook = "{\"id\":" + bookId + ",\"isbn\":\"9788324627738\",\"title\":\"Rusz glowa Java. Druga Edycja\",\"author\":\"Sierra Kathy, Bates Bert\",\"type\":\"programming\"}";
        mockMvc.perform(put("/api/books/" + bookId)
                .content(editBook)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(bookId))
                .andExpect(jsonPath("isbn").value("9788324627738"))
                .andExpect(jsonPath("title").value("Rusz glowa Java. Druga Edycja"))
                .andExpect(jsonPath("author").value("Sierra Kathy, Bates Bert"))
                .andExpect(jsonPath("type").value("programming"));

    }


    @Test
    public void shouldDeleteBook() throws Exception {
        int bookId = 1001;
        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnBookNotFoundWhenTryToDeleteNotExistedBook() throws Exception {
        int bookId = 999;
        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNotFound());

    }


}