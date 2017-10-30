/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.persistency.Keyed;
public class BollaScaricoRigaMagBase extends BollaScaricoRigaMagKey implements Keyed {
//    CD_BENE_SERVIZIO VARCHAR(15) NOT NULL
	private java.lang.String cdBeneServizio;
 
//    CD_UNITA_MISURA VARCHAR(3)
	private java.lang.String cdUnitaMisura;
 
//    QUANTITA DECIMAL(12,5)
	private java.math.BigDecimal quantita;
 
//    COEFF_CONV DECIMAL(12,5)
	private java.math.BigDecimal coeffConv;
 
//    CD_CDS_RICH VARCHAR(30)
	private java.lang.String cdCdsRich;
 
//    CD_UNITA_OPERATIVA_RICH VARCHAR(30)
	private java.lang.String cdUnitaOperativaRich;
 
//    ESERCIZIO_RICH DECIMAL(4,0)
	private java.lang.Integer esercizioRich;
 
//    CD_NUMERATORE_RICH VARCHAR(3)
	private java.lang.String cdNumeratoreRich;
 
//    NUMERO_RICH DECIMAL(6,0)
	private java.lang.Integer numeroRich;
 
//    RIGA_RICH DECIMAL(6,0)
	private java.lang.Integer rigaRich;
 
//    NOTE VARCHAR(500)
	private java.lang.String note;
 
//    CD_CDS_LOTTO VARCHAR(30) NOT NULL
	private java.lang.String cdCdsLotto;
 
//    CD_MAGAZZINO_LOTTO VARCHAR(10) NOT NULL
	private java.lang.String cdMagazzinoLotto;
 
//    ESERCIZIO_LOTTO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioLotto;
 
//    CD_NUMERATORE_LOTTO VARCHAR(3) NOT NULL
	private java.lang.String cdNumeratoreLotto;
 
//    PG_LOTTO DECIMAL(6,0) NOT NULL
	private java.lang.Integer pgLotto;
 
//    PG_MOVIMENTO DECIMAL(12,0) NOT NULL
	private java.lang.Long pgMovimento;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    CD_CDS_ORDINE VARCHAR(30)
	private java.lang.String cdCdsOrdine;
 
//    CD_UNITA_OPERATIVA VARCHAR(30)
	private java.lang.String cdUnitaOperativa;
 
//    ESERCIZIO_ORDINE DECIMAL(4,0)
	private java.lang.Integer esercizioOrdine;
 
//    CD_NUMERATORE_ORDINE VARCHAR(3)
	private java.lang.String cdNumeratoreOrdine;
 
//    NUMERO_ORDINE DECIMAL(6,0)
	private java.lang.Integer numeroOrdine;
 
//    RIGA_ORDINE DECIMAL(6,0)
	private java.lang.Integer rigaOrdine;
 
//    CONSEGNA DECIMAL(6,0)
	private java.lang.Integer consegna;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_RIGA_MAG
	 **/
	public BollaScaricoRigaMagBase() {
		super();
	}
	public BollaScaricoRigaMagBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgBollaSca, java.lang.Integer rigbollaScaN) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgBollaSca, rigbollaScaN);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		return cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.cdBeneServizio=cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		return cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.cdUnitaMisura=cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantita]
	 **/
	public java.math.BigDecimal getQuantita() {
		return quantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantita]
	 **/
	public void setQuantita(java.math.BigDecimal quantita)  {
		this.quantita=quantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [coeffConv]
	 **/
	public java.math.BigDecimal getCoeffConv() {
		return coeffConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coeffConv]
	 **/
	public void setCoeffConv(java.math.BigDecimal coeffConv)  {
		this.coeffConv=coeffConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRich]
	 **/
	public java.lang.String getCdCdsRich() {
		return cdCdsRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRich]
	 **/
	public void setCdCdsRich(java.lang.String cdCdsRich)  {
		this.cdCdsRich=cdCdsRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaRich]
	 **/
	public java.lang.String getCdUnitaOperativaRich() {
		return cdUnitaOperativaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaRich]
	 **/
	public void setCdUnitaOperativaRich(java.lang.String cdUnitaOperativaRich)  {
		this.cdUnitaOperativaRich=cdUnitaOperativaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioRich]
	 **/
	public java.lang.Integer getEsercizioRich() {
		return esercizioRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioRich]
	 **/
	public void setEsercizioRich(java.lang.Integer esercizioRich)  {
		this.esercizioRich=esercizioRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreRich]
	 **/
	public java.lang.String getCdNumeratoreRich() {
		return cdNumeratoreRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreRich]
	 **/
	public void setCdNumeratoreRich(java.lang.String cdNumeratoreRich)  {
		this.cdNumeratoreRich=cdNumeratoreRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroRich]
	 **/
	public java.lang.Integer getNumeroRich() {
		return numeroRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroRich]
	 **/
	public void setNumeroRich(java.lang.Integer numeroRich)  {
		this.numeroRich=numeroRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaRich]
	 **/
	public java.lang.Integer getRigaRich() {
		return rigaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaRich]
	 **/
	public void setRigaRich(java.lang.Integer rigaRich)  {
		this.rigaRich=rigaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [note]
	 **/
	public java.lang.String getNote() {
		return note;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [note]
	 **/
	public void setNote(java.lang.String note)  {
		this.note=note;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsLotto]
	 **/
	public java.lang.String getCdCdsLotto() {
		return cdCdsLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsLotto]
	 **/
	public void setCdCdsLotto(java.lang.String cdCdsLotto)  {
		this.cdCdsLotto=cdCdsLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzinoLotto]
	 **/
	public java.lang.String getCdMagazzinoLotto() {
		return cdMagazzinoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoLotto]
	 **/
	public void setCdMagazzinoLotto(java.lang.String cdMagazzinoLotto)  {
		this.cdMagazzinoLotto=cdMagazzinoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioLotto]
	 **/
	public java.lang.Integer getEsercizioLotto() {
		return esercizioLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioLotto]
	 **/
	public void setEsercizioLotto(java.lang.Integer esercizioLotto)  {
		this.esercizioLotto=esercizioLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreLotto]
	 **/
	public java.lang.String getCdNumeratoreLotto() {
		return cdNumeratoreLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreLotto]
	 **/
	public void setCdNumeratoreLotto(java.lang.String cdNumeratoreLotto)  {
		this.cdNumeratoreLotto=cdNumeratoreLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgLotto]
	 **/
	public java.lang.Integer getPgLotto() {
		return pgLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgLotto]
	 **/
	public void setPgLotto(java.lang.Integer pgLotto)  {
		this.pgLotto=pgLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMovimento]
	 **/
	public java.lang.Long getPgMovimento() {
		return pgMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMovimento]
	 **/
	public void setPgMovimento(java.lang.Long pgMovimento)  {
		this.pgMovimento=pgMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public java.lang.String getCdCdsOrdine() {
		return cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.cdCdsOrdine=cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public java.lang.Integer getEsercizioOrdine() {
		return esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.esercizioOrdine=esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreOrdine]
	 **/
	public java.lang.String getCdNumeratoreOrdine() {
		return cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreOrdine]
	 **/
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.cdNumeratoreOrdine=cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroOrdine]
	 **/
	public java.lang.Integer getNumeroOrdine() {
		return numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroOrdine]
	 **/
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.numeroOrdine=numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaOrdine]
	 **/
	public java.lang.Integer getRigaOrdine() {
		return rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaOrdine]
	 **/
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.rigaOrdine=rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public java.lang.Integer getConsegna() {
		return consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public void setConsegna(java.lang.Integer consegna)  {
		this.consegna=consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}