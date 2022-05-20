package com.leanpay.loancalculator.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentAmountsPerMonthsResponseDTO {

    private BigDecimal totalPayments;

    private BigDecimal totalInterest;

    private List<MonthlyInstallmentAmountResponseDTO> installmentAmountsPerMonths;

}
