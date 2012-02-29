package com.ctp.cdi.query.builder.result;

import javax.persistence.Query;

public interface QueryProcessor {

    Object executeQuery(Query query);

}
