/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 21/01/2008
 */
package it.cnr.contab.cori00.views.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class F24ep_tempTotKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long prog;
	public F24ep_tempTotKey() {
		super();
	}
	public F24ep_tempTotKey(java.lang.Long prog) {
		super();
		this.prog=prog;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof F24ep_tempTotKey)) return false;
		F24ep_tempTotKey k = (F24ep_tempTotKey) o;
		if (!compareKey(getProg(), k.getProg())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getProg());
		return i;
	}
	public void setProg(java.lang.Long prog)  {
		this.prog=prog;
	}
	public java.lang.Long getProg() {
		return prog;
	}
}