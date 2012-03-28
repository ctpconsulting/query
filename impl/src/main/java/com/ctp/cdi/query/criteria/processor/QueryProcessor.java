package com.ctp.cdi.query.criteria.processor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;

public interface QueryProcessor<P> {

    <R> void process(CriteriaQuery<R> query, CriteriaBuilder builder, Path<P> path);

}
