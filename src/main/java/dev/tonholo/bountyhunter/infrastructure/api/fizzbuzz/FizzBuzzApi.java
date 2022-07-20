package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz;


import dev.tonholo.bountyhunter.core.domain.service.bountyhunter.BountyHunterApi;
import dev.tonholo.bountyhunter.dto.ApiResponseDTO;
import dev.tonholo.bountyhunter.dto.ApiResponsePuzzleDTO;
import dev.tonholo.bountyhunter.dto.PuzzleDTO;
import dev.tonholo.bountyhunter.infrastructure._config.WebClientConfig;
import dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.config.FizzBuzzApiConfiguration;
import dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model.ErrorStatusCode;
import dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model.GeneralError;
import dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.model.Success;
import dev.tonholo.bountyhunter.shared.json.JsonParser;
import dev.tonholo.bountyhunter.shared.webclient.ApiErrorResponse;
import dev.tonholo.bountyhunter.shared.webclient.CommonWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static dev.tonholo.bountyhunter.dto.ApiResponseDTO.apiResponseDTO;
import static java.lang.Boolean.TRUE;

@Component
@Slf4j
public class FizzBuzzApi implements BountyHunterApi {
    private final String url;
    private final String xApiKey;
    private final String endpointGetFizzbuzz;
    private final String endpointFizzbuzzReset;
    private final String endpointCanIHasTreasure;
    private final String endpointPostFizzbuzz;
    private final String endpointDeleteFizzbuzz;
    private final WebClient webClient;
    private final Integer timeoutMillis;
    private final JsonParser jsonParser;

    public FizzBuzzApi(
            final WebClient webClient,
            final WebClientConfig webClientConfig,
            final FizzBuzzApiConfiguration fizzBuzzApiConfiguration,
            final JsonParser jsonParser) {
        this.webClient = webClient;
        this.jsonParser = jsonParser;
        timeoutMillis = webClientConfig.getConnectionTimeoutMillis();
        url = fizzBuzzApiConfiguration.getUrl();
        xApiKey = fizzBuzzApiConfiguration.getXApiKey();
        endpointGetFizzbuzz = fizzBuzzApiConfiguration.getGetFizzbuzz();
        endpointFizzbuzzReset = fizzBuzzApiConfiguration.getGetFizzbuzzReset();
        endpointCanIHasTreasure = fizzBuzzApiConfiguration.getGetFizzbuzzCanIHasTreasure();
        endpointPostFizzbuzz = fizzBuzzApiConfiguration.getPostFizzbuzz();
        endpointDeleteFizzbuzz = fizzBuzzApiConfiguration.getDeleteFizzbuzz();
    }

    @Override
    public Mono<ApiResponseDTO> reset() {
        return CommonWebClient
                .from(webClient, url + endpointFizzbuzzReset)
                .withApiKey(xApiKey)
                .get()
                .onError(GeneralError.class)
                .asMono(Success.class)
                .map(success -> apiResponseDTO(200, TRUE.equals(success.success()) ? "success" : "fails"))
                .timeout(Duration.ofMillis(timeoutMillis))
                .onErrorResume(this::handleAPIError)
//                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                ;
    }

    @Override
    public Flux<Integer> collectPuzzle() {
        return null;
    }

    @Override
    public Mono<ApiResponsePuzzleDTO> sendPuzzleSolution(PuzzleDTO puzzleDTO) {
        return null;
    }

    @Override
    public Mono<ApiResponseDTO> collectTreasure(String puzzleShaHash) {
        return null;
    }

    @Override
    public Mono<ApiResponseDTO> deleteLastPuzzle(String puzzleShaHash) {
        return null;
    }

    private Mono<ApiResponseDTO> handleAPIError(Throwable throwable) {
        if (!(throwable instanceof final ApiErrorResponse apiErrorResponse)) {
            log.error("API returns an unknown error");
            throw new IllegalStateException(throwable);
        }

        log.warn("API returns an error -> ", throwable);
        final var response = apiResponseDTO(400, throwable.getMessage());
        try {
            final var error = (GeneralError) apiErrorResponse.getError();
            response
                    .with(error)
                    .mutate(error.message())
                    .mutate(ErrorStatusCode.code(error.message()));
        } catch (Exception __) {}

        return Mono.just(response);
    }
}
