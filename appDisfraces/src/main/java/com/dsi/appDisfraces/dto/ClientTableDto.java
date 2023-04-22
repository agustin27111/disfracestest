package com.dsi.appDisfraces.dto;

import com.dsi.appDisfraces.enumeration.ClientStatus;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ClientTableDto implements Serializable {

  private String name;
  private String lastName;
  private String rentedCustome;
  private String type;
  private String phone;
  private ClientStatus status;
  private Long id;
  private LocalDate deadLine;

}
