package pl.firmy90.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookExistsException extends Exception {
    public BookExistsException(Integer bookId) {
        super("Book with id " + bookId + "already exists");
    }

}
