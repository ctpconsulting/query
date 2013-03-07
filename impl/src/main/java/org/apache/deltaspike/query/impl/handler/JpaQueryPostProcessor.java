package org.apache.deltaspike.query.impl.handler;

import javax.persistence.Query;

public interface JpaQueryPostProcessor {

    Query postProcess(CdiQueryInvocationContext context, Query query);

}
