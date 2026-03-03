package com.example.bookstore.dto.response;

import java.math.BigDecimal;

public record PurchaseItemResponse(
    Long bookId,
    String bookTitle,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}
