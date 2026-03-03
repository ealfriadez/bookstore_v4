package com.example.bookstore.dto.response;

public record AuthorResponse(
    Long id,
    String firstName,
    String lastName,
    String nationality
) {}
