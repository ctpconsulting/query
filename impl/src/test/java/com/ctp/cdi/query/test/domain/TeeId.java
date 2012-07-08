package com.ctp.cdi.query.test.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class TeeId implements Serializable {
    
    @Column(nullable = false)
    private long teeSetId;

    @Column(nullable = false)
    private long holeId;

    public TeeId() {
    }

    public TeeId(long teeSetId, long holeId) {
        this.teeSetId = teeSetId;
        this.holeId = holeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (holeId ^ (holeId >>> 32));
        result = prime * result + (int) (teeSetId ^ (teeSetId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeeId other = (TeeId) obj;
        if (holeId != other.holeId)
            return false;
        if (teeSetId != other.teeSetId)
            return false;
        return true;
    }

    public long getTeeSetId() {
        return teeSetId;
    }

    public void setTeeSetId(long teeSetId) {
        this.teeSetId = teeSetId;
    }

    public long getHoleId() {
        return holeId;
    }

    public void setHoleId(long holeId) {
        this.holeId = holeId;
    }
}
