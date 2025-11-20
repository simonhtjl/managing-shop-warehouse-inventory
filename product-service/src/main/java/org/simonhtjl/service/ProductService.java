package org.simonhtjl.service;

import jakarta.annotation.PostConstruct;
import org.simonhtjl.dao.entity.Product;
import org.simonhtjl.dao.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void initSampleData() {
        if (productRepository.count() == 0) {
            // Sample products
            Product p1 = new Product();
            p1.setBarcode("8999999100011");
            p1.setName("Indomie Goreng");
            p1.setDescription("Mie instan rasa goreng");
            p1.setPrice(new BigDecimal("2500"));
            p1.setStock(100);
            p1.setCategory("Food");

            Product p2 = new Product();
            p2.setBarcode("8999999200010");
            p2.setName("Aqua 600ml");
            p2.setDescription("Air mineral botol");
            p2.setPrice(new BigDecimal("3000"));
            p2.setStock(50);
            p2.setCategory("Drink");

            Product p3 = new Product();
            p3.setBarcode("8999999300019");
            p3.setName("Pocari Sweat 500ml");
            p3.setDescription("Minuman isotonik");
            p3.setPrice(new BigDecimal("8000"));
            p3.setStock(30);
            p3.setCategory("Drink");

            productRepository.saveAll(List.of(p1, p2, p3));
            System.out.println("=== SAMPLE PRODUCTS CREATED ===");
        }
    }

    public List<Product> findAllActive() {
        return productRepository.findByActiveTrue();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found with barcode: " + barcode));
    }

    public List<Product> searchByName(String keyword) {
        return productRepository.searchActiveProducts(keyword);
    }

    public Product create(Product product) {
        if (productRepository.existsByBarcode(product.getBarcode())) {
            throw new RuntimeException("Product with barcode " + product.getBarcode() + " already exists");
        }
        product.setActive(true);
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = findById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        return productRepository.save(existing);
    }

    public Product updateStock(Long id, Integer quantity) {
        Product product = findById(id);
        if (quantity < 0) {
            throw new RuntimeException("Stock quantity cannot be negative");
        }
        product.setStock(quantity);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
