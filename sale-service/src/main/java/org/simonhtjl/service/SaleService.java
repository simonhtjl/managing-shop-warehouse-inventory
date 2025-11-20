package org.simonhtjl.service;

import jakarta.transaction.Transactional;
import org.simonhtjl.dao.entity.Product;
import org.simonhtjl.dao.entity.Sale;
import org.simonhtjl.dao.entity.SaleItem;
import org.simonhtjl.dao.repository.SaleRepository;
import org.simonhtjl.dto.SaleItemRequest;
import org.simonhtjl.dto.SaleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Transactional
    public Sale processSale(SaleRequest request) {
        String transactionId = "TRX" + System.currentTimeMillis();

        Sale sale = new Sale();
        sale.setTransactionId(transactionId);
        sale.setCashierName(request.getCashierName());
        sale.setCashReceived(request.getCashReceived());

        List<SaleItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleItemRequest itemRequest : request.getItems()) {
            try {

                Product product = productServiceClient.getProductById(itemRequest.getProductId());

                if (product.getStock() < itemRequest.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName() +
                            ". Available: " + product.getStock() +
                            ", Requested: " + itemRequest.getQuantity());
                }


                productServiceClient.updateStock(product.getId(),
                        product.getStock() - itemRequest.getQuantity());

                SaleItem item = new SaleItem();
                item.setSale(sale);
                item.setProductId(product.getId());
                item.setProductName(product.getName());
                item.setBarcode(product.getBarcode());
                item.setQuantity(itemRequest.getQuantity());
                item.setUnitPrice(product.getPrice());

                BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                item.setSubtotal(subtotal);

                items.add(item);
                totalAmount = totalAmount.add(subtotal);

            } catch (Exception e) {
                throw new RuntimeException("Error processing product: " + e.getMessage());
            }
        }


        if (request.getCashReceived().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient cash received. Total: " + totalAmount +
                    ", Received: " + request.getCashReceived());
        }

        sale.setTotalAmount(totalAmount);
        sale.setChange(request.getCashReceived().subtract(totalAmount));
        sale.setItems(items);

        Sale savedSale = saleRepository.save(sale);
        System.out.println("=== SALE PROCESSED ===");
        System.out.println("Transaction ID: " + savedSale.getTransactionId());
        System.out.println("Total Amount: " + savedSale.getTotalAmount());
        System.out.println("Items: " + savedSale.getItems().size());
        System.out.println("=====================");

        return savedSale;
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    public List<Sale> findTodaySales() {
        return saleRepository.findTodaySales();
    }

    public List<Sale> findByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        return saleRepository.findBySaleDateBetween(start, end);
    }
}
