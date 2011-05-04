package com.ctp.cdi.query.test.domain;

import javax.persistence.Entity;

@Entity
public class OneToMany extends NamedEntity {

    public OneToMany() {
        super();
    }
    
    public OneToMany(String name) {
        super(name);
    }

}
