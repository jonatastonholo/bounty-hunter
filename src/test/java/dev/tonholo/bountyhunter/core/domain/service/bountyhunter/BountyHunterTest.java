package dev.tonholo.bountyhunter.core.domain.service.bountyhunter;

import dev.tonholo.bountyhunter.core.domain.fizzbuzz.FizzBuzz;
import dev.tonholo.bountyhunter.dto.PuzzleDTO;
import dev.tonholo.bountyhunter.shared.hash.CommonHash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BountyHunterTest {

    @InjectMocks private BountyHunter bountyHunter;
    @Mock private FizzBuzz fizzBuzz;
    @Mock private BountyHunterApi bountyHunterApi;
    @Mock private CommonHash commonHash;

//    @Test
//    @DisplayName("Receive the treasure")
//    void bountyHunterTest01() {
//        bountyHunter.start();
//    }


    @Test
    @DisplayName("Given a matching converted puzzle and hash, receive an equals puzzleDTO")
    void assemblyPuzzleDTOTest01() {
        bountyHunter = new BountyHunter(new CommonHash(), fizzBuzz, bountyHunterApi);
        final var puzzleConverted = List.of("fizz","buzz","fizzbuzz");
        final var hash = "c66a63862cf416c2acfe81ae697c066cff80b430af31fc9cae70957f355ded7d";
        final var puzzleDTOMock = new PuzzleDTO(hash, puzzleConverted);

        final Mono<PuzzleDTO> assemblyPuzzleDTO
                = ReflectionTestUtils.invokeMethod(bountyHunter, "assemblyPuzzleDTO", puzzleConverted);

        assert assemblyPuzzleDTO != null;

        StepVerifier
                .create(assemblyPuzzleDTO)
                .expectNext(puzzleDTOMock)
                .expectComplete()
                .verify();
        ;
    }

    @Test
    @DisplayName("Given a not matching converted puzzle and hash, receive a not equals puzzleDTO")
    void assemblyPuzzleDTOTest02() {
        bountyHunter = new BountyHunter(new CommonHash(), fizzBuzz, bountyHunterApi);
        final var puzzleConverted = List.of("fizz","buzz","fizzbuzz");
        final var hash = "c66a63862cf416c2acfe81ae697c066cff80b430af31fc9cae70957f355ded7d";
        final var puzzleDTOMock = new PuzzleDTO(hash, puzzleConverted);

        final Mono<PuzzleDTO> assemblyPuzzleDTO
                = ReflectionTestUtils.invokeMethod(bountyHunter, "assemblyPuzzleDTO", List.of("fizz","buzz","fizz"));

        assert assemblyPuzzleDTO != null;

        StepVerifier
                .create(assemblyPuzzleDTO)
                .assertNext(puzzleDTO
                        -> Assertions.assertNotEquals(puzzleDTOMock, puzzleDTO))
                .expectComplete()
                .verify();
        ;
    }
}