/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class FlussoGiornaleDiCassaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String identificativoFlusso;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaKey() {
		super();
	}
	public FlussoGiornaleDiCassaKey(java.lang.Integer esercizio, java.lang.String identificativoFlusso) {
		super();
		this.esercizio=esercizio;
		this.identificativoFlusso=identificativoFlusso;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof FlussoGiornaleDiCassaKey)) return false;
		FlussoGiornaleDiCassaKey k = (FlussoGiornaleDiCassaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getIdentificativoFlusso(), k.getIdentificativoFlusso())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getIdentificativoFlusso());
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
}