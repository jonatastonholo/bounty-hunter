package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz;


import dev.tonholo.bountyhunter.core.domain.service.bountyhunter.BountyHunterApi;
import dev.tonholo.bountyhunter.dto.ApiResponseDTO;
import dev.tonholo.bountyhunter.dto.PuzzleDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FizzBuzzApi implements BountyHunterApi {
    @Override
    public Mono<ApiResponseDTO<String>> reset() {
        return null;
    }

    @Override
    public Flux<Integer> collectPuzzle() {
        return null;
    }

    @Override
    public Mono<ApiResponseDTO<PuzzleDTO>> sendPuzzleSolution(PuzzleDTO puzzleDTO) {
        return null;
    }

    @Override
    public Mono<ApiResponseDTO<String>> collectTreasure(String puzzleShaHash) {
        return null;
    }

    @Override
    public Mono<ApiResponseDTO<String>> deleteLastPuzzle(String puzzleShaHash) {
        return null;
    }
}
