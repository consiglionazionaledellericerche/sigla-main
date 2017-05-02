/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class GruppoMerceologicoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdGruppoMerceologico;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: GRUPPO_MERCEOLOGICO
	 **/
	public GruppoMerceologicoKey() {
		super();
	}
	public GruppoMerceologicoKey(java.lang.String cdGruppoMerceologico) {
		super();
		this.cdGruppoMerceologico=cdGruppoMerceologico;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof GruppoMerceologicoKey)) return false;
		GruppoMerceologicoKey k = (GruppoMerceologicoKey) o;
		if (!compareKey(getCdGruppoMerceologico(), k.getCdGruppoMerceologico())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdGruppoMerceologico());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdGruppoMerceologico]
	 **/
	public void setCdGruppoMerceologico(java.lang.String cdGruppoMerceologico)  {
		this.cdGruppoMerceologico=cdGruppoMerceologico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdGruppoMerceologico]
	 **/
	public java.lang.String getCdGruppoMerceologico() {
		return cdGruppoMerceologico;
	}
}