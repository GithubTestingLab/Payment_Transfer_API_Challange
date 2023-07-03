package com.dws.challenge.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferRequest {

    @NotNull
    private long fromAccountId;
    @NotNull
    private long toAccountId;
    @NotNull
    @Min(value = 0, message = "Transfer amount must not be less than zero")
    private BigDecimal amount;

    // getters and setters

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
