/*
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Buono_carico_scaricoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_inventario;
	private java.lang.String ti_documento;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_buono_c_s;
	public Buono_carico_scaricoKey() {
		super();
	}
	public Buono_carico_scaricoKey(java.lang.Long pg_inventario, java.lang.String ti_documento, java.lang.Integer esercizio, java.lang.Long pg_buono_c_s) {
		super();
		this.pg_inventario=pg_inventario;
		this.ti_documento=ti_documento;
		this.esercizio=esercizio;
		this.pg_buono_c_s=pg_buono_c_s;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Buono_carico_scaricoKey)) return false;
		Buono_carico_scaricoKey k = (Buono_carico_scaricoKey) o;
		if (!compareKey(getPg_inventario(), k.getPg_inventario())) return false;
		if (!compareKey(getTi_documento(), k.getTi_documento())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_buono_c_s(), k.getPg_buono_c_s())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_inventario());
		i = i + calculateKeyHashCode(getTi_documento());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_buono_c_s());
		return i;
	}
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.pg_inventario=pg_inventario;
	}
	public java.lang.Long getPg_inventario () {
		return pg_inventario;
	}
	public void setTi_documento(java.lang.String ti_documento)  {
		this.ti_documento=ti_documento;
	}
	public java.lang.String getTi_documento () {
		return ti_documento;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setPg_buono_c_s(java.lang.Long pg_buono_c_s)  {
		this.pg_buono_c_s=pg_buono_c_s;
	}
	public java.lang.Long getPg_buono_c_s () {
		return pg_buono_c_s;
	}
}