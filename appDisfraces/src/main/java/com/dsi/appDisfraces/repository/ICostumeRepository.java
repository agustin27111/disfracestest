package com.dsi.appDisfraces.repository;

import com.dsi.appDisfraces.entity.CostumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICostumeRepository extends JpaRepository<CostumeEntity, Long>{


}
