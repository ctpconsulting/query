package com.ctp.cdi.query.meta;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.builder.part.QueryRoot;
import com.ctp.cdi.query.handler.EntityDaoHandler;
import com.ctp.cdi.query.util.EntityUtils;

/**
 * Stores information about a specific method of a DAO:
 * <ul>
 *     <li>The reference to the Method reflection object</li>
 *     <li>Whether this method delegates, is annotated or is parsed</li>
 *     <li>A reference to the parent DAO</li>
 *     <li>For parsed DAO methods, also the JPQL string is cached</li>
 * </ul>
 * 
 * @author thomashug
 */
public class DaoMethod {

    private Method method;
    private MethodType methodType;
    private DaoComponent dao;
    private QueryRoot queryRoot;
    
    public DaoMethod(Method method, DaoComponent dao) {
        this.method = method;
        this.dao = dao;
        extractMethodType();
        initQueryRoot();
    }
    
    private void extractMethodType() {
        if (isDelegateMethod()) {
            methodType = MethodType.DELEGATE;
        } else if (isAnnotated()) {
            methodType = MethodType.ANNOTATED;
        } else {
            methodType = MethodType.PARSE;
        }
    }
    
    private void initQueryRoot() {
        if (methodType == MethodType.PARSE) {
            queryRoot = QueryRoot.create(method.getName(), 
                    EntityUtils.entityName(dao.getEntityClass()));
        }
    }
    
    private boolean isAnnotated() {
        if (method.isAnnotationPresent(Query.class)) {
            Query query = method.getAnnotation(Query.class);
            return isNotEmpty(query.value()) || isNotEmpty(query.named());
        }
        return false;
    }

    private boolean isDelegateMethod() {
        return !Modifier.isAbstract(method.getModifiers()) || EntityDaoHandler.contains(method);
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public DaoComponent getDao() {
        return dao;
    }

    public QueryRoot getQueryRoot() {
        return queryRoot;
    }

}
