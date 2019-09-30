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
 * Date 31/01/2014
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.math.BigDecimal;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class VRendicontazioneBulk   extends OggettoBulk implements Persistent  {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_RENDICONTAZIONE
	 **/
	public VRendicontazioneBulk() {
		super();
	}
//  TIPODOCUMENTO VARCHAR(18)
	private java.lang.String tipodocumento;

//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

//  CD_CDS VARCHAR(30)
	private java.lang.String cdCds;

//  CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cdUnitaOrganizzativa;

//  CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cdTerzo;

//  DENOMINAZIONE VARCHAR(101)
	private java.lang.String denominazione;

//  PARTITA_IVA VARCHAR(20)
	private java.lang.String partitaIva;

//  CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;

//  NR_FATTURA_FORNITORE VARCHAR(20)
	private java.lang.String nrFatturaFornitore;

//  DT_FATTURA_FORNITORE TIMESTAMP(7)
	private java.sql.Timestamp dtFatturaFornitore;

//  TIPORAPPORTO VARCHAR(10)
	private java.lang.String tiporapporto;

//  MATRICOLA DECIMAL(22,0)
	private java.lang.Long matricola;

//  DS_DOCUMENTO VARCHAR(4000)
	private java.lang.String dsDocumento;

//  IM_TOTALE DECIMAL(22,0)
	private BigDecimal imTotale;

//  PG_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long pgDocumento;

//  DT_INIZIO_COMP TIMESTAMP(7)
	private java.sql.Timestamp dtInizioComp;

//  DT_FINE_COMP TIMESTAMP(7)
	private java.sql.Timestamp dtFineComp;

//  CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cdElementoVoce;

//  DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String dsElementoVoce;

//  CDR VARCHAR(30)
	private java.lang.String cdr;

//  GAE VARCHAR(10)
	private java.lang.String gae;

//  DS_GAE VARCHAR(300)
	private java.lang.String dsGae;

//  DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtPagamento;

//  NR_MANDATO DECIMAL(10,0)
	private java.lang.Long nrMandato;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipodocumento]
	 **/
	public java.lang.String getTipodocumento() {
		return tipodocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipodocumento]
	 **/
	public void setTipodocumento(java.lang.String tipodocumento)  {
		this.tipodocumento=tipodocumento;
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
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [denominazione]
	 **/
	public java.lang.String getDenominazione() {
		return denominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [denominazione]
	 **/
	public void setDenominazione(java.lang.String denominazione)  {
		this.denominazione=denominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [partitaIva]
	 **/
	public java.lang.String getPartitaIva() {
		return partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partitaIva]
	 **/
	public void setPartitaIva(java.lang.String partitaIva)  {
		this.partitaIva=partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscale]
	 **/
	public java.lang.String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscale]
	 **/
	public void setCodiceFiscale(java.lang.String codiceFiscale)  {
		this.codiceFiscale=codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrFatturaFornitore]
	 **/
	public java.lang.String getNrFatturaFornitore() {
		return nrFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrFatturaFornitore]
	 **/
	public void setNrFatturaFornitore(java.lang.String nrFatturaFornitore)  {
		this.nrFatturaFornitore=nrFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFatturaFornitore]
	 **/
	public java.sql.Timestamp getDtFatturaFornitore() {
		return dtFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFatturaFornitore]
	 **/
	public void setDtFatturaFornitore(java.sql.Timestamp dtFatturaFornitore)  {
		this.dtFatturaFornitore=dtFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiporapporto]
	 **/
	public java.lang.String getTiporapporto() {
		return tiporapporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiporapporto]
	 **/
	public void setTiporapporto(java.lang.String tiporapporto)  {
		this.tiporapporto=tiporapporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [matricola]
	 **/
	public java.lang.Long getMatricola() {
		return matricola;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [matricola]
	 **/
	public void setMatricola(java.lang.Long matricola)  {
		this.matricola=matricola;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsDocumento]
	 **/
	public java.lang.String getDsDocumento() {
		return dsDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsDocumento]
	 **/
	public void setDsDocumento(java.lang.String dsDocumento)  {
		this.dsDocumento=dsDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotale]
	 **/
	public BigDecimal getImTotale() {
		return imTotale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotale]
	 **/
	public void setImTotale(BigDecimal imTotale)  {
		this.imTotale=imTotale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocumento]
	 **/
	public java.lang.Long getPgDocumento() {
		return pgDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocumento]
	 **/
	public void setPgDocumento(java.lang.Long pgDocumento)  {
		this.pgDocumento=pgDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtInizioComp]
	 **/
	public java.sql.Timestamp getDtInizioComp() {
		return dtInizioComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtInizioComp]
	 **/
	public void setDtInizioComp(java.sql.Timestamp dtInizioComp)  {
		this.dtInizioComp=dtInizioComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFineComp]
	 **/
	public java.sql.Timestamp getDtFineComp() {
		return dtFineComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFineComp]
	 **/
	public void setDtFineComp(java.sql.Timestamp dtFineComp)  {
		this.dtFineComp=dtFineComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdElementoVoce]
	 **/
	public java.lang.String getCdElementoVoce() {
		return cdElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdElementoVoce]
	 **/
	public void setCdElementoVoce(java.lang.String cdElementoVoce)  {
		this.cdElementoVoce=cdElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsElementoVoce]
	 **/
	public java.lang.String getDsElementoVoce() {
		return dsElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsElementoVoce]
	 **/
	public void setDsElementoVoce(java.lang.String dsElementoVoce)  {
		this.dsElementoVoce=dsElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdr]
	 **/
	public java.lang.String getCdr() {
		return cdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdr]
	 **/
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [gae]
	 **/
	public java.lang.String getGae() {
		return gae;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [gae]
	 **/
	public void setGae(java.lang.String gae)  {
		this.gae=gae;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsGae]
	 **/
	public java.lang.String getDsGae() {
		return dsGae;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsGae]
	 **/
	public void setDsGae(java.lang.String dsGae)  {
		this.dsGae=dsGae;
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
	 * Restituisce il valore di: [nrMandato]
	 **/
	public java.lang.Long getNrMandato() {
		return nrMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrMandato]
	 **/
	public void setNrMandato(java.lang.Long nrMandato)  {
		this.nrMandato=nrMandato;
	}
}