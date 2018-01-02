/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class RaggrMagazzinoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdRaggrMagazzino;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RAGGR_MAGAZZINO
	 **/
	public RaggrMagazzinoKey() {
		super();
	}
	public RaggrMagazzinoKey(java.lang.String cdCds, java.lang.String cdRaggrMagazzino) {
		super();
		this.cdCds=cdCds;
		this.cdRaggrMagazzino=cdRaggrMagazzino;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof RaggrMagazzinoKey)) return false;
		RaggrMagazzinoKey k = (RaggrMagazzinoKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdRaggrMagazzino(), k.getCdRaggrMagazzino())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdRaggrMagazzino());
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
	 * Restituisce il valore di: [cdRaggrMagazzino]
	 **/
	public void setCdRaggrMagazzino(java.lang.String cdRaggrMagazzino)  {
		this.cdRaggrMagazzino=cdRaggrMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdRaggrMagazzino]
	 **/
	public java.lang.String getCdRaggrMagazzino() {
		return cdRaggrMagazzino;
	}
}