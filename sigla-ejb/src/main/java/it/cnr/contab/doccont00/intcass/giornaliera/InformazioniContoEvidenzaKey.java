/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class InformazioniContoEvidenzaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String identificativoFlusso;
	private java.lang.String contoEvidenza;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: INFORMAZIONI_CONTO_EVIDENZA
	 **/
	public InformazioniContoEvidenzaKey() {
		super();
	}
	public InformazioniContoEvidenzaKey(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza) {
		super();
		this.esercizio=esercizio;
		this.identificativoFlusso=identificativoFlusso;
		this.contoEvidenza=contoEvidenza;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof InformazioniContoEvidenzaKey)) return false;
		InformazioniContoEvidenzaKey k = (InformazioniContoEvidenzaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getIdentificativoFlusso(), k.getIdentificativoFlusso())) return false;
		if (!compareKey(getContoEvidenza(), k.getContoEvidenza())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getIdentificativoFlusso());
		i = i + calculateKeyHashCode(getContoEvidenza());
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
	 * Restituisce il valore di: [identificativoFlusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.identificativoFlusso=identificativoFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoFlusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		return identificativoFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [contoEvidenza]
	 **/
	public void setContoEvidenza(java.lang.String contoEvidenza)  {
		this.contoEvidenza=contoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [contoEvidenza]
	 **/
	public java.lang.String getContoEvidenza() {
		return contoEvidenza;
	}
}