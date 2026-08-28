package com.indrekjaadla.loanengine.dto;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.LoanType;
import com.indrekjaadla.loanengine.domain.RepaymentType;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record LoanResponse(
        UUID id,
        LoanType loanType,
        BigDecimal loanAmount,
        int periodInMonths,
        BigDecimal annualInterestRate,
        RepaymentType repaymentType,
        List<ScheduledPayment> scheduledPayments
) {
    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getLoanType(),
                loan.getLoanAmount(),
                loan.getPeriodInMonths(),
                loan.getAnnualInterestRate(),
                loan.getRepaymentType(),
                loan.getScheduledPayments()
        );
    }
}
