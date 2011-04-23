package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.QueryBuilderContext;
import java.util.LinkedList;
import java.util.List;

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
    
    protected boolean hasChildren() {
        return children != null && children.size() > 0;
    }
    
}
