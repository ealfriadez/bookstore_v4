package com.example.bookstore.service;

import com.example.bookstore.dto.request.BookRequest;
import com.example.bookstore.dto.response.BookReportResponse;
import com.example.bookstore.dto.response.BookResponse;
import com.example.bookstore.exception.DuplicateResourceException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Editorial;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: %d".formatted(id)));
        return bookMapper.toResponse(book);
    }

    @Transactional
    public BookResponse save(BookRequest request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor no encontrado con ID: %d".formatted(request.authorId())));

        if (bookRepository.existsByTitleAndAuthorId(
                request.title(), request.authorId())) {
            throw new DuplicateResourceException(
                    "Ya existe un libro con el titulo '%s' para el autor con ID: %d"
                            .formatted(request.title(), request.authorId()));
        }

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        book.setEditorial(Editorial.valueOf(request.editorial()));
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: %d".formatted(id)));

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor no encontrado con ID: %d".formatted(request.authorId())));

        bookMapper.updateEntityFromRequest(request, book);
        book.setAuthor(author);
        book.setEditorial(Editorial.valueOf(request.editorial()));
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: %d".formatted(id)));
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<BookReportResponse> findBooksAboveAveragePrice() {
        BigDecimal avg = bookRepository.findAveragePrice();

        return bookRepository.findBooksAboveAveragePrice().stream()
                .map(book -> {
                    BookReportResponse report = bookMapper.toReportResponse(book);
                    return new BookReportResponse(
                            report.id(), report.title(), report.price(),
                            report.editorial(), report.authorName(), avg);
                })
                .toList();
    }

    @Transactional
    public int updatePriceByEditorial(String editorial, BigDecimal factor) {
        return bookRepository.updatePriceByEditorial(editorial, factor);
    }

    public Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Libro no encontrado con ID: %d".formatted(id)));
    }
}
