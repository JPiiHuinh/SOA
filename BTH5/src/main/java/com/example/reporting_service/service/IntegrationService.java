package com.example.reporting_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class IntegrationService {
    private final RestTemplate http;
    @Value("${auth.service.url}") private String authUrl;
    @Value("${product.service.url}") private String productUrl;
    @Value("${order.service.url}") private String orderUrl;
    public IntegrationService(RestTemplate http) { this.http=http; }

    public boolean isTokenValid(String token) {
        if (token==null || token.isBlank()) return false;
        try {
            HttpHeaders headers=new HttpHeaders(); headers.set(HttpHeaders.AUTHORIZATION, token);
            ResponseEntity<Boolean> response=http.postForEntity(authUrl+"/validate", new HttpEntity<>(headers), Boolean.class);
            return response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody());
        } catch (Exception ex) { return false; }
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> getProduct(Long id,String token) {
        return request(productUrl+"/products/"+id, token, Map.class);
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> getOrder(Long id,String token) {
        return request(orderUrl+"/orders/"+id, token, Map.class);
    }
    private <T> T request(String url,String token,Class<T> type) {
        HttpHeaders headers=new HttpHeaders(); headers.set(HttpHeaders.AUTHORIZATION,token);
        return http.exchange(url,HttpMethod.GET,new HttpEntity<>(headers),type).getBody();
    }
}
