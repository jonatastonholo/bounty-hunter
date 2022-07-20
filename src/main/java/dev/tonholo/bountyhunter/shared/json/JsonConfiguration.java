package dev.tonholo.bountyhunter.shared.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JsonConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        final var mapper = new ObjectMapper();
        final var module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.registerModule(new Jdk8Module());
        // Hide null fields in serialization
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Hide Optional.empty on serialization
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        // Hide empty collections on serialization
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
