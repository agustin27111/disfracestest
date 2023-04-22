package com.dsi.appDisfraces.service;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.ClientRequestDTO;
import com.dsi.appDisfraces.dto.ClientTableDto;
import java.io.IOException;
import java.util.List;

public interface IClientService {

  ClientRequestDTO save(ClientRequestDTO clientRequestDTO);

  ClientRequestDTO getDetailsById(Long id);

  List<ClientTableDto> findAll();

  ClientRequestDTO update(Long id, ClientRequestDTO personaje);


  void delete(Long id);

  ClientHistoryDTO getHistory(Long id);

  ClientRequestDTO getDetailByDocument(String documentNumber);
}
