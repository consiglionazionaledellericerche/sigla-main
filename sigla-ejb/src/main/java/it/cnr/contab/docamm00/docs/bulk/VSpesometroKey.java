/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/11/2013
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VSpesometroKey extends OggettoBulk implements KeyedPersistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SPESOMETRO
	 **/
	public VSpesometroKey() {
		super();
	}
	public VSpesometroKey(java.lang.Long progr) {
		super();
		this.prog = progr;
	}

	private java.lang.Long prog;
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VSpesometroKey)) return false;
		VSpesometroKey k = (VSpesometroKey) o;
		if(!compareKey(getProg(),k.getProg())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getProg());
	}
	public java.lang.Long getProg() {
		return prog;
	}
	public void setProg(java.lang.Long prog) {
		this.prog = prog;
	}
}