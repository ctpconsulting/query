package com.ctp.cdi.query.util.jpa;

import javax.persistence.Query;

public interface QueryStringExtractor {

    String extractFrom(Query query);

}
