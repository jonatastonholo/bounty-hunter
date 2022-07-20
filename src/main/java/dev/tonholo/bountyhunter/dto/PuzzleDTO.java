package dev.tonholo.bountyhunter.dto;

import java.util.List;
import java.util.Objects;

public record PuzzleDTO(String hash, List<String> puzzle) {
    @Override
    public String toString() {
        return "PuzzleDTO{" +
                "hash='" + hash + '\'' +
                ", puzzle=" + puzzle +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuzzleDTO puzzleDTO)) return false;
        return hash.equals(puzzleDTO.hash) && puzzle.equals(puzzleDTO.puzzle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, puzzle);
    }
}
