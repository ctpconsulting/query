package org.apache.deltaspike.query.test.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@SuppressWarnings("serial")
public class Tee implements Serializable {
    
    @EmbeddedId
    private TeeId id;
    private int distance;

    public TeeId getId() {
        return id;
    }

    public void setId(TeeId id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
