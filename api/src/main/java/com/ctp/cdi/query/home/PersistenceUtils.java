package com.ctp.cdi.query.home;

import java.io.Serializable;

interface PersistenceUtils extends Serializable {

    Class<?> entityClass(Class<?> homeClass);
    
    Object primaryKeyValue(Object entity);

}
