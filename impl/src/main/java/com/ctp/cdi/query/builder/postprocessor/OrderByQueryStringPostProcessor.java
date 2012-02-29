package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.handler.QueryStringPostProcessor;

public class OrderByQueryStringPostProcessor implements QueryStringPostProcessor {
    
    private static final String ORDER_BY = " order by ";
    
    private final String attribute;
    private final String direction;
    
    @SuppressWarnings("rawtypes")
    public OrderByQueryStringPostProcessor(SingularAttribute attribute, String direction) {
        this.attribute = attribute.getName();
        this.direction = direction;
    }

    public OrderByQueryStringPostProcessor(String attribute, String direction) {
        this.attribute = attribute;
        this.direction = direction;
    }

    @Override
    public String postProcess(String queryString) {
        String base = queryString;
        if (base.contains(ORDER_BY)) {
            base = base.substring(0, base.indexOf(ORDER_BY));
        }
        StringBuilder builder = new StringBuilder(base);
        return builder.append(ORDER_BY)
                .append(QueryBuilder.ENTITY_NAME).append(".").append(attribute)
                .append(" ").append(direction)
                .toString();
    }

}
