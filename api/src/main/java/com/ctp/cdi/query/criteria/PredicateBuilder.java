package com.ctp.cdi.query.criteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

interface PredicateBuilder<P> {

    List<Predicate> build(CriteriaBuilder builder, Path<P> path);

}
