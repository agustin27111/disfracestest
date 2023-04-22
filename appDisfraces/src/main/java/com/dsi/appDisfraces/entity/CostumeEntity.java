package com.dsi.appDisfraces.entity;

import com.dsi.appDisfraces.enumeration.CostumeStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Disfraces")
@Getter
@Setter
public class CostumeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "Nombre", nullable = false)
  private String name;

  @Column(name="Detalle")
  private String detail;

  @Enumerated(EnumType.STRING)
  @Column(name = "Status", columnDefinition = "varchar(255)")
  private CostumeStatus status;
  public CostumeEntity() {
    this.status = CostumeStatus.DISPONIBLE;
  }

  @Column(name = "Reserva", nullable = true)
  private LocalDate reservationDate;

  @Column(name = "Entrega", nullable = true)
  private LocalDate deadLine;

  @Column(name = "Imagen", nullable = true)
  private String image;

  @ManyToMany(mappedBy = "customes")
  private List<ClientEntity> clients = new ArrayList<>();

  private boolean deleted = Boolean.FALSE;

  public boolean isRented() {
    return status == CostumeStatus.ALQUILADO;
  }

  @Column (name= "Fecha de creacion")
  @CreationTimestamp
  private LocalDate creationDate;

  @Column ( name= "Color")
  private String colour;

  @ManyToMany(mappedBy = "disfraces", cascade = CascadeType.PERSIST) //TODO: Revisar que la cascade ande ok.

  private Set<TransactionEntity> transactions = new HashSet<>();












}
