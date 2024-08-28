package org.acme.productionScheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.acme.productionScheduling.domain.*;
import org.acme.productionScheduling.solver.ProductionSchedulingConstraintProvider;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ProductionSchedulingApp {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProductionSchedulingApp.class);
  
  public void calculate() {
    LOGGER.info("----- ProductionSchedulingApp Start -----");
    SolverFactory<PlannedProduction> solverFactory = SolverFactory.create(new SolverConfig()
      .withSolutionClass(PlannedProduction.class)
      .withEntityClasses(MatnrProduction.class)
      .withConstraintProviderClass(ProductionSchedulingConstraintProvider.class)
      // The solver runs only for 5 seconds on this small dataset.
      // It's recommended to run for at least 5 minutes ("5m") otherwise.
      .withTerminationSpentLimit(Duration.ofSeconds(5)));

    PlannedProduction problem = buildData();
    // Solve the problem
    Solver<PlannedProduction> solver = solverFactory.buildSolver();
    PlannedProduction solution = solver.solve(problem);

    // Visualize the solution
    // printPlantable(solution);
  }
  
  private static PlannedProduction buildData() {
    PlannedProduction problem = new PlannedProduction();

    List<LocalDate> productionTime = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date1 = LocalDate.parse("2024-08-05", formatter);
    LocalDate date2 = LocalDate.parse("2024-08-06", formatter);
    LocalDate date3 = LocalDate.parse("2024-08-07", formatter);
    LocalDate date4 = LocalDate.parse("2024-08-08", formatter);
    LocalDate date5 = LocalDate.parse("2024-08-09", formatter);
    LocalDate date6 = LocalDate.parse("2024-08-12", formatter);
    LocalDate date7 = LocalDate.parse("2024-08-13", formatter);
    LocalDate date8 = LocalDate.parse("2024-08-14", formatter);
    LocalDate date9 = LocalDate.parse("2024-08-15", formatter);
    LocalDate date10 = LocalDate.parse("2024-08-16", formatter);
    productionTime.add(date1);
    productionTime.add(date2);
    productionTime.add(date3);
    productionTime.add(date4);
    productionTime.add(date5);
    productionTime.add(date6);
    productionTime.add(date7);
    productionTime.add(date8);
    productionTime.add(date9);
    productionTime.add(date10);
    // problem.setProductionTime(productionTime);

    List<MatnrProduction> mList = new ArrayList<>();//规划实体
    List<ReceivedOrderInfo> receivedOrderInfos = new ArrayList<>();//来单信息
    List<DeliverInfo> deliverInfos = new ArrayList<>();//交付信息
    
    long c = 0;
    for (int a = 0; a < 3; a++) {
      for (int b = 0; b < productionTime.size(); b++) {
        MatnrProduction m = new MatnrProduction();
        m.setId(++c);
        m.setProductionTime(productionTime.get(b));
        m.setpMatnr("100011-0" + a);
        m.setBusSeries("车系" + a);
        m.setBusType("整车类型" + a);
        m.setCategory("类别" + a);
        m.setInventory(50);
        m.setPriority(a);
        m.setWerks("HZ00");
        m.setWorkshop("HZ");
        m.setLine("A");
        m.setCapacity(30 + a * 10);
        m.setTotalDemandQuantity(110);
        m.setTrialNum(0);

        if (a == 1) {
          m.setTrialNum(5);
        }
        mList.add(m);
      }

      DeliverInfo deliverInfo = new DeliverInfo();
      deliverInfo.setDeliverDate(date5);
      deliverInfo.setDemandQuantity(50);
      deliverInfo.setpMatnr("100011-0" + a);
      DeliverInfo deliverInfo1 = new DeliverInfo();
      deliverInfo1.setDeliverDate(date10);
      deliverInfo1.setDemandQuantity(110);
      deliverInfo1.setpMatnr("100011-0" + a);
      deliverInfos.add(deliverInfo);
      deliverInfos.add(deliverInfo1);
      
      ReceivedOrderInfo receivedOrderInfo1 = new ReceivedOrderInfo();
      receivedOrderInfo1.setOrderDate(date2);
      receivedOrderInfo1.setOrderNum(30);
      receivedOrderInfo1.setpMatnr("100011-0" + a);
      ReceivedOrderInfo receivedOrderInfo2 = new ReceivedOrderInfo();
      receivedOrderInfo2.setOrderDate(date4);
      receivedOrderInfo2.setOrderNum(30);
      receivedOrderInfo2.setpMatnr("100011-0" + a);
      receivedOrderInfos.add(receivedOrderInfo1);
      receivedOrderInfos.add(receivedOrderInfo2);

    }

    List<TrialProduction> trialProductionInfos = new ArrayList<>();//试制信息
    TrialProduction trial = new TrialProduction();
    trial.setTrialDate(date1);
    trial.setTrialNum(5);
    trial.setpMatnr("100011-01");
    trialProductionInfos.add(trial);

    problem.setTrialProductionInfos(trialProductionInfos);
    problem.setReceivedOrderInfos(receivedOrderInfos);
    problem.setDeliverInfos(deliverInfos);
    problem.setmList(mList);

    return problem;
  }
}