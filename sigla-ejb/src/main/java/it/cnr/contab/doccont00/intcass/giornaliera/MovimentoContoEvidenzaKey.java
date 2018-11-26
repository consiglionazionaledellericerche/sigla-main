/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class MovimentoContoEvidenzaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String identificativoFlusso;
	private java.lang.String contoEvidenza;
	private java.lang.String stato;
	private java.lang.Long progressivo;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaKey() {
		super();
	}
	public MovimentoContoEvidenzaKey(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza, java.lang.String stato, java.lang.Long progressivo) {
		super();
		this.esercizio=esercizio;
		this.identificativoFlusso=identificativoFlusso;
		this.contoEvidenza=contoEvidenza;
		this.stato=stato;
		this.progressivo=progressivo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof MovimentoContoEvidenzaKey)) return false;
		MovimentoContoEvidenzaKey k = (MovimentoContoEvidenzaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getIdentificativoFlusso(), k.getIdentificativoFlusso())) return false;
		if (!compareKey(getContoEvidenza(), k.getContoEvidenza())) return false;
		if (!compareKey(getStato(), k.getStato())) return false;
		if (!compareKey(getProgressivo(), k.getProgressivo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getIdentificativoFlusso());
		i = i + calculateKeyHashCode(getContoEvidenza());
		i = i + calculateKeyHashCode(getStato());
		i = i + calculateKeyHashCode(getProgressivo());
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivo]
	 **/
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivo]
	 **/
	public java.lang.Long getProgressivo() {
		return progressivo;
	}
}