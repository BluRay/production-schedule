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
            proMonthMatch(constraintFactory),       // 硬约束 [DPU约束]  产品排产月份
            proDeliverMatch(constraintFactory),     // 硬约束 [交期约束] 排产日期不能大于交期
            productivityMatch(constraintFactory),   // 硬约束 [产能约束] 每天产量不能大于最大产能
            proTrialMatch(constraintFactory),       // 硬约束 [试制约束] 试制车 必需在指定试制日期生产
            workDayMatch(constraintFactory),      // 硬约束 非工作日不能生产

            // *******Soft constraints***************
            balancedMatch(constraintFactory) 
            //priMatch(constraintFactory)           // 软约束 优先级高的先安排生产 pri越大越优先
            // TODO 按物料优先级安排生产 
            // TODO 来单-库存约束 库存少的优先 -- 按日期产线分组 计算 初始库存 + (当前日期-生产周期)之前的产量之和 - 交付日期在当前日期之前的交付数量之和 = 当前日期的实际库存 数值低的优先
        };
    }

    // 硬约束 产品只能在指定类型的资源上生产
    Constraint resTypeMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .filter(product -> product.getResource() != null 
                && !product.getRequiredResType().equals(product.getResource().getType()))
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

    // 硬约束 每天产量不能大于最大产能
    Constraint productivityMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getResource, Product::getWorkCalendar, countDistinctLong(Product::getId))
            .filter((res, date, proCount) -> proCount > res.getProductivity() )
            .penalizeLong(HardSoftLongScore.ONE_HARD, (res, date, proCount) -> a)
            .asConstraint("productivityMatch");
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
            .groupBy(product -> product.getWorkCalendar())
            .groupBy(product -> product.getResource(), countDistinctLong(Product::getId))
            .filter((x,date, type, proCount) -> proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(10), (x,date, type, proCount) -> proCount*a)
            .asConstraint("balancedMatch");
    }
    Constraint balancedMatch_bak1(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, (product) -> product.getResource().getType(), countDistinctLong(Product::getId))
            .filter((date, type, proCount) -> proCount > 1)
            .penalizeLong(HardSoftLongScore.ofSoft(100), (date, type, proCount) -> proCount*a)
            .asConstraint("balancedMatch_bak1");
    }

    // 软约束 优先级高的先安排生产 pri越大越优先
    Constraint priMatch(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Product.class)
            .groupBy(Product::getWorkCalendar, (product) -> product.getResource().getPri(), countDistinctLong(Product::getId))
            .filter((date, pri, proCount) -> proCount > 0)
            .penalizeLong(HardSoftLongScore.ofSoft(50), (date, pri, proCount) -> (5-pri)*a)
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