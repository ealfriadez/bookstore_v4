package com.example.bookstore.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BookRequest(

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 1, max = 255,
          message = "El titulo debe tener entre 1 y 255 caracteres")
    String title,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    BigDecimal price,

    @NotBlank(message = "La URL de imagen es obligatoria")
    String imageUrl,

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    Integer stock,

    @NotBlank(message = "La editorial es obligatoria")
    String editorial,

    @NotNull(message = "El ID del autor es obligatorio")
    Long authorId
) {}
