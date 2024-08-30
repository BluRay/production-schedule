package org.acme.matnrtabling.domain;

import java.util.List;
import java.time.LocalDate;

// 生产资源 [产线 物料 对应关系]
public class ProductResource {
  private String name;
  private String code;
  private ProductLine line;       // 产线
  private ProductMatnr matnr;     // 生产物料
  private int productivity;       // 产能
  private int pri;                // 优先级
  private int productcycle;       // 生产周期
  private int inventoryQty;       // 初始库存
  List<ResDateOverLoadProductivity> productivityList;
  private boolean status;
  
  public ProductResource(String name, String code, ProductLine line, ProductMatnr matnr, int productivity, int pri, int productcycle, int inventoryQty, List<ResDateOverLoadProductivity> productivityList, boolean status) {
      this.name = name;
      this.code = code;
      this.line = line;
      this.matnr = matnr;
      this.productivity = productivity;
      this.pri = pri;
      this.productcycle = productcycle;
      this.inventoryQty = inventoryQty;
      this.productivityList = productivityList;
      this.status = status;
  }
  
  public String getName() {
      return name;
  }
  public String getCode() {
      return code;
  }
  public ProductLine getProductLine() {
      return line;
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