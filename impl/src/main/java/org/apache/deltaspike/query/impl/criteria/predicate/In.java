package org.apache.deltaspike.query.impl.criteria.predicate;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class In<P, V> implements PredicateBuilder<P> {
    
    SingularAttribute<? super P, V> singular;
    V[] values;

    public In(SingularAttribute<? super P, V> singular, V[] values) {
        this.singular = singular;
        this.values = Arrays.copyOf(values, values.length);
    }

    @Override
    public List<Predicate> build(CriteriaBuilder builder, Path<P> path) {
        Path<V> p = path.get(singular);
        CriteriaBuilder.In<V> in = builder.in(p);
        for (V value : values) {
            if (value != null) {
                in.value(value);
            }
        }
        return Arrays.asList((Predicate) in);
    }

}
