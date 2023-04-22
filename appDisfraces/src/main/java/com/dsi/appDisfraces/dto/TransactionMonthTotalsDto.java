package com.dsi.appDisfraces.dto;

import lombok.Data;

@Data
public class TransactionMonthTotalsDto {
  private Integer month;
  private Double totalAmount;
  private Double totalPending;

}
