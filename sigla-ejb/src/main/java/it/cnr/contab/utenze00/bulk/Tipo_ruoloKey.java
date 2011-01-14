/*
* Created by Generator 1.0
* Date 09/01/2006
*/
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_ruoloKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String tipo;
	public Tipo_ruoloKey() {
		super();
	}
	public Tipo_ruoloKey( java.lang.String tipo) {
		super();
		this.tipo=tipo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_ruoloKey)) return false;
		Tipo_ruoloKey k = (Tipo_ruoloKey) o;
		if (!compareKey(getTipo(), k.getTipo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
			i = i + calculateKeyHashCode(getTipo());
		return i;
	}
	
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.lang.String getTipo () {
		return tipo;
	}
}