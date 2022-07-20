package dev.tonholo.bountyhunter.infrastructure.api.fizzbuzz.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FizzBuzzApiConfiguration {
    @Value("${fizzBuzzAPI.url}") private String url;
    @Value("${fizzBuzzAPI.xApiKey}") private String xApiKey;
    @Value("${fizzBuzzAPI.endpoints.get.fizzBuzz}") private String getFizzbuzz;
    @Value("${fizzBuzzAPI.endpoints.get.reset}") private String getFizzbuzzReset;
    @Value("${fizzBuzzAPI.endpoints.get.canIHasTreasure}") private String getFizzbuzzCanIHasTreasure;
    @Value("${fizzBuzzAPI.endpoints.post.fizzBuzz}") private String postFizzbuzz;
    @Value("${fizzBuzzAPI.endpoints.delete.fizzBuzz}") private String deleteFizzbuzz;
}
