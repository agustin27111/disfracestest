package com.dsi.appDisfraces.dto;

import lombok.Data;

@Data
public class TransactionTotalsDto {
  private Double totalAmount;
  private Double totalPending;

  public TransactionTotalsDto(Double totalAmount, Double totalPending) {
    this.totalAmount = totalAmount;
    this.totalPending = totalPending;
  }

}
