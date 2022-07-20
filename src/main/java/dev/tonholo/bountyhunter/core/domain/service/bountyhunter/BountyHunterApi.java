package dev.tonholo.bountyhunter.core.domain.service.bountyhunter;

import dev.tonholo.bountyhunter.dto.ApiResponseDTO;
import dev.tonholo.bountyhunter.dto.ApiResponsePuzzleDTO;
import dev.tonholo.bountyhunter.dto.PuzzleDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BountyHunterApi {

    Mono<ApiResponseDTO> reset();
    Flux<Integer> collectPuzzle();
    Mono<ApiResponsePuzzleDTO> sendPuzzleSolution(PuzzleDTO puzzleDTO);
    Mono<ApiResponseDTO> collectTreasure(final String puzzleShaHash);
    Mono<ApiResponseDTO> deleteLastPuzzle(final String puzzleShaHash);
}
