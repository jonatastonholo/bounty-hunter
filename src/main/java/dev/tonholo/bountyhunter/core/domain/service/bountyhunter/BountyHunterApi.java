package dev.tonholo.bountyhunter.core.domain.service.bountyhunter;

import dev.tonholo.bountyhunter.dto.ApiResponseDTO;
import dev.tonholo.bountyhunter.dto.PuzzleDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BountyHunterApi {

    Mono<ApiResponseDTO<String>> reset();
    Flux<Integer> collectPuzzle();
    Mono<ApiResponseDTO<PuzzleDTO>> sendPuzzleSolution(PuzzleDTO puzzleDTO);
    Mono<ApiResponseDTO<String>> collectTreasure(final String puzzleShaHash);
    Mono<ApiResponseDTO<String>> deleteLastPuzzle(final String puzzleShaHash);
}
