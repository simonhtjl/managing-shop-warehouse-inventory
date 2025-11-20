package org.simonhtjl.controller;

import jakarta.validation.Valid;
import org.simonhtjl.dao.entity.Sale;
import org.simonhtjl.dto.SaleRequest;
import org.simonhtjl.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@Valid @RequestBody SaleRequest request) {
        return ResponseEntity.ok(saleService.processSale(request));
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @GetMapping("/today")
    public List<Sale> getTodaySales() {
        return saleService.findTodaySales();
    }

    @GetMapping("/by-date")
    public List<Sale> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return saleService.findByDateRange(startDate, endDate);
    }
}
