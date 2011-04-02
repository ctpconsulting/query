package com.ctp.cdi.query.param;

import com.ctp.cdi.query.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 * Convenience class to manage method and query parameters.
 * @author thomashug
 */
public class Parameters {
    
    private final List<Parameter> parameters;
    
    private Parameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
    
    public static Parameters create(Method method, Object[] parameters) {
        List<Parameter> result = new ArrayList<Parameter>(parameters.length);
        int paramIndex = 1;
        for (int i = 0; i < parameters.length; i++) {
            QueryParam qpAnnotation = extractFrom(method.getParameterAnnotations()[i]);
            if (qpAnnotation != null) {
                result.add(new NamedParameter(qpAnnotation.value(), parameters[i], i));
            } else {
                result.add(new IndexedParameter(paramIndex++, parameters[i], i));
            }
        }
        return new Parameters(result);
    }
    
    public Query applyTo(Query query) {
        for (Parameter param : parameters) {
            param.apply(query);
        }
        return query;
    }
    
    private static QueryParam extractFrom(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(QueryParam.class)) {
                return (QueryParam) annotation;
            }
        }
        return null;
    }
    
}
