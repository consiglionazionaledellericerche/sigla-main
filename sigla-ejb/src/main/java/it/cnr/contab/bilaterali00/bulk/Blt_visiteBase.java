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
 * Date 02/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import java.math.BigDecimal;

import it.cnr.jada.persistency.Keyed;
public class Blt_visiteBase extends Blt_visiteKey implements Keyed {
//  NUM_PROT_CANDIDATURA DECIMAL(10,0)
	private java.lang.Long numProtCandidatura;

//  DT_PROT_CANDIDATURA TIMESTAMP(7)
	private java.sql.Timestamp dtProtCandidatura;
	
//  DT_PAGAM_ANT TIMESTAMP(7)
	private java.sql.Timestamp dtPagamAnt;
	
//  DT_PAGAM_SALDO TIMESTAMP(7)
	private java.sql.Timestamp dtPagamSaldo;
	
//  NUM_PROT_AUTORIZZ_DIRETTORE DECIMAL(10,0)
	private java.lang.Long numProtAutorizzDirettore;

//  DT_PROT_AUTORIZZ_DIRETTORE TIMESTAMP(7)
	private java.sql.Timestamp dtProtAutorizzDirettore;

//  DT_INI_VISITA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtIniVisita;

//  DT_FIN_VISITA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtFinVisita;

	public java.sql.Timestamp getDtPagamAnt() {
		return dtPagamAnt;
	}
	public void setDtPagamAnt(java.sql.Timestamp dtPagamAnt) {
		this.dtPagamAnt = dtPagamAnt;
	}
	public java.sql.Timestamp getDtPagamSaldo() {
		return dtPagamSaldo;
	}
	public void setDtPagamSaldo(java.sql.Timestamp dtPagamSaldo) {
		this.dtPagamSaldo = dtPagamSaldo;
	}
	private java.lang.String cdAccordoPrg;

	private java.lang.String cdProgettoPrg;
	
	private java.lang.Long pgRecordPrg;

//  NUM_PROT_DISP_FIN DECIMAL(10,0)
	private java.lang.Long numProtDispFin;

//  DT_PROT_DISP_FIN TIMESTAMP(7)
	private java.sql.Timestamp dtProtDispFin;

//  NUM_PROT_TRASMISS_CANDIDATURA DECIMAL(10,0)
	private java.lang.Long numProtTrasmissCandidatura;

//  DT_PROT_TRASMISS_CANDIDATURA TIMESTAMP(7)
	private java.sql.Timestamp dtProtTrasmissCandidatura;

//  NUM_PROT_ACCETT_DISP_FIN DECIMAL(10,0)
	private java.lang.Long numProtAccettDispFin;

//  DT_PROT_ACCETT_DISP_FIN TIMESTAMP(7)
	private java.sql.Timestamp dtProtAccettDispFin;

//  NUM_PROT_ACCETT_ENTE_STR DECIMAL(10,0)
	private java.lang.Long numProtAccettEnteStr;

//  DT_PROT_ACCETT_ENTE_STR TIMESTAMP(7)
	private java.sql.Timestamp dtProtAccettEnteStr;

//  NUM_PROT_AUTORIZ_PARTENZA DECIMAL(10,0)
	private java.lang.Long numProtAutorizPartenza;

//  DT_PROT_AUTORIZ_PARTENZA TIMESTAMP(7)
	private java.sql.Timestamp dtProtAutorizPartenza;

//  CD_CDS_OBBLIG VARCHAR(30)
	private java.lang.String cdCdsObblig;

//  ESERCIZIO_OBBLIG DECIMAL(4,0)
	private java.lang.Integer esercizioObblig;

//  ESERCIZIO_ORI_OBBLIG DECIMAL(4,0)
	private java.lang.Integer esercizioOriObblig;

//  PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pgObbligazione;
 
//  PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pgObbligazioneScadenzario;

//  NUM_PROT_ATTESTATO_SOGG DECIMAL(10,0)
	private java.lang.Long numProtAttestatoSogg;
 
//  DT_PROT_ATTESTATO_SOGG TIMESTAMP(7)
	private java.sql.Timestamp dtProtAttestatoSogg;

//  NUM_PROT_NOTA_ADDEBITO DECIMAL(10,0)
	private java.lang.Long numProtNotaAddebito;

//  DT_PROT_NOTA_ADDEBITO TIMESTAMP(7)
	private java.sql.Timestamp dtProtNotaAddebito;

//  NUM_PROT_NOTA_ADDEBITO_ANT DECIMAL(10,0)
	private java.lang.Long numProtNotaAddebitoAnt;

//  DT_PROT_NOTA_ADDEBITO_ANT TIMESTAMP(7)
	private java.sql.Timestamp dtProtNotaAddebitoAnt;

//  NUM_PROT_PROVV_IMPEGNO DECIMAL(10,0)
	private java.lang.Long numProtProvvImpegno;

//  DT_PROT_PROVV_IMPEGNO TIMESTAMP(7)
	private java.sql.Timestamp dtProtProvvImpegno;

//  NUM_PROT_RIMB_SPESE DECIMAL(10,0)
	private java.lang.Long numProtRimbSpese;
 
//  DT_PROT_RIMB_SPESE TIMESTAMP(7)
	private java.sql.Timestamp dtProtRimbSpese;
 
//  NUM_PROT_PROVV_PAGAM_ANT DECIMAL(10,0)
	private java.lang.Long numProtProvvPagamAnt;
 
//  DT_PROT_PROVV_PAGAM_ANT TIMESTAMP(7)
	private java.sql.Timestamp dtProtProvvPagamAnt;

//  NUM_PROT_PROVV_PAGAM DECIMAL(10,0)
	private java.lang.Long numProtProvvPagam;
 
//  DT_PROT_PROVV_PAGAM TIMESTAMP(7)
	private java.sql.Timestamp dtProtProvvPagam;
 
//  LUOGO_VISITA VARCHAR(200)
	private java.lang.String luogoVisita;

//  NUM_PROT_ATTRIB_INCARICO DECIMAL(10,0)
	private java.lang.Long numProtAttribIncarico;

//  DT_PROT_ATTRIB_INCARICO TIMESTAMP(7)
	private java.sql.Timestamp dtProtAttribIncarico;

//  NUM_PROT_CONTRATTO DECIMAL(10,0)
	private java.lang.Long numProtContratto;

//  DATA_PROT_CONTRATTO TIMESTAMP(7)
	private java.sql.Timestamp dataProtContratto;

//  DT_STIPULA_CONTRATTO TIMESTAMP(7)
	private java.sql.Timestamp dtStipulaContratto;
	
//  NUM_PROT_ACCETT_CONVENZ DECIMAL(10,0)
	private java.lang.Long numProtAccettConvenz;

//  DT_PROT_ACCETT_CONVENZ TIMESTAMP(7)
	private java.sql.Timestamp dtProtAccettConvenz;

//  ESERCIZIO_REPERTORIO DECIMAL(4,0)
	private java.lang.Integer esercizioRepertorio;

//  PG_REPERTORIO DECIMAL(10,0)
	private java.lang.Long pgRepertorio;

//  NUM_PROT_COMUNIC_CAMBIO_DATE DECIMAL(10,0)
	private java.lang.Long numProtComunicCambioDate;
 
//  DT_PROT_COMUNIC_CAMBIO_DATE TIMESTAMP(7)
	private java.sql.Timestamp dtProtComunicCambioDate;
 
//  DT_NEW_INIZIO_VISITA TIMESTAMP(7)
	private java.sql.Timestamp dtNewInizioVisita;

//  DT_NEW_FINE_VISITA TIMESTAMP(7)
	private java.sql.Timestamp dtNewFineVisita;

//  NUM_PROT_PROVV_DIFF_VISITA DECIMAL(10,0)
	private java.lang.Long numProtProvvDiffVisita;

//  DT_PROT_PROVV_DIFF_VISITA TIMESTAMP(7)
	private java.sql.Timestamp dtProtProvvDiffVisita;
 
//  NUM_PROT_MODIFICA_INCARICO DECIMAL(10,0)
	private java.lang.Long numProtModificaIncarico;

//  DT_PROT_MODIFICA_INCARICO TIMESTAMP(7)
	private java.sql.Timestamp dtProtModificaIncarico;
 
//  NUM_PROT_RINUNCIA_VISITA DECIMAL(10,0)
	private java.lang.Long numProtRinunciaVisita;

//  DT_PROT_RINUNCIA_VISITA TIMESTAMP(7)
	private java.sql.Timestamp dtProtRinunciaVisita;

//  NUM_PROT_ANNULLA_PROVV DECIMAL(10,0)
	private java.lang.Long numProtAnnullaProvv;

//  DT_PROT_ANNULLA_PROVV TIMESTAMP(7)
	private java.sql.Timestamp dtProtAnnullaProvv;

//	FL_AUTORIZZ_DIRETTORE   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flAutorizzDirettore;
	
//	FL_ACCETTAZIONE_CONVENZIONE   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flAccettazioneConvenzione;

//	FL_STAMPATO_DOC_CANDIDATURA   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoDocCandidatura;

//	FL_STAMPATO_AUTORIZ_PARTENZA   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoAutorizPartenza;

//	FL_STAMPATO_PROVV_PAGAM_ANT   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoProvvPagamAnt;

//	FL_STAMPATO_PROVV_PAGAMENTO   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoProvvPagamento;

//	FL_STAMPATO_PROVV_IMPEGNO   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoProvvImpegno;

//	FL_PAGAMENTO_ENTE   CHAR(1 BYTE),
	private java.lang.Boolean flPagamentoEnte;
	
//	FL_BREVE_PERIODO   CHAR(1 BYTE),
	private java.lang.Boolean flBrevePeriodo;

//	FL_STAMPATO_ANN_PROVV_IMPEGNO   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoAnnProvvImpegno;

//	FL_STAMPATO_MODELLO_CONTRATTO   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoModelloContratto;

//	FL_STAMPATO_NOTA_ADDEBITO   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flStampatoNotaAddebito;

//	FL_PAGAMENTO_FINE_VISITA   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flPagamentoFineVisita;

//	FL_PAGAMENTO_CON_BONIFICO   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flPagamentoConBonifico;

//	FL_VISITA_ANNULLATA   CHAR(1 BYTE)  NOT NULL,
	private java.lang.Boolean flVisitaAnnullata;

//  IM_RIMB_SPESE_ANT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imRimbSpeseAnt;

//  CD_TERZO_MODPAG_ANT DECIMAL(8,0)
	private java.lang.Integer cdTerzoModpagAnt;

//  CD_MODALITA_PAG_ANT VARCHAR(10)
	private java.lang.String cdModalitaPagAnt;

//  CD_TERZO_BANCA_ANT DECIMAL(8,0)
	private java.lang.Integer cdTerzoBancaAnt;

//  PG_BANCA_ANT DECIMAL(10,0)
	private java.lang.Long pgBancaAnt;

//  IM_RIMB_SPESE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imRimbSpese;

//  CD_TERZO_MODPAG DECIMAL(8,0)
	private java.lang.Integer cdTerzoModpag;

//  CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String cdModalitaPag;

//  CD_TERZO_BANCA DECIMAL(8,0)
	private java.lang.Integer cdTerzoBanca;

//  PG_BANCA DECIMAL(10,0)
	private java.lang.Long pgBanca;
	
//  IM_RIMB_PREVISTO DECIMAL(15,2) NOT NULL
	private BigDecimal imRimbPrevisto;
	
//	PRC_ONERI_FISCALI   NUMBER(4,2)       NOT NULL,
	private java.math.BigDecimal prc_oneri_fiscali;

//	PRC_ONERI_CONTRIBUTIVI   NUMBER(4,2)       NOT NULL,
	private java.math.BigDecimal prc_oneri_contributivi;
	
//  DT_INI_VISITA_EFFETTIVA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtIniVisitaEffettiva;

//  DT_FIN_VISITA_EFFETTIVA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtFinVisitaEffettiva;
	
//	CD_TERZO_ENTE              NUMBER(8 BYTE)         NULL,
	private java.lang.Integer cdTerzoEnte;
	
	
public java.lang.Boolean getFlBrevePeriodo() {
		return flBrevePeriodo;
	}
	public void setFlBrevePeriodo(java.lang.Boolean flBrevePeriodo) {
		this.flBrevePeriodo = flBrevePeriodo;
	}
public java.lang.Integer getCdTerzoEnte() {
		return cdTerzoEnte;
	}
	public void setCdTerzoEnte(java.lang.Integer cdTerzoEnte) {
		this.cdTerzoEnte = cdTerzoEnte;
	}
/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_VISITE
	 **/
	public Blt_visiteBase() {
		super();
	}
	public java.lang.Boolean getFlPagamentoEnte() {
	return flPagamentoEnte;
}
public void setFlPagamentoEnte(java.lang.Boolean flPagamentoEnte) {
	this.flPagamentoEnte = flPagamentoEnte;
}
	public Blt_visiteBase(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo, java.lang.Long pgAutorizzazione, java.lang.Long pgVisita) {
		super(cdAccordo, cdProgetto, cdTerzo, pgAutorizzazione, pgVisita);
	}
	public java.lang.String getCdAccordoPrg() {
		return cdAccordoPrg;
	}
	public void setCdAccordoPrg(java.lang.String cdAccordoPrg) {
		this.cdAccordoPrg = cdAccordoPrg;
	}
	public java.lang.String getCdProgettoPrg() {
		return cdProgettoPrg;
	}
	public void setCdProgettoPrg(java.lang.String cdProgettoPrg) {
		this.cdProgettoPrg = cdProgettoPrg;
	}
	public java.lang.Long getPgRecordPrg() {
		return pgRecordPrg;
	}
	public void setPgRecordPrg(java.lang.Long pgRecordPrg) {
		this.pgRecordPrg = pgRecordPrg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtIniVisita]
	 **/
	public java.sql.Timestamp getDtIniVisita() {
		return dtIniVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtIniVisita]
	 **/
	public void setDtIniVisita(java.sql.Timestamp dtIniVisita)  {
		this.dtIniVisita=dtIniVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFinVisita]
	 **/
	public java.sql.Timestamp getDtFinVisita() {
		return dtFinVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFinVisita]
	 **/
	public void setDtFinVisita(java.sql.Timestamp dtFinVisita)  {
		this.dtFinVisita=dtFinVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObblig]
	 **/
	public java.lang.String getCdCdsObblig() {
		return cdCdsObblig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObblig]
	 **/
	public void setCdCdsObblig(java.lang.String cdCdsObblig)  {
		this.cdCdsObblig=cdCdsObblig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObblig]
	 **/
	public java.lang.Integer getEsercizioObblig() {
		return esercizioObblig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObblig]
	 **/
	public void setEsercizioObblig(java.lang.Integer esercizioObblig)  {
		this.esercizioObblig=esercizioObblig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOriObblig]
	 **/
	public java.lang.Integer getEsercizioOriObblig() {
		return esercizioOriObblig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOriObblig]
	 **/
	public void setEsercizioOriObblig(java.lang.Integer esercizioOriObblig)  {
		this.esercizioOriObblig=esercizioOriObblig;
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
	 * Restituisce il valore di: [numProtCandidatura]
	 **/
	public java.lang.Long getNumProtCandidatura() {
		return numProtCandidatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtCandidatura]
	 **/
	public void setNumProtCandidatura(java.lang.Long numProtCandidatura)  {
		this.numProtCandidatura=numProtCandidatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtCandidatura]
	 **/
	public java.sql.Timestamp getDtProtCandidatura() {
		return dtProtCandidatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtCandidatura]
	 **/
	public void setDtProtCandidatura(java.sql.Timestamp dtProtCandidatura)  {
		this.dtProtCandidatura=dtProtCandidatura;
	}
		
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtAutorizzDirettore]
	 **/
	public java.lang.Long getNumProtAutorizzDirettore() {
		return numProtAutorizzDirettore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtAutorizzDirettore]
	 **/
	public void setNumProtAutorizzDirettore(java.lang.Long numProtAutorizzDirettore)  {
		this.numProtAutorizzDirettore=numProtAutorizzDirettore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtAutorizzDirettore]
	 **/
	public java.sql.Timestamp getDtProtAutorizzDirettore() {
		return dtProtAutorizzDirettore;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtAutorizzDirettore]
	 **/
	public void setDtProtAutorizzDirettore(java.sql.Timestamp dtProtAutorizzDirettore)  {
		this.dtProtAutorizzDirettore=dtProtAutorizzDirettore;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtRimbSpese]
	 **/
	public java.lang.Long getNumProtRimbSpese() {
		return numProtRimbSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtRimbSpese]
	 **/
	public void setNumProtRimbSpese(java.lang.Long numProtRimbSpese)  {
		this.numProtRimbSpese=numProtRimbSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtRimbSpese]
	 **/
	public java.sql.Timestamp getDtProtRimbSpese() {
		return dtProtRimbSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtRimbSpese]
	 **/
	public void setDtProtRimbSpese(java.sql.Timestamp dtProtRimbSpese)  {
		this.dtProtRimbSpese=dtProtRimbSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtProvvPagamAnt]
	 **/
	public java.lang.Long getNumProtProvvPagamAnt() {
		return numProtProvvPagamAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtProvvPagamAnt]
	 **/
	public void setNumProtProvvPagamAnt(java.lang.Long numProtProvvPagamAnt)  {
		this.numProtProvvPagamAnt=numProtProvvPagamAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtProvvPagamAnt]
	 **/
	public java.sql.Timestamp getDtProtProvvPagamAnt() {
		return dtProtProvvPagamAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtProvvPagamAnt]
	 **/
	public void setDtProtProvvPagamAnt(java.sql.Timestamp dtProtProvvPagamAnt)  {
		this.dtProtProvvPagamAnt=dtProtProvvPagamAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtProvvPagam]
	 **/
	public java.lang.Long getNumProtProvvPagam() {
		return numProtProvvPagam;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtProvvPagam]
	 **/
	public void setNumProtProvvPagam(java.lang.Long numProtProvvPagam)  {
		this.numProtProvvPagam=numProtProvvPagam;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtProvvPagam]
	 **/
	public java.sql.Timestamp getDtProtProvvPagam() {
		return dtProtProvvPagam;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtProvvPagam]
	 **/
	public void setDtProtProvvPagam(java.sql.Timestamp dtProtProvvPagam)  {
		this.dtProtProvvPagam=dtProtProvvPagam;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtComunicCambioDate]
	 **/
	public java.lang.Long getNumProtComunicCambioDate() {
		return numProtComunicCambioDate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtComunicCambioDate]
	 **/
	public void setNumProtComunicCambioDate(java.lang.Long numProtComunicCambioDate)  {
		this.numProtComunicCambioDate=numProtComunicCambioDate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtComunicCambioDate]
	 **/
	public java.sql.Timestamp getDtProtComunicCambioDate() {
		return dtProtComunicCambioDate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtComunicCambioDate]
	 **/
	public void setDtProtComunicCambioDate(java.sql.Timestamp dtProtComunicCambioDate)  {
		this.dtProtComunicCambioDate=dtProtComunicCambioDate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtAccettEnteStr]
	 **/
	public java.lang.Long getNumProtAccettEnteStr() {
		return numProtAccettEnteStr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtAccettEnteStr]
	 **/
	public void setNumProtAccettEnteStr(java.lang.Long numProtAccettEnteStr)  {
		this.numProtAccettEnteStr=numProtAccettEnteStr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtAccettEnteStr]
	 **/
	public java.sql.Timestamp getDtProtAccettEnteStr() {
		return dtProtAccettEnteStr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtAccettEnteStr]
	 **/
	public void setDtProtAccettEnteStr(java.sql.Timestamp dtProtAccettEnteStr)  {
		this.dtProtAccettEnteStr=dtProtAccettEnteStr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [luogoVisita]
	 **/
	public java.lang.String getLuogoVisita() {
		return luogoVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [luogoVisita]
	 **/
	public void setLuogoVisita(java.lang.String luogoVisita)  {
		this.luogoVisita=luogoVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtStipulaContratto]
	 **/
	public java.sql.Timestamp getDtStipulaContratto() {
		return dtStipulaContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtStipulaContratto]
	 **/
	public void setDtStipulaContratto(java.sql.Timestamp dtStipulaContratto)  {
		this.dtStipulaContratto=dtStipulaContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtAttribIncarico]
	 **/
	public java.lang.Long getNumProtAttribIncarico() {
		return numProtAttribIncarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtAttribIncarico]
	 **/
	public void setNumProtAttribIncarico(java.lang.Long numProtAttribIncarico)  {
		this.numProtAttribIncarico=numProtAttribIncarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtAttribIncarico]
	 **/
	public java.sql.Timestamp getDtProtAttribIncarico() {
		return dtProtAttribIncarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtAttribIncarico]
	 **/
	public void setDtProtAttribIncarico(java.sql.Timestamp dtProtAttribIncarico)  {
		this.dtProtAttribIncarico=dtProtAttribIncarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtContratto]
	 **/
	public java.lang.Long getNumProtContratto() {
		return numProtContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtContratto]
	 **/
	public void setNumProtContratto(java.lang.Long numProtContratto)  {
		this.numProtContratto=numProtContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataProtContratto]
	 **/
	public java.sql.Timestamp getDataProtContratto() {
		return dataProtContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataProtContratto]
	 **/
	public void setDataProtContratto(java.sql.Timestamp dataProtContratto)  {
		this.dataProtContratto=dataProtContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtNewInizioVisita]
	 **/
	public java.sql.Timestamp getDtNewInizioVisita() {
		return dtNewInizioVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtNewInizioVisita]
	 **/
	public void setDtNewInizioVisita(java.sql.Timestamp dtNewInizioVisita)  {
		this.dtNewInizioVisita=dtNewInizioVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtNewFineVisita]
	 **/
	public java.sql.Timestamp getDtNewFineVisita() {
		return dtNewFineVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtNewFineVisita]
	 **/
	public void setDtNewFineVisita(java.sql.Timestamp dtNewFineVisita)  {
		this.dtNewFineVisita=dtNewFineVisita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtAccettConvenz]
	 **/
	public java.lang.Long getNumProtAccettConvenz() {
		return numProtAccettConvenz;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtAccettConvenz]
	 **/
	public void setNumProtAccettConvenz(java.lang.Long numProtAccettConvenz)  {
		this.numProtAccettConvenz=numProtAccettConvenz;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtAccettConvenz]
	 **/
	public java.sql.Timestamp getDtProtAccettConvenz() {
		return dtProtAccettConvenz;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtAccettConvenz]
	 **/
	public void setDtProtAccettConvenz(java.sql.Timestamp dtProtAccettConvenz)  {
		this.dtProtAccettConvenz=dtProtAccettConvenz;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numProtNotaAddebito]
	 **/
	public java.lang.Long getNumProtNotaAddebito() {
		return numProtNotaAddebito;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numProtNotaAddebito]
	 **/
	public void setNumProtNotaAddebito(java.lang.Long numProtNotaAddebito)  {
		this.numProtNotaAddebito=numProtNotaAddebito;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProtNotaAddebito]
	 **/
	public java.sql.Timestamp getDtProtNotaAddebito() {
		return dtProtNotaAddebito;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProtNotaAddebito]
	 **/
	public void setDtProtNotaAddebito(java.sql.Timestamp dtProtNotaAddebito)  {
		this.dtProtNotaAddebito=dtProtNotaAddebito;
	}
	public java.lang.Long getNumProtAccettDispFin() {
		return numProtAccettDispFin;
	}
	public void setNumProtAccettDispFin(java.lang.Long numProtAccettDispFin) {
		this.numProtAccettDispFin = numProtAccettDispFin;
	}
	public java.sql.Timestamp getDtProtAccettDispFin() {
		return dtProtAccettDispFin;
	}
	public void setDtProtAccettDispFin(java.sql.Timestamp dtProtAccettDispFin) {
		this.dtProtAccettDispFin = dtProtAccettDispFin;
	}
	public java.lang.Boolean getFlStampatoDocCandidatura() {
		return flStampatoDocCandidatura;
	}
	public void setFlStampatoDocCandidatura(java.lang.Boolean flStampatoDocCandidatura) {
		this.flStampatoDocCandidatura = flStampatoDocCandidatura;
	}
	public java.lang.Long getNumProtDispFin() {
		return numProtDispFin;
	}
	public void setNumProtDispFin(java.lang.Long numProtDispFin) {
		this.numProtDispFin = numProtDispFin;
	}
	public java.sql.Timestamp getDtProtDispFin() {
		return dtProtDispFin;
	}
	public void setDtProtDispFin(java.sql.Timestamp dtProtDispFin) {
		this.dtProtDispFin = dtProtDispFin;
	}
	public java.lang.Long getNumProtTrasmissCandidatura() {
		return numProtTrasmissCandidatura;
	}
	public void setNumProtTrasmissCandidatura(java.lang.Long numProtTrasmissCandidatura) {
		this.numProtTrasmissCandidatura = numProtTrasmissCandidatura;
	}
	public java.sql.Timestamp getDtProtTrasmissCandidatura() {
		return dtProtTrasmissCandidatura;
	}
	public void setDtProtTrasmissCandidatura(java.sql.Timestamp dtProtTrasmissCandidatura) {
		this.dtProtTrasmissCandidatura = dtProtTrasmissCandidatura;
	}
	public java.lang.Long getNumProtAutorizPartenza() {
		return numProtAutorizPartenza;
	}
	public void setNumProtAutorizPartenza(java.lang.Long numProtAutorizPartenza) {
		this.numProtAutorizPartenza = numProtAutorizPartenza;
	}
	public java.sql.Timestamp getDtProtAutorizPartenza() {
		return dtProtAutorizPartenza;
	}
	public void setDtProtAutorizPartenza(java.sql.Timestamp dtProtAutorizPartenza) {
		this.dtProtAutorizPartenza = dtProtAutorizPartenza;
	}
	public java.lang.Long getNumProtAttestatoSogg() {
		return numProtAttestatoSogg;
	}
	public void setNumProtAttestatoSogg(java.lang.Long numProtAttestatoSogg) {
		this.numProtAttestatoSogg = numProtAttestatoSogg;
	}
	public java.sql.Timestamp getDtProtAttestatoSogg() {
		return dtProtAttestatoSogg;
	}
	public void setDtProtAttestatoSogg(java.sql.Timestamp dtProtAttestatoSogg) {
		this.dtProtAttestatoSogg = dtProtAttestatoSogg;
	}
	public java.lang.Long getNumProtProvvImpegno() {
		return numProtProvvImpegno;
	}
	public void setNumProtProvvImpegno(java.lang.Long numProtProvvImpegno) {
		this.numProtProvvImpegno = numProtProvvImpegno;
	}
	public java.sql.Timestamp getDtProtProvvImpegno() {
		return dtProtProvvImpegno;
	}
	public void setDtProtProvvImpegno(java.sql.Timestamp dtProtProvvImpegno) {
		this.dtProtProvvImpegno = dtProtProvvImpegno;
	}
	public java.lang.Integer getEsercizioRepertorio() {
		return esercizioRepertorio;
	}
	public void setEsercizioRepertorio(java.lang.Integer esercizioRepertorio) {
		this.esercizioRepertorio = esercizioRepertorio;
	}
	public java.lang.Long getPgRepertorio() {
		return pgRepertorio;
	}
	public void setPgRepertorio(java.lang.Long pgRepertorio) {
		this.pgRepertorio = pgRepertorio;
	}
	public java.lang.Long getNumProtProvvDiffVisita() {
		return numProtProvvDiffVisita;
	}
	public void setNumProtProvvDiffVisita(java.lang.Long numProtProvvDiffVisita) {
		this.numProtProvvDiffVisita = numProtProvvDiffVisita;
	}
	public java.sql.Timestamp getDtProtProvvDiffVisita() {
		return dtProtProvvDiffVisita;
	}
	public void setDtProtProvvDiffVisita(java.sql.Timestamp dtProtProvvDiffVisita) {
		this.dtProtProvvDiffVisita = dtProtProvvDiffVisita;
	}
	public java.lang.Long getNumProtModificaIncarico() {
		return numProtModificaIncarico;
	}
	public void setNumProtModificaIncarico(java.lang.Long numProtModificaIncarico) {
		this.numProtModificaIncarico = numProtModificaIncarico;
	}
	public java.sql.Timestamp getDtProtModificaIncarico() {
		return dtProtModificaIncarico;
	}
	public void setDtProtModificaIncarico(java.sql.Timestamp dtProtModificaIncarico) {
		this.dtProtModificaIncarico = dtProtModificaIncarico;
	}
	public java.lang.Long getNumProtRinunciaVisita() {
		return numProtRinunciaVisita;
	}
	public void setNumProtRinunciaVisita(java.lang.Long numProtRinunciaVisita) {
		this.numProtRinunciaVisita = numProtRinunciaVisita;
	}
	public java.sql.Timestamp getDtProtRinunciaVisita() {
		return dtProtRinunciaVisita;
	}
	public void setDtProtRinunciaVisita(java.sql.Timestamp dtProtRinunciaVisita) {
		this.dtProtRinunciaVisita = dtProtRinunciaVisita;
	}
	public java.lang.Long getNumProtAnnullaProvv() {
		return numProtAnnullaProvv;
	}
	public void setNumProtAnnullaProvv(java.lang.Long numProtAnnullaProvv) {
		this.numProtAnnullaProvv = numProtAnnullaProvv;
	}
	public java.sql.Timestamp getDtProtAnnullaProvv() {
		return dtProtAnnullaProvv;
	}
	public void setDtProtAnnullaProvv(java.sql.Timestamp dtProtAnnullaProvv) {
		this.dtProtAnnullaProvv = dtProtAnnullaProvv;
	}
	public java.lang.Boolean getFlAccettazioneConvenzione() {
		return flAccettazioneConvenzione;
	}
	public java.lang.Boolean getFlAutorizzDirettore() {
		return flAutorizzDirettore;
	}
	public void setFlAutorizzDirettore(java.lang.Boolean flAutorizzDirettore) {
		this.flAutorizzDirettore = flAutorizzDirettore;
	}
	public void setFlAccettazioneConvenzione(java.lang.Boolean flAccettazioneConvenzione) {
		this.flAccettazioneConvenzione = flAccettazioneConvenzione;
	}
	public java.lang.Boolean getFlStampatoAutorizPartenza() {
		return flStampatoAutorizPartenza;
	}
	public void setFlStampatoAutorizPartenza(java.lang.Boolean flStampatoAutorizPartenza) {
		this.flStampatoAutorizPartenza = flStampatoAutorizPartenza;
	}
	public java.lang.Boolean getFlStampatoProvvPagamAnt() {
		return flStampatoProvvPagamAnt;
	}
	public void setFlStampatoProvvPagamAnt(java.lang.Boolean flStampatoProvvPagamAnt) {
		this.flStampatoProvvPagamAnt = flStampatoProvvPagamAnt;
	}
	public java.lang.Boolean getFlStampatoProvvPagamento() {
		return flStampatoProvvPagamento;
	}
	public void setFlStampatoProvvPagamento(java.lang.Boolean flStampatoProvvPagamento) {
		this.flStampatoProvvPagamento = flStampatoProvvPagamento;
	}
	public java.lang.Boolean getFlStampatoProvvImpegno() {
		return flStampatoProvvImpegno;
	}
	public void setFlStampatoProvvImpegno(java.lang.Boolean flStampatoProvvImpegno) {
		this.flStampatoProvvImpegno = flStampatoProvvImpegno;
	}
	public java.lang.Boolean getFlStampatoAnnProvvImpegno() {
		return flStampatoAnnProvvImpegno;
	}
	public void setFlStampatoAnnProvvImpegno(java.lang.Boolean flStampatoAnnProvvImpegno) {
		this.flStampatoAnnProvvImpegno = flStampatoAnnProvvImpegno;
	}
	public java.lang.Boolean getFlStampatoModelloContratto() {
		return flStampatoModelloContratto;
	}
	public void setFlStampatoModelloContratto(java.lang.Boolean flStampatoModelloContratto) {
		this.flStampatoModelloContratto = flStampatoModelloContratto;
	}
	public java.lang.Boolean getFlVisitaAnnullata() {
		return flVisitaAnnullata;
	}
	public void setFlVisitaAnnullata(java.lang.Boolean flVisitaAnnullata) {
		this.flVisitaAnnullata = flVisitaAnnullata;
	}
	public java.math.BigDecimal getImRimbSpeseAnt() {
		return imRimbSpeseAnt;
	}
	public void setImRimbSpeseAnt(java.math.BigDecimal imRimbSpeseAnt) {
		this.imRimbSpeseAnt = imRimbSpeseAnt;
	}
	public java.lang.Integer getCdTerzoModpagAnt() {
		return cdTerzoModpagAnt;
	}
	public void setCdTerzoModpagAnt(java.lang.Integer cdTerzoModpagAnt) {
		this.cdTerzoModpagAnt = cdTerzoModpagAnt;
	}
	public java.lang.String getCdModalitaPagAnt() {
		return cdModalitaPagAnt;
	}
	public void setCdModalitaPagAnt(java.lang.String cdModalitaPagAnt) {
		this.cdModalitaPagAnt = cdModalitaPagAnt;
	}
	public java.lang.Integer getCdTerzoBancaAnt() {
		return cdTerzoBancaAnt;
	}
	public void setCdTerzoBancaAnt(java.lang.Integer cdTerzoBancaAnt) {
		this.cdTerzoBancaAnt = cdTerzoBancaAnt;
	}
	public java.lang.Long getPgBancaAnt() {
		return pgBancaAnt;
	}
	public void setPgBancaAnt(java.lang.Long pgBancaAnt) {
		this.pgBancaAnt = pgBancaAnt;
	}
	public java.math.BigDecimal getImRimbSpese() {
		return imRimbSpese;
	}
	public void setImRimbSpese(java.math.BigDecimal imRimbSpese) {
		this.imRimbSpese = imRimbSpese;
	}
	public java.lang.Integer getCdTerzoModpag() {
		return cdTerzoModpag;
	}
	public void setCdTerzoModpag(java.lang.Integer cdTerzoModpag) {
		this.cdTerzoModpag = cdTerzoModpag;
	}
	public java.lang.String getCdModalitaPag() {
		return cdModalitaPag;
	}
	public void setCdModalitaPag(java.lang.String cdModalitaPag) {
		this.cdModalitaPag = cdModalitaPag;
	}
	public java.lang.Integer getCdTerzoBanca() {
		return cdTerzoBanca;
	}
	public void setCdTerzoBanca(java.lang.Integer cdTerzoBanca) {
		this.cdTerzoBanca = cdTerzoBanca;
	}
	public java.lang.Long getPgBanca() {
		return pgBanca;
	}
	public void setPgBanca(java.lang.Long pgBanca) {
		this.pgBanca = pgBanca;
	}
	public BigDecimal getImRimbPrevisto() {
		return imRimbPrevisto;
	}
	public void setImRimbPrevisto(BigDecimal imRimbPrevisto) {
		this.imRimbPrevisto = imRimbPrevisto;
	}
	public java.math.BigDecimal getPrc_oneri_fiscali() {
		return prc_oneri_fiscali;
	}
	public void setPrc_oneri_fiscali(java.math.BigDecimal prc_oneri_fiscali) {
		this.prc_oneri_fiscali = prc_oneri_fiscali;
	}
	public java.math.BigDecimal getPrc_oneri_contributivi() {
		return prc_oneri_contributivi;
	}
	public void setPrc_oneri_contributivi(java.math.BigDecimal prc_oneri_contributivi) {
		this.prc_oneri_contributivi = prc_oneri_contributivi;
	}
	public java.sql.Timestamp getDtIniVisitaEffettiva() {
		return dtIniVisitaEffettiva;
	}
	public void setDtIniVisitaEffettiva(java.sql.Timestamp dtIniVisitaEffettiva) {
		this.dtIniVisitaEffettiva = dtIniVisitaEffettiva;
	}
	public java.sql.Timestamp getDtFinVisitaEffettiva() {
		return dtFinVisitaEffettiva;
	}
	public void setDtFinVisitaEffettiva(java.sql.Timestamp dtFinVisitaEffettiva) {
		this.dtFinVisitaEffettiva = dtFinVisitaEffettiva;
	}
	public java.lang.Boolean getFlPagamentoFineVisita() {
		return flPagamentoFineVisita;
	}
	public void setFlPagamentoFineVisita(java.lang.Boolean flPagamentoFineVisita) {
		this.flPagamentoFineVisita = flPagamentoFineVisita;
	}
	public java.lang.Boolean getFlPagamentoConBonifico() {
		return flPagamentoConBonifico;
	}
	public void setFlPagamentoConBonifico(java.lang.Boolean flPagamentoConBonifico) {
		this.flPagamentoConBonifico = flPagamentoConBonifico;
	}
	public java.lang.Long getNumProtNotaAddebitoAnt() {
		return numProtNotaAddebitoAnt;
	}
	public void setNumProtNotaAddebitoAnt(java.lang.Long numProtNotaAddebitoAnt) {
		this.numProtNotaAddebitoAnt = numProtNotaAddebitoAnt;
	}
	public java.sql.Timestamp getDtProtNotaAddebitoAnt() {
		return dtProtNotaAddebitoAnt;
	}
	public void setDtProtNotaAddebitoAnt(java.sql.Timestamp dtProtNotaAddebitoAnt) {
		this.dtProtNotaAddebitoAnt = dtProtNotaAddebitoAnt;
	}
	public java.lang.Boolean getFlStampatoNotaAddebito() {
		return flStampatoNotaAddebito;
	}
	public void setFlStampatoNotaAddebito(java.lang.Boolean flStampatoNotaAddebito) {
		this.flStampatoNotaAddebito = flStampatoNotaAddebito;
	}
}