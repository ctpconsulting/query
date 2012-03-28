package com.ctp.cdi.query.criteria;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

class GreaterThan<E, V extends Number> extends SingleValueBuilder<E, V> {
    
    GreaterThan(SingularAttribute<? super E, V> att, V value) {
        super(att, value);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.gt(path.get(att), value));
    }

}
