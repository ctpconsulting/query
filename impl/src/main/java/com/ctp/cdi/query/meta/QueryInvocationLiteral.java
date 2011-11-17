package com.ctp.cdi.query.meta;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Utility class to construct a qualifier and resolve a dependency programmatically.
 * 
 * @author thomashug
 */
@SuppressWarnings("all")
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
