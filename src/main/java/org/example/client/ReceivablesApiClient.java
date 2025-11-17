package org.example.client;

import org.example.exceptions.ExceptionService;
import org.example.model.receivable.ReceivablesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Component
public class ReceivablesApiClient {

    @Value("${receivables.api.base-url:http://localhost:8080}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final String API_PATH = "/receivables";

    public ReceivablesApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ReceivablesDTO> getAllReceivables() {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH).toUriString();
        ResponseEntity<List<ReceivablesDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReceivablesDTO>>() {
                }
        );
        return response.getBody() != null ? response.getBody() : List.of();
    }

    public Optional<ReceivablesDTO> getReceivableById(String id) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH)
                .path("/{id}")
                .buildAndExpand(id)
                .toUriString();

        try {
            // getForObject es ideal para obtener un objeto único
            ReceivablesDTO receivable = restTemplate.getForObject(url, ReceivablesDTO.class);
            return Optional.ofNullable(receivable);
        } catch (Exception e) {
            throw new ExceptionService(e.getMessage(), "getReceivableById", baseUrl, API_PATH);

        }
    }

    public ReceivablesDTO createReceivable(ReceivablesDTO newReceivable) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH).toUriString();
        try {
            ResponseEntity<ReceivablesDTO> createdReceivable = restTemplate.postForEntity(
                    url,
                    newReceivable,
                    ReceivablesDTO.class
            );
            if (createdReceivable.getStatusCode().is2xxSuccessful() || createdReceivable.hasBody()) {
                return createdReceivable.getBody();
            }
            throw new ExceptionService(createdReceivable.getStatusCode().toString(), "createReceivable", baseUrl, API_PATH);
        } catch (HttpServerErrorException ee) {
            throw new ExceptionService(ee.getMessage(), "ReceivablesApiClient.createReceivable", baseUrl, API_PATH);
        }


    }
}