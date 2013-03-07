package org.apache.deltaspike.query.impl.builder.postprocessor;

import javax.persistence.metamodel.SingularAttribute;

import org.apache.deltaspike.query.impl.builder.OrderDirection;
import org.apache.deltaspike.query.impl.builder.QueryBuilder;
import org.apache.deltaspike.query.impl.handler.QueryStringPostProcessor;


public class OrderByQueryStringPostProcessor implements QueryStringPostProcessor {
    
    private static final String ORDER_BY = " order by ";
    
    private final String attribute;
    private OrderDirection direction;

    public OrderByQueryStringPostProcessor(SingularAttribute<?, ?> attribute, OrderDirection direction) {
        this.attribute = attribute.getName();
        this.direction = direction;
    }

    public OrderByQueryStringPostProcessor(String attribute, OrderDirection direction) {
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
    
    public boolean matches(SingularAttribute<?, ?> attribute) {
        return matches(attribute.getName());
    }

    public boolean matches(String attribute) {
        return this.attribute.equals(attribute);
    }
    
    public void changeDirection() {
        direction = direction.change();
    }
    

}
