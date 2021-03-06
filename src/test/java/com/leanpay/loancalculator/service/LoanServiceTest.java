package com.leanpay.loancalculator.service;

import com.leanpay.loancalculator.domain.Installment;
import com.leanpay.loancalculator.domain.Loan;
import com.leanpay.loancalculator.repository.LoanRepository;
import com.leanpay.loancalculator.service.impl.LoanServiceImpl;
import com.leanpay.loancalculator.util.TestCalculations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanRepository loanRepository;

    @Test
    void getMonthlyInstallmentAmount() {
        Loan loan = Loan.builder()
                .amount(BigDecimal.valueOf(20000))
                .numberOfMonths(60)
                .interestRate(5d)
                .installments(new ArrayList<>()).build();

        when(loanRepository.save(any(Loan.class)))
                .thenReturn(loan);

        Installment installment = loanService.getMonthlyInstallmentAmount(loan);

        assertEquals(loan.getAmount().setScale(2, RoundingMode.HALF_EVEN),
                TestCalculations.calculateLoanAmount(loan.getNumberOfMonths(), loan.getInterestRate(), installment.getAmount()).setScale(2, RoundingMode.HALF_EVEN));
        assertNotNull(installment.getLoan());
    }

    @Test
    void calculateInstallmentAmountsPerMonths() {
        Loan loan = Loan.builder()
                .amount(BigDecimal.valueOf(20000))
                .numberOfMonths(60)
                .interestRate(5d)
                .installments(new ArrayList<>()).build();

        when(loanRepository.save(loan))
                .thenReturn(loan);

        List<Installment> installments = loanService.getInstallmentAmountsPerMonths(loan);

        assertFalse(loan.getInstallments().isEmpty());
        assertEquals(loan.getNumberOfMonths(), installments.size());
        BigDecimal loanAmount = TestCalculations.calculateLoanAmount(loan.getNumberOfMonths(), loan.getInterestRate(),
                loan.getInstallments().get(0).getAmount()).setScale(2, RoundingMode.HALF_EVEN);
        for (int i = 1; i < loan.getNumberOfMonths(); i++) {
            assertEquals(i, loan.getInstallments().get(i - 1).getNumberOfMonth());
            assertEquals(loan.getAmount().setScale(2, RoundingMode.HALF_EVEN), loanAmount);
        }
    }

}
