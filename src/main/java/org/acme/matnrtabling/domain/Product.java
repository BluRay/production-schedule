package org.acme.matnrtabling.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import java.time.LocalDate;

@PlanningEntity
public class Product {
    @PlanningId
    private Long id;

    private String pname;
    private ProductMatnr matnr;     // 物料
    private String vehicle_type;    // 车辆类型 K9/K8
    private int product_month;      // 生产月份 1/2
    private int product_day;        // 生产截止日期 1/2
    private LocalDate deliver_date; // 交期
    private boolean trial_flag;     // 是否试制
    private LocalDate trial_date;   // 试制日期
    private String requiredResType; // 生产资源类型
    
    @PlanningVariable
    private WorkCalendar workCalendar;
    @PlanningVariable
    private ProductResource resource;

    // No-arg constructor required for OptaPlanner
    public Product() {
    }
    public Product(long id, String pname, ProductMatnr matnr, String vehicle_type, int product_month, int product_day, LocalDate deliver_date, boolean trial_flag, LocalDate trial_date, String requiredResType) {
        this.id = id;
        this.pname = pname;
        this.matnr = matnr;
        this.vehicle_type = vehicle_type;
        this.product_month = product_month;
        this.product_day = product_day;
        this.deliver_date = deliver_date;
        this.trial_flag = trial_flag;
        this.trial_date = trial_date;
        this.requiredResType = requiredResType;
    }
    public Product(long id, String pname, ProductMatnr matnr, String vehicle_type, int product_month, int product_day, LocalDate deliver_date, boolean trial_flag, LocalDate trial_date, String requiredResType, WorkCalendar workCalendar, ProductResource resource) {
        this(id, pname, matnr, vehicle_type, product_month, product_day, deliver_date, trial_flag, trial_date, requiredResType);
        this.workCalendar = workCalendar;
        this.resource = resource;
    }
    
    @Override
    public String toString() {
        return pname + "(" + matnr + ")";
    }
    
    public Long getId() {
        return id;
    }
    public String getPname() {
        return pname;
    }
    public ProductMatnr getMatnr() {
        return matnr;
    }
    public String getVehicleType() {
        return vehicle_type;
    }
    public int getProductMonth() {
        return product_month;
    }
    public int getProductDay() {
        return product_day;
    }
    public LocalDate getDeliverDate() {
      return deliver_date;
    }
    public boolean getTrialFlag() {
      return trial_flag;
    }
    public LocalDate getTrialDate() {
      return trial_date;
    }
    public String getRequiredResType() {
        return requiredResType;
    }
    public WorkCalendar getWorkCalendar() {
        return workCalendar;
    }
    public void setWorkCalendar(WorkCalendar workCalendar) {
        this.workCalendar = workCalendar;
    }
    
    public ProductResource getResource() {
        return resource;
    }
    public void setResource(ProductResource resource) {
        this.resource = resource;
    }
}