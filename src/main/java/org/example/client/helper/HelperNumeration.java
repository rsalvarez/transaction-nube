package org.example.client.helper;

import org.example.exceptions.ExceptionService;
import org.example.util.ConstantProcesor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.example.util.ConstantProcesor.OK;

@Component
public class HelperNumeration {
    @Value("${numerator.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public HelperNumeration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Retryable(value = { ExceptionService.class }, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public String look(HttpMethod http) {
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
        } catch (HttpServerErrorException | HttpClientErrorException ee) {
            throw new ExceptionService("Error un/look the current number (" +http.toString() + ")" , "look",  baseUrl, "/numerator/lock");
        }
        return OK;
    }

    @Recover
    public String recover(ExceptionService e) {
        return ConstantProcesor.FAILED;
    }

}
