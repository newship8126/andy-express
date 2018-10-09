package com.manage.express.model;

public class OrderExpressInfo {
  private Integer rowId;

  private String orderNo;

  private String LogisticCode;

  private String shipperCode;

  private String senderCode;

  private Integer status;

  private String updateTime;

  private String expName;

  public Integer getRowId() {
    return rowId;
  }

  public void setRowId(Integer rowId) {
    this.rowId = rowId;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getLogisticCode() {
    return LogisticCode;
  }

  public void setLogisticCode(String logisticCode) {
    LogisticCode = logisticCode;
  }

  public String getShipperCode() {
    return shipperCode;
  }

  public void setShipperCode(String shipperCode) {
    this.shipperCode = shipperCode;
  }

  public String getSenderCode() {
    return senderCode;
  }

  public void setSenderCode(String senderCode) {
    this.senderCode = senderCode;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getExpName() {
    return expName;
  }

  public void setExpName(String expName) {
    this.expName = expName;
  }
}
