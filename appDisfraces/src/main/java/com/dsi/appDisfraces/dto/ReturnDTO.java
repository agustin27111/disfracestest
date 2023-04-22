package com.dsi.appDisfraces.dto;

import java.util.List;
import lombok.Data;

@Data
public class ReturnDTO {

  private String clientDocument;
  private List<Long> costumesIds;
  private Long transactionId;
  private Boolean devolution;
  private Boolean totalPayment;

}
