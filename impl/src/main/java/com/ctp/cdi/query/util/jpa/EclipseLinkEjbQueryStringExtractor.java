package com.ctp.cdi.query.util.jpa;

import javax.persistence.Query;

import org.eclipse.persistence.jpa.JpaQuery;

@ProviderSpecific("org.eclipse.persistence.jpa.JpaQuery")
public class EclipseLinkEjbQueryStringExtractor implements QueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        return query.unwrap(JpaQuery.class).getDatabaseQuery().getJPQLString();
    }

}
