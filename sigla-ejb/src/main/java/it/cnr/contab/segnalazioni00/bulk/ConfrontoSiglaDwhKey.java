/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ConfrontoSiglaDwhKey extends OggettoBulk implements KeyedPersistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFRONTO_SIGLA_DWH
	 **/
	public ConfrontoSiglaDwhKey() {
		super();
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ConfrontoSiglaDwhKey)) return false;
		ConfrontoSiglaDwhKey k = (ConfrontoSiglaDwhKey) o;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		return i;
	}
}