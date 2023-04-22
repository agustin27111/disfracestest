package com.dsi.appDisfraces.entity;

import com.dsi.appDisfraces.enumeration.AmountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Transacciones")
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Cliente_id", referencedColumnName = "id")
  private ClientEntity client;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "Transaccion_Disfraz",
      joinColumns = @JoinColumn(name = "Transaccion_id"),
      inverseJoinColumns = @JoinColumn(name = "Disfraz_id")
  )
  private Set<CostumeEntity> disfraces = new HashSet<>();

  @Column(name = "Fecha_de_alquiler")
  private LocalDate rentDate;

  @Column(name = "Fecha_de_devolucion", nullable = true)
  private LocalDate deadline;

  @Column(name = "Tipo_de_pago")
  private String type;

  @Column(name = "REMITO")
  private String billPayment;

  @Column(name = "Fecha_pago")
  private LocalDate date;

  @Column(name = "completa")
  private Boolean complete; //TRUE SI DEVOLVIÓ EL DISFRAZ, FALSE SI NO DEVOLVIO EL DISFRAZ

  @Enumerated(EnumType.STRING)
  @Column(name = "Status pago")
  private AmountStatus status;

  @Column(name="pago parcial")
  private Double partialPayment;

  @Column(name = "Monto")
  private Double ammount;

  @Column(name = "pendiente")
  private Double pending;

  @Column(name= "Pago Realizado")// SI ABONA UN APARTE AL RESERVAR POR TELEFONO Y ABONA EL RESTO AL RETIRAR EL DISEFRAZ
  private Boolean totalPayment;//TRUE SI AL MOMENTO DE RETIRAR EL DISFRAZ ABONA EL TOTAL ADEUDADO, FALSE SI TIENE SALDO PENDIENTE AL RETIRAR

}
