package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model;

import java.util.List;

public record Unauthorized(List<BaseError> errors, String message) {
}
