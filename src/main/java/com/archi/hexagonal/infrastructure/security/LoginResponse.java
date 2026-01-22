package com.archi.hexagonal.infrastructure.security;

import java.util.List;

public record LoginResponse (String accessToken, String refreshToken, List<String> roles){
}
