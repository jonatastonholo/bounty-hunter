package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model;

import java.util.List;

public record BadRequest(List<BaseError> errors, String message) {
}
