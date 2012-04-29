package com.ctp.cdi.query.test.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
    @NamedQuery(name = Simple.BY_NAME_LIKE,
            query = "select e from Simple e where e.name like ?1"),
    @NamedQuery(name = Simple.BY_NAME_ENABLED,
                query = "select s from Simple s where s.name = ?1 and s.enabled = ?2 order by s.id asc"),
    @NamedQuery(name = Simple.BY_ID,
                query = "select s from Simple s where s.id = :id and s.enabled = :enabled")
})
@Table(name = "SIMPLE_TABLE")
public class Simple {

    public static final String BY_NAME_LIKE = "simple.byNameLike";
    public static final String BY_NAME_ENABLED = "simple.byNameAndEnabled";
    public static final String BY_ID = "simple.byId";

    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String camelCase;
    private Boolean enabled = Boolean.TRUE;
    private Integer counter = Integer.valueOf(0);
    @Temporal(TemporalType.TIMESTAMP)
    private Date temporal;
    
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

    public String getCamelCase() {
        return camelCase;
    }

    public void setCamelCase(String camelCase) {
        this.camelCase = camelCase;
    }

    public Date getTemporal() {
        return temporal;
    }

    public void setTemporal(Date temporal) {
        this.temporal = temporal;
    }

}
