package pl.firmy90.factory;

import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import pl.firmy90.controller.BookController;
import pl.firmy90.controller.LendingController;
import pl.firmy90.exception.BookEditException;
import pl.firmy90.exception.BookExistsException;
import pl.firmy90.exception.BookNotFoundException;
import pl.firmy90.exception.LendingNotFoundException;
import pl.firmy90.model.Book;
import pl.firmy90.model.LendingRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Component
public class BookLinkFactory {

    public Link createLinkAddBook(Book book) throws BookExistsException, BookNotFoundException, BookEditException {
        return WebMvcLinkBuilder.linkTo(methodOn(BookController.class)
                .addBook(book))
                .withRel("addBook");
    }

    public Link createLinkGetBook(Integer bookId) throws BookNotFoundException, BookEditException, BookExistsException {
        return
                linkTo(methodOn(
                        BookController.class)
                        .getBook(bookId))
                        .withRel("getBook");
    }

    public Link createLinkEditBook(Integer bookId, Book newBook) throws BookNotFoundException, BookEditException, BookExistsException {
        return linkTo(methodOn(BookController.class)
                .editBook(bookId, newBook))
                .withRel("editBook");
    }

    public Link createLinkDeleteBook(Integer bookId) throws BookNotFoundException, BookEditException, BookExistsException {
        return linkTo(methodOn(BookController.class)
                .deleteBook(bookId))
                .withRel("deleteBook");
    }

    public Link createLinkLendBook(Integer bookId, LendingRequest lendingRequest) throws BookNotFoundException, LendingNotFoundException, BookExistsException, BookEditException {
        return linkTo(methodOn(LendingController.class)
                .lendBook(bookId,lendingRequest))
                .withRel("lendBook");
    }




}

