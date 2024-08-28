package org.acme.matnrtabling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.acme.matnrtabling.domain.PlanTable;
import org.acme.matnrtabling.domain.Product;
import org.acme.matnrtabling.domain.ProductMatnr;
import org.acme.matnrtabling.domain.Resource;
import org.acme.matnrtabling.domain.WorkCalendar;
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
    System.out.println("-->Product 2024-09-25 production Detail:");
    int c1 = 1;
    int c2 = 1;
    for (Product pro : planTable.getProduct()) {
      if (pro.getWorkCalendar().getWorkDate().isEqual(LocalDate.of(2024, 9, 25)) && pro.getResource().getType().equals("重卡")) {
        System.out.println("[重卡]" + (c1++) + ": " + pro.getPname() + "|matnr:" + pro.getMatnr().getMatnr() + "|pri:" + pro.getMatnr().getPri());
      }
      if (pro.getWorkCalendar().getWorkDate().isEqual(LocalDate.of(2024, 9, 25)) && pro.getResource().getType().equals("轻卡")) {
        System.out.println("[轻卡]" + (c2++) + ": " + pro.getPname() + "|matnr:" + pro.getMatnr().getMatnr() + "|pri:" + pro.getMatnr().getPri());
      }
    }
  }

  public static PlanTable generateDemoData() {
    List<WorkCalendar> workCalendarList = new ArrayList<>(15);
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 25), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 26), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 27), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 28), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 29), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 9, 30), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 1), false));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 2), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 3), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 4), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 5), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 6), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 7), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 8), true));
    workCalendarList.add(new WorkCalendar(LocalDate.of(2024, 10, 9), true));
    
    // 生产料号
    ProductMatnr matnr1_1 = new ProductMatnr("sap-001-1", "sap-001-1-abcd", 1, 0);
    ProductMatnr matnr1_2 = new ProductMatnr("sap-001-2", "sap-001-2-abcd", 2, 0);
    ProductMatnr matnr2_1 = new ProductMatnr("sap-002-1", "sap-002-1-abcd", 1, 0);

    // 生产资源 [产线]
    List<Resource> resourceList = new ArrayList<>(2);
    resourceList.add(new Resource("ZZ", "Q11", "q11", "重卡", matnr1_1, 8, 1, 1, true));
    //resourceList.add(new Resource("ZZ", "Q12", "q12", "重卡", 2, 2, 1, true));
    //resourceList.add(new Resource("ZZ", "Q12", "q12", "重卡", 1, 3, 1, true));
    resourceList.add(new Resource("ZZ", "Q21", "q22", "轻卡", matnr2_1, 5, 1, 1, true));
    
    // 生产产品
    List<Product> productList = new ArrayList<>();  // 按交期 生成待排产产品列表
    long id = 0;
    productList.add(new Product(id++, "product-11", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-12", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-13", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-14", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-15", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-16", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-17", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-18", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-19", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 26), false, null, "重卡"));
    productList.add(new Product(id++, "product-t1", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), true, LocalDate.of(2024, 9, 25), "重卡"));
    productList.add(new Product(id++, "product-t2", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), true, LocalDate.of(2024, 9, 25), "重卡"));
    productList.add(new Product(id++, "product-110", matnr1_2, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-111", matnr1_2, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-112", matnr1_2, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-113", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-114", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-115", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-116", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-117", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-118", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-119", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-120", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-121", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-122", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-123", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-124", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-125", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-126", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-127", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-128", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-129", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-130", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-131", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-132", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-133", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-134", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    productList.add(new Product(id++, "product-135", matnr1_1, "K9", 9, 30, LocalDate.of(2024, 9, 30), false, null, "重卡"));
    
    productList.add(new Product(id++, "product-t3", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), true, LocalDate.of(2024, 9, 29), "轻卡"));
    productList.add(new Product(id++, "product-t4", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), true, LocalDate.of(2024, 9, 29), "轻卡"));
    productList.add(new Product(id++, "product-t5", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), true, LocalDate.of(2024, 9, 29), "轻卡"));
    productList.add(new Product(id++, "product-21", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡"));
    productList.add(new Product(id++, "product-22", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡"));
    productList.add(new Product(id++, "product-23", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡"));
    productList.add(new Product(id++, "product-24", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡"));
    productList.add(new Product(id++, "product-25", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡"));
    productList.add(new Product(id++, "product-26", matnr2_1, "K8", 9, 30, LocalDate.of(2024, 9, 30), false, null, "轻卡"));
    productList.add(new Product(id++, "product-27", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));
    productList.add(new Product(id++, "product-28", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));
    productList.add(new Product(id++, "product-29", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));
    productList.add(new Product(id++, "product-210", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));
    productList.add(new Product(id++, "product-211", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));
    productList.add(new Product(id++, "product-212", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));
    productList.add(new Product(id++, "product-213", matnr2_1, "K8", 10, 9, LocalDate.of(2024, 10, 9), false, null, "轻卡"));

    return new PlanTable(workCalendarList, resourceList, productList);
  }
}