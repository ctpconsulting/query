package com.ctp.cdi.query.handler;

import javax.interceptor.InvocationContext;

public class QueryInvocationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QueryInvocationException(Throwable t, CdiQueryInvocationContext context) {
        super(createMessage(context, t), t);
    }
    
    public QueryInvocationException(Throwable t, InvocationContext context) {
        super(createMessage(context, t), t);
    }

    private static final String createMessage(CdiQueryInvocationContext context, Throwable t) {
        StringBuilder builder = new StringBuilder();
        builder.append("Exception calling DAO: [");
        builder.append("DAO=").append(context.getDaoMethod().getDao().getDaoClass().getName()).append(",");
        builder.append("entity=").append(context.getEntityClass().getName()).append(",");
        builder.append("method=").append(context.getMethod().getName()).append(",");
        builder.append("query=").append(context.getQueryString()).append("],");
        builder.append("exception=").append(t.getClass()).append(",message=").append(t.getMessage());
        return builder.toString();
    }
    
    private static String createMessage(InvocationContext context, Throwable t) {
        StringBuilder builder = new StringBuilder();
        builder.append("Exception calling DAO: [");
        builder.append("DAO=").append(context.getTarget().getClass()).append(",");
        builder.append("method=").append(context.getMethod().getName()).append("],");
        builder.append("exception=").append(t.getClass()).append(",message=").append(t.getMessage());
        return builder.toString();
    }

}
