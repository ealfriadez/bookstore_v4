package com.example.bookstore.dto.request;

import jakarta.validation.constraints.*;

public record AuthorRequest(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 100,
          message = "El nombre debe tener entre 1 y 100 caracteres")
    String firstName,

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 1, max = 100,
          message = "El apellido debe tener entre 1 y 100 caracteres")
    String lastName,

    @NotBlank(message = "La nacionalidad es obligatoria")
    @Size(min = 2, max = 100,
          message = "La nacionalidad debe tener entre 2 y 100 caracteres")
    String nationality
) {}
