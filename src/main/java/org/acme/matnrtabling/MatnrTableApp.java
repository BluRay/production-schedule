package org.acme.matnrtabling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.acme.matnrtabling.domain.PlanTable;
import org.acme.matnrtabling.domain.Product;
import org.acme.matnrtabling.domain.ProductMatnr;
import org.acme.matnrtabling.domain.ResDateOverLoadProductivity;
import org.acme.matnrtabling.domain.Resource;
import org.acme.matnrtabling.domain.WorkCalendar;
import org.acme.matnrtabling.domain.DeliverInfo;
import org.acme.matnrtabling.solver.PlanTableConstraintProvider;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MatnrTableApp {
  private static final Logger LOGGER = LoggerFactory.getLogger(MatnrTableApp.class);
  
  public void calculate() {
    LOGGER.info("----- Production Schedule Start -----");
    SolverFactory<PlanTable> solverFactory = SolverFactory.create(new SolverConfig()
      .withSolutionClass(PlanTable.class)
      .withEntityClasses(Product.class)
      .withConstraintProviderClass(PlanTableConstraintProvider.class)
      // The solver runs only for 5 seconds on this small dataset.
      // It's recommended to run for at least 5 minutes ("5m") otherwise.
      .withTerminationSpentLimit(Duration.ofSeconds(5)));

    PlanTable problem = generateDemoData();
    // Solve the problem
    Solver<PlanTable> solver = solverFactory.buildSolver();
    LOGGER.info("----- Solver Start -----");
    PlanTable solution = solver.solve(problem);

    // Visualize the solution
    printPlantable(solution);
  }

  private static void printPlantable(PlanTable planTable) {
    LOGGER.info("*******Plan Calendar**********");
    List<WorkCalendar> dateList = planTable.getWorkCalendar();
    List<Product> productList = planTable.getProduct();

    Map<Resource, Map<WorkCalendar, List<Product>>> productMap = productList.stream()
      .filter(product -> product.getResource() != null && product.getWorkCalendar() != null)
      .collect(Collectors.groupingBy(Product::getResource, Collectors.groupingBy(Product::getWorkCalendar)));
    System.out.println("|-------------|" + "-------|".repeat(dateList.size()));
    System.out.println("|资源  \\  日期| " + dateList.stream().map(date -> String.format("%-5s", date.getMonthValue() + "/" + date.getDayOfMonth())).collect(Collectors.joining(" | ")) + " |");
    System.out.println("|-------------|" + "-------|".repeat(dateList.size()));
    for (Resource res : planTable.getResource()) {
      // LOGGER.info("-->" + res.toString());
      List<List<Product>> cellList = dateList.stream().map(date -> {
        Map<WorkCalendar, List<Product>> byDateMap = productMap.get(res);
        if (byDateMap == null) {
          return Collections.<Product>emptyList();
        }
        List<Product> cellProductList = byDateMap.get(date);
        if (cellProductList == null) {
          return Collections.<Product>emptyList();
        }
        return cellProductList;
      })
      .collect(Collectors.toList());

      System.out.println("| " + String.format("%-9s", res.getName() + "[" + res.getType() + "]") + " | "
      + cellList.stream().map(cellProductList -> String.format("%-5s", (cellProductList.size() == 0) ? "" : cellProductList.size()))
        // cellProductList.stream().map(Product::getPname).collect(Collectors.joining(", "))))
        .collect(Collectors.joining(" | "))
      + " |");
      System.out.println("|-------------|" + "-------|".repeat(dateList.size()));
    }
    System.out.println("-->Product 2024-09-15 production Detail:");
    int c1 = 1;
    int c2 = 1;
    for (Product pro : planTable.getProduct()) {
      if (pro.getWorkCalendar().getWorkDate().isEqual(LocalDate.of(2024, 9, 15)) && pro.getResource().getType().equals("重卡")) {
        System.out.println("[重卡]" + (c1++) + ": " + pro.getPname() + "|matnr:" + pro.getMatnr().getMatnr() + "|pri:" + pro.getMatnr().getPri());
      }
      if (pro.getWorkCalendar().getWorkDate().isEqual(LocalDate.of(2024, 9, 15)) && pro.getResource().getType().equals("轻卡")) {
        System.out.println("[轻卡]" + (c2++) + ": " + pro.getPname() + "|matnr:" + pro.getMatnr().getMatnr() + "|pri:" + pro.getMatnr().getPri());
      }
    }
  }

  public static PlanTable generateDemoData() {
    List<WorkCalendar> workCalendarList = new ArrayList<>(15);
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 15), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 16), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 17), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 18), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 19), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 20), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 21), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 22), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 23), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 24), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 25), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 26), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 27), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 28), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 29), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 30), true));
    
    // 生产料号
    ProductMatnr matnr1_1 = new ProductMatnr("sap-001-1", "sap-001-1-abcd", 1, 0);
    ProductMatnr matnr1_2 = new ProductMatnr("sap-001-2", "sap-001-2-abcd", 2, 0);
    ProductMatnr matnr2_1 = new ProductMatnr("sap-002-1", "sap-002-1-abcd", 1, 0);

    // 生产资源 [产线]
    List<Resource> resourceList = new ArrayList<>(3);
    resourceList.add(new Resource("ZZ", "Q11", "q11", "重卡", matnr1_1, 5, 1, 1, 0, null, false));
    resourceList.add(new Resource("ZZ", "Q12", "q12", "重卡", matnr1_2, 10, 2, 1, 2, null, false));
    resourceList.add(new Resource("ZZ", "Q21", "q22", "轻卡", matnr2_1, 5, 1, 1, 0, null, false));
    
    // 获取交期列表
    List<DeliverInfo> deliverList = new ArrayList<DeliverInfo>();
    DeliverInfo deliver1 = new DeliverInfo(LocalDate.of(2024, 9, 16), 9, 0, null, getWorkDayCount(workCalendarList, LocalDate.of(2024, 9, 16)), matnr1_1);
    DeliverInfo deliver2 = new DeliverInfo(LocalDate.of(2024, 9, 17), 15, 0, null, getWorkDayCount(workCalendarList, LocalDate.of(2024, 9, 17)), matnr1_1);
    DeliverInfo deliver3 = new DeliverInfo(LocalDate.of(2024, 9, 30), 12, 0, null, getWorkDayCount(workCalendarList, LocalDate.of(2024, 9, 30)), matnr1_1);
    DeliverInfo deliver4 = new DeliverInfo(LocalDate.of(2024, 9, 30), 9, 3, LocalDate.of(2024, 9, 25), getWorkDayCount(workCalendarList, LocalDate.of(2024, 9, 30)), matnr2_1);
    DeliverInfo deliver5 = new DeliverInfo(LocalDate.of(2024, 9, 30), 56, 0, null, getWorkDayCount(workCalendarList, LocalDate.of(2024, 9, 30)), matnr1_2);
    deliverList.add(deliver1);
    deliverList.add(deliver2);
    deliverList.add(deliver3);
    deliverList.add(deliver4);
    deliverList.add(deliver5);

    // 根据交期 生成产品列表
    List<Product> productList = generateProductData(deliverList, resourceList, 9);

    // 计算产线产能是否满足生产需求 不满足则设置 超产能值overload;
    for (Resource res : resourceList) {
      String matnr = res.getMatnr().getMatnr();
      int productivity = res.getProductivity();  // 产线正常产能
      int totalProductivity = 0;  // 当前产线 计划周期内最大产量
      int product_qty = 0;        // 当前产线 计划排产数

      int pre_qty = res.getInventoryQty();       // 交期数量和 默认为初始库存
      List<ResDateOverLoadProductivity> productivityList = new ArrayList<ResDateOverLoadProductivity>();
      for (DeliverInfo deliver : deliverList) {
        if (matnr.equals(deliver.getProductMatnr().getMatnr())) {
          System.out.println("-->getDeliverDate:" + matnr + "|" + deliver.getDeliverDate());
          res.setProductivityList(null);
          LocalDate day1 = LocalDate.of(2024, 9, 15);       // 排产开始日期
          int day_count = deliver.getProductDayCount();     // 可排产天数 TODO 需减去(生产周期-1)
          int qty = pre_qty + deliver.getDemandQuantity();  // 需求数量
          totalProductivity = productivity * day_count;     // 正常产能最大总产量
          if (totalProductivity < qty) {                    // 是否超产能
            int exps = qty - totalProductivity;
            int plus = 0;       // 产能超载值
            if (exps % day_count == 0) {
              plus = exps / day_count;
            } else {
              plus = exps / day_count + 1;
            }
            while (exps > 0) {
              System.out.println("---->exps:" + exps + "|" + plus);
              int add = (exps > plus)? plus : exps;   // 当前工作日产能超载值
              System.out.println("---->" + day1.getDayOfMonth() + "|add:" + add + "|plus:" + plus);
              ResDateOverLoadProductivity p1 = new ResDateOverLoadProductivity(day1, add);
              productivityList.add(p1);
              day1 = day1.plusDays(1);
              exps -= add;
            } 
          }
          pre_qty += qty;
        }
      }
      res.setProductivityList(productivityList);
    }

    return new PlanTable(workCalendarList, resourceList, productList);
  }

  public static List<Product> generateProductData(List<DeliverInfo> deliverList, List<Resource> resourceList, int plan_month) {
    List<Product> productList = new ArrayList<>();
    int id = 1;
    //new Product(id++, "product-26", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡");
    for (DeliverInfo deliver : deliverList) {
      int demandQuantity = deliver.getDemandQuantity();
      int trialQuantity = deliver.getTrialQuantity();
      
      for(int i = 0; i < trialQuantity; i++){
        productList.add(new Product(id++, "product-" + id, deliver.getProductMatnr(), "K8", plan_month, 30, deliver.getDeliverDate(), true, deliver.getTrialDate(), "重卡"));
      }
      for(int i = 0; i < demandQuantity - trialQuantity; i++){
        // 如果存在初始库存 则相应减少生成的产品数量
        Boolean InventoryFlag = true;
        for (Resource res : resourceList) {
          String matnr = res.getMatnr().getMatnr();
          if (matnr.equals(deliver.getProductMatnr().getMatnr())) {
            if (res.getInventoryQty() > 0) {
              InventoryFlag = false;
              res.setInventoryQty(res.getInventoryQty() - 1);
            }
          }
        }
        if (InventoryFlag) {
          productList.add(new Product(id++, "product-" + id, deliver.getProductMatnr(), "K8", plan_month, 30, deliver.getDeliverDate(), false, null, "重卡"));
        }
      }
    }
    return productList;
  }

  // 计算可排产天数 扣除非工作日
  public static int getWorkDayCount(List<WorkCalendar> workCalendarList, LocalDate workDate){
    int dayCount = 0;
    for (WorkCalendar cal : workCalendarList) {
      if (cal.getWorkDate().compareTo(workDate) <= 0 && cal.getWorkDay()) {
        dayCount++;
      }
    }
    return dayCount;
  }
}