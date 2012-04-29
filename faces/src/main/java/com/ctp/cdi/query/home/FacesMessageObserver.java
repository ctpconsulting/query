package com.ctp.cdi.query.home;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Alternative
public class FacesMessageObserver {

    public void observe(@Observes EntityMessage message) {
        if (message.isFailure()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(message.getException().getMessage()));
        }
    }

}
