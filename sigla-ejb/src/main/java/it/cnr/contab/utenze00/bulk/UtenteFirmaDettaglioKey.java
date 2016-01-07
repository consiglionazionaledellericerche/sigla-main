/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/12/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class UtenteFirmaDettaglioKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUtente;
	private java.lang.String cdUnitaOrganizzativa;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UTENTE_FIRMA_DETTAGLIO
	 **/
	public UtenteFirmaDettaglioKey() {
		super();
	}
	public UtenteFirmaDettaglioKey(java.lang.String cdUtente, java.lang.String cdUnitaOrganizzativa) {
		super();
		this.cdUtente=cdUtente;
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof UtenteFirmaDettaglioKey)) return false;
		UtenteFirmaDettaglioKey k = (UtenteFirmaDettaglioKey) o;
		if (!compareKey(getCdUtente(), k.getCdUtente())) return false;
		if (!compareKey(getCdUnitaOrganizzativa(), k.getCdUnitaOrganizzativa())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUtente());
		i = i + calculateKeyHashCode(getCdUnitaOrganizzativa());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public void setCdUtente(java.lang.String cdUtente)  {
		this.cdUtente=cdUtente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public java.lang.String getCdUtente() {
		return cdUtente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
}