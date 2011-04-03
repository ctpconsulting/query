package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.BuilderContext;

/**
 *
 * @author thomashug
 */
class AndQueryPart extends ConnectingQueryPart {

    @Override
    protected QueryPart build(String queryPart) {
        children.add(new PropertyQueryPart().build(queryPart));
        return this;
    }

    @Override
    protected QueryPart buildQuery(StringBuilder builder, BuilderContext ctx) {
        if (!isFirst) {
            builder.append(" and ");
        }
        buildQueryForChildren(builder, ctx);
        return this;
    }
    
}
