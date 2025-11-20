package org.simonhtjl.controller;

import org.simonhtjl.dto.DailyReport;
import org.simonhtjl.dto.ProductSales;
import org.simonhtjl.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/daily")
    public DailyReport getDailyReport() {
        return reportService.generateDailyReport();
    }

    @GetMapping("/daily/{date}")
    public DailyReport getDailyReportByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportService.generateDailyReport(date);
    }

    @GetMapping("/weekly")
    public List<DailyReport> getWeeklyReport() {
        return reportService.generateWeeklyReport();
    }

    @GetMapping("/top-products")
    public List<ProductSales> getTopProducts(@RequestParam(defaultValue = "10") int limit) {
        return reportService.getTopSellingProducts(limit);
    }
}