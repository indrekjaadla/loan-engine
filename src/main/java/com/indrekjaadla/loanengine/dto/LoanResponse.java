package com.indrekjaadla.loanengine.dto;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.LoanType;
import com.indrekjaadla.loanengine.domain.RepaymentType;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;

public record LoanResponse(
        UUID id,
        LoanType loanType,
        BigDecimal loanAmount,
        int periodInMonths,
        BigDecimal annualInterestRate,
        RepaymentType repaymentType,
        List<ScheduledPayment> scheduledPayments
) {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getLoanType(),
                loan.getLoanAmount(),
                loan.getPeriodInMonths(),
                toPercentFormat(loan.getAnnualInterestRate()),
                loan.getRepaymentType(),
                loan.getScheduledPayments()
        );
    }

    private static BigDecimal toPercentFormat(BigDecimal fractionRate) {
        return fractionRate.multiply(ONE_HUNDRED).setScale(2, HALF_UP);
    }
}
