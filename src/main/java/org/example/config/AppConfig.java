package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Deserializar con Snake Case:
        // Si tu JSON viene en "nombre_campo" y tu clase usa "nombreCampo", esto lo soluciona.
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 2. Ignorar campos que no están definidos en la clase DTO (útil para evitar errores)
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 3. Fallar si la clase DTO no tiene constructor sin argumentos
        // mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true);

        return mapper;
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}