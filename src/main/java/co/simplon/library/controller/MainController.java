package co.simplon.library.controller;

import co.simplon.library.entity.BookEntity;
import co.simplon.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class MainController {

    private final BookService bookService;

    public MainController(
            BookService bookServiceInjected) {
        this.bookService = bookServiceInjected;
    }
//    GET /api/books
    @GetMapping
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }
//    GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable UUID id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }
//    POST /api/books
    @PostMapping
    public ResponseEntity<BookEntity> createBook(@Valid @RequestBody BookEntity book) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.OK);
    }
//    PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable UUID id,@Valid @RequestBody BookEntity book) {
        return new ResponseEntity<>(bookService.updateBook(id, book), HttpStatus.OK);
    }
//    DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }
}
