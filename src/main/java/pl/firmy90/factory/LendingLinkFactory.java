package pl.firmy90.factory;

import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.firmy90.controller.LendingController;
import pl.firmy90.exception.BookEditException;
import pl.firmy90.exception.BookExistsException;
import pl.firmy90.exception.BookNotFoundException;
import pl.firmy90.exception.LendingNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Component
public class LendingLinkFactory {

    public Link createLinkGetLending(Integer bookId) throws LendingNotFoundException, BookExistsException, BookNotFoundException, BookEditException {
        return linkTo(methodOn(LendingController.class)
                .getLending(bookId))
                .withRel("getLending");
    }

    public Link createLinkDeleteLending(Integer bookId) throws LendingNotFoundException, BookExistsException, BookNotFoundException, BookEditException {
        return linkTo(methodOn(LendingController.class)
                .returnBook(bookId))
                .withRel("deleteLending");
    }


}

