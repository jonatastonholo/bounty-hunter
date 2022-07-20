package dev.tonholo.bountyhunter.core.domain.fizzbuzz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FizzBuzzTest {

    private FizzBuzz fizzBuzz;
    private final Integer fizzNumberRule = 3;
    private final String fizzExpressionRule = "fizz";
    private final Integer buzzNumberRule = 5;
    private final String buzzExpressionRule = "buzz";

    private String failsMessage(String expressionRule, Integer number, String converted) {
        return "Expected [:rule] output for [:number] but received [:converted]"
                .replace(":rule", expressionRule)
                .replace(":number", number.toString())
                .replace(":converted", converted);
    }

    @BeforeEach
    void setUp() {
        fizzBuzz = new FizzBuzz(fizzNumberRule, fizzExpressionRule, buzzNumberRule, buzzExpressionRule);
    }

    @Test
    @DisplayName("Given a fizz number, returns a string 'fizz'")
    void convertFizz() {
        Stream.of(3, 6, 9, 12, 18, 21, 24, 27, 33, 36, 39, 42, 48, 51)
                .forEach(number -> {
                    final String converted = ReflectionTestUtils.invokeMethod(fizzBuzz,"convert",number);
                    final var failsMessage = failsMessage(fizzExpressionRule, number, converted);
                    assertEquals(fizzExpressionRule, converted, failsMessage);
                });
    }

    @Test
    @DisplayName("Given a buzz number, returns a string 'buzz'")
    void convertBuzz() {
        Stream.of(5, 10, 20, 25, 35, 40, 50, 55, 65, 70, 80, 85, 95, 100)
                .forEach(number -> {
                    final String converted = ReflectionTestUtils.invokeMethod(fizzBuzz,"convert",number);
                    final var failsMessage = failsMessage(buzzExpressionRule, number, converted);
                    assertEquals(buzzExpressionRule, converted, failsMessage);
                });
    }

    @Test
    @DisplayName("Given a fizzbuzz number, returns a string 'fizzbuzz'")
    void convertFizzBuzz() {
        Stream.of(0, 15, 30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180, 195, 210)
                .forEach(number -> {
                    final String converted = ReflectionTestUtils.invokeMethod(fizzBuzz,"convert",number);
                    final var failsMessage = failsMessage(fizzExpressionRule+buzzExpressionRule, number, converted);
                    assertEquals(fizzExpressionRule+buzzExpressionRule, converted, failsMessage);
                });
    }

    @Test
    @DisplayName("Given a non fizzbuzz number, returns a string with the number")
    void convertNonFizzBuzz() {
        Stream.of(1, 2, 4, 7, 8, 11, 13, 14, 16, 17, 19, 22, 23, 26, 29)
                .forEach(number -> {
                    final String converted = ReflectionTestUtils.invokeMethod(fizzBuzz,"convert",number);
                    final var failsMessage = failsMessage(number.toString(), number, converted);
                    assertEquals(number.toString(), converted, failsMessage);
                });
    }

    @Test
    @DisplayName("Given a puzzle, solve the puzzle correctly")
    void solvePuzzle() {
        final var puzzle = List.of(0,1,2,3,4,5,6,7,8,15);
        final var expectedSolved = List.of("fizzbuzz", "1", "2", "fizz", "4", "buzz", "fizz", "7", "8", "fizzbuzz");
        final var solved = fizzBuzz.solvePuzzle(puzzle);
        assertEquals(expectedSolved, solved);
    }
}