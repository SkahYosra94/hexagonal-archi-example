package com.archi.hexagonal.infrastructure.adapters.input.rest.data;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductResponse (
        @Schema(example = "1")
        Long id,
        @Schema(example = "iPhone 15")
        String name,
        @Schema(example = "1200.00")
        String description){}
