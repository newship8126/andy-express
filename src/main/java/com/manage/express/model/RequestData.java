package com.manage.express.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestData {
  @JsonProperty("OrderCode")
  private String orderCode;

  @JsonProperty("ShipperCode")
  private String shipperCode;

  @JsonProperty("LogisticCode")
  private String logisticCode;

  @JsonProperty("PayType")
  private Integer payType = 1;

  @JsonProperty("ExpType")
  private Integer expType = 1;

  @JsonProperty("IsNotice")
  private Integer isNotice = 0;

  @JsonProperty("Cost")
  private Double cost = new Double(1);

  @JsonProperty("OtherCost")
  private Double otherCost = new Double(1);

  @JsonProperty("Sender")
  private Addr sender;

  @JsonProperty("Receiver")
  private Addr receiver;

  public RequestData() {
    sender = new Addr();
    receiver = new Addr();
  }

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public String getShipperCode() {
    return shipperCode;
  }

  public void setShipperCode(String shipperCode) {
    this.shipperCode = shipperCode;
  }

  public String getLogisticCode() {
    return logisticCode;
  }

  public void setLogisticCode(String logisticCode) {
    this.logisticCode = logisticCode;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public Integer getExpType() {
    return expType;
  }

  public void setExpType(Integer expType) {
    this.expType = expType;
  }

  public Integer getIsNotice() {
    return isNotice;
  }

  public void setIsNotice(Integer isNotice) {
    this.isNotice = isNotice;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public Double getOtherCost() {
    return otherCost;
  }

  public void setOtherCost(Double otherCost) {
    this.otherCost = otherCost;
  }

  public Addr getSender() {
    return sender;
  }

  public void setSender(Addr sender) {
    this.sender = sender;
  }

  public Addr getReceiver() {
    return receiver;
  }

  public void setReceiver(Addr receiver) {
    this.receiver = receiver;
  }
}
