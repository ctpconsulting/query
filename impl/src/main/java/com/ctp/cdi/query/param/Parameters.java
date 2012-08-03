package com.ctp.cdi.query.param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.jboss.solder.logging.Logger;

import com.ctp.cdi.query.FirstResult;
import com.ctp.cdi.query.MaxResults;
import com.ctp.cdi.query.QueryParam;

/**
 * Convenience class to manage method and query parameters.
 * @author thomashug
 */
public final class Parameters {

    private static final Logger LOG = Logger.getLogger(Parameters.class);

    private static final int DEFAULT_MAX = 0;
    private static final int DEFAULT_FIRST = -1;

    private final List<Parameter> parameterList;
    private final int max;
    private final int firstResult;

    private Parameters(List<Parameter> parameters, int max, int firstResult) {
        this.parameterList = parameters;
        this.max = max;
        this.firstResult = firstResult;
    }

    public static Parameters createEmpty() {
        List<Parameter> empty = Collections.emptyList();
        return new Parameters(empty, DEFAULT_MAX, DEFAULT_FIRST);
    }

    public static Parameters create(Method method, Object[] parameters) {
        int max = extractSizeRestriction(method);
        int first = DEFAULT_FIRST;
        List<Parameter> result = new ArrayList<Parameter>(parameters.length);
        int paramIndex = 1;
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < parameters.length; i++) {
            if (isParameter(method.getParameterAnnotations()[i])) {
                QueryParam qpAnnotation = extractFrom(annotations[i], QueryParam.class);
                if (qpAnnotation != null) {
                    result.add(new NamedParameter(qpAnnotation.value(), parameters[i], i));
                } else {
                    result.add(new IndexedParameter(paramIndex++, parameters[i], i));
                }
            } else {
                max = extractInt(parameters[i], annotations[i], MaxResults.class, max);
                first = extractInt(parameters[i], annotations[i], FirstResult.class, first);
            }
        }
        return new Parameters(result, max, first);
    }

    public Query applyTo(Query query) {
        for (Parameter param : parameterList) {
            param.apply(query);
        }
        return query;
    }

    public boolean hasSizeRestriction() {
        return max > DEFAULT_MAX;
    }

    public int getSizeRestriciton() {
        return max;
    }

    public boolean hasFirstResult() {
        return firstResult > DEFAULT_FIRST;
    }

    public int getFirstResult() {
        return firstResult;
    }

    private static int extractSizeRestriction(Method method) {
        if (method.isAnnotationPresent(com.ctp.cdi.query.Query.class)) {
            return method.getAnnotation(com.ctp.cdi.query.Query.class).max();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A extractFrom(Annotation[] annotations, Class<A> target) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(target)) {
                return (A) annotation;
            }
        }
        return null;
    }

    private static <A extends Annotation> int extractInt(Object parameter, Annotation[] annotations,
            Class<A> target, int defaultVal) {
        if (parameter != null) {
            A result = extractFrom(annotations, target);
            if (result != null) {
                if (parameter instanceof Integer) {
                    return (Integer) parameter;
                } else {
                    LOG.warnv("Method parameter extraction: Param type must be int: {0}->is:{1}",
                            target, parameter.getClass());
                }
            }
        }
        return defaultVal;
    }

    private static boolean isParameter(Annotation[] annotations) {
        return extractFrom(annotations, MaxResults.class) == null &&
               extractFrom(annotations, FirstResult.class) == null;
    }

}
