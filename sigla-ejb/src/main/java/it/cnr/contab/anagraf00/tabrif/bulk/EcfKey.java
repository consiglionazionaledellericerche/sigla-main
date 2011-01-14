/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/09/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class EcfKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long prog;
	public EcfKey() {
		super();
	}
	public EcfKey(java.lang.Long prog) {
		super();
		this.prog=prog;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof EcfKey)) return false;
		EcfKey k = (EcfKey) o;
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