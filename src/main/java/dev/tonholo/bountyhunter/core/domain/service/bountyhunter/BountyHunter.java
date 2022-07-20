package dev.tonholo.bountyhunter.core.domain.service.bountyhunter;

import dev.tonholo.bountyhunter.core.domain.fizzbuzz.FizzBuzz;
import dev.tonholo.bountyhunter.dto.ApiResponseDTO;
import dev.tonholo.bountyhunter.dto.ApiResponsePuzzleDTO;
import dev.tonholo.bountyhunter.dto.PuzzleDTO;
import dev.tonholo.bountyhunter.shared.hash.CommonHash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class BountyHunter {
    private final CommonHash commonHash;
    private final FizzBuzz fizzBuzz;
    private final BountyHunterApi bountyHunterApi;
    private final Integer attemptsBeforeFailing = 3;
    private final Integer interactionsBeforeFailing = 3;
    private final AtomicInteger attemptsCounter = new AtomicInteger(0);
    private final AtomicInteger interactionsCounter = new AtomicInteger(0);
    private final AtomicBoolean treasuryCollected = new AtomicBoolean(false);
    private final AtomicBoolean terminate = new AtomicBoolean(false);

    public void start() {
        log.info("Starting to hunt the treasure...");
        runAttempt()
            .repeat(() -> terminate.get() || treasuryCollected.get() || (attemptsCounter.get() < attemptsBeforeFailing))
            .last()
            .doOnNext(treasuryResponse -> log.info("Algorithm finalized after {} attempts with -> {}", attemptsCounter.get(), treasuryResponse))
            .subscribe();
    }

    private Mono<ApiResponseDTO> runAttempt() {
        return reset()
                .doOnNext(__ -> log.info("Initializing the attempt #{} to solve the puzzle", attemptsCounter.incrementAndGet()))
                .flatMap(__ -> runInteractionsToSolve());
    }

    private Mono<ApiResponseDTO> reset() {
        log.info("Restarting the algorithm...");
        return bountyHunterApi
                .reset()
                .checkpoint("Call reset")
                .doOnNext(response -> log.info("Reset response -> {}", response))
                .map(apiResponseDTO -> {
                    if (200 != apiResponseDTO.getCode() && 403 != apiResponseDTO.getCode()) {
                        throw new IllegalStateException("Failed to reset the game");
                    }
                    return apiResponseDTO;
                })
                ;
    }

    private Mono<ApiResponseDTO> runInteractionsToSolve() {
        return runInteraction()
                .repeat(() -> terminate.get() || treasuryCollected.get() || (interactionsCounter.get() < interactionsBeforeFailing))
                .last()
                .doOnNext(treasuryResponse
                    -> log.info("Attempt #{} finalized after {} interactions with -> {}",
                    attemptsCounter.get(),
                    interactionsCounter.get(),
                    treasuryResponse))
            ;
    }

    private Mono<ApiResponseDTO> runInteraction() {
        return collectPuzzle()
                .flatMap(this::solvePuzzle)
                .flatMap(this::sendSolvedPuzzle)
                .flatMap(this::collectTreasure)
            .doOnNext(__ -> log.info("Initializing the interaction #{} to solve the puzzle", interactionsCounter.incrementAndGet()));
    }

    private Mono<ApiResponseDTO> collectTreasure(ApiResponsePuzzleDTO apiResponsePuzzleDTO) {
        final var hash = apiResponsePuzzleDTO.puzzleDTO().hash();
        log.info("Trying collect the treasure for hash [{}]...", hash);
        return bountyHunterApi
                .collectTreasure(hash)
                .checkpoint("collecting the treasure")
                .doOnNext(response -> log.info("Response of collect treasury -> {}", response))
                .flatMap(treasuryResponse -> checkTreasury(treasuryResponse, hash));
    }

    private Mono<ApiResponseDTO> checkTreasury(ApiResponseDTO treasuryResponse, String hash) {
        if (treasuryResponse.isSuccess()) {
            log.info("Treasury collected!!!");
            treasuryCollected.set(true);
            return Mono.just(treasuryResponse);
        }

        log.info("Failed to get the treasury... -> {}", treasuryResponse);
        return prepareForNextInteraction(hash);
    }

    private Mono<ApiResponseDTO> prepareForNextInteraction(String hash) {
        log.info("Preparing for the next interaction... Deleting the hash [{}]", hash);
        return bountyHunterApi
                .deleteLastPuzzle(hash)
                .checkpoint("deleting the hash " + hash);
    }

    private Mono<List<Integer>> collectPuzzle() {
        log.info("Collecting puzzle...");
        return bountyHunterApi
                .collectPuzzle()
                .checkpoint("collecting puzzle")
                .switchIfEmpty(Mono.error(() -> {
                    terminate.set(true);
                    return new IllegalStateException("Failed to get puzzle");
                }))
                .collectList()
                .doOnNext(puzzle -> log.info("Puzzle collected -> {}", puzzle));
    }

    private Mono<ApiResponsePuzzleDTO> sendSolvedPuzzle(PuzzleDTO puzzleDTO) {
        log.info("Sending puzzle solution...");
        return bountyHunterApi
                .sendPuzzleSolution(puzzleDTO)
                .map(response -> {
                    log.info("Checking response...");
                    if (response.apiResponseDTO().isError()) {
                        terminate.set(true);
                        throw new IllegalStateException("Failed to send puzzle");
                    }
                    return response;
                });
    }

    private Mono<PuzzleDTO> solvePuzzle(List<Integer> rawPuzzle) {
        return Mono
                .fromCallable(() -> fizzBuzz.solvePuzzle(rawPuzzle))
                .checkpoint("Converting the puzzle")
                .doOnNext(puzzleConverted -> log.info("Converted Puzzle -> {}", puzzleConverted))
                .subscribeOn(Schedulers.parallel())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(__ -> log.info("Hashing the converted puzzle"))
                .flatMap(this::assemblyPuzzleDTO)
                ;
    }

    private Mono<PuzzleDTO> assemblyPuzzleDTO(List<String> puzzleConverted) {
        return Mono
        .fromCallable(()-> commonHash.sha256(puzzleConverted))
        .subscribeOn(Schedulers.boundedElastic())
        .checkpoint("Sha 256 on converted puzzle")
        .doOnNext(sha256 -> log.info("hash generated: [{}]", sha256))
        .map(sha256 -> new PuzzleDTO(sha256, puzzleConverted))
        .doOnNext(puzzle -> log.info("Puzzle created -> {}", puzzle))
        ;
    }

}
