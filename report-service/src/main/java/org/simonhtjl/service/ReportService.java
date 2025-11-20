package org.simonhtjl.service;

import org.simonhtjl.client.SaleServiceClient;
import org.simonhtjl.dto.DailyReport;
import org.simonhtjl.dto.ProductSales;
import org.simonhtjl.dto.Sale;
import org.simonhtjl.dto.SaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private SaleServiceClient saleServiceClient;

    public DailyReport generateDailyReport() {
        List<Sale> todaySales = saleServiceClient.getTodaySales();
        return generateDailyReport(todaySales, LocalDate.now());
    }

    public DailyReport generateDailyReport(LocalDate date) {
        List<Sale> sales = saleServiceClient.getSalesByDateRange(date, date);
        return generateDailyReport(sales, date);
    }

    private DailyReport generateDailyReport(List<Sale> sales, LocalDate date) {
        Long totalTransactions = (long) sales.size();
        BigDecimal totalRevenue = sales.stream()
                .map(Sale::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalItemsSold = sales.stream()
                .flatMap(sale -> sale.getItems().stream())
                .mapToLong(SaleItem::getQuantity)
                .sum();

        BigDecimal averageTransactionValue = totalTransactions > 0 ?
                totalRevenue.divide(BigDecimal.valueOf(totalTransactions), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        return new DailyReport(date, totalTransactions, totalRevenue, totalItemsSold, averageTransactionValue);
    }

    public List<ProductSales> getTopSellingProducts(int limit) {
        List<Sale> allSales = saleServiceClient.getAllSales();

        Map<Long, ProductSales> productSalesMap = new HashMap<>();

        for (Sale sale : allSales) {
            for (SaleItem item : sale.getItems()) {
                ProductSales productSales = productSalesMap.getOrDefault(item.getProductId(),
                        new ProductSales(item.getProductId(), item.getProductName(), item.getBarcode(), 0L, BigDecimal.ZERO));

                productSales.setTotalQuantitySold(productSales.getTotalQuantitySold() + item.getQuantity());
                productSales.setTotalRevenue(productSales.getTotalRevenue().add(item.getSubtotal()));

                productSalesMap.put(item.getProductId(), productSales);
            }
        }

        return productSalesMap.values().stream()
                .sorted((ps1, ps2) -> ps2.getTotalQuantitySold().compareTo(ps1.getTotalQuantitySold()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<DailyReport> generateWeeklyReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<DailyReport> weeklyReports = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DailyReport dailyReport = generateDailyReport(date);
            weeklyReports.add(dailyReport);
        }

        return weeklyReports;
    }
}
