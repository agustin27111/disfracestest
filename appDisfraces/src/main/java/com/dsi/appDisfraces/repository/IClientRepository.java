package com.dsi.appDisfraces.repository;

import com.dsi.appDisfraces.entity.ClientEntity;
import com.dsi.appDisfraces.entity.CostumeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends JpaRepository<ClientEntity, Long> {

  Optional<ClientEntity> findByDocumentNumber(String documentNumber);


}
