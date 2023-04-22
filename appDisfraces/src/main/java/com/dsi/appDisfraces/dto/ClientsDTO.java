package com.dsi.appDisfraces.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ClientsDTO {

  private Long id;
  private String name;
  private String lastName;
  private String documentNumber;
  private LocalDate deadLine;
  private LocalDate reservationDate;


}
