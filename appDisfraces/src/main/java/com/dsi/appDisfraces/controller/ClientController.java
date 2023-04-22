package com.dsi.appDisfraces.controller;

import com.dsi.appDisfraces.dto.ClientHistoryDTO;
import com.dsi.appDisfraces.dto.ClientRequestDTO;
import com.dsi.appDisfraces.dto.ClientTableDto;
import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.repository.IClientRepository;
import com.dsi.appDisfraces.service.IClientService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
public class ClientController {
  @Autowired
  private IClientService clientService;
  @Autowired
  private IClientRepository clientRepository;



  @PostMapping("/newClient")
  public ResponseEntity<ClientRequestDTO> createClient(@RequestBody ClientRequestDTO dto){

    ClientRequestDTO result = this.clientService.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientRequestDTO> getclient(@PathVariable Long id){
    ClientRequestDTO client = this.clientService.getDetailsById(id);
    return ResponseEntity.ok(client);
  }

  @GetMapping("/document/{documentNumber}")
  public ResponseEntity<ClientRequestDTO> getClientByDocument(@PathVariable String documentNumber){
    ClientRequestDTO client = clientService.getDetailByDocument(documentNumber);
    return ResponseEntity.ok(client);
  }
      //Trae la lista basica de clientes con el ULTIMO disfraz alquilado,
  @GetMapping
  public ResponseEntity<List<ClientTableDto>> getAllClients(){
    List<ClientTableDto> clients = this.clientService.findAll();
    return ResponseEntity.ok().body(clients);

  }

  @PatchMapping("/{id}")
  public ResponseEntity<ClientRequestDTO> update(
       @PathVariable Long id, @RequestBody ClientRequestDTO personaje) {
    ClientRequestDTO result = this.clientService.update(id, personaje);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id){
    this.clientService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


    //trae el historial de disfraces alquilados por el cliente, para la lista de detalles del cliente


  @GetMapping("/{id}/history")
  public ResponseEntity<ClientHistoryDTO> clientHistory(@PathVariable Long id){
    ClientHistoryDTO history = this.clientService.getHistory(id);
    return ResponseEntity.ok(history);
  }


  }


