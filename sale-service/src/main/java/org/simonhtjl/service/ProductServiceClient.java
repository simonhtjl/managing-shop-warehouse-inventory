package org.simonhtjl.service;

import org.springframework.beans.factory.annotation.Value;
import org.simonhtjl.dao.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ProductServiceClient {

    @Value("${product.service.url:http://localhost:8082}")
    private String productServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Product getProductById(Long id) {
        try {
            String url = productServiceUrl + "/api/products/" + id;
            System.out.println("Calling product service: " + url);
            Product product = restTemplate.getForObject(url, Product.class);
            System.out.println("Product received: " + product);
            return product;
        } catch (Exception e) {
            System.err.println("Error getting product: " + e.getMessage());
            throw new RuntimeException("Cannot get product with id: " + id);
        }
    }

    public Product updateStock(Long id, Integer quantity) {
        try {
            String url = productServiceUrl + "/api/products/" + id + "/stock";

            String fullUrl = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("quantity", quantity)
                    .toUriString();

            System.out.println("Updating stock: " + fullUrl);

            ResponseEntity<Product> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.PUT,
                    null,
                    Product.class
            );

            Product updatedProduct = response.getBody();
            System.out.println("Stock updated: " + updatedProduct);
            return updatedProduct;

        } catch (Exception e) {
            System.err.println("Error updating stock: " + e.getMessage());
            throw new RuntimeException("Cannot update stock for product id: " + id);
        }
    }
}
