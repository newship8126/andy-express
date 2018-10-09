package com.manage.express.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Traces {
  @JsonProperty("AcceptStation")
  private String acceptStation;

  @JsonProperty("AcceptTime")
  private String acceptTime;

  @JsonProperty("Location")
  private String location;

  @JsonProperty("Action")
  private String action;

  @JsonIgnore
  private String expCode;

  @JsonIgnore
  private String logistic;

  @JsonIgnore
  private Integer acceptState;

  @JsonIgnore
  private String pushTime;

  @JsonIgnore
  private String expName;

  @JsonIgnore
  private String trace;

  @JsonIgnore
  private Integer rowId;

  public String getAcceptStation() {
    return acceptStation;
  }

  public void setAcceptStation(String acceptStation) {
    this.acceptStation = acceptStation;
  }

  public String getAcceptTime() {
    return acceptTime;
  }

  public void setAcceptTime(String acceptTime) {
    this.acceptTime = acceptTime;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getExpCode() {
    return expCode;
  }

  public void setExpCode(String expCode) {
    this.expCode = expCode;
  }

  public String getLogistic() {
    return logistic;
  }

  public void setLogistic(String logistic) {
    this.logistic = logistic;
  }

  public Integer getAcceptState() {
    return acceptState;
  }

  public void setAcceptState(Integer acceptState) {
    this.acceptState = acceptState;
  }

  public String getPushTime() {
    return pushTime;
  }

  public void setPushTime(String pushTime) {
    this.pushTime = pushTime;
  }

  public String getExpName() {
    return expName;
  }

  public void setExpName(String expName) {
    this.expName = expName;
  }

  public String getTrace() {
    return trace;
  }

  public void setTrace(String trace) {
    this.trace = trace;
  }

  public Integer getRowId() {
    return rowId;
  }

  public void setRowId(Integer rowId) {
    this.rowId = rowId;
  }
}
