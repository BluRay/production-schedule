package org.acme.productionScheduling.domain;

import java.time.LocalDate;

public class TrialProduction {
  
  private LocalDate trialDate;//试制日期

  private int trialNum;//试制数量

  private String pMatnr;

  public TrialProduction() {
  }

  public TrialProduction(LocalDate trialDate, int trialNum, String pMatnr) {
    this.trialDate = trialDate;
    this.trialNum = trialNum;
    this.pMatnr = pMatnr;
  }

  public LocalDate getTrialDate() {
    return trialDate;
  }

  public void setTrialDate(LocalDate trialDate) {
    this.trialDate = trialDate;
  }

  public int getTrialNum() {
    return trialNum;
  }

  public void setTrialNum(int trialNum) {
    this.trialNum = trialNum;
  }

  public String getpMatnr() {
    return pMatnr;
  }

  public void setpMatnr(String pMatnr) {
    this.pMatnr = pMatnr;
  }
  
}
