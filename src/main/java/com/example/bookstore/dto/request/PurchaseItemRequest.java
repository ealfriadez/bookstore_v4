package com.example.bookstore.dto.request;

import jakarta.validation.constraints.*;

public record PurchaseItemRequest(
    @NotNull(message = "El ID del libro es obligatorio")
    Long bookId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    Integer quantity
) {}
