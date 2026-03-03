package com.example.bookstore.service;

import com.example.bookstore.dto.request.AuthorRequest;
import com.example.bookstore.dto.response.AuthorResponse;
import com.example.bookstore.dto.response.AuthorWithBookCountResponse;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.mapper.AuthorMapper;
import com.example.bookstore.model.Author;
import com.example.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository,
                         AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponse findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor no encontrado con ID: %d".formatted(id)));
        return authorMapper.toResponse(author);
    }

    @Transactional
    public AuthorResponse save(AuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor no encontrado con ID: %d".formatted(id)));
        authorMapper.updateEntityFromRequest(request, author);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Transactional
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor no encontrado con ID: %d".formatted(id)));
        authorRepository.delete(author);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> findByNationality(String nationality) {
        return authorRepository.findByNationality(nationality).stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> searchByLastName(String lastName) {
        return authorRepository.searchByLastName(lastName).stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuthorWithBookCountResponse> findAuthorsOrderedByBookCount() {
        List<Object[]> results = authorRepository.findAuthorsOrderedByBookCount();
        return results.stream()
                .map(row -> new AuthorWithBookCountResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((Number) row[4]).longValue()))
                .toList();
    }
}
