package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.BuilderContext;
import static com.ctp.cdi.query.util.QueryUtils.splitByKeyword;

/**
 *
 * @author thomashug
 */
class OrQueryPart extends ConnectingQueryPart {

    @Override
    protected QueryPart build(String queryPart) {
        String[] andParts = splitByKeyword(queryPart, "And");
        boolean first = true;
        for (String and : andParts) {
            AndQueryPart andPart = new AndQueryPart();
            andPart.setIsFirst(first);
            first = false;
            children.add(andPart.build(and));
        }
        return this;
    }

    @Override
    protected QueryPart buildQuery(StringBuilder builder, BuilderContext ctx) {
        if (!isFirst) {
            builder.append(" or ");
        }
        buildQueryForChildren(builder, ctx);
        return this;
    }
    
}
