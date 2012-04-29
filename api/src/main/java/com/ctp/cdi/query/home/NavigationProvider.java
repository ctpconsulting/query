package com.ctp.cdi.query.home;

import java.io.Serializable;

interface NavigationProvider extends Serializable {

    boolean isPostback();

    Object create();
    
    Object view(Object primaryKey);

    Object exception();

    Object search();

}
