package org.acme.productionScheduling.domain;

import java.time.LocalDate;

//交付信息
public class DeliverInfo {
  
  private LocalDate deliverDate;//交付日期

  private int demandQuantity;//需求数量

  private String pMatnr;

  public DeliverInfo() {
  }

  public DeliverInfo(LocalDate deliverDate, int demandQuantity, String pMatnr) {
    this.deliverDate = deliverDate;
    this.demandQuantity = demandQuantity;
    this.pMatnr = pMatnr;
  }

  public LocalDate getDeliverDate() {
    return deliverDate;
  }

  public void setDeliverDate(LocalDate deliverDate) {
    this.deliverDate = deliverDate;
  }

  public int getDemandQuantity() {
    return demandQuantity;
  }

  public void setDemandQuantity(int demandQuantity) {
    this.demandQuantity = demandQuantity;
  }

  public String getpMatnr() {
    return pMatnr;
  }

  public void setpMatnr(String pMatnr) {
    this.pMatnr = pMatnr;
  }
  
}
