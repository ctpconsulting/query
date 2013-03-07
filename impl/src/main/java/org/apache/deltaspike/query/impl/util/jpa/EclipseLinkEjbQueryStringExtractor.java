package org.apache.deltaspike.query.impl.util.jpa;

import javax.persistence.Query;

@ProviderSpecific("org.eclipse.persistence.jpa.JpaQuery")
public class EclipseLinkEjbQueryStringExtractor extends BaseQueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        Object dbQuery = invoke("getDatabaseQuery", query);
        return (String) invoke("getJPQLString", dbQuery);
    }

}
