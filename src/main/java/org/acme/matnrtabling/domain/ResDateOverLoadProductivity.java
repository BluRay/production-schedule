package org.acme.matnrtabling.domain;

import java.time.LocalDate;

public class ResDateOverLoadProductivity {
  private LocalDate workDate;
  private int overLoadProductivity;
  public ResDateOverLoadProductivity(LocalDate workDate, int overLoadProductivity) {
    this.workDate = workDate;
    this.overLoadProductivity = overLoadProductivity;
  }
  public LocalDate getWorkDate() {
      return workDate;
  }
  public int getOverLoadProductivity() {
      return overLoadProductivity;
  }
  public void setOverLoadProductivity(int overLoadProductivity) {
      this.overLoadProductivity = overLoadProductivity;
  }
}