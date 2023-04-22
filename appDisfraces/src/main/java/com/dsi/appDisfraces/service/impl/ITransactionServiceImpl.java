package com.dsi.appDisfraces.service.impl;

import com.dsi.appDisfraces.dto.TransactionDTO;
import com.dsi.appDisfraces.dto.TransactionMonthTotalsDto;
import com.dsi.appDisfraces.dto.TransactionTotalsDto;
import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.entity.CostumeEntity;
import com.dsi.appDisfraces.entity.TransactionEntity;
import com.dsi.appDisfraces.enumeration.AmountStatus;
import com.dsi.appDisfraces.enumeration.ClientStatus;
import com.dsi.appDisfraces.enumeration.CostumeStatus;
import com.dsi.appDisfraces.exception.IdNotFound;
import com.dsi.appDisfraces.exception.ParamNotFound;
import com.dsi.appDisfraces.mapper.TransactionMapper;
import com.dsi.appDisfraces.repository.IClientRepository;
import com.dsi.appDisfraces.repository.ICostumeRepository;
import com.dsi.appDisfraces.repository.ITransactionRepository;
import com.dsi.appDisfraces.service.ITransactionService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ITransactionServiceImpl implements ITransactionService {

  @Autowired
  ITransactionRepository transactionRepository;
  @Autowired
  IClientRepository clientRepository;
  @Autowired
  ICostumeRepository costumeRepository;
  @Autowired
  TransactionMapper transactionMapper;
  @Autowired
  TransactionTemplate transactionTemplate;

  @Override
  public String getCostumeNameById(Long id) {
    CostumeEntity costume = costumeRepository.findById(id).orElse(null);
    return costume != null ? costume.getName() : null;
  }

  @Override
  public List<TransactionDTO> findAll() {
    List<TransactionEntity> transactions = transactionRepository.findAll();
    List<TransactionDTO> result = transactionMapper.transactionEntityList2DTOList(transactions);
    return result;
  }

  @Override
  public TransactionDTO getDetailById(Long id) {
    TransactionEntity transaction = transactionRepository.findById(id).orElseThrow(
        ()->new IdNotFound("El id "+id+" no se encntra en la base de datos"));
    TransactionDTO result = transactionMapper.transactionEntityToDTO(transaction);
    return result;
  }

  @Override
  public List<TransactionDTO> getDetailByDocumentNumber(String documentNumber) {
    List<TransactionEntity> transactions = transactionRepository.findByClientDocumentNumber(documentNumber).orElseThrow(
        ()-> new ParamNotFound("El número de documento " + documentNumber + " no se encuentra en la base de datos."));
    List<TransactionDTO> result = transactionMapper.transactionEntityList2DTOList(transactions);
    return result;
  }

  @Override
  public TransactionTotalsDto getTransactionTotals() {
    Map<String, Object> totalsMap = (Map<String, Object>) transactionRepository.getTransactionTotals();//TODO REVISAR PARA DEPLOY
    Double totalAmount = (Double) totalsMap.get("totalAmount");
    Double pendingAmount = (Double) totalsMap.get("pendingAmount");
    return new TransactionTotalsDto(totalAmount, pendingAmount);
  }

  @Override
  @Transactional
  public TransactionMonthTotalsDto getCurrentMonthTransactionTotals() {
    TransactionMonthTotalsDto dto = transactionRepository.getCurrentMonthTransactionTotals();
    return dto;
  }


  @Transactional
  @Override
  public TransactionDTO create(TransactionDTO transactionDTO) {
    ClientEntity client = clientRepository.findById(transactionDTO.getClientId())
        .orElseThrow(() -> new ParamNotFound("El id del cliente es invalido"));

    Set<CostumeEntity> costumes = new HashSet<>();

    for (Long costumeId : transactionDTO.getCostumeIds()) {
      CostumeEntity costume = costumeRepository.findById(costumeId)
          .orElseThrow(() -> new ParamNotFound("No existe un disfraz con el ID " + costumeId + ", verifique nuevamente"));

     /* if (costume.getStatus() == CostumeStatus.ALQUILADO) {
        throw new ParamNotFound("El disfraz " + costume.getName() + " con ID " + costumeId + " ya se encuentra alquilado.");
      }*/

      LocalDate returnDate = transactionDTO.getDeadline();

      List<TransactionEntity> transactions = transactionRepository.findAllByDisfracesId(costumeId);
      for (TransactionEntity transaction : transactions) {
        if (!transaction.getComplete()) {
          LocalDate reservationDate2 = transaction.getRentDate();
          if (reservationDate2 != null) {
            LocalDate deadline2 = transaction.getDeadline();
            if ((reservationDate2.isEqual(transactionDTO.getReservationDate()) || reservationDate2.isAfter(transactionDTO.getReservationDate())) && reservationDate2.isBefore(
                transactionDTO.getDeadline())) {
              throw new ParamNotFound("El disfraz " + costume.getName() + " con ID " + costumeId + " se encuentra reservado para la fecha seleccionada.");
            } else if (reservationDate2.isBefore(transactionDTO.getDeadline()) && deadline2.isAfter(transactionDTO.getReservationDate())) {
              throw new ParamNotFound("El disfraz " + costume.getName() + " con ID " + costumeId + " se encuentra reservado para la fecha seleccionada.");
            } else if (returnDate.equals(reservationDate2)) {
              throw new ParamNotFound("El disfraz " + costume.getName() + " con ID " + costumeId + " ya está reservado para la fecha seleccionada.");
            } else if ((reservationDate2.isBefore(returnDate.plusDays(1)) && reservationDate2.isAfter(returnDate.minusDays(3)))
                || (returnDate.isBefore(reservationDate2.plusDays(1)) && returnDate.isAfter(reservationDate2.minusDays(3)))) {
              throw new ParamNotFound("El disfraz " + costume.getName() + " con ID " + costumeId + " se encuentra reservado para la fecha seleccionada.");
            }
          }
        }
      }
      costumes.add(costume);
    }

    TransactionEntity transactionEntity = new TransactionEntity();
    transactionEntity.setClient(client);
    transactionEntity.setDisfraces(costumes);
    transactionEntity.setRentDate(transactionDTO.getReservationDate());
    transactionEntity.setDeadline(transactionDTO.getDeadline());
    transactionEntity.setBillPayment(transactionDTO.getCheckIn());
    transactionEntity.setType(transactionDTO.getType());
    transactionEntity.setComplete(false);
    transactionEntity.setAmmount(transactionDTO.getAmount());
    transactionEntity.setPartialPayment(transactionDTO.getPartialPayment());
    if (transactionDTO.getPartialPayment()==null||transactionDTO.getPartialPayment()==0) {
      transactionEntity.setPending(0.0);
    }else {transactionEntity.setPending(transactionDTO.getAmount() - transactionDTO.getPartialPayment());
    }
    if (transactionEntity.getPending()==0){
      transactionEntity.setPartialPayment(0.0);
      transactionEntity.setStatus(AmountStatus.APROVE);
    }
    if(transactionEntity.getPending()!=0){
      transactionEntity.setStatus(AmountStatus.PENDING);
    }
    transactionRepository.save(transactionEntity);

    LocalDate reservationDate = transactionDTO.getReservationDate();
    LocalDate currentDate = LocalDate.now();

    costumes.forEach(costume -> {
      costume.setStatus(currentDate.isBefore(reservationDate) ? CostumeStatus.RESERVADO : CostumeStatus.ALQUILADO);
      costume.setReservationDate(transactionDTO.getReservationDate());
      costume.setDeadLine(transactionDTO.getDeadline());
      costumeRepository.save(costume);
    });

    client.setClientStatus(currentDate.isBefore(reservationDate) ? ClientStatus.CON_RESERVA : ClientStatus.ACTIVO);
    client.setCustomes(costumes);
    clientRepository.save(client);

    TransactionDTO result = new TransactionDTO();
    result.setId(transactionEntity.getId());
    result.setClientId(transactionEntity.getClient().getId());
    result.setCostumeIds(transactionEntity.getDisfraces().stream().map(CostumeEntity::getId).collect(Collectors.toList()));
    result.setNames(transactionEntity.getDisfraces().stream().map(costume -> getCostumeNameById(costume.getId())).collect(Collectors.toList()));
    result.setReservationDate(transactionEntity.getRentDate());
    result.setDeadline(transactionEntity.getDeadline());
    result.setType(transactionEntity.getType());
    result.setAmount(transactionEntity.getAmmount());
    result.setCheckIn(transactionEntity.getBillPayment());
    result.setPartialPayment(transactionEntity.getPartialPayment());
    result.setPending(transactionEntity.getPending());
    result.setStatusPayment(String.valueOf(transactionEntity.getStatus()));


    return result;
  }//TODO: ACTUALIZAR STATUS DE AMOUNT CUNADO SE RETIRA UN DISFRAZ
}
/*transaccion estados de pago aprove , pending , partial

if(pagostate.aprove){
 (transaction.getamount)=+ totalAmount
}

AGREGAR BOTON QUE DIGA MONTO 1000 / PAGO PARCIAL 600 / PENDIENTE 400

AGREGAR FUNCION EN FRONT QUE RESUELVA LA LOGICA DEL MONTO CON LOS PARCIALES, QUE VERIFIQUE ANTES DE ENVIAR LOS DATOS
QUE EL PARCIAL Y EL PENDIENTE SUMEN EL TOTAL, SI ES QUE SON DIFERENTES DE NULL


 */