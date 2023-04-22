package com.dsi.appDisfraces.service;

import com.dsi.appDisfraces.dto.CostumeDetailDTO;
import com.dsi.appDisfraces.dto.CostumeHistoryDTO;
import com.dsi.appDisfraces.dto.CostumeRequestDTO;
import com.dsi.appDisfraces.dto.CostumeTableDTO;
import com.dsi.appDisfraces.dto.ReturnDTO;
import java.util.List;

public interface IcostumeService {

  CostumeRequestDTO saveCostume(CostumeRequestDTO costumeDTO);

  CostumeDetailDTO getDetailsById(Long id);

  List<CostumeTableDTO> findAll();

  CostumeRequestDTO update(Long id, CostumeRequestDTO newCostume);

  void delete(Long id);

  CostumeHistoryDTO getHistory(Long id);

  void returnAndUpdate(ReturnDTO returnDTO);
}
