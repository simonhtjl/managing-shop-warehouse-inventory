package org.simonhtjl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailyReport {
    private LocalDate date;
    private Long totalTransactions;
    private BigDecimal totalRevenue;
    private Long totalItemsSold;

    public DailyReport(LocalDate date, Long totalTransactions, BigDecimal totalRevenue, Long totalItemsSold, BigDecimal averageTransactionValue) {
        this.date = date;
        this.totalTransactions = totalTransactions;
        this.totalRevenue = totalRevenue;
        this.totalItemsSold = totalItemsSold;
        this.averageTransactionValue = averageTransactionValue;
    }

    private BigDecimal averageTransactionValue;
}