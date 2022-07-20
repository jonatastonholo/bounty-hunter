package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model;

import java.util.List;

public record GeneralError(List<BaseError> errors, String message) {}
