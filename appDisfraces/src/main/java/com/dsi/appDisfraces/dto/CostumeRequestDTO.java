package com.dsi.appDisfraces.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CostumeRequestDTO {

  private Long id;
  private String name;
  private String colour;
  private String detail;
  private String image;
  private LocalDate creationDay;
  private String status;

}
