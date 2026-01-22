package com.archi.hexagonal.infrastructure.adapters.input.rest.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank(message = "name is mandatory")
        @Size(min = 2, max = 50, message = "name must have between 2 and 50 chars")
        String name,
        @NotBlank(message = "description is mandatory")
        @Size(min = 5, max = 100, message = "description must have between 2 and 100 chars")
        String description) {
}
