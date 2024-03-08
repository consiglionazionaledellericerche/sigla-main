/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 08/03/2024
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.sql.Timestamp;

public class VConsIndicatorePagamentiBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(0,-127)
	private java.lang.Integer esercizio;
 
//    TRIMESTRE DECIMAL(0,-127)
	private java.lang.Integer trimestre;
 
//    TIPO_RIGA VARCHAR(13)
	private String tipoRiga;
 
//    CD_TERZO DECIMAL(0,-127)
	private java.math.BigDecimal cdTerzo;
 
//    TIPO_DOCUMENTO VARCHAR(9)
	private String tipoDocumento;
 
//    ESERCIZIO_DOCUMENTO DECIMAL(0,-127)
	private java.lang.Integer esercizioDocumento;
 
//    UO_DOCUMENTO VARCHAR(30)
	private String uoDocumento;
 
//    NUMERO_DOCUMENTO DECIMAL(0,-127)
	private java.lang.Integer numeroDocumento;
 
//    IMPORTO_DOCUMENTO DECIMAL(0,-127)
	private java.math.BigDecimal importoDocumento;
 
//    DATA_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dataScadenza;
 
//    DATA_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dataTrasmissione;
	//    DATA_RICEZIONE TIMESTAMP(7)
	private java.sql.Timestamp dataRicezione;
	//    DATA_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dataRegistrazione;
	//    DATA_LIQUIDAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dataLiquidazione;

	//    DIFFERENZA_GIORNI DECIMAL(0,-127)
	private java.lang.Integer differenzaGiorni;
 
//    IMPORTO_PAGATO DECIMAL(0,-127)
	private java.math.BigDecimal importoPagato;
 
//    IMPORTO_PESATO DECIMAL(0,-127)
	private java.math.BigDecimal importoPesato;
 
//    INDICE_PAGAMENTI DECIMAL(0,-127)
	private java.math.BigDecimal indicePagamenti;
 
//    CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private String cdCdsObbligazione;
 
//    ESERCIZIO_OBBLIGAZIONE DECIMAL(0,-127)
	private java.lang.Integer esercizioObbligazione;
 
//    ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(0,-127)
	private java.lang.Integer esercizioOriObbligazione;
 
//    PG_OBBLIGAZIONE DECIMAL(0,-127)
	private java.lang.Long pgObbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(0,-127)
	private java.lang.Long pgObbligazioneScadenzario;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_CONS_INDICATORE_PAGAMENTI
	 **/
	public VConsIndicatorePagamentiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [trimestre]
	 **/
	public java.lang.Integer getTrimestre() {
		return trimestre;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [trimestre]
	 **/
	public void setTrimestre(java.lang.Integer trimestre)  {
		this.trimestre=trimestre;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoRiga]
	 **/
	public String getTipoRiga() {
		return tipoRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoRiga]
	 **/
	public void setTipoRiga(String tipoRiga)  {
		this.tipoRiga=tipoRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.math.BigDecimal getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.math.BigDecimal cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoDocumento]
	 **/
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoDocumento]
	 **/
	public void setTipoDocumento(String tipoDocumento)  {
		this.tipoDocumento=tipoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioDocumento]
	 **/
	public java.lang.Integer getEsercizioDocumento() {
		return esercizioDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioDocumento]
	 **/
	public void setEsercizioDocumento(java.lang.Integer esercizioDocumento)  {
		this.esercizioDocumento=esercizioDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [uoDocumento]
	 **/
	public String getUoDocumento() {
		return uoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [uoDocumento]
	 **/
	public void setUoDocumento(String uoDocumento)  {
		this.uoDocumento=uoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroDocumento]
	 **/
	public java.lang.Integer getNumeroDocumento() {
		return numeroDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroDocumento]
	 **/
	public void setNumeroDocumento(java.lang.Integer numeroDocumento)  {
		this.numeroDocumento=numeroDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoDocumento]
	 **/
	public java.math.BigDecimal getImportoDocumento() {
		return importoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoDocumento]
	 **/
	public void setImportoDocumento(java.math.BigDecimal importoDocumento)  {
		this.importoDocumento=importoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataScadenza]
	 **/
	public java.sql.Timestamp getDataScadenza() {
		return dataScadenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataScadenza]
	 **/
	public void setDataScadenza(java.sql.Timestamp dataScadenza)  {
		this.dataScadenza=dataScadenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataTrasmissione]
	 **/
	public java.sql.Timestamp getDataTrasmissione() {
		return dataTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataTrasmissione]
	 **/
	public void setDataTrasmissione(java.sql.Timestamp dataTrasmissione)  {
		this.dataTrasmissione=dataTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [differenzaGiorni]
	 **/
	public java.lang.Integer getDifferenzaGiorni() {
		return differenzaGiorni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [differenzaGiorni]
	 **/
	public void setDifferenzaGiorni(java.lang.Integer differenzaGiorni)  {
		this.differenzaGiorni=differenzaGiorni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoPagato]
	 **/
	public java.math.BigDecimal getImportoPagato() {
		return importoPagato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoPagato]
	 **/
	public void setImportoPagato(java.math.BigDecimal importoPagato)  {
		this.importoPagato=importoPagato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoPesato]
	 **/
	public java.math.BigDecimal getImportoPesato() {
		return importoPesato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoPesato]
	 **/
	public void setImportoPesato(java.math.BigDecimal importoPesato)  {
		this.importoPesato=importoPesato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indicePagamenti]
	 **/
	public java.math.BigDecimal getIndicePagamenti() {
		return indicePagamenti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indicePagamenti]
	 **/
	public void setIndicePagamenti(java.math.BigDecimal indicePagamenti)  {
		this.indicePagamenti=indicePagamenti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObbligazione]
	 **/
	public String getCdCdsObbligazione() {
		return cdCdsObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObbligazione]
	 **/
	public void setCdCdsObbligazione(String cdCdsObbligazione)  {
		this.cdCdsObbligazione=cdCdsObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbligazione]
	 **/
	public java.lang.Integer getEsercizioObbligazione() {
		return esercizioObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbligazione]
	 **/
	public void setEsercizioObbligazione(java.lang.Integer esercizioObbligazione)  {
		this.esercizioObbligazione=esercizioObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOriObbligazione]
	 **/
	public java.lang.Integer getEsercizioOriObbligazione() {
		return esercizioOriObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOriObbligazione]
	 **/
	public void setEsercizioOriObbligazione(java.lang.Integer esercizioOriObbligazione)  {
		this.esercizioOriObbligazione=esercizioOriObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScadenzario]
	 **/
	public java.lang.Long getPgObbligazioneScadenzario() {
		return pgObbligazioneScadenzario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScadenzario]
	 **/
	public void setPgObbligazioneScadenzario(java.lang.Long pgObbligazioneScadenzario)  {
		this.pgObbligazioneScadenzario=pgObbligazioneScadenzario;
	}

	public Timestamp getDataRicezione() {
		return dataRicezione;
	}

	public void setDataRicezione(Timestamp dataRicezione) {
		this.dataRicezione = dataRicezione;
	}

	public Timestamp getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Timestamp dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public Timestamp getDataLiquidazione() {
		return dataLiquidazione;
	}

	public void setDataLiquidazione(Timestamp dataLiquidazione) {
		this.dataLiquidazione = dataLiquidazione;
	}
}