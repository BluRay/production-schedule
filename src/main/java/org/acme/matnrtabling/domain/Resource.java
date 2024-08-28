package org.acme.matnrtabling.domain;

public class Resource {
  private String workshop;
  private String name;
  private String code;
  private String type;
  private ProductMatnr matnr;
  private int productivity;       // 产能
  private int pri;                // 优先级
  private int productcycle;       // 生产周期
  // private int inventory_qty;   // 初始库存
  // private int minBatch;        // 最小生产批次
  private boolean status;
  
  public Resource(String workshop, String name, String code, String type, ProductMatnr matnr, int productivity, int pri, int productcycle, boolean status) {
      this.workshop = workshop;
      this.name = name;
      this.code = code;
      this.type = type;
      this.matnr = matnr;
      this.productivity = productivity;
      this.pri = pri;
      this.productcycle = productcycle;
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
  public boolean getStatus() {
      return status;
  }
}