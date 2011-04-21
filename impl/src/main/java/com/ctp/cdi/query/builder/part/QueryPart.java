package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.ParameterContext;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author thomashug
 */
abstract class QueryPart  {
    
    protected List<QueryPart> children = new LinkedList<QueryPart>();
    
    protected abstract QueryPart build(String queryPart);
    
    protected abstract QueryPart buildQuery(StringBuilder builder, ParameterContext ctx);
    
    protected void buildQueryForChildren(StringBuilder builder, ParameterContext ctx) {
        for (QueryPart child : children) {
            child.buildQuery(builder, ctx);
        }
    }
    
    protected boolean hasChildren() {
        return children != null && children.size() > 0;
    }
    
}
