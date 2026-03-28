package com.example.bookstore.dto.response;

import java.math.BigDecimal;

public record BookResponse(
    Long id,
    String title,
    BigDecimal price,
    String imageUrl,
    Integer stock,
    String editorial,
    String authorFullName
) {}
