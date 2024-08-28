package org.acme.productionScheduling.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class MatnrProduction {

  @PlanningId
  private Long id;

  private String pMatnr;//生产料号

  private String category;//类别

  private String busSeries;//车系

  private String busType;//整车类型

  private int inventory;//库存

  private int priority;//优先级

  private String werks;

  private String workshop;

  private String line;

  private int capacity;

  private int totalDemandQuantity;//总需求数

  private int trialNum;//试制数量

  private LocalDate productionTime;//排产日期

  @PlanningVariable(valueRangeProviderRefs = {"productionQuantityRange"})
  private Integer productionQuantity;//排产数量

  private List<Integer> productionQuantityList;//排产数量集合

  private BigDecimal proportion;//排产量占一天产能的比例

  /**复合方法 */

  //排产数量集合
  @ValueRangeProvider(id = "productionQuantityRange")
  public List<Integer> getProductionQuantityList() {
    List<Integer> list = new ArrayList<>();
    list.add(0);
    list.add(capacity);
    
    if (trialNum > 0) {
      list.add(trialNum);
    }
    
    list.add(totalDemandQuantity % capacity);
    return list;
  }

  //计算排产量占一天产能的比例
  public BigDecimal getProportion() {
    BigDecimal a = new BigDecimal(productionQuantity);
    BigDecimal b = new BigDecimal(capacity);

    return a.divide(b, 2, RoundingMode.HALF_UP);
  }

  public MatnrProduction() {
  }

  public MatnrProduction(Long id, String pMatnr, String category, String busSeries, String busType, int inventory,
      int priority, String werks, String workshop, String line, int capacity, int totalDemandQuantity, int trialNum,
      LocalDate productionTime, Integer productionQuantity, List<Integer> productionQuantityList,
      BigDecimal proportion) {
    this.id = id;
    this.pMatnr = pMatnr;
    this.category = category;
    this.busSeries = busSeries;
    this.busType = busType;
    this.inventory = inventory;
    this.priority = priority;
    this.werks = werks;
    this.workshop = workshop;
    this.line = line;
    this.capacity = capacity;
    this.totalDemandQuantity = totalDemandQuantity;
    this.trialNum = trialNum;
    this.productionTime = productionTime;
    this.productionQuantity = productionQuantity;
    this.productionQuantityList = productionQuantityList;
    this.proportion = proportion;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getpMatnr() {
    return pMatnr;
  }

  public void setpMatnr(String pMatnr) {
    this.pMatnr = pMatnr;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getBusSeries() {
    return busSeries;
  }

  public void setBusSeries(String busSeries) {
    this.busSeries = busSeries;
  }

  public String getBusType() {
    return busType;
  }

  public void setBusType(String busType) {
    this.busType = busType;
  }

  public int getInventory() {
    return inventory;
  }

  public void setInventory(int inventory) {
    this.inventory = inventory;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
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

  public int getTotalDemandQuantity() {
    return totalDemandQuantity;
  }

  public void setTotalDemandQuantity(int totalDemandQuantity) {
    this.totalDemandQuantity = totalDemandQuantity;
  }

  public int getTrialNum() {
    return trialNum;
  }

  public void setTrialNum(int trialNum) {
    this.trialNum = trialNum;
  }

  public LocalDate getProductionTime() {
    return productionTime;
  }

  public void setProductionTime(LocalDate productionTime) {
    this.productionTime = productionTime;
  }

  public Integer getProductionQuantity() {
    return productionQuantity;
  }

  public void setProductionQuantity(Integer productionQuantity) {
    this.productionQuantity = productionQuantity;
  }

  public void setProductionQuantityList(List<Integer> productionQuantityList) {
    this.productionQuantityList = productionQuantityList;
  }

  public void setProportion(BigDecimal proportion) {
    this.proportion = proportion;
  }

}
