package org.apache.deltaspike.query.impl.builder.part;

import static org.apache.deltaspike.query.impl.util.QueryUtils.splitByKeyword;

import org.apache.deltaspike.query.impl.builder.QueryBuilderContext;
import org.apache.deltaspike.query.impl.meta.DaoComponent;


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
