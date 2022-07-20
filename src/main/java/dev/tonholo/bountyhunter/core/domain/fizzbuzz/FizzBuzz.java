package dev.tonholo.bountyhunter.core.domain.fizzbuzz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FizzBuzz {
    private final int fizzNumberRule;
    private final String fizzExpressionRule;
    private final int buzzNumberRule;
    private final String buzzExpressionRule;

    @Autowired
    public FizzBuzz(
            @Value("${core.domain.fizzbuzz.fizz.numberRule}") final int fizzNumberRule,
            @Value("${core.domain.fizzbuzz.fizz.expressionRule}") final String fizzExpressionRule,
            @Value("${core.domain.fizzbuzz.buzz.numberRule}") final int buzzNumberRule,
            @Value("${core.domain.fizzbuzz.buzz.expressionRule}") final String buzzExpressionRule) {
        this.fizzNumberRule = fizzNumberRule;
        this.fizzExpressionRule = fizzExpressionRule;
        this.buzzNumberRule = buzzNumberRule;
        this.buzzExpressionRule = buzzExpressionRule;
    }

    /**
     * <p>Given a puzzle, which is an Integer List, will convert in a String List with the Fizz Buzz Rule:</p>
     *
     * <ul>
     *    <li>If the number is divisible by {@link FizzBuzz#fizzNumberRule} will return {@link FizzBuzz#fizzExpressionRule}</li>
     *    <li>If the number is divisible by {@link FizzBuzz#buzzNumberRule} will return {@link FizzBuzz#buzzExpressionRule}</li>
     *    <li>If the number is divisible by {@link FizzBuzz#fizzNumberRule} and {@link FizzBuzz#buzzNumberRule} will return {@link FizzBuzz#fizzExpressionRule}+{@link FizzBuzz#buzzExpressionRule}</li>
     *    <li>Otherwise, will return the inputted number as String.</li>
     * </ul>
     *
     * <p>
     *     Example:
     *     <pre>
     *         {@link FizzBuzz#fizzNumberRule} = 3;
     *         {@link FizzBuzz#fizzExpressionRule} = "fizz";
     *         {@link FizzBuzz#buzzNumberRule} = 5;
     *         {@link FizzBuzz#buzzExpressionRule} = buzz;
     *
     *         solvePuzzle(List.of(0,1,2,3,4,5,6,7,8,15);
     *         //out -> ["fizzbuzz", "1", "2", "fizz", "4", "buzz", "fizz", "7", "8", "fizzbuzz"]
     *
     *     </pre>
     * </p>
     *
     * @param puzzle
     * @return
     */
    public List<String> solvePuzzle(List<Integer> puzzle) {
        Objects.requireNonNull(puzzle, "Puzzle was not initialized");
        if (puzzle.isEmpty()) throw new IllegalStateException("The puzzle is empty");

        return puzzle
                .stream()
                .map(this::convert)
                .toList();
    }

    private String convert(final int number) {
        final var out = new StringBuilder();
        if (isFizz(number)) out.append(fizzExpressionRule);
        if (isBuzz(number)) out.append(buzzExpressionRule);
        if (out.isEmpty()) out.append(number);
        return out.toString();
    }

    private boolean isFizz(final int number) {
        return number % fizzNumberRule == 0;
    }

    private boolean isBuzz(final int number) {
        return number % buzzNumberRule == 0;
    }
}
