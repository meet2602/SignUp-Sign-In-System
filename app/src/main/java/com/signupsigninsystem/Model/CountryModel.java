package com.signupsigninsystem.Model;

public class CountryModel {

  private String code;
  private String name;
  private String dialCode;
  private int flag;
  private String currency;


  public CountryModel(String code, String name, String dialCode, int flag, String currency) {
    this.code = code;
    this.name = name;
    this.dialCode = dialCode;
    this.flag = flag;
    this.currency = currency;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getDialCode() {
    return dialCode;
  }

  public void setDialCode(String dialCode) {
    this.dialCode = dialCode;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
