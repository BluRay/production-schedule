package org.acme.matnrtabling.domain;

// 产线实体类
public class ProductLine {
  private String werks;
  private String workshop;
  private String line;
  private String lineName;

  public ProductLine(String werks, String workshop, String line, String lineName) {
    this.werks = werks;
    this.workshop = workshop;
    this.line = line;
    this.lineName = lineName;
  }

  public String getWerks() {
    return werks;
  }
  public String getWorkshop() {
    return workshop;
  }
  public String getLine() {
    return line;
  }
  public String getLineName() {
    return lineName;
  }
}
