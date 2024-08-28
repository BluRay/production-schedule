package org.acme.matnrtabling.domain;

import java.time.LocalDate;

/**
 * 销售来单信息
 */
public class ReceivedOrderInfo {
  private int orderNum;         //来单数量
  private LocalDate orderDate;  //来单日期
  private String matnr;
  
  public ReceivedOrderInfo(int orderNum, LocalDate orderDate, String matnr) {
    this.orderNum = orderNum;
    this.orderDate = orderDate;
    this.matnr = matnr;
  }
  public int getOrderNum() {
    return orderNum;
  }
  public LocalDate getOrderDate() {
    return orderDate;
  }
  public String getMatnr() {
    return matnr;
  }
}