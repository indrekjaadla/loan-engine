package com.indrekjaadla.loanengine.controller;

import com.indrekjaadla.loanengine.dto.LoanRequest;
import com.indrekjaadla.loanengine.dto.LoanResponse;
import com.indrekjaadla.loanengine.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.indrekjaadla.loanengine.dto.LoanResponse.from;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest loanRequest) {
        LoanResponse loanResponse = from(loanService.createLoanWithScheduledPayments(loanRequest));
        return ResponseEntity.status(CREATED).body(loanResponse);
    }
}
