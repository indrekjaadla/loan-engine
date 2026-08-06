package com.indrekjaadla.loanengine.domain;

import java.math.BigDecimal;

public record ScheduledPayment(
        int paymentNumber,
        BigDecimal paymentAmount,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        BigDecimal remainingBalance
) {
}
