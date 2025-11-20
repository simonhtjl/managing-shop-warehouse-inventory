package org.simonhtjl.client;

import org.simonhtjl.dto.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Component
public class SaleServiceClient {

    @Value("${sale.service.url:http://localhost:8083}")
    private String saleServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<Sale> getAllSales() {
        try {
            String url = saleServiceUrl + "/api/sales";
            System.out.println("Calling sale service: " + url);

            ResponseEntity<List<Sale>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Sale>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error getting sales: " + e.getMessage());
            throw new RuntimeException("Cannot connect to sale service");
        }
    }

    public List<Sale> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(saleServiceUrl + "/api/sales/by-date")
                    .queryParam("startDate", startDate)
                    .queryParam("endDate", endDate)
                    .toUriString();

            System.out.println("Calling sale service with date range: " + url);

            ResponseEntity<List<Sale>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Sale>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error getting sales by date range: " + e.getMessage());
            throw new RuntimeException("Cannot connect to sale service");
        }
    }

    public List<Sale> getTodaySales() {
        try {
            String url = saleServiceUrl + "/api/sales/today";
            System.out.println("Calling today sales: " + url);

            ResponseEntity<List<Sale>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Sale>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error getting today sales: " + e.getMessage());
            throw new RuntimeException("Cannot connect to sale service");
        }
    }
}
