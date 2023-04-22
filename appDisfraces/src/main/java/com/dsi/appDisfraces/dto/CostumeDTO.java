package com.dsi.appDisfraces.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CostumeDTO {
  private Long id;
  private String name;
  private LocalDate deadLine;
  private LocalDate reservationDate;
  private String image;


}
