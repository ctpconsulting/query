package com.ctp.cdi.query.builder.part;

/**
 * 
 * @author thomashug
 */
abstract class ConnectingQueryPart extends QueryPart {
    
    protected boolean isFirst;

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isFirst() {
        return isFirst;
    }
    
}
