package com.ctp.cdi.query.criteria;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class OrBuilder<P> implements PredicateBuilder<P> {
    
    final Criteria<P> criteria;

    public OrBuilder(Criteria<P> criteria) {
        this.criteria = criteria;
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
        return Arrays.asList(
                builder.or(
                        builder.and(criteria.collectPredicates(builder, path).toArray(new Predicate[0]))));
    }

}
