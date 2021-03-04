package pl.firmy90.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LendingNotFoundException extends Exception {

    public LendingNotFoundException(Integer bookId){
        super("Lending with bookId: " + bookId + " not found: ");

    }
}
