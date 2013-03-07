package org.apache.deltaspike.query.impl.builder.part;

import static org.apache.deltaspike.query.impl.util.QueryUtils.splitByKeyword;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.deltaspike.query.impl.builder.MethodExpressionException;
import org.apache.deltaspike.query.impl.builder.QueryBuilder;
import org.apache.deltaspike.query.impl.builder.QueryBuilderContext;
import org.apache.deltaspike.query.impl.meta.DaoComponent;


/**
 * Root of the query tree. Also the only exposed class in the package.
 * @author thomashug
 */
public class QueryRoot extends QueryPart {

    public static final QueryRoot UNKNOWN_ROOT = new QueryRoot("null-object");

    public static final String QUERY_PREFIX = "findBy";

    private static final Logger log = Logger.getLogger(QueryRoot.class.getName());

    private final String entityName;

    private String jpqlQuery;

    protected QueryRoot(String entityName) {
        this.entityName = entityName;
    }

    public static QueryRoot create(String method, DaoComponent dao) {
        QueryRoot root = new QueryRoot(dao.getEntityName());
        root.build(method, method, dao);
        root.createJpql();
        return root;
    }

    public String getJpqlQuery() {
        return jpqlQuery;
    }

    @Override
    protected QueryPart build(String queryPart, String method, DaoComponent dao) {
        String[] orderByParts = splitByKeyword(queryPart, "OrderBy");
        if (hasQueryConditions(orderByParts)) {
            String[] orParts = splitByKeyword(removePrefix(orderByParts[0]), "Or");
            boolean first = true;
            for (String or : orParts) {
                OrQueryPart orPart = new OrQueryPart(first);
                first = false;
                children.add(orPart.build(or, method, dao));
            }
        }
        if (orderByParts.length > 1) {
            OrderByQueryPart orderByPart = new OrderByQueryPart();
            children.add(orderByPart.build(orderByParts[1], method, dao));
        }
        if (children.isEmpty()) {
            throw new MethodExpressionException(dao.getDaoClass(), method);
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
        log.log(Level.FINER, "createJpql: Query is {0}", jpqlQuery);
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
