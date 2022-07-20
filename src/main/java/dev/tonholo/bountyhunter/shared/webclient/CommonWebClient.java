package dev.tonholo.bountyhunter.shared.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static dev.tonholo.bountyhunter.shared.webclient.ApiErrorResponse.throwApiErrorResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class CommonWebClient {
    private final WebClient webClient;
    private final String url;
    private String xApiKey;
    private MediaType contentType;
    private final String requestId;
    private WebClient.ResponseSpec response;

    private CommonWebClient(final WebClient webClient, final String url, final String requestId) {
        this.webClient = webClient;
        this.url = url;
        this.requestId = requestId;
    }

    public static CommonWebClient from(
            final WebClient webClient,
            final String url) {
        return new CommonWebClient(webClient, url, UUID.randomUUID().toString());
    }

    public CommonWebClient withApiKey(final String xApiKey) {
        this.xApiKey = xApiKey;
        return this;
    }

    public CommonWebClient withContentType(final MediaType contentType) {
        this.contentType = contentType;
        return this;
    }

    public WebClient.RequestBodySpec preparePost() {
        final var requestBodySpec = webClient.post()
                .uri(url)
                .contentType(mediaType());

        if (StringUtils.hasLength(xApiKey)) {
            requestBodySpec.header("X-API-KEY", xApiKey);
        }

        return requestBodySpec;
    }

    public <RESPONSE> Mono<RESPONSE> asMono(Class<RESPONSE> responseClass) {
        return response.bodyToMono(responseClass);
    }

    public <RESPONSE> Mono<RESPONSE> asFlux(Class<RESPONSE> responseClass) {
        return response.bodyToMono(responseClass);
    }

    public CommonWebClient get() {
        final var headersSpec = webClient
                .get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, mediaType().toString());

        if (StringUtils.hasLength(xApiKey)) {
            headersSpec.header("X-API-KEY", xApiKey);
        }

        response = headersSpec.retrieve();
        return this;
    }

    public <T> CommonWebClient onError(Class<T> errorClazz) {
        response
                .onStatus(HttpStatus::isError,
                        clientResponse -> handleErrorStatus(clientResponse,errorClazz));
        return this;
    }

    private <T> Mono<ApiErrorResponse> handleErrorStatus(
            final ClientResponse clientResponse,
            final Class<T> errorClass) {
        return clientResponse
                .bodyToMono(errorClass)
                .map(error ->
                        throwApiErrorResponse(
                            clientResponse.rawStatusCode(),
                            error,
                            "[requestId: {}] | endpoint {} responded with error -> {}",
                            requestId,
                            url,
                            error));
    }

    private MediaType mediaType() {
        return contentType == null
                ? APPLICATION_JSON
                : contentType;
    }
}
