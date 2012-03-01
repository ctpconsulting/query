package com.ctp.cdi.query.builder.part;

import static com.ctp.cdi.query.util.QueryUtils.splitByKeyword;

import java.util.HashSet;
import java.util.Set;

import org.jboss.solder.logging.Logger;

import com.ctp.cdi.query.builder.QueryBuilder;
import com.ctp.cdi.query.builder.QueryBuilderContext;

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
        String[] orderByParts = splitByKeyword(queryPart, "OrderBy");
        if (hasQueryConditions(orderByParts)) {
            String[] orParts = splitByKeyword(removePrefix(orderByParts[0]), "Or");
            boolean first = true;
            for (String or : orParts) {
                OrQueryPart orPart = new OrQueryPart(first);
                first = false;
                children.add(orPart.build(or));
            }
        }
        if (orderByParts.length > 1) {
            OrderByQueryPart orderByPart = new OrderByQueryPart();
            children.add(orderByPart.build(orderByParts[1]));
        }
        return this;
    }

    @Override
    protected QueryPart buildQuery(QueryBuilderContext ctx) {
        ctx.append(QueryBuilder.selectQuery(entityName));
        if (hasChildren(excludedForWhereCheck())) {
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
    
    private Set<Class<? extends QueryPart>> excludedForWhereCheck() {
        Set<Class<? extends QueryPart>> excluded = new HashSet<Class<? extends QueryPart>>();
        excluded.add(OrderByQueryPart.class);
        return excluded;
    }
    
    private boolean hasQueryConditions(String[] orderByParts) {
        return !QUERY_PREFIX.equals(orderByParts[0]);
    }

    private String removePrefix(String queryPart) {
        if (queryPart.startsWith(QUERY_PREFIX)) {
            return queryPart.substring(QUERY_PREFIX.length());
        }
        return queryPart;
    }

}
