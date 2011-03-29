package com.ctp.cdi.query.handler;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Entry point for the query generation.
 * 
 * @author thomashug
 */
public class QueryHandler {

    @AroundInvoke
    protected Object handle(InvocationContext ctx) {
	return null;
    }

}
