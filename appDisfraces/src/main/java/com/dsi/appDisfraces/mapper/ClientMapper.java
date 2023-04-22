package com.dsi.appDisfraces.mapper;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.ClientRequestDTO;
import com.dsi.appDisfraces.dto.ClientTableDto;
import com.dsi.appDisfraces.dto.CostumeDTO;
import com.dsi.appDisfraces.dto.TransactionDTO;
import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.entity.CostumeEntity;
import com.dsi.appDisfraces.enumeration.ClientStatus;
import com.dsi.appDisfraces.enumeration.CostumeStatus;
import com.dsi.appDisfraces.repository.IClientRepository;
import com.dsi.appDisfraces.repository.ICostumeRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
  @Autowired
  IClientRepository clientRepository;
  @Autowired
  TransactionMapper transactionMapper;

  public ClientEntity clientDTO2Entity(ClientRequestDTO dto) {
    ClientEntity entity = new ClientEntity();
    entity.setName(dto.getName());
    entity.setLastName(dto.getLastName());
    entity.setAdress(dto.getAdress());
    entity.setDocumentNumber(dto.getDocumentNumber());
   //entity.setClientStatus(ClientStatus.valueOf(dto.getClientStatus()));
    entity.setImage(dto.getImage());
    entity.setType(dto.getType());
    entity.setPhone(dto.getPhone());

    return entity;
  }

  public ClientRequestDTO clientEntity2Dto(ClientEntity entity) {
    ClientRequestDTO dto = new ClientRequestDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setLastName(entity.getLastName());
    dto.setAdress(entity.getAdress());
    dto.setDocumentNumber(entity.getDocumentNumber());
    dto.setClientStatus(String.valueOf(entity.getClientStatus()));
    dto.setImage(entity.getImage());
    dto.setType(entity.getType());
    dto.setPhone(entity.getPhone());
    return dto;
  }

  public ClientTableDto clientBasicEntity2DTO(ClientEntity entity){
    ClientTableDto dto = new ClientTableDto();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setLastName(entity.getLastName());
    dto.setType(entity.getType());
    dto.setPhone(entity.getPhone());
    dto.setStatus(entity.getClientStatus());

    if(entity.getClientStatus().equals(ClientStatus.ACTIVO)){
      Optional<CostumeEntity> lastCostume = entity.getCustomes().stream()
          .filter(c -> c.getStatus() == CostumeStatus.ALQUILADO)
          .sorted(Comparator.comparing(CostumeEntity::getDeadLine).reversed()).findFirst();
      if (lastCostume.isPresent()){
        dto.setRentedCustome(String.valueOf(costumeEntity2DTO(lastCostume.get())));
        dto.setDeadLine(lastCostume.get().getDeadLine());
      } else {
        dto.setRentedCustome("sin alquilar");
      }

    } else {
      dto.setRentedCustome("sin alquilar");
    }
    return dto;

  }

  public CostumeDTO costumeEntity2DTO(CostumeEntity entity) {
   CostumeDTO dto = new CostumeDTO();
   dto.setId(entity.getId());
   dto.setDeadLine(entity.getDeadLine());
   dto.setName(entity.getName());


    return dto;
  }



  public List<ClientTableDto> clientEntityList2DTOList(List<ClientEntity> entities) {
    List<ClientTableDto> dtos = new ArrayList<>();
    for (ClientEntity entity : entities){
      dtos.add(this.clientBasicEntity2DTO(entity));
    }
    return dtos;
  }

  public void clientEntityUpdate(ClientEntity client, ClientRequestDTO clientRequestDTO ) {
    client.setName(clientRequestDTO.getName());
    client.setLastName(clientRequestDTO.getLastName());
    client.setAdress(clientRequestDTO.getAdress());
    client.setDocumentNumber(clientRequestDTO.getDocumentNumber());
    client.setType(clientRequestDTO.getType());
    client.setPhone(clientRequestDTO.getPhone());
    client.setImage(clientRequestDTO.getImage());

  }


  public ClientHistoryDTO clientHistoryEntity2Dto(ClientEntity entity) {
    ClientHistoryDTO dto = new ClientHistoryDTO();
    dto.setName(entity.getName());
    dto.setLastName(entity.getLastName());
    List<CostumeDTO> costumes = entity.getCustomes().stream().map(CostumeEntity->{
      CostumeDTO costumeDTO = new CostumeDTO();
      costumeDTO.setId(CostumeEntity.getId());
      costumeDTO.setName(CostumeEntity.getName());
      costumeDTO.setDeadLine(CostumeEntity.getDeadLine());
      costumeDTO.setReservationDate(CostumeEntity.getReservationDate());
      costumeDTO.setImage(CostumeEntity.getImage());
      return costumeDTO;
        }).
        collect(Collectors.toList());
    dto.setCostumes(costumes);
    List<TransactionDTO> transactions = entity.getTransactions().stream()
        .map(transactionMapper::transactionEntityToDTO)
        .collect(Collectors.toList());
    dto.setTransactions(transactions);


    return dto;
  }
}
  