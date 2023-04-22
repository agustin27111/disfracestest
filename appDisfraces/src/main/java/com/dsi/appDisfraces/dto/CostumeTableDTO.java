package com.dsi.appDisfraces.dto;

import com.dsi.appDisfraces.enumeration.CostumeStatus;
import java.time.LocalDate;
import lombok.Data;

@Data

public class CostumeTableDTO {
  private Long id;
  private String name;
  private String detail;
  private String clientRented;
  private LocalDate reservationDate;
  private LocalDate deadlineDate;
  private CostumeStatus costumeStatus;
  private String image;

}
