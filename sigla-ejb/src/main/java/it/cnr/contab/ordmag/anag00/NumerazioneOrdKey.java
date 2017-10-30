/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class NumerazioneOrdKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUnitaOperativa;
	private java.lang.Integer esercizio;
	private java.lang.String cdNumeratore;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_ORD
	 **/
	public NumerazioneOrdKey() {
		super();
	}
	public NumerazioneOrdKey(java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore) {
		super();
		this.cdUnitaOperativa=cdUnitaOperativa;
		this.esercizio=esercizio;
		this.cdNumeratore=cdNumeratore;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof NumerazioneOrdKey)) return false;
		NumerazioneOrdKey k = (NumerazioneOrdKey) o;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCdNumeratore(), k.getCdNumeratore())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCdNumeratore());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
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
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.cdNumeratore=cdNumeratore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		return cdNumeratore;
	}
}