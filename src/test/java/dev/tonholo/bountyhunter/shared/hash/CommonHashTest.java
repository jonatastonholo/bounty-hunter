package dev.tonholo.bountyhunter.shared.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommonHashTest {

    private CommonHash commonHash;

    @BeforeEach
    void setUp() {
        commonHash = new CommonHash();
    }

    @Test
    void sha256() {
        final var inputString = List.of("fizz","buzz","fizzbuzz");
        final var hash = "c66a63862cf416c2acfe81ae697c066cff80b430af31fc9cae70957f355ded7d";
        final String hashGenerated = commonHash.sha256(inputString.toArray(new String[0]));
        Assertions.assertEquals(hash, hashGenerated);
    }
}