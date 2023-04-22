package com.dsi.appDisfraces.service.impl;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.ClientRequestDTO;
import com.dsi.appDisfraces.dto.CostumeDetailDTO;
import com.dsi.appDisfraces.dto.CostumeHistoryDTO;
import com.dsi.appDisfraces.dto.CostumeRequestDTO;
import com.dsi.appDisfraces.dto.CostumeTableDTO;
import com.dsi.appDisfraces.dto.ReturnDTO;
import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.entity.CostumeEntity;
import com.dsi.appDisfraces.entity.TransactionEntity;
import com.dsi.appDisfraces.enumeration.AmountStatus;
import com.dsi.appDisfraces.enumeration.ClientStatus;
import com.dsi.appDisfraces.enumeration.CostumeStatus;
import com.dsi.appDisfraces.exception.IdNotFound;
import com.dsi.appDisfraces.exception.ParamNotFound;
import com.dsi.appDisfraces.mapper.CostumeMapper;
import com.dsi.appDisfraces.repository.IClientRepository;
import com.dsi.appDisfraces.repository.ICostumeRepository;
import com.dsi.appDisfraces.repository.ITransactionRepository;
import com.dsi.appDisfraces.service.IcostumeService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CostumeServiceImpl implements IcostumeService {

  @Autowired
  CostumeMapper costumeMapper;
  @Autowired
  ICostumeRepository costumeRepository;
  @Autowired
  IClientRepository clientRepository;
  @Autowired
  ITransactionRepository transactionRepository;

  @Override
  public CostumeRequestDTO saveCostume(CostumeRequestDTO costumeDTO) {
    CostumeEntity entity = this.costumeMapper.costumeDTO2Entity(costumeDTO);
    CostumeEntity entitysaved = this.costumeRepository.save(entity);
    CostumeRequestDTO result = this.costumeMapper.costumeEntity2DTO(entitysaved);
    return result;
  }

  @Override
  public CostumeDetailDTO getDetailsById(Long id) {
    CostumeEntity entity = this.costumeRepository.findById(id).orElseThrow(
        () -> new IdNotFound("El ID del disfraz no existe"));
    CostumeDetailDTO dto = costumeMapper.costumeDetailEntity2DTO(entity);
    return dto;
  }

  @Override
  public List<CostumeTableDTO> findAll() {
    List<CostumeEntity> entities = this.costumeRepository.findAll();
    List<CostumeTableDTO> dtos = this.costumeMapper.costumeTableEntityList2DTO(entities);
    return dtos;
  }

  @Override
  public CostumeRequestDTO update(Long id, CostumeRequestDTO newCostumeDTO) {
    CostumeEntity entity = this.costumeRepository.findById(id).orElseThrow(
        () -> new IdNotFound("El id del disfraz es invalido"));
    this.costumeMapper.costumeUpdateEntity2DTO(entity, newCostumeDTO);
    CostumeEntity updateEntity = this.costumeRepository.save(entity);
    CostumeRequestDTO result = costumeMapper.costumeEntity2DTO(updateEntity);

    return result;
  }

  @Override
  public void delete(Long id) {
    CostumeEntity entity = this.costumeRepository.findById(id).orElseThrow(
        () -> new IdNotFound("El id del disfraz es invalido"));
    this.costumeRepository.deleteById(id);
  }

  @Override
  public CostumeHistoryDTO getHistory(Long id) {
    CostumeEntity entity = this.costumeRepository.findById(id).orElseThrow(
        () -> new IdNotFound("El id del disfraz no existe"));
    CostumeHistoryDTO history = costumeMapper.costumeHistoryEntity2Dto(entity);
    return history;

  }

  @Override
  public void returnAndUpdate(ReturnDTO returnDTO) {

    TransactionEntity transaction = transactionRepository.findById(returnDTO.getTransactionId()).orElseThrow(
        () -> new IdNotFound("El id " + returnDTO.getTransactionId() + " no existe en la base de datos"));

    ClientEntity client = this.clientRepository.findByDocumentNumber(returnDTO.getClientDocument()).orElseThrow(
        () -> new ParamNotFound("El dni " + returnDTO.getClientDocument() + "  No existe en la base de datos"));

    Set<CostumeEntity> costumes = new HashSet<>();

    for (Long costumeId : returnDTO.getCostumesIds()) {
      CostumeEntity costume = costumeRepository.findById(costumeId).orElseThrow(
          () -> new IdNotFound("El id " + costumeId + "  no existe en la base de datos"));

      if (!returnDTO.getDevolution() && !costume.getClients().contains(client)) {
        throw new ParamNotFound("el cliente" + client.getName() + " " + client.getLastName() + " no reservó disfraz con id " + costumeId);
      }

      if (!costume.getClients().contains(client)) {
        throw new ParamNotFound("el cliente  " + client.getName() + " " + client.getLastName() + "  no tiene el disfraz con id " + costumeId);
      }

      if (!returnDTO.getDevolution()) {
        costume.setStatus(CostumeStatus.ALQUILADO);
      } else {
        costume.setStatus(CostumeStatus.DISPONIBLE);
        costume.setReservationDate(null);
        costume.setDeadLine(null);
        costume.getClients().remove(client);
      }

      costumes.add(costume);
    }

    if (!returnDTO.getDevolution()) {
      client.setClientStatus(ClientStatus.ACTIVO);

    } else {
      client.setClientStatus(ClientStatus.INACTIVO);
      transaction.setComplete(true);
      client.getCustomes().removeAll(costumes);
    }
    if (returnDTO.getTotalPayment()){
      transaction.setPending(0.0);
      transaction.setPartialPayment(0.0);
      transaction.setTotalPayment(true);
      transaction.setStatus(AmountStatus.APROVE);
    }
    clientRepository.save(client);
    costumeRepository.saveAll(costumes);
    transactionRepository.save(transaction);
  }

}

