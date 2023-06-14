package com.megafair.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class JsonMapper {

    @Inject
    ObjectMapper objectMapper;

    public <T> T fromJson(String json, Class<T> resultClass) {
        try {
            return objectMapper.readValue(json, resultClass);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public String toJson(Object src) {
        try {
            return objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }
}
