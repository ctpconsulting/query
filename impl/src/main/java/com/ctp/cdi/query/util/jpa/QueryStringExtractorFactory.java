package com.ctp.cdi.query.util.jpa;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

public class QueryStringExtractorFactory {

    private final List<QueryStringExtractor> extractors = Arrays.asList(
            new HibernateQueryStringExtractor(),
            new EclipseLinkEjbQueryStringExtractor(),
            new OpenJpaQueryStringExtractor());

    public QueryStringExtractor select(Query query) {
        for (QueryStringExtractor extractor : extractors) {
            String compare = extractor.getClass().getAnnotation(ProviderSpecific.class).value();
            if (isQueryClass(compare, query)) {
                return extractor;
            }
        }
        throw new RuntimeException("Persistence provider not supported");
    }

    private boolean isQueryClass(String clazzName, Query query) {
        try {
            Class<?> toClass = Class.forName(clazzName);
            toClass.cast(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
