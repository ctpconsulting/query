package com.ctp.cdi.query.criteria.selection.temporal;

import java.sql.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import com.ctp.cdi.query.criteria.QuerySelection;

public class CurrentDate<P> implements QuerySelection<P, Date> {

    @Override
    public <R> Selection<Date> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.currentDate();
    }

}
