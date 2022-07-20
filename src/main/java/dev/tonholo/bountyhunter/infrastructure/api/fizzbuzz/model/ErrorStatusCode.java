package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model;

import org.springframework.http.HttpStatus;

public enum ErrorStatusCode {
    FORBIDDEN {
        @Override
        public int code() {
            return HttpStatus.FORBIDDEN.value();
        }
    };

    public abstract int code();

    public static int code(final String value) {
        try {
            return from(value).code();
        } catch (Exception __) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public static ErrorStatusCode from(final String value) {
        return ErrorStatusCode.valueOf(value.toUpperCase());
    }
}
