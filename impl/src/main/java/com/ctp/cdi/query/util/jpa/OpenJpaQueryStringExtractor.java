package com.ctp.cdi.query.util.jpa;

import javax.persistence.Query;

@ProviderSpecific("org.apache.openjpa.persistence.OpenJPAQuery")
public class OpenJpaQueryStringExtractor extends BaseQueryStringExtractor {

    @Override
    public String extractFrom(Query query) {
        return (String) invoke("getQueryString", query);
    }

}
