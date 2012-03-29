package com.ctp.cdi.query.criteria.selection.numeric;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.selection.SingularAttributeSelection;

public class Modulo<P> extends SingularAttributeSelection<P, Integer> {

    private final Integer modulo;

    public Modulo(SingularAttribute<P, Integer> attribute, Integer modulo) {
        super(attribute);
        this.modulo = modulo;
    }

    @Override
    public <R> Selection<Integer> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        return builder.mod(path.get(attribute), modulo);
    }

}
