package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.QueryBuilderContext;

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
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        if (!isFirst) {
            ctx.append(" and ");
        }
        buildQueryForChildren(ctx);
        return this;
    }
    
}
