package dev.tonholo.bountyhunter.shared.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JacksonJsonParser implements JsonParser {
    private static final Logger log = LoggerFactory.getLogger(JacksonJsonParser.class);
    private final ObjectMapper mapper;

    @Override
    public String toJson(final Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erro ao converter JSON");
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Erro ao converter {} para Objeto", json);
            throw new RuntimeException(e);
        }
    }
}
