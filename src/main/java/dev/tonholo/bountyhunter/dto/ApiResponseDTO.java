package dev.tonholo.bountyhunter.dto;

import dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model.GeneralError;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ApiResponseDTO {
    private Integer code;
    private String message;
    private GeneralError error;

    private ApiResponseDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiResponseDTO apiResponseDTO(Integer code, String message) {
        return new ApiResponseDTO(code, message);
    }

    public ApiResponseDTO mutate(int code) {
        this.code = code;
        return this;
    }

    public ApiResponseDTO mutate(String message) {
        this.message = message;
        return this;
    }

    public ApiResponseDTO with(GeneralError error) {
        this.error = error;
        return this;
    }

    public boolean isSuccess() {
        return code >= 200 && code < 400;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public Optional<GeneralError> getError() {
        return Optional.ofNullable(error);
    }
}
