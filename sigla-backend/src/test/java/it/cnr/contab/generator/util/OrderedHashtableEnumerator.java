package it.cnr.contab.generator.util;

import java.io.Serializable;
import java.util.Enumeration;


class OrderedHashtableEnumerator
        implements Serializable, Enumeration {

    private Enumeration keysEnum;
    private OrderedHashtable oht;

    public OrderedHashtableEnumerator(OrderedHashtable orderedhashtable) {
        oht = orderedhashtable;
        keysEnum = orderedhashtable.keys();
    }

    public boolean hasMoreElements() {
        return keysEnum.hasMoreElements();
    }

    public Object nextElement() {
        return oht.get(keysEnum.nextElement());
    }
}