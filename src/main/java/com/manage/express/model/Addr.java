package com.manage.express.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Addr {
  @JsonProperty("Name")
  private String name;

  @JsonProperty("Mobile")
  private String mobile;

  @JsonProperty("ProvinceName")
  private String provinceName;

  @JsonProperty("CityName")
  private String cityName;

  @JsonProperty("ExpAreaName")
  private String expAreaName;

  @JsonProperty("Address")
  private String address;
}
