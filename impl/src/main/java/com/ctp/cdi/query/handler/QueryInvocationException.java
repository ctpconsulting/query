package com.ctp.cdi.query.handler;

import java.lang.reflect.Method;

public class QueryInvocationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueryInvocationException(Throwable t, CdiQueryInvocationContext context) {
        super(createMessage(context, t), t);
    }

    public QueryInvocationException(String message, CdiQueryInvocationContext context) {
        super(createMessage(context));
    }

    public QueryInvocationException(Throwable t, Class<?> proxy, Method method) {
        super(createMessage(proxy, method, t), t);
    }

    private static final String createMessage(CdiQueryInvocationContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("Failed calling DAO: [");
        builder.append("DAO=").append(context.getDaoMethod().getDao().getDaoClass().getName()).append(",");
        builder.append("entity=").append(context.getEntityClass().getName()).append(",");
        builder.append("method=").append(context.getMethod().getName()).append(",");
        builder.append("query=").append(context.getQueryString()).append("],");
        return builder.toString();
    }

    private static final String createMessage(CdiQueryInvocationContext context, Throwable t) {
        StringBuilder builder = new StringBuilder(createMessage(context));
        builder.append("exception=").append(t.getClass()).append(",message=").append(t.getMessage());
        return builder.toString();
    }

    private static String createMessage(Class<?> proxy, Method method, Throwable t) {
        StringBuilder builder = new StringBuilder();
        builder.append("Exception calling DAO: [");
        builder.append("DAO=").append(proxy).append(",");
        builder.append("method=").append(method.getName()).append("],");
        builder.append("exception=").append(t.getClass()).append(",message=").append(t.getMessage());
        return builder.toString();
    }

}
