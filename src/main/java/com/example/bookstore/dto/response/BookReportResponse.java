package com.example.bookstore.dto.response;

import java.math.BigDecimal;

public record BookReportResponse(
    Long id,
    String title,
    BigDecimal price,
    String editorial,
    String authorName,
    BigDecimal averagePrice
) {}
