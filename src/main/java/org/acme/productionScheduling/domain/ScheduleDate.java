package org.acme.productionScheduling.domain;

import java.time.LocalDate;

public class ScheduleDate {
  
  private LocalDate productionScheduleDate;//可排产日期

  public ScheduleDate() {
  }

  public ScheduleDate(LocalDate productionScheduleDate) {
    this.productionScheduleDate = productionScheduleDate;
  }

  public LocalDate getProductionScheduleDate() {
    return productionScheduleDate;
  }

  public void setProductionScheduleDate(LocalDate productionScheduleDate) {
    this.productionScheduleDate = productionScheduleDate;
  }

}
