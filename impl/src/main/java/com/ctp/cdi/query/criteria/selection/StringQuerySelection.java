package com.ctp.cdi.query.criteria.selection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.criteria.QuerySelection;


public class StringQuerySelection<P> implements QuerySelection<P, String> {
    
    private final SingularAttribute<P, String> attribute;
    private final int from;
    private final int length;
    private final Operation operation;
    
    public StringQuerySelection(SingularAttribute<P, String> attribute, Operation operation) {
        this(attribute, operation, -1, -1);
    }
    
    public StringQuerySelection(SingularAttribute<P, String> attribute, Operation operation, int from) {
        this(attribute, operation, from, -1);
    }

    public StringQuerySelection(SingularAttribute<P, String> attribute, Operation operation, 
            int from, int length) {
        this.attribute = attribute;
        this.operation = operation;
        this.from = from;
        this.length = length;
    }

    @Override
    public <R> Selection<String> toSelection(CriteriaQuery<R> query, CriteriaBuilder builder, Path<? extends P> path) {
        switch (operation) {
            case SUBSTRING:
                if (length == -1)
                    return builder.substring(path.get(attribute), from);
                else
                    return builder.substring(path.get(attribute), from, length);
            case LOWER:
                return builder.lower(path.get(attribute));
            case UPPER:
                return builder.upper(path.get(attribute));
        }
        return null;
    }
    
    public static enum Operation {
        SUBSTRING, LOWER, UPPER
    }

}
