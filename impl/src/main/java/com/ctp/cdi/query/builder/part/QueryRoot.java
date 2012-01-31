package com.ctp.cdi.query.builder.part;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.QueryBuilderContext;
import org.jboss.solder.logging.Logger;

import static com.ctp.cdi.query.util.QueryUtils.splitByKeyword;

/**
 * Root of the query tree. Also the only exposed class in the package.
 * @author thomashug
 */
public class QueryRoot extends QueryPart {

    public static final QueryRoot UNKNOWN_ROOT = new QueryRoot("null-object");

    public static final String QUERY_PREFIX = "findBy";

    private final Logger log = Logger.getLogger(QueryRoot.class);

    private final String entityName;

    private String jpqlQuery;

    protected QueryRoot(String entityName) {
        this.entityName = entityName;
    }

    public static QueryRoot create(String method, String entityName) {
        QueryRoot root = new QueryRoot(entityName);
        root.build(method);
        root.createJpql();
        return root;
    }

    public String getJpqlQuery() {
        return jpqlQuery;
    }

    @Override
    protected QueryPart build(String queryPart) {
        String[] orParts = splitByKeyword(removePrefix(queryPart), "Or");
        boolean first = true;
        for (String or : orParts) {
            OrQueryPart orPart = new OrQueryPart(first);
            first = false;
            children.add(orPart.build(or));
        }
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        ctx.append(QueryBuilder.selectQuery(entityName));
        if (hasChildren()) {
            ctx.append(" where ");
        }
        buildQueryForChildren(ctx);
        return this;
    }

    protected String createJpql() {
        QueryBuilderContext ctx = new QueryBuilderContext();
        buildQuery(ctx);
        jpqlQuery = ctx.resultString();
        log.debugv("createJpql: Query is {0}", jpqlQuery);
        return jpqlQuery;
    }

    private String removePrefix(String queryPart) {
        if (queryPart.startsWith(QUERY_PREFIX)) {
            return queryPart.substring(QUERY_PREFIX.length());
        }
        return queryPart;
    }

}
