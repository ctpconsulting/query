package com.ctp.cdi.query.builder.part;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ctp.cdi.query.builder.QueryBuilderContext;

/**
 *
 * @author thomashug
 */
public abstract class QueryPart  {
    
    protected List<QueryPart> children = new LinkedList<QueryPart>();
    
    protected abstract QueryPart build(String queryPart);
    
    protected abstract QueryPart buildQuery(QueryBuilderContext ctx);
    
    protected void buildQueryForChildren(QueryBuilderContext ctx) {
        for (QueryPart child : children) {
            child.buildQuery(ctx);
        }
    }
    
    protected boolean hasChildren(Set<Class<? extends QueryPart>> excluded) {
        if (children == null || children.isEmpty()) {
            return false;
        }
        for (QueryPart part : children) {
            if (!excluded.contains(part.getClass())) {
                return true;
            }
        }
        return false;
    }
    
}
