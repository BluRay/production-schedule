package org.acme.productionScheduling.domain;

import java.time.LocalDate;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * 排产数量
 */
@PlanningEntity
public class ProductionNum {

  @PlanningVariable(valueRangeProviderRefs = "productionQuantityRange")
  private int productionQuantity;//排产数量

  @PlanningVariable(valueRangeProviderRefs = "productionTimeRange")
  private LocalDate productionTime;//排产时间

  public ProductionNum() {
  }

  public ProductionNum(int productionQuantity, LocalDate productionTime) {
    this.productionQuantity = productionQuantity;
    this.productionTime = productionTime;
  }

  public int getProductionQuantity() {
    return productionQuantity;
  }

  public void setProductionQuantity(int productionQuantity) {
    this.productionQuantity = productionQuantity;
  }

  public LocalDate getProductionTime() {
    return productionTime;
  }

  public void setProductionTime(LocalDate productionTime) {
    this.productionTime = productionTime;
  }

  @ValueRangeProvider(id = "productionQuantityRange")
  public CountableValueRange<Integer> getDelayRange() {
      return ValueRangeFactory.createIntValueRange(0, 100);
  }
  
}
