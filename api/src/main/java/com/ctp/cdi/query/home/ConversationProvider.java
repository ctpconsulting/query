package com.ctp.cdi.query.home;

import java.io.Serializable;

interface ConversationProvider extends Serializable {

    void begin();
    
    void end();
    
    boolean isTransient();

}
