package org.apache.deltaspike.query.impl.builder.part;

/**
 * 
 * @author thomashug
 */
abstract class ConnectingQueryPart extends QueryPart {

    protected final boolean first;

    public ConnectingQueryPart(boolean first) {
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

}
