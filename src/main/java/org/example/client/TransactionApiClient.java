package org.example.client;

import org.example.client.helper.HelperNumeration;
import org.example.exceptions.ExceptionService;
import org.example.model.transaction.TransactionDTO;
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
public class TransactionApiClient {

    // Se inyecta la URL base (aunque la pusiste fija, es mejor inyectarla)
    @Value("${transaction.api.base-url:http://localhost:8080}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final String API_PATH = "/transactions";

    private final NumeratorApiClient numeratorApiClient;
    private final HelperNumeration helperNumeration;

    public TransactionApiClient(RestTemplate restTemplate, NumeratorApiClient numeratorApiClient, HelperNumeration helperNumeration) {
        this.restTemplate = restTemplate;
        this.numeratorApiClient = numeratorApiClient;
        this.helperNumeration = helperNumeration;
    }

    public List<TransactionDTO> getAllTransactions() {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(API_PATH)
                .build()
                .toUriString();

        // Usamos exchange con ParameterizedTypeReference para manejar listas genéricas
        ResponseEntity<List<TransactionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null, // No hay cuerpo de request
                new ParameterizedTypeReference<List<TransactionDTO>>() {
                }
        );

        // Se asume que siempre retorna una lista válida (aunque puede estar vacía)
        return response.getBody() != null ? response.getBody() : List.of();
    }

    public Optional<TransactionDTO> getTransactionById(String id) {

        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH)
                .path("/{id}")
                .buildAndExpand(id)
                .toUriString();
        try {
            TransactionDTO transaction = restTemplate.getForObject(url, TransactionDTO.class);
            return Optional.ofNullable(transaction);
        } catch (Exception e) {
            // Manejar excepciones si el ID no existe (ej. 404 Not Found)
            throw new ExceptionService(e.getMessage(), "getTransactionById", baseUrl, API_PATH);
        }
    }

    /**
     * POST: Dar de alta una nueva transacción.
     * Endpoint: POST http://localhost:8080/transactions
     *
     * @param newTransaction El objeto TransactionDTO a enviar.
     * @return El TransactionDTO retornado por el servidor (incluyendo el ID generado).
     */
    public TransactionDTO createTransaction(TransactionDTO newTransaction) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(API_PATH)
                .build()
                .toUriString();

        try {
            newTransaction.setId(numeratorApiClient.getIdNumber());
            ResponseEntity<TransactionDTO> createdTransaction = restTemplate.postForEntity(
                    url,
                    newTransaction,
                    TransactionDTO.class
            );
            if (createdTransaction.getStatusCode().is2xxSuccessful() && createdTransaction.hasBody()) {
                helperNumeration.look(HttpMethod.DELETE);
                return createdTransaction.getBody();
            }
            throw new ExceptionService(createdTransaction.getStatusCode().toString(), "createReceivable", baseUrl, API_PATH);
        } catch (HttpServerErrorException ee) {
            throw new ExceptionService(ee.getMessage(), "TransactionApiClient.createTransaction", baseUrl, API_PATH);
        }

    }
}