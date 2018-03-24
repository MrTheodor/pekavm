package com.jkrajniak;

public class BollardNotFound extends Exception {

  public BollardNotFound(String bollardTag) {
    super(bollardTag);
  }
}
