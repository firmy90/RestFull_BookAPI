package pl.firmy90.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.firmy90.exception.LendingNotFoundException;
import pl.firmy90.exception.BookExistsException;
import pl.firmy90.exception.BookNotFoundException;
import pl.firmy90.model.Book;
import pl.firmy90.model.Lending;

import java.util.ArrayList;
import java.util.List;

@Service
public class LendingService {
    private final List<Lending> lendings = new ArrayList<>();
    @Autowired
    private BookService bookService;

    public Lending addLending(Integer bookId, String lenderUsername) throws BookNotFoundException, LendingNotFoundException {
        Book book = bookService.getBook(bookId);
        if (lendings.contains(book)){
            throw new LendingNotFoundException(bookId);
        }
        Lending lending = Lending.builder()
                .book(book)
                .lenderUsername(lenderUsername)
                .build();
        lendings.add(lending);
        bookService.deleteBook(bookId);
        return lending;
    }

    public Book deleteLending(Integer bookId) throws LendingNotFoundException, BookExistsException {
        Lending lending = this.getLending(bookId);
        Book book = lending.getBook();
            lendings.remove(lending);
            bookService.addBook(book);
            return book;
    }


    public Lending getLending(Integer bookId) throws LendingNotFoundException {
       return lendings
                .stream()
                .filter(a->a.getBook().getId().equals(bookId))
                .findFirst()
                .map(this::createCopy)
                .orElseThrow(() -> new LendingNotFoundException(bookId));

    }

    public Lending createCopy(Lending lending){
        return Lending.builder()
                .book(lending.getBook())
                .lenderUsername(lending.getLenderUsername())
                .build();
    }


}
