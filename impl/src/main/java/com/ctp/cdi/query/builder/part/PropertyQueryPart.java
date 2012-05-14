package com.ctp.cdi.query.builder.part;

import static com.ctp.cdi.query.util.QueryUtils.uncapitalize;

import java.text.MessageFormat;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.QueryBuilderContext;
import com.ctp.cdi.query.builder.QueryOperator;
import com.ctp.cdi.query.meta.DaoComponent;

/**
 *
 * @author thomashug
 */
class PropertyQueryPart extends BasePropertyQueryPart {
        
    private String name;
    private QueryOperator comparator;

    @Override
    protected QueryPart build(String queryPart, String method, DaoComponent dao) {
        comparator = QueryOperator.Equal;
        name = uncapitalize(queryPart);
        for (QueryOperator comp : QueryOperator.values()) {
            if (queryPart.endsWith(comp.getExpression())) {
                comparator = comp;
                name = uncapitalize(queryPart.substring(0, queryPart.indexOf(comp.getExpression())));
                break;
            }
        }
        validate(name, method, dao);
        name = rewriteSeparator(name);
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        String[] args = new String[comparator.getParamNum() + 1];
        args[0] = QueryBuilder.ENTITY_NAME + "." + name;
        for (int i = 1; i < args.length; i++) {
            args[i] = "?" + ctx.increment();
        }
        ctx.append(MessageFormat.format(comparator.getJpql(), (Object[]) args));
        return this;
    }
    
}
