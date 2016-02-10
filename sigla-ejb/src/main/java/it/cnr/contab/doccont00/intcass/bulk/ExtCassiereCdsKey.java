/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 30/05/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ExtCassiereCdsKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String codiceProto;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EXT_CASSIERE_CDS
	 **/
	public ExtCassiereCdsKey() {
		super();
	}
	public ExtCassiereCdsKey(java.lang.Integer esercizio, java.lang.String codiceProto) {
		super();
		this.esercizio=esercizio;
		this.codiceProto=codiceProto;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ExtCassiereCdsKey)) return false;
		ExtCassiereCdsKey k = (ExtCassiereCdsKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCodiceProto(), k.getCodiceProto())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCodiceProto());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceProto]
	 **/
	public void setCodiceProto(java.lang.String codiceProto)  {
		this.codiceProto=codiceProto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceProto]
	 **/
	public java.lang.String getCodiceProto() {
		return codiceProto;
	}
}