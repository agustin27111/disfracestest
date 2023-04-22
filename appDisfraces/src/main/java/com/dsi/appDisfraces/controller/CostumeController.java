package com.dsi.appDisfraces.controller;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.CostumeDetailDTO;
import com.dsi.appDisfraces.dto.CostumeHistoryDTO;
import com.dsi.appDisfraces.dto.CostumeRequestDTO;
import com.dsi.appDisfraces.dto.CostumeTableDTO;
import com.dsi.appDisfraces.dto.ReturnDTO;
import com.dsi.appDisfraces.repository.ICostumeRepository;
import com.dsi.appDisfraces.service.IcostumeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/costumes")
@CrossOrigin(origins = "*")

public class CostumeController {

  @Autowired
  IcostumeService costumeService;
  @Autowired
  ICostumeRepository costumeRepository;

  @PostMapping("/newCostume")
  public ResponseEntity<CostumeRequestDTO> createCostume(@RequestBody CostumeRequestDTO costumeDTO) {
    CostumeRequestDTO result = this.costumeService.saveCostume(costumeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  //trae disfraz con el ultimo cliente que lo alquilo si es que esta alquilado
  @GetMapping("/{id}")
  public ResponseEntity<CostumeDetailDTO> getcostume(@PathVariable Long id) {
    CostumeDetailDTO client = this.costumeService.getDetailsById(id);
    return ResponseEntity.ok(client);
  }

  //Tabla de pantalla principal

  @GetMapping()
  public ResponseEntity<List<CostumeTableDTO>> getAllCostumes() {
    List<CostumeTableDTO> costumes = this.costumeService.findAll();
    return ResponseEntity.ok().body(costumes);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CostumeRequestDTO> update(
      @PathVariable Long id, @RequestBody CostumeRequestDTO newCostume) {
    CostumeRequestDTO result = this.costumeService.update(id, newCostume);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCotume(@PathVariable Long id) {
    this.costumeService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/{id}/history")
  public ResponseEntity<CostumeHistoryDTO> costumeHistory(@PathVariable Long id) {
    CostumeHistoryDTO history = this.costumeService.getHistory(id);
    return ResponseEntity.ok(history);
  }

  @PutMapping("/return")
  public ResponseEntity<Void> returnCostume(@RequestBody ReturnDTO returnDTO) {
    this.costumeService.returnAndUpdate(returnDTO);
    return ResponseEntity.ok().build();
  }




}
