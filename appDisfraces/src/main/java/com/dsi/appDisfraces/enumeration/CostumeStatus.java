package com.dsi.appDisfraces.enumeration;

public enum CostumeStatus {

  ALQUILADO("ALQUILADO"), DISPONIBLE("DISPONIBLE"), RESERVADO("RESERVADO");

  private final String name ;

  CostumeStatus(String name) {
    this.name = name;
  }
  public CostumeStatus getName() {
    return CostumeStatus.valueOf(name);
  }

}
