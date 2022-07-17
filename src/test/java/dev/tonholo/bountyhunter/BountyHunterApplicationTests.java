package dev.tonholo.bountyhunter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles(value = {"default", "blockhound", "logs"})
@TestPropertySource(locations = {
        "classpath:.env/.env.developer.logs",
        "classpath:.env/.env.developer.blockhound",
})
class BountyHunterApplicationTests {

    @Test
    void contextLoads() {
        final var foo = 1L;
        final var bar = 1L;
        Assertions.assertEquals(foo,bar);
    }

}
