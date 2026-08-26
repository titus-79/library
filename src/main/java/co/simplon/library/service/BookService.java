package co.simplon.library.service;

import co.simplon.library.entity.BookEntity;
import co.simplon.library.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<BookEntity> getBookById(UUID id) {
        return bookRepository.findById(id);
    }

    public BookEntity createBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public BookEntity updateBook(UUID id, BookEntity updateBook) {
        Optional<BookEntity> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookEntity currentBook = book.get();
            currentBook.setTitle(updateBook.getTitle());
            currentBook.setAutor(updateBook.getAutor());
            currentBook.setCategory(updateBook.getCategory());
            currentBook.setYearPublish(updateBook.getYearPublish());
            currentBook.setNbCopyAllowed(updateBook.getNbCopyAllowed());
        return bookRepository.save(currentBook);
        }
        return updateBook;
    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
