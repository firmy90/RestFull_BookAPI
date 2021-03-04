package pl.firmy90.service;

import org.springframework.stereotype.Service;
import pl.firmy90.exception.BookExistsException;
import pl.firmy90.exception.BookNotFoundException;
import pl.firmy90.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final List<Book> availableBooks = new ArrayList<>();

    public BookService() {
        availableBooks.add(Book
                .builder()
                .id(1000)
                .isbn("9780132350884")
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .author("Robert C. Martin")
                .type("programming")
                .build());
        availableBooks.add(Book
                .builder()
                .id(1001)
                .isbn("978832463176")
                .title("Thinking in Java")
                .author("Bruce Eckel")
                .type("programming")
                .build());
        availableBooks.add(Book
                .builder()
                .id(1002)
                .isbn("9788324627738")
                .title("Rusz glowa Java")
                .author("Sierra Kathy, Bates Bert")
                .type("programming")
                .build());
    }

    public Book addBook(Book book) throws BookExistsException {
        if (availableBooks.contains(book)){
            throw new BookExistsException(book.getId());
        }
        availableBooks.add(book);
        return book;
    }

    public Book getBook(Integer bookId) throws BookNotFoundException {
        return availableBooks
                .stream()
                .filter(b->b.getId().equals(bookId))
                .findFirst()
                .map(this::createCopy)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    public void deleteBook(Integer bookId) throws BookNotFoundException {
        Book book = this.getBook(bookId);
        availableBooks.remove(book);
    }

    public Book editBook(Integer bookId, Book book) throws BookNotFoundException {
        Book oldBook = this.getBook(bookId);
        availableBooks.remove(oldBook);
        availableBooks.add(book);
        return book;
    }

    public List<Book> getAvailableBooks(){
        return availableBooks.stream().map(this::createCopy).collect(Collectors.toList());
    }

    public Book createCopy(Book book){
        return Book.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .title(book.getTitle())
                .type(book.getType())
                .build();
    }

}
