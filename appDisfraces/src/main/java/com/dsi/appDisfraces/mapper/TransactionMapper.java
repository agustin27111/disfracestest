package com.dsi.appDisfraces.mapper;

import com.dsi.appDisfraces.dto.CostumeTableDTO;
import com.dsi.appDisfraces.dto.TransactionDTO;
import com.dsi.appDisfraces.entity.CostumeEntity;
import com.dsi.appDisfraces.entity.TransactionEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {


  public TransactionDTO transactionEntityToDTO(TransactionEntity entity) {
    TransactionDTO dto = new TransactionDTO();
    dto.setId(entity.getId());
    dto.setClientId(entity.getClient().getId());
    dto.setCostumeIds(entity.getDisfraces().stream().map(CostumeEntity::getId).collect(Collectors.toList()));
    List<String> costumeNames = entity.getDisfraces().stream()
        .map(CostumeEntity::getName)
        .collect(Collectors.toList());
    dto.setNames(costumeNames);

    dto.setReservationDate(entity.getRentDate());
    dto.setDeadline(entity.getDeadline());
    dto.setType(entity.getType());
    dto.setAmount(entity.getAmmount());
    dto.setCheckIn(entity.getBillPayment());
    return dto;
  }


  public List<TransactionDTO> transactionEntityList2DTOList(List<TransactionEntity> transactions) {
    List<TransactionDTO> dtos = new ArrayList<>();
    for (TransactionEntity entity : transactions) {
      dtos.add(transactionEntityToDTO(entity));
    }
    return dtos;
  }
}
