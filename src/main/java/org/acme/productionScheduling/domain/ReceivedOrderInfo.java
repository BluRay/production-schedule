package org.acme.productionScheduling.domain;

import java.time.LocalDate;

/**
 * 销售来单信息
 */
public class ReceivedOrderInfo {

  private int orderNum;//来单数量

  private LocalDate orderDate;//来单日期

  private String pMatnr;

  public ReceivedOrderInfo() {
  }

  public ReceivedOrderInfo(int orderNum, LocalDate orderDate, String pMatnr) {
    this.orderNum = orderNum;
    this.orderDate = orderDate;
    this.pMatnr = pMatnr;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  public String getpMatnr() {
    return pMatnr;
  }

  public void setpMatnr(String pMatnr) {
    this.pMatnr = pMatnr;
  }

}
