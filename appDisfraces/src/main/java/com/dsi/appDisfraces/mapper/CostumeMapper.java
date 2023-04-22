package com.dsi.appDisfraces.mapper;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.ClientTableDto;
import com.dsi.appDisfraces.dto.ClientsDTO;
import com.dsi.appDisfraces.dto.CostumeDTO;
import com.dsi.appDisfraces.dto.CostumeDetailDTO;
import com.dsi.appDisfraces.dto.CostumeHistoryDTO;
import com.dsi.appDisfraces.dto.CostumeRequestDTO;
import com.dsi.appDisfraces.dto.CostumeTableDTO;
import com.dsi.appDisfraces.dto.TransactionDTO;
import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.entity.CostumeEntity;

import com.dsi.appDisfraces.entity.TransactionEntity;
import com.dsi.appDisfraces.enumeration.CostumeStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CostumeMapper {

  @Autowired
  TransactionMapper transactionMapper;

  public CostumeEntity costumeDTO2Entity(CostumeRequestDTO dto) {
    CostumeEntity entity = new CostumeEntity();
    entity.setName(dto.getName());
    entity.setDetail(dto.getDetail());
    entity.setColour(dto.getColour());
    entity.setImage(dto.getImage());
    entity.setCreationDate(LocalDate.now());

    return entity;
  }


  public CostumeRequestDTO costumeEntity2DTO(CostumeEntity entitysaved) {
    CostumeRequestDTO dto = new CostumeRequestDTO();
    dto.setId(entitysaved.getId());
    dto.setName(entitysaved.getName());
    dto.setDetail(entitysaved.getDetail());
    dto.setColour(entitysaved.getColour());
    dto.setImage(entitysaved.getImage());
    dto.setCreationDay(entitysaved.getCreationDate());
    dto.setStatus(String.valueOf(entitysaved.getStatus()));

    return dto;
  }

  public CostumeDetailDTO costumeDetailEntity2DTO(CostumeEntity entity) {
    CostumeDetailDTO dto = new CostumeDetailDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDetail(entity.getDetail());
    dto.setImage(entity.getImage());
    dto.setColour(entity.getColour());
    dto.setStatus(String.valueOf(entity.getStatus()));
    dto.setCreationDay(entity.getCreationDate());
    if (!entity.getClients().isEmpty()) {
      ClientEntity lastClient = entity.getClients().stream()
          .max(Comparator.comparing(ClientEntity::getLastRentedDate))
          .orElseThrow(NoSuchElementException::new);
      dto.setLastClientRented(lastClient.getName());
    } else {
      dto.setLastClientRented("Este disfraz todavia no se alquiló");
    }

    return dto;
  }

  public List<CostumeTableDTO> costumeTableEntityList2DTO(List<CostumeEntity> entities) {
    List<CostumeTableDTO> dtos = new ArrayList<>();
    for (CostumeEntity entity : entities) {
      dtos.add(costumeTableEntity2DTO(entity));
    }
    return dtos;

  }

  private CostumeTableDTO costumeTableEntity2DTO(CostumeEntity entity) {
    CostumeTableDTO dto = new CostumeTableDTO();
    dto.setId(entity.getId());
    dto.setImage(entity.getImage());
    dto.setName(entity.getName());
    dto.setDetail(entity.getDetail());
    dto.setCostumeStatus(entity.getStatus());

    if (entity.getStatus().equals(CostumeStatus.ALQUILADO) || entity.getStatus().equals(CostumeStatus.RESERVADO)) {
      dto.setDeadlineDate(entity.getDeadLine());
      dto.setReservationDate(entity.getReservationDate());

      List<ClientEntity> clients = entity.getClients();
      if (!clients.isEmpty()) {
        ClientEntity selectedClient = clients.get(0);
        LocalDate closestDate = entity.getReservationDate();

        for (ClientEntity client : clients) {
          if (client.getLastRentedDate() != null && client.getLastRentedDate().isBefore(closestDate)) {
            selectedClient = client;
            closestDate = client.getLastRentedDate();
          }
        }

        if (selectedClient != null) {
          dto.setClientRented(selectedClient.getName());
        }
      }
    }

    return dto;
  }

  public void costumeUpdateEntity2DTO(CostumeEntity entity, CostumeRequestDTO costumeDTO) {
    entity.setName(costumeDTO.getName());
    entity.setColour(costumeDTO.getColour());
    entity.setDetail(costumeDTO.getDetail());
    entity.setImage(costumeDTO.getImage());
  }

  public CostumeHistoryDTO costumeHistoryEntity2Dto(CostumeEntity entity) {
    CostumeHistoryDTO dto = new CostumeHistoryDTO();
    dto.setName(entity.getName());
    dto.setId(entity.getId());
    dto.setDetail(entity.getDetail());
    List<ClientsDTO> clients = entity.getClients().stream().map(ClientEntity->{
          ClientsDTO clientsDTO = new ClientsDTO();
          clientsDTO.setName(ClientEntity.getName());
          clientsDTO.setId(ClientEntity.getId());
          clientsDTO.setDocumentNumber(ClientEntity.getDocumentNumber());
          return clientsDTO;
        }).
        collect(Collectors.toList());
    dto.setClients(clients);
    List<TransactionDTO> transactions = entity.getTransactions().stream()
        .map(transactionMapper::transactionEntityToDTO)
        .collect(Collectors.toList());
    dto.setTransactions(transactions);
    return dto;
  }
}