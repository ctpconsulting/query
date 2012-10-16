package com.ctp.cdi.query.handler;

import javax.persistence.Query;

public interface JpaQueryPostProcessor {

    Query postProcess(CdiQueryInvocationContext context, Query query);

}
