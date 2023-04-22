package com.dsi.appDisfraces.dto;

import com.dsi.appDisfraces.enumeration.ClientStatus;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data

public class ClientRequestDTO implements Serializable {

  private Long id;
  private String name;
  private String lastName;
  private String adress;
  private String documentNumber;
  private String clientStatus;
  private String type;
  private String phone;
 // private ClientStatus status;
  private String image;

}
