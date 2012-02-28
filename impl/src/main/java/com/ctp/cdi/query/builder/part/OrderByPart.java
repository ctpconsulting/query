package com.ctp.cdi.query.builder.part;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;
import static com.ctp.cdi.query.util.QueryUtils.uncapitalize;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.QueryBuilderContext;

public class OrderByPart extends QueryPart {
    
    private String attribute;
    private Direction direction;
    
    public OrderByPart() {}

    @Override
    protected QueryPart build(String queryPart) {
        direction = Direction.fromQueryPart(queryPart);
        attribute = direction.attribute(queryPart);
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        String entityPrefix = QueryBuilder.ENTITY_NAME + ".";
        ctx.append(" order by ")
            .append(entityPrefix).append(attribute)
            .append(direction.queryDirection());
        return this;
    }
    
    private static enum Direction {
        ASC("Asc"), 
        DESC("Desc"), 
        DEFAULT("");
        
        private final String postfix;
        
        private Direction(String postfix) {
            this.postfix = postfix;
        }
        
        public boolean endsWith(String queryPart) {
            return isNotEmpty(postfix) ? queryPart.endsWith(postfix) : false;
        }
        
        public String attribute(String queryPart) {
            String attribute = isNotEmpty(postfix) ? 
                    queryPart.substring(0, queryPart.indexOf(postfix)) : 
                    queryPart;
            return uncapitalize(attribute);
        }
        
        public String queryDirection() {
            return isNotEmpty(postfix) ? " " + uncapitalize(postfix) : "";
        }
        
        public static Direction fromQueryPart(String queryPart) {
            for (Direction dir : values()) {
                if (dir.endsWith(queryPart)) {
                    return dir;
                }
            }
            return DEFAULT;
        }
       
    }

}
