/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2019
 */
package it.cnr.contab.siope.plus.bulk;
import it.cnr.jada.persistency.Keyed;
public class SIOPEPlusEsitoBase extends SIOPEPlusEsitoKey implements Keyed {
//    PROG_ESITO_APPLICATIVO DECIMAL(38,0) NOT NULL
	private java.lang.Integer progEsitoApplicativo;
 
//    DATA_UPLOAD TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataUpload;
 
//    IDENTIFICATIVO_FLUSSO VARCHAR(250) NOT NULL
	private java.lang.String identificativoFlusso;
 
//    ESITO_OPERAZIONE VARCHAR(30) NOT NULL
	private java.lang.String esitoOperazione;
 
//    DT_ORA_ESITO_OPERAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtOraEsitoOperazione;
 
//    ESERCIZIO_MANDATO DECIMAL(38,0)
	private java.lang.Integer esercizioMandato;
 
//    CD_CDS_MANDATO VARCHAR(30)
	private java.lang.String cdCdsMandato;
 
//    PG_MANDATO DECIMAL(38,0)
	private java.lang.Long pgMandato;
 
//    ESERCIZIO_REVERSALE DECIMAL(38,0)
	private java.lang.Integer esercizioReversale;
 
//    CD_CDS_REVERSALE VARCHAR(30)
	private java.lang.String cdCdsReversale;
 
//    PG_REVERSALE DECIMAL(38,0)
	private java.lang.Long pgReversale;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SIOPE_PLUS_ESITO
	 **/
	public SIOPEPlusEsitoBase() {
		super();
	}
	public SIOPEPlusEsitoBase(java.lang.Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Progressivo esito applicativo]
	 **/
	public java.lang.Integer getProgEsitoApplicativo() {
		return progEsitoApplicativo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Progressivo esito applicativo]
	 **/
	public void setProgEsitoApplicativo(java.lang.Integer progEsitoApplicativo)  {
		this.progEsitoApplicativo=progEsitoApplicativo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Data upload]
	 **/
	public java.sql.Timestamp getDataUpload() {
		return dataUpload;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Data upload]
	 **/
	public void setDataUpload(java.sql.Timestamp dataUpload)  {
		this.dataUpload=dataUpload;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo flusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		return identificativoFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo flusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.identificativoFlusso=identificativoFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esito operazione]
	 **/
	public java.lang.String getEsitoOperazione() {
		return esitoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esito operazione]
	 **/
	public void setEsitoOperazione(java.lang.String esitoOperazione)  {
		this.esitoOperazione=esitoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Data Esito operazione siope+]
	 **/
	public java.sql.Timestamp getDtOraEsitoOperazione() {
		return dtOraEsitoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Data Esito operazione siope+]
	 **/
	public void setDtOraEsitoOperazione(java.sql.Timestamp dtOraEsitoOperazione)  {
		this.dtOraEsitoOperazione=dtOraEsitoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio mandato]
	 **/
	public java.lang.Integer getEsercizioMandato() {
		return esercizioMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio mandato]
	 **/
	public void setEsercizioMandato(java.lang.Integer esercizioMandato)  {
		this.esercizioMandato=esercizioMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [CdS mandato]
	 **/
	public java.lang.String getCdCdsMandato() {
		return cdCdsMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [CdS mandato]
	 **/
	public void setCdCdsMandato(java.lang.String cdCdsMandato)  {
		this.cdCdsMandato=cdCdsMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio mandato]
	 **/
	public java.lang.Long getPgMandato() {
		return pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio mandato]
	 **/
	public void setPgMandato(java.lang.Long pgMandato)  {
		this.pgMandato=pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio reversale]
	 **/
	public java.lang.Integer getEsercizioReversale() {
		return esercizioReversale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio reversale]
	 **/
	public void setEsercizioReversale(java.lang.Integer esercizioReversale)  {
		this.esercizioReversale=esercizioReversale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [CdS reversale]
	 **/
	public java.lang.String getCdCdsReversale() {
		return cdCdsReversale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [CdS reversale]
	 **/
	public void setCdCdsReversale(java.lang.String cdCdsReversale)  {
		this.cdCdsReversale=cdCdsReversale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio reversale]
	 **/
	public java.lang.Long getPgReversale() {
		return pgReversale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio reversale]
	 **/
	public void setPgReversale(java.lang.Long pgReversale)  {
		this.pgReversale=pgReversale;
	}
}