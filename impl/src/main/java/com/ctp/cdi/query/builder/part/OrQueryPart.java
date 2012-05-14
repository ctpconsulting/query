package com.ctp.cdi.query.builder.part;

import static com.ctp.cdi.query.util.QueryUtils.splitByKeyword;

import com.ctp.cdi.query.builder.QueryBuilderContext;
import com.ctp.cdi.query.meta.DaoComponent;

/**
 * @author thomashug
 */
class OrQueryPart extends ConnectingQueryPart {

    public OrQueryPart(boolean first) {
        super(first);
    }

    @Override
    protected QueryPart build(String queryPart, String method, DaoComponent dao) {
        String[] andParts = splitByKeyword(queryPart, "And");
        boolean first = true;
        for (String and : andParts) {
            AndQueryPart andPart = new AndQueryPart(first);
            first = false;
            children.add(andPart.build(and, method, dao));
        }
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        if (!first) {
            ctx.append(" or ");
        }
        buildQueryForChildren(ctx);
        return this;
    }

}
