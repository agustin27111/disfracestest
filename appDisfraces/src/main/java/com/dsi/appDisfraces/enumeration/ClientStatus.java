package com.dsi.appDisfraces.enumeration;

public enum ClientStatus {

  ACTIVO("ACTIVO"), CON_RESERVA("CON_RESERVA"), INACTIVO("INACTIVO");

  private final String name ;

  ClientStatus (String name) {
    this.name = name;
  }
  public ClientStatus getName() {
    return ClientStatus.valueOf(name);
  }

}
