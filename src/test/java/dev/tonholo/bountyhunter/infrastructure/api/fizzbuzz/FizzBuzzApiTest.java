package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles({"api-test"})
@TestPropertySource(locations = {
        "classpath:.env/.env.developer.api.fizzbuzz",
        "classpath:.env/.env.developer.blockhound",
        "classpath:.env/.env.developer.core.domain.fizzbuzz",
        "classpath:.env/.env.developer.logs",
        "classpath:.env/.env.developer.webclient",
})
class FizzBuzzApiTest {

    @Autowired
    private FizzBuzzApi fizzBuzzApi;

    @Test
    void reset() {
        StepVerifier
                .create(fizzBuzzApi.reset())
                .assertNext(apiResponseDTO -> {
                    Assertions.assertTrue(200 == apiResponseDTO.getCode() || 403 == apiResponseDTO.getCode());
                })
                .expectComplete()
                .verify();
    }
}