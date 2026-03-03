package com.example.bookstore.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record PurchaseRequest(
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 200, message = "El nombre no debe exceder 200 caracteres")
    String customerName,

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    String customerEmail,

    @NotEmpty(message = "La compra debe tener al menos un libro")
    @Valid
    List<PurchaseItemRequest> items
) {}
