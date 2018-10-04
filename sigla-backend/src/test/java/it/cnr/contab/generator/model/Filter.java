package it.cnr.contab.generator.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 22-nov-04
 *
 * @author Marco Spasiano
 * @version 2.0 [16-Aug-2006] adattamento a plugin
 */
public class Filter {

    private Collection<String> collection;

    public Filter() {
        collection = new ArrayList<String>();
        collection.add("DACR");
        collection.add("DUVA");
        collection.add("UTCR");
        collection.add("UTUV");
        collection.add("PG_VER_REC");
    }

    public boolean isFilter(String string) {
        return collection.contains(string);
    }

}
