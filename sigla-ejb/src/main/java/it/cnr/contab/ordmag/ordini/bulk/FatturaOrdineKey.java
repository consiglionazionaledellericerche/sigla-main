/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class FatturaOrdineKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdUnitaOrganizzativa;
	private java.lang.Integer esercizio;
	private java.lang.Long pgFatturaPassiva;
	private java.lang.Long progressivoRiga;
	private java.lang.String cdCdsOrdine;
	private java.lang.String cdUnitaOperativa;
	private java.lang.Integer esercizioOrdine;
	private java.lang.String cdNumeratore;
	private java.lang.Integer numero;
	private java.lang.Integer riga;
	private java.lang.Integer consegna;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_ORDINE
	 **/
	public FatturaOrdineKey() {
		super();
	}
	public FatturaOrdineKey(java.lang.String cdCds, java.lang.String cdUnitaOrganizzativa, java.lang.Integer esercizio, java.lang.Long pgFatturaPassiva, java.lang.Long progressivoRiga, java.lang.String cdCdsOrdine, java.lang.String cdUnitaOperativa, java.lang.Integer esercizioOrdine, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super();
		this.cdCds=cdCds;
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
		this.esercizio=esercizio;
		this.pgFatturaPassiva=pgFatturaPassiva;
		this.progressivoRiga=progressivoRiga;
		this.cdCdsOrdine=cdCdsOrdine;
		this.cdUnitaOperativa=cdUnitaOperativa;
		this.esercizioOrdine=esercizioOrdine;
		this.cdNumeratore=cdNumeratore;
		this.numero=numero;
		this.riga=riga;
		this.consegna=consegna;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof FatturaOrdineKey)) return false;
		FatturaOrdineKey k = (FatturaOrdineKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdUnitaOrganizzativa(), k.getCdUnitaOrganizzativa())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgFatturaPassiva(), k.getPgFatturaPassiva())) return false;
		if (!compareKey(getProgressivoRiga(), k.getProgressivoRiga())) return false;
		if (!compareKey(getCdCdsOrdine(), k.getCdCdsOrdine())) return false;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		if (!compareKey(getEsercizioOrdine(), k.getEsercizioOrdine())) return false;
		if (!compareKey(getCdNumeratore(), k.getCdNumeratore())) return false;
		if (!compareKey(getNumero(), k.getNumero())) return false;
		if (!compareKey(getRiga(), k.getRiga())) return false;
		if (!compareKey(getConsegna(), k.getConsegna())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdUnitaOrganizzativa());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgFatturaPassiva());
		i = i + calculateKeyHashCode(getProgressivoRiga());
		i = i + calculateKeyHashCode(getCdCdsOrdine());
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		i = i + calculateKeyHashCode(getEsercizioOrdine());
		i = i + calculateKeyHashCode(getCdNumeratore());
		i = i + calculateKeyHashCode(getNumero());
		i = i + calculateKeyHashCode(getRiga());
		i = i + calculateKeyHashCode(getConsegna());
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
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
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
	 * Restituisce il valore di: [pgFatturaPassiva]
	 **/
	public void setPgFatturaPassiva(java.lang.Long pgFatturaPassiva)  {
		this.pgFatturaPassiva=pgFatturaPassiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgFatturaPassiva]
	 **/
	public java.lang.Long getPgFatturaPassiva() {
		return pgFatturaPassiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivoRiga]
	 **/
	public void setProgressivoRiga(java.lang.Long progressivoRiga)  {
		this.progressivoRiga=progressivoRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivoRiga]
	 **/
	public java.lang.Long getProgressivoRiga() {
		return progressivoRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.cdCdsOrdine=cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public java.lang.String getCdCdsOrdine() {
		return cdCdsOrdine;
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
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.esercizioOrdine=esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public java.lang.Integer getEsercizioOrdine() {
		return esercizioOrdine;
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public void setNumero(java.lang.Integer numero)  {
		this.numero=numero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public java.lang.Integer getNumero() {
		return numero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riga]
	 **/
	public void setRiga(java.lang.Integer riga)  {
		this.riga=riga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riga]
	 **/
	public java.lang.Integer getRiga() {
		return riga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public void setConsegna(java.lang.Integer consegna)  {
		this.consegna=consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public java.lang.Integer getConsegna() {
		return consegna;
	}
}