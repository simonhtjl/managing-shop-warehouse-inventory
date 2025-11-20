package org.simonhtjl.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {
    @NotNull
    private String cashierName;

    @NotNull
    private BigDecimal cashReceived;

    @NotEmpty
    private List<SaleItemRequest> items;

    public @NotNull String getCashierName() {
        return cashierName;
    }

    public void setCashierName(@NotNull String cashierName) {
        this.cashierName = cashierName;
    }

    public @NotNull BigDecimal getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(@NotNull BigDecimal cashReceived) {
        this.cashReceived = cashReceived;
    }

    public @NotEmpty List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(@NotEmpty List<SaleItemRequest> items) {
        this.items = items;
    }
}
