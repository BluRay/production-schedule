package org.acme.productionScheduling.solver;

import java.time.Duration;
import org.acme.productionScheduling.domain.*;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.countDistinctLong;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sumLong;

public class ProductionSchedulingConstraintProvider implements ConstraintProvider {
    private long a = 1L;
  
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
            // Hard constraints
            deliverConflict(constraintFactory)  // 硬约束：要在交付期之内生产完成需求数量
        };
    }
    
    Constraint deliverConflict(ConstraintFactory constraintFactory) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return constraintFactory
            .forEach(MatnrProduction.class)
            .filter(product -> product.getProductionTime().isBefore(LocalDate.parse("2024-09-05", formatter)))
            .penalizeLong(HardSoftLongScore.ONE_HARD, product -> a)
            .asConstraint("deliverConflict");
    }
}