package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.QueryBuilderContext;
import com.ctp.cdi.query.meta.DaoComponent;

/**
 *
 * @author thomashug
 */
class AndQueryPart extends ConnectingQueryPart {

    public AndQueryPart(boolean first) {
        super(first);
    }

    @Override
    protected QueryPart build(String queryPart, String method, DaoComponent dao) {
        children.add(new PropertyQueryPart().build(queryPart, method, dao));
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        if (!first) {
            ctx.append(" and ");
        }
        buildQueryForChildren(ctx);
        return this;
    }

}
