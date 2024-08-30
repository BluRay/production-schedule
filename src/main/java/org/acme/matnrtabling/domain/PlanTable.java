package org.acme.matnrtabling.domain;

import java.util.List;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

@PlanningSolution
public class PlanTable {
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<WorkCalendar> workCalendarList;
  
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<ProductResource> resourceList;
    
    @PlanningEntityCollectionProperty
    private List<Product> productList;

    @PlanningScore
    private HardSoftLongScore score;

    public PlanTable() {
    }

    public PlanTable(List<WorkCalendar> workCalendarList, List<ProductResource> resourceList, List<Product> productList) {
        this.workCalendarList = workCalendarList;
        this.resourceList = resourceList;
        this.productList = productList;
    }

    public HardSoftLongScore getScore() {
        return score;
    }
    public List<WorkCalendar> getWorkCalendar() {
        return workCalendarList;
    }
    public List<ProductResource> getResource() {
        return resourceList;
    }
    public List<Product> getProduct() {
        return productList;
    }
}
