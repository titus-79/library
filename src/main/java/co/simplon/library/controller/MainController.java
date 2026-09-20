package co.simplon.library.controller;

import co.simplon.library.entity.BookEntity;
import co.simplon.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }
//    GET /api/books/{id}
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable UUID id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }
//    POST /api/books
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookEntity> createBook(@Valid @RequestBody BookEntity book) {
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.OK);
    }
//    PUT /api/books/{id}
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable UUID id,@Valid @RequestBody BookEntity book) {
        return new ResponseEntity<>(bookService.updateBook(id, book), HttpStatus.OK);
    }
//    DELETE /api/books/{id}
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }
}
