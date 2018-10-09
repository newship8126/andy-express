package com.manage.express.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ExpressInfo {
  @JsonProperty("PushTime")
  private String pushTime;

  @JsonProperty("EBusinessID")
  private String eBusinessId;

  @JsonProperty("Data")
  private List<Logistic> data = new ArrayList<>();

  @JsonProperty("Count")
  private Integer count;

  public String getPushTime() {
    return pushTime;
  }

  public void setPushTime(String pushTime) {
    this.pushTime = pushTime;
  }

  public String geteBusinessId() {
    return eBusinessId;
  }

  public void seteBusinessId(String eBusinessId) {
    this.eBusinessId = eBusinessId;
  }

  public List<Logistic> getData() {
    return data;
  }

  public void setData(List<Logistic> data) {
    this.data = data;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
