package com.ctp.cdi.query.test.domain;

import javax.persistence.Entity;

@Entity
public class OneToOne extends NamedEntity {
    
    public OneToOne() {
        super();
    }

    public OneToOne(String name) {
        super(name);
    }
}
