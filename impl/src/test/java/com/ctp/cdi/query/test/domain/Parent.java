package com.ctp.cdi.query.test.domain;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;

@Entity
public class Parent extends NamedEntity {

    @javax.persistence.OneToOne(cascade = CascadeType.ALL)
    private OneToOne one;
    
    @javax.persistence.OneToMany(cascade = CascadeType.ALL)
    private List<OneToMany> many = new LinkedList<OneToMany>();
    
    private Long value = Long.valueOf(0);
    
    public Parent() {
        super();
    }

    public Parent(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Parent [value=" + value + ", getName()=" + getName() + ", getId()=" + getId() + "]";
    }

    public void add(OneToMany otm) {
        many.add(otm);
    }

    public OneToOne getOne() {
        return one;
    }

    public void setOne(OneToOne one) {
        this.one = one;
    }

    public List<OneToMany> getMany() {
        return many;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
