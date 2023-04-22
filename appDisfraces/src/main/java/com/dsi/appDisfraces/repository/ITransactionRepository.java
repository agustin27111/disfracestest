package com.dsi.appDisfraces.repository;

import com.dsi.appDisfraces.dto.TransactionMonthTotalsDto;
import com.dsi.appDisfraces.dto.TransactionTotalsDto;
import com.dsi.appDisfraces.entity.TransactionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepository extends JpaRepository<TransactionEntity,Long> {

  TransactionEntity findTransactionsByDisfracesId(Long costumeId);

  List<TransactionEntity> findAllByDisfracesId(Long costumeId);

  Optional<List<TransactionEntity>> findByClientDocumentNumber(String documentNumber);

  @Query("SELECT SUM(t.ammount) as totalAmount, SUM(t.pending) as totalPending FROM TransactionEntity t")
  TransactionTotalsDto getTransactionTotals();

  @Query("SELECT new com.dsi.appDisfraces.dto.TransactionMonthTotalsDto(SUM(t.ammount), SUM(t.pending)) FROM TransactionEntity t WHERE YEAR(t.rentDate) = YEAR(CURRENT_DATE()) AND MONTH(t.rentDate) = MONTH(CURRENT_DATE())")
  TransactionMonthTotalsDto getCurrentMonthTransactionTotals();

}
