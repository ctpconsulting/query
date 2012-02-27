package com.ctp.cdi.query.meta.unit;

import java.util.Comparator;

public class PersistentClassComparator implements Comparator<PersistentClassDescriptor> {
    
    public static final PersistentClassComparator INSTANCE = new PersistentClassComparator();

    @Override
    public int compare(PersistentClassDescriptor o1, PersistentClassDescriptor o2) {
        Class<?> e1 = o1.getEntityClass();
        Class<?> e2 = o2.getEntityClass();
        if (e1.isAssignableFrom(e2)) {
            return 1;
        }
        if (e2.isAssignableFrom(e1)) {
            return -1;
        }
        return 0;
    }

}
