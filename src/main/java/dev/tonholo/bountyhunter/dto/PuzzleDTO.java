package dev.tonholo.bountyhunter.dto;

import java.util.List;

public record PuzzleDTO(String hash, List<String> puzzle) {
    @Override
    public String toString() {
        return "PuzzleDTO{" +
                "hash='" + hash + '\'' +
                ", puzzle=" + puzzle +
                '}';
    }
}
