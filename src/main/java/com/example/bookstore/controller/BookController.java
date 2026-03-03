package com.example.bookstore.controller;

import com.example.bookstore.dto.request.BookRequest;
import com.example.bookstore.dto.response.BookReportResponse;
import com.example.bookstore.dto.response.BookResponse;
import com.example.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Libros", description = "Gestion de libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/above-average")
    public ResponseEntity<List<BookReportResponse>> getAboveAverage() {
        return ResponseEntity.ok(bookService.findBooksAboveAveragePrice());
    }

    @PutMapping("/update-price")
    public ResponseEntity<Integer> updatePrice(
            @RequestParam String editorial,
            @RequestParam BigDecimal factor) {
        return ResponseEntity.ok(bookService.updatePriceByEditorial(editorial, factor));
    }
}
