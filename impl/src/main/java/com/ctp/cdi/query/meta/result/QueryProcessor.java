package com.ctp.cdi.query.meta.result;

import javax.persistence.Query;

public interface QueryProcessor {

    Object executeQuery(Query query);

}
