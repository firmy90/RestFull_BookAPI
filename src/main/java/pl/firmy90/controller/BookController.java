package pl.firmy90.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.firmy90.exception.BookEditException;
import pl.firmy90.exception.BookExistsException;
import pl.firmy90.exception.BookNotFoundException;
import pl.firmy90.exception.LendingNotFoundException;
import pl.firmy90.factory.BookLinkFactory;
import pl.firmy90.model.Book;
import pl.firmy90.model.LendingRequest;
import pl.firmy90.service.BookService;
import pl.firmy90.factory.LendingLinkFactory;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final LendingLinkFactory lendingLinkFactory;
    private final BookLinkFactory bookLinkFactory;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CollectionModel<Book> getBooks() throws BookNotFoundException, BookEditException, BookExistsException, LendingNotFoundException {
        List<Book> books = bookService.getAvailableBooks();
        LendingRequest lendingRequest = new LendingRequest();

        for (Book book : books) {
            book.add(bookLinkFactory.createLinkGetBook(book.getId()));
            book.add(bookLinkFactory.createLinkEditBook(book.getId(), book));
            book.add(bookLinkFactory.createLinkAddBook(book));
            book.add(bookLinkFactory.createLinkDeleteBook(book.getId()));
            book.add(bookLinkFactory.createLinkLendBook(book.getId(), lendingRequest));
        }

        Link link = linkTo(methodOn(BookController.class)
                .getBooks())
                .withSelfRel();

        return CollectionModel.of(books, link);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book addBook(
            @RequestBody Book book) throws BookExistsException, BookNotFoundException, BookEditException {

        Book newBook = bookService.addBook(book);
        Link selfLink =
                linkTo(methodOn(BookController.class).addBook(book))
                        .withSelfRel();
        newBook.add(selfLink);
        newBook.add(bookLinkFactory.createLinkGetBook(book.getId()));
        newBook.add(bookLinkFactory.createLinkEditBook(book.getId(), book));
        newBook.add(bookLinkFactory.createLinkDeleteBook(book.getId()));
        return newBook;
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "{bookId:\\d+}")
    public Book getBook(
            @PathVariable Integer bookId) throws BookNotFoundException, BookEditException, BookExistsException {
        Book book = bookService.getBook(bookId);
        Link selfLink = linkTo(methodOn(BookController.class)
                .getBook(bookId))
                .withSelfRel();
        book.add(selfLink);
        book.add(bookLinkFactory.createLinkEditBook(bookId,book));
        book.add(bookLinkFactory.createLinkDeleteBook(bookId));
        return book;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(path = "{bookId:\\d+}")
    public Book editBook(
            @PathVariable Integer bookId,
            @RequestBody Book newBook) throws BookEditException, BookNotFoundException, BookExistsException {

        Book book = bookService.editBook(bookId, newBook);
        Link selfLink = linkTo(methodOn(BookController.class)
                .editBook(bookId, newBook))
                .withSelfRel();
        book.add(selfLink);
        book.add(bookLinkFactory.createLinkGetBook(bookId));
        book.add(bookLinkFactory.createLinkDeleteBook(bookId));
        return book;

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "{bookId:\\d+}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Integer bookId) throws BookNotFoundException, BookEditException, BookExistsException {

        Book book = bookService.getBook(bookId);
        bookService.deleteBook(bookId);
        Link selfLink = linkTo(methodOn(BookController.class)
                .deleteBook(bookId))
                .withSelfRel();
        book.add(selfLink);
        book.add(bookLinkFactory.createLinkAddBook(book));
        return ResponseEntity.noContent().build();
    }

}
