package dev.tonholo.bountyhunter.dto;

public record ApiResponseDTO<T>(Integer code, T message) {
    public boolean isSuccess() {
        return code >= 200 && code < 400;
    }

    public boolean isError() {
        return !isSuccess();
    }
}
