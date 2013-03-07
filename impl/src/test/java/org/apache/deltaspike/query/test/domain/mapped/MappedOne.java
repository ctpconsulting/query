package org.apache.deltaspike.query.test.domain.mapped;

public class MappedOne {

    private Long id;
    private String name;
    
    public MappedOne() { }
    
    public MappedOne(String name) {
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
