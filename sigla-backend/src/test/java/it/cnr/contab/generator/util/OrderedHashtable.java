package it.cnr.contab.generator.util;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class OrderedHashtable extends Dictionary
        implements Serializable, Cloneable {

    public static final Hashtable EMPTY_HASHTABLE = new Hashtable();
    private Vector<Object> keys;
    private Hashtable<Object, Object> values;

    public OrderedHashtable() {
        keys = new Vector<Object>();
        values = new Hashtable<Object, Object>();
    }

    public OrderedHashtable(int i) {
        keys = new Vector<Object>();
        values = new Hashtable<Object, Object>(i);
    }

    public OrderedHashtable(int i, float f) {
        keys = new Vector<Object>();
        values = new Hashtable<Object, Object>(i, f);
    }

    private OrderedHashtable(Vector<Object> vector, Hashtable<Object, Object> hashtable) {
        keys = vector;
        values = hashtable;
    }

    public synchronized void clear() {
        values.clear();
        keys.removeAllElements();
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        Vector<Object> clone = (Vector<Object>) keys.clone();
        return new OrderedHashtable(clone, (Hashtable<Object, Object>) values.clone());
    }

    public synchronized Enumeration elements() {
        return new OrderedHashtableEnumerator(this);
    }

    public synchronized Object get(int i) {
        return values.get(keys.elementAt(i));
    }

    public synchronized Object get(Object obj) {
        return values.get(obj);
    }

    public synchronized boolean isEmpty() {
        return values.isEmpty();
    }

    public synchronized Enumeration<Object> keys() {
        return keys.elements();
    }

    public synchronized Object put(Object obj, Object obj1) {
        if (values.get(obj) == null)
            keys.addElement(obj);
        return values.put(obj, obj1);
    }

    public synchronized Object putFirst(Object obj, Object obj1) {
        if (values.get(obj) == null)
            keys.add(0, obj);
        return values.put(obj, obj1);
    }

    public synchronized Object remove(Object obj) {
        keys.removeElement(obj);
        return values.remove(obj);
    }

    public synchronized int size() {
        return values.size();
    }

}