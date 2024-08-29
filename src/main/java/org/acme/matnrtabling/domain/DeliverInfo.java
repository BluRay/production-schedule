package org.acme.matnrtabling.domain;

import java.time.LocalDate;

public class DeliverInfo {
  private LocalDate deliverDate; // 交期
  private int demandQuantity;    // 数量
  private int trialQuantity;     // 试制数量
  private LocalDate trialDate;   // 试制日期
  private int productDayCount;   // 可生产天数 用于计算是否超产能
  private ProductMatnr matnr;

  public DeliverInfo(LocalDate deliverDate, int demandQuantity, int trialQuantity, LocalDate trialDate, int productDayCount, ProductMatnr matnr) {
    this.deliverDate = deliverDate;
    this.demandQuantity = demandQuantity;
    this.productDayCount = productDayCount;
    this.trialQuantity = trialQuantity;
    this.trialDate = trialDate;
    this.matnr = matnr;
  }

  public LocalDate getDeliverDate() {
    return deliverDate;
  }
  public int getDemandQuantity() {
    return demandQuantity;
  }
  public int getTrialQuantity() {
    return trialQuantity;
  }
  public LocalDate getTrialDate() {
    return trialDate;
  }
  public int getProductDayCount() {
    return productDayCount;
  }
  public ProductMatnr getProductMatnr() {
    return matnr;
  }
}
