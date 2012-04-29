package com.ctp.cdi.query.util.jpa;

import javax.persistence.Query;

import org.apache.openjpa.persistence.OpenJPAQuery;

@ProviderSpecific("org.apache.openjpa.persistence.OpenJPAQuery")
public class OpenJpaQueryStringExtractor implements QueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        return query.unwrap(OpenJPAQuery.class).getQueryString();
    }

}
