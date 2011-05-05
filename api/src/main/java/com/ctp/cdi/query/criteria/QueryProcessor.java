package com.ctp.cdi.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;

public interface QueryProcessor<P> {

    void process(CriteriaQuery<P> query, CriteriaBuilder builder, Path<P> path);

}
