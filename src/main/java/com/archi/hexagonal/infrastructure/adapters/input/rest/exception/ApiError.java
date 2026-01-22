package com.archi.hexagonal.infrastructure.adapters.input.rest.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;


//Utilisation d'un DTO d’erreur generique
@Schema(description = "Erreur API")
public record ApiError (String message,
                        String code,
                        Instant timestamp){}
