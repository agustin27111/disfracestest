package com.dsi.appDisfraces.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class TransactionDTO {

  private Long id;
  private Double amount;
  private Double partialPayment;
  private Double pending;
  private String type;
  private Long clientId;
  @NotEmpty(message = "El campo id del disfraz a alquilar no puede ser nulo")
  private List<Long> costumeIds;
  private List<String> names;
  private LocalDate reservationDate;
  private LocalDate deadline;
  private String checkIn;
  private String StatusPayment;

}
