package com.indrekjaadla.loanengine.strategy;

import com.indrekjaadla.loanengine.domain.Loan;
import com.indrekjaadla.loanengine.domain.ScheduledPayment;

import java.util.List;

public interface RepaymentScheduleGenerator {
    List<ScheduledPayment> generate(Loan loan);
}
