package co.simplon.library.controller;

import co.simplon.library.dto.BookRequestDto;
import co.simplon.library.dto.BookResponseDto;
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

    public MainController(BookService bookServiceInjected) {
        this.bookService = bookServiceInjected;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> books = bookService.getAllBooks().stream()
                .map(BookResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable UUID id) {
        BookEntity book = bookService.getBookById(id);
        return ResponseEntity.ok(BookResponseDto.fromEntity(book));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto bookDto) {
        BookEntity created = bookService.createBook(bookDto);
        return new ResponseEntity<>(BookResponseDto.fromEntity(created), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable UUID id, @Valid @RequestBody BookRequestDto bookDto) {
        BookEntity updated = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(BookResponseDto.fromEntity(updated));
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}