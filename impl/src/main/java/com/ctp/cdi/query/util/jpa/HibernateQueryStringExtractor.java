package com.ctp.cdi.query.util.jpa;

import javax.persistence.Query;

@ProviderSpecific("org.hibernate.ejb.HibernateQuery")
public class HibernateQueryStringExtractor extends BaseQueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        Object hibernateQuery = invoke("getHibernateQuery", query);
        return (String) invoke("getQueryString", hibernateQuery);
    }

}
