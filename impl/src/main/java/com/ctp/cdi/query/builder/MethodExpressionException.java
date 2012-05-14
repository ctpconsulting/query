package com.ctp.cdi.query.builder;

public class MethodExpressionException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String property;
    private final Class<?> daoClass;
    private final String method;

    public MethodExpressionException(String property, Class<?> daoClass, String method) {
        this.property = property;
        this.daoClass = daoClass;
        this.method = method;
    }

    @Override
    public String getMessage() {
        return "Invalid property '" + property + "' in method expression " +
                    daoClass.getName() + "." + method;
    }
    
}
