package com.ctp.cdi.query.test.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name=Simple.BY_NAME,
                query="select s from Simple s where s.name = ?1 and s.enabled = ?2"),
    @NamedQuery(name=Simple.BY_ID,
                query="select s from Simple s where s.id = :id and s.enabled = :enabled")
})
public class Simple {
    
    public static final String BY_NAME = "simple.byName";
    public static final String BY_ID = "simple.byId";

    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private Boolean enabled = Boolean.TRUE;
    private Integer counter = Integer.valueOf(0);
    
    public Simple() {
    }

    public Simple(String name) {
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

}
