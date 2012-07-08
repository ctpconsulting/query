package com.ctp.cdi.query.test.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Principal {

    @Id @GeneratedValue
    private Long id;
    
    private String name;
    
    public Principal() {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Principal [id=").append(id)
               .append(", name=").append(name).append("]");
        return builder.toString();
    }

    public Principal(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
