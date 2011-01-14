package it.cnr.contab.varstanz00.bp;

import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.util.ObjectReplacer;

public class AssestatoResiduoReplacer extends PrimaryKeyHashtable implements ObjectReplacer {

	public Object replaceObject(Object obj) {
		if (get(obj) != null)
		  return get(obj);
		return obj;
	}
}
