package com.ctp.cdi.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

public interface QuerySelection<P, X> {

    <R> Selection<X> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path);
    
}
