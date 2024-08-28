package org.acme.productionScheduling.domain;

import java.util.List;

/**
 * 本地资源
 * 问题事实：在求解的过程中不会改变
 */
public class LocalResource {

  private String werks;

  private String workshop;

  private String line;

  private int capacity;//产能

  private List<ProductionNum> productionNums;//排产日期及数量

  public LocalResource() {
  }

  public LocalResource(String werks, String workshop, String line, int capacity, List<ProductionNum> productionNums) {
    this.werks = werks;
    this.workshop = workshop;
    this.line = line;
    this.capacity = capacity;
    this.productionNums = productionNums;
  }

  public String getWerks() {
    return werks;
  }

  public void setWerks(String werks) {
    this.werks = werks;
  }

  public String getWorkshop() {
    return workshop;
  }

  public void setWorkshop(String workshop) {
    this.workshop = workshop;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public List<ProductionNum> getProductionNums() {
    return productionNums;
  }

  public void setProductionNums(List<ProductionNum> productionNums) {
    this.productionNums = productionNums;
  }

  // ************************************************************************
  // Complex methods
  // ************************************************************************

  //当前线别产量
  public int getProductionQuantity() {
    int quantity = 0;
    for (ProductionNum p : productionNums) {
      quantity += p.getProductionQuantity();
    }
    return quantity;
  }

}
