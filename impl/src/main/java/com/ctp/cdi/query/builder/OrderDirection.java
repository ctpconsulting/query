package com.ctp.cdi.query.builder;

public enum OrderDirection {

    ASC {
        @Override
        public OrderDirection change() {
            return DESC;
        }
    },
    DESC {
        @Override
        public OrderDirection change() {
            return ASC;
        }
    };
    
    public abstract OrderDirection change();
}
