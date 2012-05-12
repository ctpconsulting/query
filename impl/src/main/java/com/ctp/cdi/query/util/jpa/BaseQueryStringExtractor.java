package com.ctp.cdi.query.util.jpa;

import java.lang.reflect.Method;

public abstract class BaseQueryStringExtractor implements QueryStringExtractor {

    Object invoke(String methodName, Object target) {
        try {
            Method method = target.getClass().getMethod(methodName);
            return method.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
