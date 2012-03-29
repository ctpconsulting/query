package com.ctp.cdi.query.criteria.selection.temporal;

import java.sql.Time;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import com.ctp.cdi.query.criteria.QuerySelection;

public class CurrentTime<P> implements QuerySelection<P, Time> {

    @Override
    public <R> Selection<Time> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.currentTime();
    }

}
