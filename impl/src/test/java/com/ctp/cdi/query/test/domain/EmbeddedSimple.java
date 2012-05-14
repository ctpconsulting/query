package com.ctp.cdi.query.test.domain;

import javax.persistence.Embeddable;

@Embeddable
public class EmbeddedSimple {

    private String embedd;

    public String getEmbedd() {
        return embedd;
    }

    public void setEmbedd(String embedd) {
        this.embedd = embedd;
    }
    
}
