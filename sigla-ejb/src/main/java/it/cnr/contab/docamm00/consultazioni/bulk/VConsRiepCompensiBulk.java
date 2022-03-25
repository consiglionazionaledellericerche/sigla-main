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
 * Date 09/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.math.BigDecimal;

public class VConsRiepCompensiBulk extends OggettoBulk implements Persistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_CONS_RIEP_COMPENSI
	 **/
//  CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;

//  CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cdTerzo;

//  COGNOME VARCHAR(50)
	private java.lang.String cognome;

//  NOME VARCHAR(50)
	private java.lang.String nome;

//  DT_DA_COMPETENZA_COGE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtDaCompetenzaCoge;

//  DT_A_COMPETENZA_COGE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtACompetenzaCoge;

//  DT_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtTrasmissione;

//  DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtPagamento;

//  IM_LORDO DECIMAL(22,0)
	private java.math.BigDecimal imLordo;

//  IM_FISCALE DECIMAL(22,0)
	private java.math.BigDecimal imFiscale;

//  IRAP_ENTE DECIMAL(22,0)
	private java.math.BigDecimal irapEnte;

//  INPS_ENTE DECIMAL(22,0)
	private java.math.BigDecimal inpsEnte;

//  INAIL_ENTE DECIMAL(22,0)
	private java.math.BigDecimal inailEnte;

//  IRPEF DECIMAL(22,0)
	private java.math.BigDecimal irpef;

	public BigDecimal getCuneodl320() {
		return cuneodl320;
	}

	public void setCuneodl320(BigDecimal cuneodl320) {
		this.cuneodl320 = cuneodl320;
	}

	//  BONUSDL66 DECIMAL(22,0)
	private java.math.BigDecimal bonusdl66;
	private java.math.BigDecimal cuneodl320;

//  INPS_PERCIPIENTE DECIMAL(22,0)
	private java.math.BigDecimal inpsPercipiente;

//  INAIL_PERCIPIENTE DECIMAL(22,0)
	private java.math.BigDecimal inailPercipiente;

//  ADD_REG DECIMAL(22,0)
	private java.math.BigDecimal addReg;

//  ADD_COM DECIMAL(22,0)
	private java.math.BigDecimal addCom;

	// PG_COMPENSO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_compenso;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String ds_unita_organizzativa;

	// CD_TRATTAMENTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_trattamento;

	// DS_TI_TRATTAMENTO VARCHAR(100) NOT NULL (PK)
	private java.lang.String ds_ti_trattamento;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	private java.math.BigDecimal totCosto;

//  IMPONIBILE_IVA DECIMAL(22,0)
	private java.math.BigDecimal imponibileIva;

//  IMPORTO_IVA DECIMAL(22,0)
	private java.math.BigDecimal importoIva;

//  CASSA_RIVALSA DECIMAL(22,0)
	private java.math.BigDecimal cassaRivalsa;

//  INPGI_ENTE DECIMAL(22,0)
	private java.math.BigDecimal inpgiEnte;

//  ENPAPI_ENTE DECIMAL(22,0)
	private java.math.BigDecimal enpapiEnte;

//  INAIL_PERCIPIENTE DECIMAL(22,0)
	private java.math.BigDecimal inpgiPercipiente;

//  INAIL_PERCIPIENTE DECIMAL(22,0)
	private java.math.BigDecimal enpapiPercipiente;

	private java.lang.String daData;

	private java.lang.String aData;

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
	 * Restituisce il valore di: [cognome]
	 **/
	public java.lang.String getCognome() {
		return cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognome]
	 **/
	public void setCognome(java.lang.String cognome)  {
		this.cognome=cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nome]
	 **/
	public java.lang.String getNome() {
		return nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nome]
	 **/
	public void setNome(java.lang.String nome)  {
		this.nome=nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtDaCompetenzaCoge]
	 **/
	public java.sql.Timestamp getDtDaCompetenzaCoge() {
		return dtDaCompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtDaCompetenzaCoge]
	 **/
	public void setDtDaCompetenzaCoge(java.sql.Timestamp dtDaCompetenzaCoge)  {
		this.dtDaCompetenzaCoge=dtDaCompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtACompetenzaCoge]
	 **/
	public java.sql.Timestamp getDtACompetenzaCoge() {
		return dtACompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtACompetenzaCoge]
	 **/
	public void setDtACompetenzaCoge(java.sql.Timestamp dtACompetenzaCoge)  {
		this.dtACompetenzaCoge=dtACompetenzaCoge;
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
	 * Restituisce il valore di: [imLordo]
	 **/
	public java.math.BigDecimal getImLordo() {
		return imLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imLordo]
	 **/
	public void setImLordo(java.math.BigDecimal imLordo)  {
		this.imLordo=imLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imFiscale]
	 **/
	public java.math.BigDecimal getImFiscale() {
		return imFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imFiscale]
	 **/
	public void setImFiscale(java.math.BigDecimal imFiscale)  {
		this.imFiscale=imFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [irapEnte]
	 **/
	public java.math.BigDecimal getIrapEnte() {
		return irapEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [irapEnte]
	 **/
	public void setIrapEnte(java.math.BigDecimal irapEnte)  {
		this.irapEnte=irapEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inpsEnte]
	 **/
	public java.math.BigDecimal getInpsEnte() {
		return inpsEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inpsEnte]
	 **/
	public void setInpsEnte(java.math.BigDecimal inpsEnte)  {
		this.inpsEnte=inpsEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inailEnte]
	 **/
	public java.math.BigDecimal getInailEnte() {
		return inailEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inailEnte]
	 **/
	public void setInailEnte(java.math.BigDecimal inailEnte)  {
		this.inailEnte=inailEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [irpef]
	 **/
	public java.math.BigDecimal getIrpef() {
		return irpef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [irpef]
	 **/
	public void setIrpef(java.math.BigDecimal irpef)  {
		this.irpef=irpef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [bonusdl66]
	 **/
	public java.math.BigDecimal getBonusdl66() {
		return bonusdl66;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [bonusdl66]
	 **/
	public void setBonusdl66(java.math.BigDecimal bonusdl66)  {
		this.bonusdl66=bonusdl66;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inpsPercipiente]
	 **/
	public java.math.BigDecimal getInpsPercipiente() {
		return inpsPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inpsPercipiente]
	 **/
	public void setInpsPercipiente(java.math.BigDecimal inpsPercipiente)  {
		this.inpsPercipiente=inpsPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inailPercipiente]
	 **/
	public java.math.BigDecimal getInailPercipiente() {
		return inailPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inailPercipiente]
	 **/
	public void setInailPercipiente(java.math.BigDecimal inailPercipiente)  {
		this.inailPercipiente=inailPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [addReg]
	 **/
	public java.math.BigDecimal getAddReg() {
		return addReg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [addReg]
	 **/
	public void setAddReg(java.math.BigDecimal addReg)  {
		this.addReg=addReg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [addCom]
	 **/
	public java.math.BigDecimal getAddCom() {
		return addCom;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [addCom]
	 **/
	public void setAddCom(java.math.BigDecimal addCom)  {
		this.addCom=addCom;
	}

	public VConsRiepCompensiBulk() {
		super();
	}
	private Unita_organizzativaBulk uoForPrint;
	private TerzoBulk filtroSoggetto = null;
	private java.sql.Timestamp da_dt_pagamento = null;
	private java.sql.Timestamp a_dt_pagamento = null;
	private java.sql.Timestamp da_dt_competenza = null;
	private java.sql.Timestamp a_dt_competenza = null;

	public Boolean getDettagliata() {
		return dettagliata;
	}

	public void setDettagliata(Boolean dettagliata) {
		this.dettagliata = dettagliata;
	}

	private boolean isUOForPrintEnabled;
	private Boolean dettagliata;
	private Boolean groupTrattamento;
	public Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}
	public void setUoForPrint(Unita_organizzativaBulk uoForPrint) {
		this.uoForPrint = uoForPrint;
	}
	public TerzoBulk getFiltroSoggetto() {
		return filtroSoggetto;
	}
	public void setFiltroSoggetto(TerzoBulk filtroSoggetto) {
		this.filtroSoggetto = filtroSoggetto;
	}
	public java.sql.Timestamp getDa_dt_pagamento() {
		return da_dt_pagamento;
	}
	public void setDa_dt_pagamento(java.sql.Timestamp da_dt_pagamento) {
		this.da_dt_pagamento = da_dt_pagamento;
	}
	public java.sql.Timestamp getA_dt_pagamento() {
		return a_dt_pagamento;
	}
	public void setA_dt_pagamento(java.sql.Timestamp a_dt_pagamento) {
		this.a_dt_pagamento = a_dt_pagamento;
	}
	public java.sql.Timestamp getDa_dt_competenza() {
		return da_dt_competenza;
	}
	public void setDa_dt_competenza(java.sql.Timestamp da_dt_competenza) {
		this.da_dt_competenza = da_dt_competenza;
	}
	public java.sql.Timestamp getA_dt_competenza() {
		return a_dt_competenza;
	}
	public void setA_dt_competenza(java.sql.Timestamp a_dt_competenza) {
		this.a_dt_competenza = a_dt_competenza;
	}
	public boolean isUOForPrintEnabled() {
		return isUOForPrintEnabled;
	}

	public void setUOForPrintEnabled(boolean isUOForPrintEnabled) {
		this.isUOForPrintEnabled = isUOForPrintEnabled;
	}
	public Boolean almenoUnaDataSelezionata(){
		return 	((getDa_dt_competenza() != null && getA_dt_competenza() != null) ||
				(getDa_dt_pagamento() != null && getA_dt_pagamento() != null));
	}
	public boolean isROsoggetto() {
		return getFiltroSoggetto() == null ||
				getFiltroSoggetto().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	}

	public boolean isROUoForPrint() {
		return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
	}
	public java.lang.Long getPg_compenso() {
		return pg_compenso;
	}
	public void setPg_compenso(java.lang.Long pg_compenso) {
		this.pg_compenso = pg_compenso;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.math.BigDecimal getTotCosto() {
		return totCosto;
	}
	public void setTotCosto(java.math.BigDecimal totCosto) {
		this.totCosto = totCosto;
	}
	public java.lang.String getDs_unita_organizzativa() {
		return ds_unita_organizzativa;
	}
	public void setDs_unita_organizzativa(java.lang.String ds_unita_organizzativa) {
		this.ds_unita_organizzativa = ds_unita_organizzativa;
	}
	public java.lang.String getCd_trattamento() {
		return cd_trattamento;
	}
	public void setCd_trattamento(java.lang.String cd_trattamento) {
		this.cd_trattamento = cd_trattamento;
	}
	public java.lang.String getDs_ti_trattamento() {
		return ds_ti_trattamento;
	}
	public void setDs_ti_trattamento(java.lang.String ds_ti_trattamento) {
		this.ds_ti_trattamento = ds_ti_trattamento;
	}
	public java.math.BigDecimal getImponibileIva() {
		return imponibileIva;
	}
	public void setImponibileIva(java.math.BigDecimal imponibileIva) {
		this.imponibileIva = imponibileIva;
	}
	public java.math.BigDecimal getImportoIva() {
		return importoIva;
	}
	public void setImportoIva(java.math.BigDecimal importoIva) {
		this.importoIva = importoIva;
	}
	public java.math.BigDecimal getCassaRivalsa() {
		return cassaRivalsa;
	}
	public void setCassaRivalsa(java.math.BigDecimal cassaRivalsa) {
		this.cassaRivalsa = cassaRivalsa;
	}
	public java.math.BigDecimal getInpgiEnte() {
		return inpgiEnte;
	}
	public void setInpgiEnte(java.math.BigDecimal inpgiEnte) {
		this.inpgiEnte = inpgiEnte;
	}
	public java.math.BigDecimal getEnpapiEnte() {
		return enpapiEnte;
	}
	public void setEnpapiEnte(java.math.BigDecimal enpapiEnte) {
		this.enpapiEnte = enpapiEnte;
	}
	public java.math.BigDecimal getInpgiPercipiente() {
		return inpgiPercipiente;
	}
	public void setInpgiPercipiente(java.math.BigDecimal inpgiPercipiente) {
		this.inpgiPercipiente = inpgiPercipiente;
	}
	public java.math.BigDecimal getEnpapiPercipiente() {
		return enpapiPercipiente;
	}
	public void setEnpapiPercipiente(java.math.BigDecimal enpapiPercipiente) {
		this.enpapiPercipiente = enpapiPercipiente;
	}
	public java.lang.String getDaData() {
		return daData;
	}
	public void setDaData(java.lang.String daData) {
		this.daData = daData;
	}
	public java.lang.String getaData() {
		return aData;
	}
	public void setaData(java.lang.String aData) {
		this.aData = aData;
	}
	public Boolean getGroupTrattamento() {
		return groupTrattamento;
	}
	public void setGroupTrattamento(Boolean groupTrattamento) {
		this.groupTrattamento = groupTrattamento;
	}

}