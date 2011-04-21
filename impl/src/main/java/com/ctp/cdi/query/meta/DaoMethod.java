package com.ctp.cdi.query.meta;

import static com.ctp.cdi.query.util.QueryUtils.isNotEmpty;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.ctp.cdi.query.Query;
import com.ctp.cdi.query.handler.EntityDaoHandler;

public class DaoMethod {

    private Method method;
    private MethodType methodType;
    private DaoComponent dao;
    
    public DaoMethod(Method method, DaoComponent dao) {
        this.method = method;
        this.dao = dao;
        extractMethodType();
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

}
