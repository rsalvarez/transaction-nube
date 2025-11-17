package org.example.client;

import org.example.client.helper.HelperNumeration;
import org.example.exceptions.ExceptionService;
import org.example.model.numerator.NumeratorRequestTestAndSet;
import org.example.model.numerator.ResponseNumerator;
import org.example.util.ConstantProcesor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.example.util.ConstantProcesor.FAILED;
import static org.example.util.ConstantProcesor.OK;
import static org.example.util.ConstantProcesor.pathgetNumerator;


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

    private final HelperNumeration helperNumeration;

    @Value("${numerator.api.path.getId}")
    private String getId;
    private final RestTemplate restTemplate;

    public NumeratorApiClient(HelperNumeration helperNumeration, RestTemplate restTemplate) {
        this.helperNumeration = helperNumeration;
        this.restTemplate = restTemplate;
    }


    //@CircuitBreaker(name = "NumeratorApiClient", fallbackMethod = "handleRollback")
    public String getIdNumber() {
        String result = helperNumeration.look(HttpMethod.POST);
        if (result.equals(FAILED))
            throw new ExceptionService("Error blocking ID number after maximum retries", "NumeratorApi.getIdNumber",baseUrl, getId );
        ResponseNumerator current = getNumber();
        NumeratorRequestTestAndSet data = new NumeratorRequestTestAndSet();
        data.setOldValue(Double.valueOf(current.getNumerator()));
        data.setNewValue(Double.valueOf(String.valueOf(Integer.valueOf(current.getNumerator())+1)));
        String newId = getNumeratorId(data);
        return newId;
    }

    public String handleRollback(ExceptionService ee) {
        if (!ee.getOrigin().equals("look")) {
            helperNumeration.look(HttpMethod.DELETE);
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

    private String getNumeratorId(NumeratorRequestTestAndSet requestBody) {


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
            ResponseEntity<ResponseNumerator> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.PUT,
                    new HttpEntity<>(requestBody),
                    ResponseNumerator.class
            );

            // Verificar el código de estado HTTP
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                // Devolver el cuerpo de la respuesta
                return response.getBody().getNumerator();
            } else {
                throw new ExceptionService("Error retrieving the new number", "getNumeratorId",  baseUrl, generateIdPath);
            }

        } catch (Exception e) {
            // Manejo de errores de conexión o del servicio
            throw new RuntimeException("Error en la comunicación con la API de numerador: " + e.getMessage(), e);
        }
    }

    private ResponseNumerator getNumber() {
       String getnumber = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(getId)
                .build()
                .toUriString();
       ResponseEntity<ResponseNumerator> body =  restTemplate.getForEntity(getnumber,ResponseNumerator.class);
       if (body.getStatusCode().is2xxSuccessful() && body.hasBody()) {
           return body.getBody();
       }
       throw new ExceptionService("Error retrieving the current number", "getNumber",  baseUrl, generateIdPath);
    }




}
