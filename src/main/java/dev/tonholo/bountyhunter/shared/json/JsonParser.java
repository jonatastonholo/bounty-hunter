package dev.tonholo.bountyhunter.shared.json;

public interface JsonParser {
    String toJson(final Object object);
    <T> T fromJson(final String json, final Class<T> clazz);
}
