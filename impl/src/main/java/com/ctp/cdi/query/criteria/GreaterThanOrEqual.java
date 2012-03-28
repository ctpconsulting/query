package com.ctp.cdi.query.criteria;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

class GreaterThanOrEqual<E, V extends Comparable<? super V>> extends SingleValueBuilder<E, V> {
    
    GreaterThanOrEqual(SingularAttribute<? super E, V> att, V value) {
        super(att, value);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.greaterThanOrEqualTo(path.get(att), value));
    }

}
