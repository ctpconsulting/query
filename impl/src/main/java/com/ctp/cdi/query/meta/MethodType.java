package com.ctp.cdi.query.meta;

import com.ctp.cdi.query.handler.EntityDaoHandler;

/**
 * DAO method type. Stands for
 * <ul>
 *     <li>Delegated methods - the DAO has a concrete implementation for this
 *         or the method is implemented in the {@link EntityDaoHandler}.</li>
 *     <li>Annotated method - the query is defined via a Query annotation.</li>
 *     <li>The method defines a query expression by its name.</li>
 * </ul>
 * 
 * @author thomashug
 */
public enum MethodType {

    DELEGATE, ANNOTATED, PARSE

}
