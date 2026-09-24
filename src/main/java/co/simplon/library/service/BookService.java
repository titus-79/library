package co.simplon.library.service;

import co.simplon.library.mapper.BookMapper;
import co.simplon.library.dto.BookRequestDto;
import co.simplon.library.entity.BookEntity;
import co.simplon.library.exception.ResourceNotFoundException;
import co.simplon.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'ID: " + id));
    }

    @Transactional
    public BookEntity createBook(BookRequestDto dto) {
        BookEntity book = BookMapper.toEntity(dto);
        return bookRepository.save(book);
    }

    @Transactional
    public BookEntity updateBook(UUID id, BookRequestDto dto) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'ID: " + id));

        BookMapper.updateEntity(book, dto);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livre non trouvé avec l'ID: " + id);
        }
        bookRepository.deleteById(id);
    }
}