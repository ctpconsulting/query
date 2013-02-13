package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.MethodExpressionException;
import com.ctp.cdi.query.meta.DaoComponent;
import com.ctp.cdi.query.property.Property;
import com.ctp.cdi.query.property.query.NamedPropertyCriteria;
import com.ctp.cdi.query.property.query.PropertyQueries;
import com.ctp.cdi.query.property.query.PropertyQuery;


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
