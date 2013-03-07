package org.apache.deltaspike.query.impl.builder.result;

import javax.persistence.Query;

public interface QueryProcessor {

    Object executeQuery(Query query);

}
