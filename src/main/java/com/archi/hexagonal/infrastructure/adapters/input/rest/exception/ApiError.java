package com.archi.hexagonal.infrastructure.adapters.input.rest.exception;

import java.time.Instant;


//Utilisation d'un DTO d’erreur generique
public record ApiError (String message,
                        String code,
                        Instant timestamp){}
