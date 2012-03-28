package com.ctp.cdi.query.criteria.predicate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;


public class Between<E, V extends Comparable<? super V>> extends SingleValueBuilder<E, V> {
    
    private final V upper;
    
    public Between(SingularAttribute<? super E, V> att, V lower, V upper) {
        super(att, lower);
        this.upper = upper;
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.between(path.get(att), value, upper));
    }

}
