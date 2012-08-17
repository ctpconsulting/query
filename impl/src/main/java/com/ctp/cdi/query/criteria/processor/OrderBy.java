package com.ctp.cdi.query.criteria.processor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.builder.OrderDirection;


public class OrderBy<P, V> implements QueryProcessor<P> {
    
    private final SingularAttribute<? super P, V> att;
    private final OrderDirection dir;

    public OrderBy(SingularAttribute<? super P, V> att, OrderDirection dir) {
        this.att = att;
        this.dir = dir;
    }

    @Override
    public <R> void process(CriteriaQuery<R> query, CriteriaBuilder builder, Path<P> path) {
        switch (dir) {
            case ASC:
                query.orderBy(builder.asc(path.get(att)));
                break;
            default:
                query.orderBy(builder.desc(path.get(att)));
        }
    }

}
