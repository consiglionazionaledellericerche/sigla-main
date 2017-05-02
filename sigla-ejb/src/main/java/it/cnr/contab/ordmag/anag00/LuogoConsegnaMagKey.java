/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class LuogoConsegnaMagKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdLuogoConsegna;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LUOGO_CONSEGNA_MAG
	 **/
	public LuogoConsegnaMagKey() {
		super();
	}
	public LuogoConsegnaMagKey(java.lang.String cdCds, java.lang.String cdLuogoConsegna) {
		super();
		this.cdCds=cdCds;
		this.cdLuogoConsegna=cdLuogoConsegna;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof LuogoConsegnaMagKey)) return false;
		LuogoConsegnaMagKey k = (LuogoConsegnaMagKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdLuogoConsegna(), k.getCdLuogoConsegna())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdLuogoConsegna());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLuogoConsegna]
	 **/
	public void setCdLuogoConsegna(java.lang.String cdLuogoConsegna)  {
		this.cdLuogoConsegna=cdLuogoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLuogoConsegna]
	 **/
	public java.lang.String getCdLuogoConsegna() {
		return cdLuogoConsegna;
	}
}