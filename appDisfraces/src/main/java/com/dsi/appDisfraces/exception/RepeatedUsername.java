package com.dsi.appDisfraces.exception;

import javax.naming.AuthenticationException;

public class RepeatedUsername extends RuntimeException {

  public RepeatedUsername(String error) {
    super(error);
  }
}
