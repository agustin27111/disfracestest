package com.dsi.appDisfraces.dto;

import com.dsi.appDisfraces.entity.CostumeEntity;
import java.util.List;
import lombok.Data;

@Data
public class ClientHistoryDTO {

  private String name;
  private String lastName;
  private List<CostumeDTO> Costumes;
  private List<TransactionDTO> transactions;

}
