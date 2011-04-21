package com.ctp.cdi.query.meta;

import javax.enterprise.util.AnnotationLiteral;

public class QueryInvocationLiteral extends AnnotationLiteral<QueryInvocation>
        implements QueryInvocation {

    private static final long serialVersionUID = 1L;
    
    private final MethodType methodType;

    public QueryInvocationLiteral(MethodType methodType) {
        this.methodType = methodType;
    }

    @Override
    public MethodType value() {
        return methodType;
    }

}
