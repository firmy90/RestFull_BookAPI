package pl.firmy90.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.firmy90.exception.BookEditException;
import pl.firmy90.exception.BookExistsException;
import pl.firmy90.exception.BookNotFoundException;
import pl.firmy90.exception.LendingNotFoundException;
import pl.firmy90.factory.BookLinkFactory;
import pl.firmy90.model.Book;
import pl.firmy90.model.Lending;
import pl.firmy90.model.LendingRequest;
import pl.firmy90.service.LendingService;
import pl.firmy90.factory.LendingLinkFactory;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books/{bookId:\\d+}/lendings")
public class LendingController {

    private final LendingService lendingService;
    private final LendingLinkFactory lendingLinkFactory;
    private final BookLinkFactory bookLinkFactory;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Lending> lendBook(
            @PathVariable Integer bookId,
            @RequestBody LendingRequest lendingRequest) throws BookNotFoundException, LendingNotFoundException, BookExistsException, BookEditException {

        Lending lending = lendingService.addLending(bookId, lendingRequest.getLenderUsername());

        Link selfLink = linkTo(methodOn(LendingController.class)
                .lendBook(bookId, lendingRequest))
                .withSelfRel();
        lending.add(selfLink);
        lending.add(lendingLinkFactory.createLinkGetLending(bookId));
        lending.add(lendingLinkFactory.createLinkDeleteLending(bookId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(
                        HttpHeaders.LOCATION,
                        "/api/books/" + bookId + "/lending")
                .body(lending);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Lending getLending(
            @PathVariable Integer bookId) throws LendingNotFoundException, BookExistsException, BookNotFoundException, BookEditException {
        Lending lending = lendingService.getLending(bookId);
        LendingRequest lendingRequest = new LendingRequest();

        Link selfLink = linkTo(methodOn(LendingController.class)
                .getLending(lending.getBook().getId()))
                .withSelfRel();
        lending.add(selfLink);
        lending.add(lendingLinkFactory.createLinkDeleteLending(bookId));
        lending.add(bookLinkFactory.createLinkLendBook(bookId, lendingRequest));
        return lending;

    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public ResponseEntity<Void> returnBook(
            @PathVariable Integer bookId) throws BookExistsException, LendingNotFoundException, BookNotFoundException, BookEditException {

        Lending lending = lendingService.getLending(bookId);
        Book book = lendingService.deleteLending(bookId);
        Link selfLink = linkTo(methodOn(LendingController.class)
                .returnBook(bookId))
                .withSelfRel();
        book.add(selfLink);
        book.add(lendingLinkFactory.createLinkGetLending(bookId));

        return ResponseEntity.noContent().build();
    }


}
