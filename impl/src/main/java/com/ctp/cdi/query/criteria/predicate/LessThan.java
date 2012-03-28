package com.ctp.cdi.query.criteria.predicate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;


public class LessThan<E, V extends Number> extends SingleValueBuilder<E, V> {
    
    public LessThan(SingularAttribute<? super E, V> att, V value) {
        super(att, value);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.lt(path.get(att), value));
    }

}
