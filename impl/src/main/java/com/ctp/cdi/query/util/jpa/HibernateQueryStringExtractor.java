package com.ctp.cdi.query.util.jpa;

import javax.persistence.Query;

import org.hibernate.ejb.HibernateQuery;

@ProviderSpecific("org.hibernate.ejb.HibernateQuery")
public class HibernateQueryStringExtractor implements QueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        return query.unwrap(HibernateQuery.class).getHibernateQuery().getQueryString();
    }

}
