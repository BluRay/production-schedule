package org.acme.matnrtabling.solver;

import java.time.Duration;
import org.acme.matnrtabling.domain.Product;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.countDistinctLong;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sumLong;

public class PlanTableConstraintProvider implements ConstraintProvider {
    private long a = 1L;
  
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
            // *******Hard constraints***************
            resTypeMatch(constraintFactory),        // 硬约束 [产线约束] 产品只能在指定类型的资源上生产
            proMonthMatch(constraintFactory),       // 硬约束 [DPT约束] 产品排产月份
            proDeliverMatch(constraintFactory),     // 硬约束 [交期约束] 排产日期不能大于交期
            proTrialMatch(constraintFactory),       // 硬约束 [试制约束] 试制车 必需在指定试制日期生产
            workDayMatch(constraintFactory),        // 硬约束 非工作日不能生产

            // *******Soft constraints***************
            productivityMatch(constraintFactory),   // 软约束 [产能约束] 每天产量不能大于最大产能
            productivityMatch2(constraintFactory)
            // priMatch(constraintFactory)           // 软约束 优先级高的先安排生产
            // TODO 按物料优先级安排生产 
        };
    }

    // 硬约束 产品只能在指定类型的资源上生产
    Constraint resTypeMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getResource() != null 
                && !product.getMatnr().getMatnr().equals(product.getResource().getMatnr().getMatnr()))
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("resTypeMatch");
    }

    // 硬约束 产品排产月份约束
    Constraint proMonthMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getWorkCalendar() != null 
                && product.getProductMonth() != product.getWorkCalendar().getMonthValue())
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("proMonthMatch");
    }
    Constraint proDayMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getWorkCalendar() != null 
                && product.getProductDay() < product.getWorkCalendar().getDayOfMonth())
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("proDayMatch");
    }
    // 硬约束 交期
    Constraint proDeliverMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getWorkCalendar() != null 
                && product.getDeliverDate().isBefore(product.getWorkCalendar().getWorkDate()))
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("proDeliverMatch");
    }
    // 硬约束 试制车 必需在指定试制日期生产
    Constraint proTrialMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getTrialFlag() == true 
                && !product.getTrialDate().isEqual(product.getWorkCalendar().getWorkDate()))
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("proTrialMatch");
    }

    // 软约束 每天产量不能大于最大产能
    Constraint productivityMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getResource, Product::getWorkCalendar, countDistinctLong(Product::getId))
            .filter((res, date, proCount) -> proCount > res.getProductivity())
            .penalizeLong(HardSoftLongScore.ofSoft(100), (res, date, proCount) -> a)
            .asConstraint("productivityMatch");
    }
    Constraint productivityMatch2(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getResource, Product::getWorkCalendar, countDistinctLong(Product::getId))
            .filter((res, date, proCount) -> proCount <= res.getProductivity() + res.getOverloadProductcycle(date.getWorkDate()) && proCount > res.getProductivity())
            .rewardLong(HardSoftLongScore.ofSoft(100), (res, date, proCount) -> a)
            .asConstraint("productivityMatch2");
    }

    // 硬约束 非工作日不能生产
    Constraint workDayMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getWorkCalendar() != null && !product.getWorkCalendar().getWorkDay())
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("workDayMatch");
    }

    // 软约束 按日期先后安排生产
    // bak1软约束 每天每台资源尽量都安排生产
    Constraint balancedMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, Product::getResource, (product) -> product.getResource().getProductivity(), countDistinctLong(Product::getId))
            //.filter((date, res, productivity, proCount) -> Integer.valueOf(proCount + "") != productivity)
            .filter((date, res, productivity, proCount) -> Integer.valueOf(proCount + "") > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(100), (date, res, productivity, proCount) -> Math.abs(productivity - Integer.valueOf(proCount + "")) * a)
            .asConstraint("balancedMatch");
    }
    Constraint balancedMatch_bak1(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, (product) -> product.getResource().getProductLine().getLineName(), countDistinctLong(Product::getId))
            .filter((date, line, proCount) -> proCount > 1)
            .penalizeLong(HardSoftLongScore.ofSoft(100), (date, line, proCount) -> proCount*a)
            .asConstraint("balancedMatch_bak1");
    }

    // 软约束 优先级高的先安排生产
    Constraint priMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, Product::getResource, (product) -> product.getResource().getPri(), countDistinctLong(Product::getId))
            .filter((date, res, pri, proCount) -> proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(50), (date, res, pri, proCount) -> pri*a)
            .asConstraint("priMatch");
    }
    Constraint priMatch1(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, (product) -> product.getResource().getPri(), countDistinctLong(Product::getId))
            .filter((date, pri, proCount) -> pri == 1 && proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(500), (date, pri, proCount) -> a)
            .asConstraint("priMatch1");
    }
    Constraint priMatch2(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, (product) -> product.getResource().getPri(), countDistinctLong(Product::getId))
            .filter((date, pri, proCount) -> pri == 2 && proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(250), (date, pri, proCount) -> a)
            .asConstraint("priMatch2");
    }
    Constraint priMatch3(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, (product) -> product.getResource().getPri(), countDistinctLong(Product::getId))
            .filter((date, pri, proCount) -> pri == 3 && proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(100), (date, pri, proCount) -> a)
            .asConstraint("priMatch3");
    }
    
    Constraint proMatnrInventory(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(product -> product.getWorkCalendar(),
                    product -> product.getResource(),
                    countDistinctLong(Product::getId))
            .filter((date, res, proCount) -> proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(100), (date, res, proCount) -> a)
            .asConstraint("proMatnrInventory");
    }
}