/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/02/2013
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class VConsCupMandatiBulk  extends OggettoBulk implements Persistent  {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_CONS_CUP_MANDATI
	 **/
	public VConsCupMandatiBulk() {
		super();
	}
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cdCds;
//  uo VARCHAR(30) NOT NULL
	private java.lang.String uo;	

//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

//  PG_MANDATO DECIMAL(10,0) NOT NULL
	private java.lang.Long pgMandato;

//  ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioObbligazione;

//  ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioOriObbligazione;

//  PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pgObbligazione;

//  PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pgObbligazioneScadenzario;

//  CD_CDS_DOC_AMM VARCHAR(30) NOT NULL
	private java.lang.String cdCdsDocAmm;

//  CD_UO_DOC_AMM VARCHAR(30) NOT NULL
	private java.lang.String cdUoDocAmm;

//  ESERCIZIO_DOC_AMM DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioDocAmm;

//  CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL
	private java.lang.String cdTipoDocumentoAmm;

//  DS_TIPO_DOC_AMM VARCHAR(100) NOT NULL
	private java.lang.String dsTipoDocAmm;

//  PG_DOC_AMM DECIMAL(10,0) NOT NULL
	private java.lang.Long pgDocAmm;

//  CD_CUP VARCHAR(15) NOT NULL
	private java.lang.String cdCup;

//  DS_CUP VARCHAR(200) NOT NULL
	private java.lang.String dsCup;

//  IMPORTO DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal importo;

//  DT_EMISSIONE TIMESTAMP(8)
	private java.sql.Timestamp dtEmissione;

//  DT_PAGAMENTO TIMESTAMP(8)
	private java.sql.Timestamp dtPagamento;

//  DT_TRASMISSIONE TIMESTAMP(8)
	private java.sql.Timestamp dtTrasmissione;

	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
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
	 * Restituisce il valore di: [pgMandato]
	 **/
	public java.lang.Long getPgMandato() {
		return pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMandato]
	 **/
	public void setPgMandato(java.lang.Long pgMandato)  {
		this.pgMandato=pgMandato;
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsDocAmm]
	 **/
	public java.lang.String getCdCdsDocAmm() {
		return cdCdsDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsDocAmm]
	 **/
	public void setCdCdsDocAmm(java.lang.String cdCdsDocAmm)  {
		this.cdCdsDocAmm=cdCdsDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoDocAmm]
	 **/
	public java.lang.String getCdUoDocAmm() {
		return cdUoDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoDocAmm]
	 **/
	public void setCdUoDocAmm(java.lang.String cdUoDocAmm)  {
		this.cdUoDocAmm=cdUoDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioDocAmm]
	 **/
	public java.lang.Integer getEsercizioDocAmm() {
		return esercizioDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioDocAmm]
	 **/
	public void setEsercizioDocAmm(java.lang.Integer esercizioDocAmm)  {
		this.esercizioDocAmm=esercizioDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoDocumentoAmm]
	 **/
	public java.lang.String getCdTipoDocumentoAmm() {
		return cdTipoDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoDocumentoAmm]
	 **/
	public void setCdTipoDocumentoAmm(java.lang.String cdTipoDocumentoAmm)  {
		this.cdTipoDocumentoAmm=cdTipoDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoDocAmm]
	 **/
	public java.lang.String getDsTipoDocAmm() {
		return dsTipoDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoDocAmm]
	 **/
	public void setDsTipoDocAmm(java.lang.String dsTipoDocAmm)  {
		this.dsTipoDocAmm=dsTipoDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocAmm]
	 **/
	public java.lang.Long getPgDocAmm() {
		return pgDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocAmm]
	 **/
	public void setPgDocAmm(java.lang.Long pgDocAmm)  {
		this.pgDocAmm=pgDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		return cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.cdCup=cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCup]
	 **/
	public java.lang.String getDsCup() {
		return dsCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCup]
	 **/
	public void setDsCup(java.lang.String dsCup)  {
		this.dsCup=dsCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importo]
	 **/
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importo]
	 **/
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtEmissione]
	 **/
	public java.sql.Timestamp getDtEmissione() {
		return dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtEmissione]
	 **/
	public void setDtEmissione(java.sql.Timestamp dtEmissione)  {
		this.dtEmissione=dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtPagamento]
	 **/
	public java.sql.Timestamp getDtPagamento() {
		return dtPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtPagamento]
	 **/
	public void setDtPagamento(java.sql.Timestamp dtPagamento)  {
		this.dtPagamento=dtPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtTrasmissione]
	 **/
	public java.sql.Timestamp getDtTrasmissione() {
		return dtTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtTrasmissione]
	 **/
	public void setDtTrasmissione(java.sql.Timestamp dtTrasmissione)  {
		this.dtTrasmissione=dtTrasmissione;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo) {
		this.uo = uo;
	}
}