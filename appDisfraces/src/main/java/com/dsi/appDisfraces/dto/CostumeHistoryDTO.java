package com.dsi.appDisfraces.dto;

import java.util.List;
import lombok.Data;

@Data
public class CostumeHistoryDTO {
  private String name;
  private Long id;
  private String detail;
  private List<ClientsDTO> clients;
  private List<TransactionDTO> transactions;


}
