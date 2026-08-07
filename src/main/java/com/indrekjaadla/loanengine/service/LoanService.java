package com.indrekjaadla.loanengine.service;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.RepaymentType;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;
import com.indrekjaadla.loanengine.dto.LoanRequest;
import com.indrekjaadla.loanengine.exception.UnsupportedLoanConfigurationException;
import com.indrekjaadla.loanengine.strategy.AnnuityRepaymentScheduleGenerator;
import com.indrekjaadla.loanengine.strategy.RepaymentScheduleGenerator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.indrekjaadla.loanengine.domain.LoanType.CONSUMER_LOAN;
import static com.indrekjaadla.loanengine.domain.RepaymentType.ANNUITY;
import static java.math.MathContext.UNLIMITED;
import static java.math.RoundingMode.HALF_UP;

@Service
public class LoanService {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private final AnnuityRepaymentScheduleGenerator annuityRepaymentScheduleGenerator;

    public LoanService(AnnuityRepaymentScheduleGenerator annuityRepaymentScheduleGenerator) {
        this.annuityRepaymentScheduleGenerator = annuityRepaymentScheduleGenerator;
    }

    public Loan createLoanWithScheduledPayments(LoanRequest loanRequest) {
        return generateAndAssignScheduledPaymentsTo(createLoan(loanRequest));
    }

    private Loan createLoan(LoanRequest loanRequest) {
        if (loanRequest.loanType() != CONSUMER_LOAN) {
            throw new UnsupportedLoanConfigurationException("Loan type not supported: " + loanRequest.loanType());
        }
        if (loanRequest.repaymentType() != ANNUITY) {
            throw new UnsupportedLoanConfigurationException("Loan repayment type not supported: " + loanRequest.repaymentType());
        }

        return mapToLoan(loanRequest);
    }

    private Loan generateAndAssignScheduledPaymentsTo(Loan loan) {
        RepaymentScheduleGenerator generator = selectGeneratorFor(loan.getRepaymentType());
        List<ScheduledPayment> scheduledPayments = generator.generate(loan);
        loan.assignScheduledPayments(scheduledPayments);

        return loan;
    }

    private Loan mapToLoan(LoanRequest loanRequest) {
        return new Loan(loanRequest.loanType(),
                toAmountFormat(loanRequest.loanAmount()),
                loanRequest.periodInMonths(),
                toInterestFormat(loanRequest.annualInterestRate()),
                loanRequest.repaymentType());
    }

    private RepaymentScheduleGenerator selectGeneratorFor(RepaymentType repaymentType) {
        if (repaymentType == ANNUITY) {
            return annuityRepaymentScheduleGenerator;
        }
        throw new UnsupportedLoanConfigurationException("Loan repayment type not supported: " + repaymentType);
    }

    private BigDecimal toAmountFormat(BigDecimal money) {
        return money.setScale(2, HALF_UP);
    }

    private BigDecimal toInterestFormat(BigDecimal percentRate) {
        return percentRate.divide(ONE_HUNDRED, UNLIMITED).setScale(4, HALF_UP);
    }
}
