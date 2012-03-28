package com.ctp.cdi.query.criteria;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

class IsNotNull<E, V> extends NoValueBuilder<E, V> {
    
    IsNotNull(SingularAttribute<? super E, V> att) {
        super(att);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.isNotNull(path.get(att)));
    }

}
