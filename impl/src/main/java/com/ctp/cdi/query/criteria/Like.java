package com.ctp.cdi.query.criteria;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

class Like<E> extends SingleValueBuilder<E, String> {
    
    Like(SingularAttribute<? super E, String> att, String value) {
        super(att, value);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.like(path.get(att), value));
    }

}
