package com.dsi.appDisfraces.controller;

import com.dsi.appDisfraces.dto.TransactionDTO;
import com.dsi.appDisfraces.dto.TransactionMonthTotalsDto;
import com.dsi.appDisfraces.dto.TransactionTotalsDto;
import com.dsi.appDisfraces.service.ITransactionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")

public class TransactionController {
  @Autowired
  ITransactionService transactionService;


  @PostMapping("/newTransaction")
  ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO){
    TransactionDTO result = transactionService.create(transactionDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @GetMapping("")
  ResponseEntity<List<TransactionDTO>> getAll(){
    List<TransactionDTO> transactions = transactionService.findAll();
    return ResponseEntity.ok(transactions);
  }

  @GetMapping("/{id}")
  ResponseEntity<TransactionDTO> transactionById(@PathVariable Long id){
    TransactionDTO transaction = transactionService.getDetailById(id);
    return ResponseEntity.ok(transaction);
  }

  @GetMapping("/document/{documentNumber}")
  ResponseEntity<List<TransactionDTO>> transactionByDocumentNumber(@PathVariable String documentNumber){
    List<TransactionDTO> transactions = transactionService.getDetailByDocumentNumber(documentNumber);
    return ResponseEntity.ok(transactions);
  }

  @GetMapping("/totals")
  public ResponseEntity<TransactionTotalsDto> getTransactionTotals() {
    TransactionTotalsDto transactionTotals = transactionService.getTransactionTotals();
    return ResponseEntity.ok(transactionTotals);
  }
  @GetMapping("/totals/current-month")
  public ResponseEntity<TransactionMonthTotalsDto> getCurrentMonthTransactionTotals() {
    TransactionMonthTotalsDto currentMonthTotals = transactionService.getCurrentMonthTransactionTotals();
    if (currentMonthTotals == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(currentMonthTotals);
    }
  }



}
