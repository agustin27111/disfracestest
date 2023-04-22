package com.dsi.appDisfraces.entity;

import com.dsi.appDisfraces.enumeration.ClientStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Lob;
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
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter @Setter
@Table(name="Clientes")
public class ClientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name= "Nombre", nullable = false)
  private String name;

  @Column(name = "Apellido", nullable = false)
  private String lastName;

  @Column(name = "Direccion", nullable = false)
  private String adress;

  @Column(name = "DNI", nullable = false)
  private String documentNumber;

  @Column(name = "Telefono", nullable = false)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name = "Status", columnDefinition = "varchar(255)")
  private ClientStatus clientStatus;
  public ClientEntity() {
    this.clientStatus = ClientStatus.INACTIVO;
  }


  @Column(name = "Tipo_Cliente", nullable = false)
  private String type;


  @Column(name = "Imagen DNI", nullable = false)
  private String image;


  @Column (name = "Fecha_creacion")
  @CreationTimestamp
  private LocalDate createDataTime;

  @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinTable(
      name= "Disfraz_Cliente",
      joinColumns = @JoinColumn(name= "Cliente_id"),
      inverseJoinColumns = @JoinColumn(name="Disfraz_id")
  )
  private Set<CostumeEntity> customes = new HashSet<>();

  private boolean deleted = Boolean.FALSE;

  @Column(name = "Last_Rented_Date")
  private LocalDate lastRentedDate;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, //TODO: revisar que lacascada este ok
      cascade = CascadeType.ALL)
  private List<TransactionEntity> transactions = new ArrayList<>();


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ClientEntity)) {
      return false;
    } else {
      ClientEntity clientEntity = (ClientEntity) obj;
      if (this.getDocumentNumber() != null) {
        return this.getDocumentNumber().equals(clientEntity.getDocumentNumber());

      } else {
        return false;
      }
    }
  }



  //TODO: Ver como relacionar el cliente con la fecha de entrega del disfraz (puede ser
  // mostrando la fecha de entrega cumpliendo la condicion  si el status está como pendiente de entrega)













}
