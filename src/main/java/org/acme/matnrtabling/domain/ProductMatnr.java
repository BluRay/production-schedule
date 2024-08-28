package org.acme.matnrtabling.domain;

// 产品料号实体类
public class ProductMatnr {
  
    private String matnr;
    private String maktx;
    private int pri;          // 优先级
    private int status;
    
    public ProductMatnr(String matnr, String maktx, int pri, int status) {
      this.matnr = matnr;
      this.maktx = maktx;
      this.pri = pri;
      this.status = status;
    }
    public String getMatnr() {
        return matnr;
    }
    public String getMaktx() {
        return maktx;
    }
    public int getPri() {
        return pri;
    }
    public int getStatus() {
        return status;
    }
}