package org.zetrahytes.todoapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    public static <T> String toJson(T obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
}
