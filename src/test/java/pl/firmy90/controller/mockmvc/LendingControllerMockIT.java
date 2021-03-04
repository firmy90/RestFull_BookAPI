package pl.firmy90.controller.mockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LendingControllerMockIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void shouldLendBook() throws Exception {
        int bookId = 1001;
        String s = "{\"lenderUsername\":\"oliviajones\"}";

        mockMvc.perform(post("/api/books/" + bookId + "/lendings")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBookNotFoundWhenTryToLendUnavailableBook() throws Exception {
        int bookId = 1001;
        String s = "{\"lenderUsername\":\"oliviajones\"}";

        mockMvc.perform(post("/api/books/" + bookId + "/lendings")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(res ->
                        mockMvc.perform(post("/api/books/" + bookId + "/lendings")
                                .content(s)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound()));
    }

    @Test
    @DirtiesContext
    public void shouldGetLending() throws Exception {
        int bookId = 1002;
        String s = "{\"lenderUsername\":\"oliviajones\"}";

        mockMvc.perform(post("/api/books/" + bookId + "/lendings")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(res -> mockMvc.perform(get("/api/books/" + bookId + "/lendings"))
                        .andExpect(content().contentType("application/hal+json"))
                        .andExpect(jsonPath("lenderUsername").value("oliviajones"))
                        .andExpect(jsonPath("book.id").value(bookId))
                        .andExpect(jsonPath("book.isbn").value("9788324627738"))
                        .andExpect(status().isOk()));
    }

    @Test
    public void shouldReturnNotFoundWhenTryToGetNotExistingLending() throws Exception {
        int bookId = 1002;
        String s = "{\"lenderUsername\":\"oliviajones\"}";

        mockMvc.perform(get("/api/books/" + bookId + "/lendings"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteLendingWhenReturnBook() throws Exception {
        int bookId = 1002;
        String s = "{\"lenderUsername\":\"oliviajones\"}";

        mockMvc.perform(post("/api/books/" + bookId + "/lendings")
                .content(s)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(res -> mockMvc.perform(delete("/api/books/" + bookId + "/lendings"))
                        .andExpect(status().isNoContent()));
    }

    @Test
    public void shouldReturnNotFoundWhenTryToDeleteNotExistingLending() throws Exception {
        int bookId = 1002;
        String s = "{\"lenderUsername\":\"oliviajones\"}";

        mockMvc.perform(delete("/api/books/" + bookId + "/lendings"))
                .andExpect(status().isNotFound());
    }


}