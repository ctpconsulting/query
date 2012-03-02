package com.ctp.cdi.query.handler;

import javax.persistence.Query;

public interface JpaQueryPostProcessor {

    void postProcess(Query query);

}
