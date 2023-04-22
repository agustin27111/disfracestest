package com.dsi.appDisfraces.enumeration;

public enum AmountStatus {

  APROVE("APROVE"), PENDING("PENDING"), PARTIAL("PARTIAL");

  private final String name ;

  AmountStatus (String name) {
    this.name = name;
  }
  public AmountStatus getName() {
    return AmountStatus.valueOf(name);
  }

}
