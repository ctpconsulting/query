package org.apache.deltaspike.query.impl.util.jpa;

import javax.persistence.Query;

@ProviderSpecific("org.apache.openjpa.persistence.OpenJPAQuery")
public class OpenJpaQueryStringExtractor extends BaseQueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        return (String) invoke("getQueryString", query);
    }

}
