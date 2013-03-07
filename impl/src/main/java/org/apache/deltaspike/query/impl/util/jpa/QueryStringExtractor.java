package org.apache.deltaspike.query.impl.util.jpa;

import javax.persistence.Query;

public interface QueryStringExtractor {

    String extractFrom(Query query);

}
