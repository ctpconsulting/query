package com.ctp.cdi.query.builder.postprocessor;

import javax.persistence.metamodel.SingularAttribute;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.handler.QueryStringPostProcessor;

public class OrderByQueryStringPostProcessor implements QueryStringPostProcessor {
    
    private static final String ORDER_BY = " order by ";
    
    private final String attribute;
    private final String direction;

    public OrderByQueryStringPostProcessor(SingularAttribute<?, ?> attribute, String direction) {
        this.attribute = attribute.getName();
        this.direction = direction;
    }

    public OrderByQueryStringPostProcessor(String attribute, String direction) {
        this.attribute = attribute;
        this.direction = direction;
    }

    @Override
    public String postProcess(String queryString) {
        StringBuilder builder = new StringBuilder(queryString);
        if (queryString.contains(ORDER_BY)) {
            builder.append(",");
        } else {
            builder.append(ORDER_BY);
        }
        return builder.append(QueryBuilder.ENTITY_NAME).append(".").append(attribute)
                .append(" ").append(direction)
                .toString();
    }

}
