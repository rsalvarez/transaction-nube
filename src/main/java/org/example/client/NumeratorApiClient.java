package org.example.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.exceptions.ExceptionService;
import org.example.model.numerator.NumeratorRequestTestAndSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.example.util.ConstantProcesor.FAILED;
import static org.example.util.ConstantProcesor.OK;


/**
 * Cliente para interactuar con la API que genera el numerador (ID).
 */
@Component
public class NumeratorApiClient {

    // 1. Inyectar el HOST base (ej: http://api-ids.com)
    @Value("${numerator.api.base-url}")
    private String baseUrl;
    @Value("${numerator.api.path.generate-id}")
    private String generateIdPath;
    private final RestTemplate restTemplate;

    public NumeratorApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    //@CircuitBreaker(name = "NumeratorApiClient", fallbackMethod = "handleRollback")
    public String getIdNumber() {
        look(HttpMethod.POST);
        String current = getNumber();
        NumeratorRequestTestAndSet data = new NumeratorRequestTestAndSet();
        data.setOldValue(Double.valueOf(current));
        data.setNewValue(Double.valueOf(String.valueOf(Integer.getInteger(current)+1)));
        String newId = getNumeratorId(data);
        return newId;
    }

    public String handleRollback(ExceptionService ee) {
        if (!ee.getOrigin().equals("look")) {
            look(HttpMethod.DELETE);
            return OK;
        }
        return FAILED;
    }


    /**
     * Llama al endpoint POST para obtener un numerador (ID).
     *
     * @param requestBody El objeto con los datos a enviar en el body.
     * @return El cuerpo de la respuesta como un String.
     */

    public String getNumeratorId(NumeratorRequestTestAndSet requestBody) {


        // 3. Construir la URL completa
        String apiUrl = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(generateIdPath)
                .build()
                .toUriString();

        try {
            // Realizar la solicitud POST
            // El primer argumento es la URL
            // El segundo argumento es el body del request
            // El tercer argumento es el tipo de respuesta esperado (String.class)
            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl,
                    requestBody,
                    String.class
            );

            // Verificar el código de estado HTTP
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                // Devolver el cuerpo de la respuesta
                return response.getBody();
            } else {
                throw new ExceptionService("Error retrieving the new number", "getNumeratorId",  baseUrl, generateIdPath);
            }

        } catch (Exception e) {
            // Manejo de errores de conexión o del servicio
            throw new RuntimeException("Error en la comunicación con la API de numerador: " + e.getMessage(), e);
        }
    }

    private String getNumber() {
       String getnumber = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(generateIdPath)
                .build()
                .toUriString();
       ResponseEntity<String> body =  restTemplate.getForEntity(getnumber,String.class);
       if (body.getStatusCode().is2xxSuccessful() && body.hasBody()) {
           return body.getBody();
       }
       throw new ExceptionService("Error retrieving the current number", "getNumber",  baseUrl, generateIdPath);
    }
    @Retry(name = "NumeratorApiClient")
    private String look(HttpMethod http) {
        String unLook = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/numerator/lock")
                .build()
                .toUriString();
       try {
           if (http.equals(HttpMethod.DELETE))
               restTemplate.delete(unLook);
           else {
               restTemplate.postForEntity(unLook,null,String.class);
           }
       } catch (HttpServerErrorException ee) {
           throw new ExceptionService("Error unlook the current number (" +http.toString() + ")" , "look",  baseUrl, "/numerator/lock");
       }
        return OK;
    }



}
