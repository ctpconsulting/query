package org.apache.deltaspike.query.impl.builder;

public class MethodExpressionException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String property;
    private final Class<?> daoClass;
    private final String method;
    
    public MethodExpressionException(Class<?> daoClass, String method) {
        this(null, daoClass, method);
    }

    public MethodExpressionException(String property, Class<?> daoClass, String method) {
        this.property = property;
        this.daoClass = daoClass;
        this.method = method;
    }

    @Override
    public String getMessage() {
        if (property != null) {
            return "Invalid property '" + property + "' in method expression " + daoClass.getName() + "." + method;
        }
        return "Method '" + method + "'of DAO " + daoClass.getName() + " is not a method expression";
    }
    
}
