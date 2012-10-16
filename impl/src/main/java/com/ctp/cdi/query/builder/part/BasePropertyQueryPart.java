package com.ctp.cdi.query.builder.part;

import org.jboss.solder.properties.Property;
import org.jboss.solder.properties.query.NamedPropertyCriteria;
import org.jboss.solder.properties.query.PropertyQueries;
import org.jboss.solder.properties.query.PropertyQuery;

import com.ctp.cdi.query.builder.MethodExpressionException;
import com.ctp.cdi.query.meta.DaoComponent;


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
