package com.manage.express.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Logistic {
  @JsonProperty("LogisticCode")
  private String logisticCode;

  @JsonProperty("ShipperCode")
  private String shipperCode;

  @JsonProperty("Traces")
  private List<Traces> traces = new ArrayList<>();

  @JsonProperty("State")
  private String state;

  @JsonProperty("EBusinessID")
  private String eBusinessId;

  @JsonProperty("Success")
  private Boolean success;

  @JsonProperty("Location")
  private String location;

  @JsonProperty("StateEx")
  private String stateEx;

  public String getLogisticCode() {
    return logisticCode;
  }

  public void setLogisticCode(String logisticCode) {
    this.logisticCode = logisticCode;
  }

  public String getShipperCode() {
    return shipperCode;
  }

  public void setShipperCode(String shipperCode) {
    this.shipperCode = shipperCode;
  }

  public List<Traces> getTraces() {
    return traces;
  }

  public void setTraces(List<Traces> traces) {
    this.traces = traces;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String geteBusinessId() {
    return eBusinessId;
  }

  public void seteBusinessId(String eBusinessId) {
    this.eBusinessId = eBusinessId;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getStateEx() {
    return stateEx;
  }

  public void setStateEx(String stateEx) {
    this.stateEx = stateEx;
  }
}
