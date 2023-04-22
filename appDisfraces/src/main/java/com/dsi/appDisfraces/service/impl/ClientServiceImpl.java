package com.dsi.appDisfraces.service.impl;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.ClientRequestDTO;
import com.dsi.appDisfraces.dto.ClientTableDto;
import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.exception.IdNotFound;
import com.dsi.appDisfraces.exception.ParamNotFound;
import com.dsi.appDisfraces.exception.RepeatedUsername;
import com.dsi.appDisfraces.mapper.ClientMapper;
import com.dsi.appDisfraces.repository.IClientRepository;
import com.dsi.appDisfraces.service.IClientService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.parser.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements IClientService {
  @Autowired
  ClientMapper clientMapper;
  @Autowired
  IClientRepository clientRepository;

  @Override
  public ClientRequestDTO save(ClientRequestDTO dto){

    Optional<ClientEntity> user = clientRepository.findByDocumentNumber(dto.getDocumentNumber());
    if(user.isPresent()) {
      throw new ParamNotFound("El nombre de usuario ya está en uso");
    }

    ClientEntity entity2 = clientMapper.clientDTO2Entity(dto);
    ClientEntity entitySaved = clientRepository.save(entity2);
    ClientRequestDTO result = clientMapper.clientEntity2Dto(entitySaved);

    return result;
  }

  @Override
  public ClientRequestDTO getDetailsById(Long id) {
    ClientEntity entity = clientRepository.findById(id).orElseThrow(
        () -> new ParamNotFound("no se encuentra el id del cliente"));
    ClientRequestDTO clientDTO = this.clientMapper.clientEntity2Dto(entity);
    return clientDTO;
  }

  @Override
  public ClientRequestDTO getDetailByDocument(String documentNumber) {
    ClientEntity entity = clientRepository.findByDocumentNumber(documentNumber).orElseThrow(
        ()->new ParamNotFound("El nuemro de DNI "+documentNumber+" no se encuentra en la base de datos"));
    ClientRequestDTO clienDTO = this.clientMapper.clientEntity2Dto(entity);

    return clienDTO;
  }

  @Override
  public List<ClientTableDto> findAll() {
    List<ClientEntity> entities = this.clientRepository.findAll();
    List<ClientTableDto> result = this.clientMapper.clientEntityList2DTOList(entities);

    return result;
  }


  @Override
  public ClientRequestDTO update(Long id, ClientRequestDTO clientRequestDTO) {
    Optional<ClientEntity> entity = this.clientRepository.findById(id);
    ClientEntity client = entity.orElseThrow(
        ()-> new ParamNotFound("El ID del cleinte es invalido"));
    this.clientMapper.clientEntityUpdate(client, clientRequestDTO);
    ClientEntity updateEntity = this.clientRepository.save(client);
    ClientRequestDTO result = clientMapper.clientEntity2Dto(updateEntity);

    return result;
  }

  @Override
  public void delete(Long id) {
    ClientEntity entity = this.clientRepository.findById(id).orElseThrow(
        ()-> new ParamNotFound("El ID del usuario no existe o es incorrecto"));
    this.clientRepository.deleteById(id);
  }

  @Override
  public ClientHistoryDTO getHistory(Long id) {
    ClientEntity entity = this.clientRepository.findById(id).orElseThrow(
        ()-> new ParamNotFound("El id del cliente no existe"));
    ClientHistoryDTO history = clientMapper.clientHistoryEntity2Dto(entity);
    return history;
  }



}
 
