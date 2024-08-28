package org.acme.productionScheduling.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class PlannedProduction {

  @ProblemFactCollectionProperty
  private List<DeliverInfo> deliverInfos;//交付信息集合

  @ProblemFactCollectionProperty
  private List<ReceivedOrderInfo> receivedOrderInfos;//来单信息集合

  @ProblemFactCollectionProperty
  private List<TrialProduction> trialProductionInfos;//试制信息集合

  @PlanningEntityCollectionProperty
  private List<MatnrProduction> mList;

  //表示输出的分数
  @PlanningScore
  private HardSoftScore score;

  public PlannedProduction() {
  }

  public PlannedProduction(List<DeliverInfo> deliverInfos, List<ReceivedOrderInfo> receivedOrderInfos,
      List<TrialProduction> trialProductionInfos, List<MatnrProduction> mList) {
    this.deliverInfos = deliverInfos;
    this.receivedOrderInfos = receivedOrderInfos;
    this.trialProductionInfos = trialProductionInfos;
    this.mList = mList;
  }

  public List<DeliverInfo> getDeliverInfos() {
    return deliverInfos;
  }

  public void setDeliverInfos(List<DeliverInfo> deliverInfos) {
    this.deliverInfos = deliverInfos;
  }

  public List<ReceivedOrderInfo> getReceivedOrderInfos() {
    return receivedOrderInfos;
  }

  public void setReceivedOrderInfos(List<ReceivedOrderInfo> receivedOrderInfos) {
    this.receivedOrderInfos = receivedOrderInfos;
  }

  public List<TrialProduction> getTrialProductionInfos() {
    return trialProductionInfos;
  }

  public void setTrialProductionInfos(List<TrialProduction> trialProductionInfos) {
    this.trialProductionInfos = trialProductionInfos;
  }

  public List<MatnrProduction> getmList() {
    return mList;
  }

  public void setmList(List<MatnrProduction> mList) {
    this.mList = mList;
  }

  public HardSoftScore getScore() {
    return score;
  }

  public void setScore(HardSoftScore score) {
    this.score = score;
  }
  
}
