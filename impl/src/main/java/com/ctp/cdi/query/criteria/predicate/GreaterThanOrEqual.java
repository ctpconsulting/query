package com.ctp.cdi.query.criteria.predicate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;


public class GreaterThanOrEqual<E, V extends Comparable<? super V>> extends SingleValueBuilder<E, V> {
    
    public GreaterThanOrEqual(SingularAttribute<? super E, V> att, V value) {
        super(att, value);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.greaterThanOrEqualTo(path.get(att), value));
    }

}
