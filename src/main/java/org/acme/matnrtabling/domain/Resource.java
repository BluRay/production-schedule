package org.acme.matnrtabling.domain;

import java.util.List;
import java.time.LocalDate;

public class Resource {
  private String workshop;
  private String name;
  private String code;
  private String type;
  private ProductMatnr matnr;
  private int productivity;       // 产能
  private int pri;                // 优先级
  private int productcycle;       // 生产周期
  private int inventoryQty;       // 初始库存
  // private int minBatch;        // 最小生产批次
  List<ResDateOverLoadProductivity> productivityList;
  private boolean status;
  
  public Resource(String workshop, String name, String code, String type, ProductMatnr matnr, int productivity, int pri, int productcycle, int inventoryQty, List<ResDateOverLoadProductivity> productivityList, boolean status) {
      this.workshop = workshop;
      this.name = name;
      this.code = code;
      this.type = type;
      this.matnr = matnr;
      this.productivity = productivity;
      this.pri = pri;
      this.productcycle = productcycle;
      this.inventoryQty = inventoryQty;
      this.productivityList = productivityList;
      this.status = status;
  }
  
  public String getWorkshop() {
      return workshop;
  }
  public String getName() {
      return name;
  }
  public String getCode() {
      return code;
  }
  public String getType() {
      return type;
  }
  public ProductMatnr getMatnr() {
      return matnr;
  }
  public int getProductivity() {
      return productivity;
  }
  public int getPri() {
      return pri;
  }
  public int getProductcycle() {
      return productcycle;
  }
  public int getInventoryQty() {
      return inventoryQty;
  }
  public void setInventoryQty(int inventoryQty) {
      this.inventoryQty = inventoryQty;
  }
  public int getOverloadProductcycle(LocalDate workDate) {
    int overloadProductcycle = 0;
    if (productivityList == null) {
      return overloadProductcycle;
    }
    for(ResDateOverLoadProductivity olpt : productivityList) {
      if (olpt.getWorkDate().isEqual(workDate)) {
        overloadProductcycle = olpt.getOverLoadProductivity();
      }
    }
    return overloadProductcycle;
  }
  public List<ResDateOverLoadProductivity> getProductivityList() {
    return productivityList;
  }
  public void setProductivityList(List<ResDateOverLoadProductivity> productivityList) {
    this.productivityList = productivityList;
  }
  public boolean getStatus() {
      return status;
  }
}