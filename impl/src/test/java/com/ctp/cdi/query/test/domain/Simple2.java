package com.ctp.cdi.query.test.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Simple2 {

    @Id
    @GeneratedValue
    private Long id;
    
    private String name;

    public Simple2() {
    }

    public Simple2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

}
