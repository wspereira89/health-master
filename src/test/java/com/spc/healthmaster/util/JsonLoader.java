package com.spc.healthmaster.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class JsonLoader {
    public static <T> T loadObject(String fileName, Class<T> objectType) throws IOException {
        // Obtener el InputStream del archivo JSON
        InputStream inputStream = JsonLoader.class.getResourceAsStream(fileName);

        // Crear un ObjectMapper de Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        // Cargar el objeto desde el archivo JSON
        return objectMapper.readValue(inputStream, objectType);
    }
}