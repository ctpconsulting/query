package com.ctp.cdi.query.criteria.predicate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public interface PredicateBuilder<P> {

    List<Predicate> build(CriteriaBuilder builder, Path<P> path);

}
