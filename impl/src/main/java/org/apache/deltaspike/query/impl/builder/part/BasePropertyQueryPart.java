package org.apache.deltaspike.query.impl.builder.part;

import org.apache.deltaspike.query.impl.builder.MethodExpressionException;
import org.apache.deltaspike.query.impl.meta.DaoComponent;
import org.apache.deltaspike.query.impl.property.Property;
import org.apache.deltaspike.query.impl.property.query.NamedPropertyCriteria;
import org.apache.deltaspike.query.impl.property.query.PropertyQueries;
import org.apache.deltaspike.query.impl.property.query.PropertyQuery;


abstract class BasePropertyQueryPart extends QueryPart {

    final static String SEPARATOR = "_";

    void validate(String name, String method, DaoComponent dao) {
        Class<?> current = dao.getEntityClass();
        if (name == null) {
            throw new MethodExpressionException(null, dao.getDaoClass(), method);
        }
        for (String property : name.split(SEPARATOR)) {
            PropertyQuery<?> query = PropertyQueries.createQuery(current)
                    .addCriteria(new NamedPropertyCriteria(property));
            Property<?> result = query.getFirstResult();
            if (result == null) {
                throw new MethodExpressionException(property, dao.getDaoClass(), method);
            }
            current = result.getJavaClass();
        }
    }

    String rewriteSeparator(String name) {
        if (name.contains("_")) {
            return name.replaceAll(SEPARATOR, ".");
        }
        return name;
    }

}
