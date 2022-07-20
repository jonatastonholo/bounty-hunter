package dev.tonholo.bountyhunter.shared.webclient;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Getter
public class ApiErrorResponse extends Throwable {
    private final int statusCode;
    private final String message;
    private final Object error;

    private ApiErrorResponse(int statusCode, final Object error, final String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }

    public static ApiErrorResponse throwApiErrorResponse(int statusCode, final Object error, final String message, Object...messageParams) {
        log.error(message,messageParams);
        final var messageError = message(message,messageParams);
        return new ApiErrorResponse(statusCode, error, messageError);
    }

    private static String message(String message, Object...messageParams) {
        final var split = message.split("\\{}");
        final var paramsWithoutThrowable = withoutThrowable(messageParams);
        assertThatNumberOfParamsMatches(split, paramsWithoutThrowable.size());

        final var sb = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i]);
            sb.append(paramsWithoutThrowable.get(i));
        }
        return sb.toString();
    }

    private static void assertThatNumberOfParamsMatches(String[] split, int messageParamsSize) {
        if (split.length != messageParamsSize)
            throw new ArrayIndexOutOfBoundsException("Received invalid count of message parameters");
    }

    private static List<Object> withoutThrowable(Object...messageParams) {
        return Stream
                .of(messageParams)
                .filter(o -> !(o instanceof Throwable))
                .toList()
                ;
    }
}
