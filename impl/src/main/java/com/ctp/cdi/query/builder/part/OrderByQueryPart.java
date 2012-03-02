package com.ctp.cdi.query.builder.part;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;
import static com.ctp.cdi.query.util.QueryUtils.splitByKeyword;
import static com.ctp.cdi.query.util.QueryUtils.uncapitalize;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.QueryBuilderContext;

public class OrderByQueryPart extends QueryPart {

    private static final String KEYWORD_ASC = "Asc";
    private static final String KEYWORD_DESC = "Desc";
    
    private final List<OrderByQueryAttribute> attributes = new LinkedList<OrderByQueryAttribute>();

    @Override
    protected QueryPart build(String queryPart) {
        Set<String> collect = new TreeSet<String>();
        List<String> ascSplit = new LinkedList<String>();
        split(queryPart, KEYWORD_ASC, ascSplit);
        for (String ascPart : ascSplit) {
            split(ascPart, KEYWORD_DESC, collect);
        }
        for (String part : collect) {
            Direction direction = Direction.fromQueryPart(part);
            String attribute = direction.attribute(part);
            attributes.add(new OrderByQueryAttribute(attribute, direction));
        }
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        ctx.append(" order by ");
        for (Iterator<OrderByQueryAttribute> it = attributes.iterator(); it.hasNext();) {
            it.next().buildQuery(ctx);
            if (it.hasNext())  {
                ctx.append(", ");
            }
        }
        return this;
    }
    
    private void split(String queryPart, String keyword, Collection<String> result) {
        for (String part : splitByKeyword(queryPart, keyword)) {
            String attribute = !part.endsWith(KEYWORD_DESC) && !part.endsWith(KEYWORD_ASC) ? part + keyword : part;
            result.add(attribute);
        }
    }
    
    private static class OrderByQueryAttribute {
        
        private final String attribute;
        private final Direction direction;
        
        public OrderByQueryAttribute(String attribute, Direction direction) {
            this.attribute = attribute;
            this.direction = direction;
        }

        protected void buildQuery(QueryBuilderContext ctx) {
            String entityPrefix = QueryBuilder.ENTITY_NAME + ".";
            ctx.append(entityPrefix).append(attribute)
                .append(direction.queryDirection());
        }
    }
    
    private static enum Direction {
        ASC(KEYWORD_ASC), 
        DESC(KEYWORD_DESC), 
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
            return isNotEmpty(postfix) ? " " + postfix.toLowerCase() : "";
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
