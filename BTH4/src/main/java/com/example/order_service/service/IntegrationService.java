package com.example.order_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class IntegrationService {
    private final RestTemplate restTemplate;
    @Value("${auth.service.url}") private String authUrl;
    @Value("${product.service.url}") private String productUrl;

    public IntegrationService(RestTemplate restTemplate){this.restTemplate=restTemplate;}

    public boolean validateToken(String authorization) {
        if (authorization==null || authorization.isBlank()) return false;
        try {
            HttpHeaders h=new HttpHeaders(); h.set("Authorization", authorization);
            Boolean result=restTemplate.postForObject(authUrl+"/validate", new HttpEntity<>(h), Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch(Exception ex){ return false; }
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getProduct(Long id, String authorization) {
        HttpHeaders h=new HttpHeaders(); h.set("Authorization",authorization);
        ResponseEntity<Map> response=restTemplate.exchange(productUrl+"/products/"+id, HttpMethod.GET, new HttpEntity<>(h), Map.class);
        return response.getBody();
    }
}