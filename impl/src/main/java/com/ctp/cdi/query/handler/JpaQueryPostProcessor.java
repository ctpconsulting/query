package com.ctp.cdi.query.handler;

import javax.persistence.Query;

public interface JpaQueryPostProcessor {

    Query postProcess(QueryInvocationContext context, Query query);

}
