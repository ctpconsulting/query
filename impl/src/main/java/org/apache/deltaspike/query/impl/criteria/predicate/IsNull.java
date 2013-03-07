package org.apache.deltaspike.query.impl.criteria.predicate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class IsNull<E, V> extends NoValueBuilder<E, V> {
    
    public IsNull(SingularAttribute<? super E, V> att) {
        super(att);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<E> path) {
        return Arrays.asList(builder.isNull(path.get(att)));
    }

}
