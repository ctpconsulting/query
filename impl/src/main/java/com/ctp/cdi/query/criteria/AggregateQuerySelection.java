package com.ctp.cdi.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;


public class AggregateQuerySelection<P, X extends Number> implements QuerySelection<P, X> {
    
    private final Operator operator;
    private final SingularAttribute<P, X> attribute;

    public AggregateQuerySelection(Operator operator, SingularAttribute<P, X> attribute) {
        this.operator = operator;
        this.attribute = attribute;
    }

    @Override
    public <R> Selection<X> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        Path<X> expression = path.get(attribute);
        return toSelection(expression, builder);
    }
    
    @SuppressWarnings("unchecked")
    private Selection<X> toSelection(Path<X> expression, CriteriaBuilder builder) {
        switch (operator) {
            case ABS:
                return builder.abs(expression);
            case SUM:
                return builder.sum(expression);
            case AVG:
                return (Selection<X>) builder.avg(expression);
            case COUNT:
                return (Selection<X>) builder.count(expression);
            case MAX:
                return builder.max(expression);
            case MIN:
                return builder.min(expression);
            case NEG:
                return builder.neg(expression);
        }
        return null;
    }

    public static enum Operator {
        ABS, SUM, AVG, COUNT, MAX, MIN, NEG
    }

}
