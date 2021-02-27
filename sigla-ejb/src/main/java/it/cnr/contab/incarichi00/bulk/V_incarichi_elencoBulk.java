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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 05/11/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.util.List;
public class V_incarichi_elencoBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_REPERTORIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_repertorio;
 
//    CD_TIPO_ATTIVITA VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_attivita;

//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    DS_CDS VARCHAR(300) NOT NULL
	private java.lang.String ds_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    DS_UNITA_ORGANIZZATIVA VARCHAR(300) NOT NULL
	private java.lang.String ds_unita_organizzativa;
 
//  non esiste su DB, verrà riempito con la Sede del CDS
	private java.lang.String sede;

//    terzo_codice_fiscale VARCHAR(20) NULL
	private java.lang.String benef_codice_fiscale;

//    terzo_partita_iva VARCHAR(20) NULL
	private java.lang.String benef_partita_iva;
	
//    benef_denominazione_sede VARCHAR(100)
	private java.lang.String benef_denominazione_sede;

//    resp_denominazione_sede VARCHAR(100)
	private java.lang.String resp_denominazione_sede;

//    firm_denominazione_sede VARCHAR(100)
	private java.lang.String firm_denominazione_sede;

//    OGGETTO VARCHAR(1000) NOT NULL
	private java.lang.String oggetto;
 
//    IMPORTO_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_lordo;
 
//    IMPORTO_VARIAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_variazione;

//    DT_INIZIO_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dt_inizio_validita;
 
//    DT_FINE_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_validita;
 
//    DT_FINE_VALIDITA_VARIAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_validita_variazione;

//    DT_STIPULA TIMESTAMP(7)
	private java.sql.Timestamp dt_stipula;

//    DS_PROVVEDIMENTO VARCHAR(70)
	private java.lang.String ds_provvedimento;

//    ESERCIZIO_PROCEDURA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_procedura;
 
//    PG_PROCEDURA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_procedura;

//    DS_TIPO_NORMA VARCHAR NULL
	private java.lang.String ds_tipo_norma;

//    DS_PROC_AMM VARCHAR NULL
	private java.lang.String ds_proc_amm;

//    non esiste su DB, contiene l'anagrafica del terzo dell'incarico
	private AnagraficoBulk anagraficoTerzo;

//    non esiste su DB, contiene la procedura di conferimento incarico
	private Incarichi_repertorioBulk incaricoRepertorio;

//  non esiste su DB, verrà riempito con ll URL dove scaricare il pdf del bando
	private List<Incarichi_archivioBulk> listDownloadUrl;

//  non esiste su DB, contiene l'ultima variazione valida dell'incarico
	private Incarichi_repertorio_varBulk incaricoVariazione;

//  non esiste su DB, contiene i dettagli degli altri rapporti avuto dal contraente con altre PA
	private BulkList incarichi_repertorio_rapp_detColl = new BulkList();

	private java.sql.Timestamp dt_dichiarazione;

	private Integer idPerla;

	private String codiceAooIpa;

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_repertorio() {
		return pg_repertorio;
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio)  {
		this.pg_repertorio=pg_repertorio;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getDs_cds() {
		return ds_cds;
	}
	public void setDs_cds(java.lang.String ds_cds)  {
		this.ds_cds=ds_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getDs_unita_organizzativa() {
		return ds_unita_organizzativa;
	}
	public void setDs_unita_organizzativa(java.lang.String ds_unita_organizzativa)  {
		this.ds_unita_organizzativa=ds_unita_organizzativa;
	}
	public java.lang.String getOggetto() {
		return oggetto;
	}
	public void setOggetto(java.lang.String oggetto)  {
		this.oggetto=oggetto;
	}
	public java.math.BigDecimal getImporto_lordo() {
		return importo_lordo;
	}
	public void setImporto_lordo(java.math.BigDecimal importo_lordo)  {
		this.importo_lordo=importo_lordo;
	}
	public java.sql.Timestamp getDt_inizio_validita() {
		return dt_inizio_validita;
	}
	public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita)  {
		this.dt_inizio_validita=dt_inizio_validita;
	}
	public java.sql.Timestamp getDt_fine_validita() {
		return dt_fine_validita;
	}
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita)  {
		this.dt_fine_validita=dt_fine_validita;
	}
	public java.lang.String getSede() {
		return sede;
	}
	public void setSede(java.lang.String sede) {
		this.sede = sede;
	}
	public java.sql.Timestamp getDt_stipula() {
		return dt_stipula;
	}
	public void setDt_stipula(java.sql.Timestamp dt_stipula)  {
		this.dt_stipula=dt_stipula;
	}
	public List<Incarichi_archivioBulk> getListDownloadUrl() {
		return listDownloadUrl;
	}
	public void setListDownloadUrl(List<Incarichi_archivioBulk> listDownloadUrl) {
		this.listDownloadUrl = listDownloadUrl;
	}
	public Incarichi_repertorio_varBulk getIncaricoVariazione() {
		return incaricoVariazione;
	}
	public void setIncaricoVariazione(
			Incarichi_repertorio_varBulk incaricoVariazione) {
		this.incaricoVariazione = incaricoVariazione;
	}
	public java.math.BigDecimal getImporto_lordo_con_variazioni() {
		if (getIncaricoVariazione()==null || getIncaricoVariazione().getImporto_lordo()==null )
			return getImporto_lordo();
		else
			return getImporto_lordo().add(getIncaricoVariazione().getImporto_lordo());
	}
	public java.math.BigDecimal getImporto_variazione() {
		return importo_variazione;
	}
	public void setImporto_variazione(java.math.BigDecimal importo_variazione) {
		this.importo_variazione = importo_variazione;
	}
	public java.sql.Timestamp getDt_fine_validita_variazione() {
		return dt_fine_validita_variazione;
	}
	public void setDt_fine_validita_variazione(
			java.sql.Timestamp dt_fine_validita_variazione) {
		this.dt_fine_validita_variazione = dt_fine_validita_variazione;
	}
	public java.lang.String getDs_provvedimento() {
		return ds_provvedimento;
	}
	public void setDs_provvedimento(java.lang.String dsProvvedimento) {
		ds_provvedimento = dsProvvedimento;
	}
	public java.lang.Integer getEsercizio_procedura() {
		return esercizio_procedura;
	}
	public void setEsercizio_procedura(java.lang.Integer esercizioProcedura) {
		esercizio_procedura = esercizioProcedura;
	}
	public java.lang.Long getPg_procedura() {
		return pg_procedura;
	}
	public void setPg_procedura(java.lang.Long pgProcedura) {
		pg_procedura = pgProcedura;
	}
	public AnagraficoBulk getAnagraficoTerzo() {
		return anagraficoTerzo;
	}
	public void setAnagraficoTerzo(AnagraficoBulk anagraficoTerzo) {
		this.anagraficoTerzo = anagraficoTerzo;
	}
	public Incarichi_repertorioBulk getIncaricoRepertorio() {
		return incaricoRepertorio;
	}
	public void setIncaricoRepertorio(Incarichi_repertorioBulk incaricoRepertorio) {
		this.incaricoRepertorio = incaricoRepertorio;
	}
	public BulkList getIncarichi_repertorio_rapp_detColl() {
		return incarichi_repertorio_rapp_detColl;
	}
	public void setIncarichi_repertorio_rapp_detColl(BulkList incarichi_repertorio_rapp_detColl) {
		this.incarichi_repertorio_rapp_detColl = incarichi_repertorio_rapp_detColl;
	}
	public java.lang.String getBenef_codice_fiscale() {
		return benef_codice_fiscale;
	}
	public void setBenef_codice_fiscale(java.lang.String benef_codice_fiscale) {
		this.benef_codice_fiscale = benef_codice_fiscale;
	}
	public java.lang.String getBenef_partita_iva() {
		return benef_partita_iva;
	}
	public void setBenef_partita_iva(java.lang.String benef_partita_iva) {
		this.benef_partita_iva = benef_partita_iva;
	}
	public java.lang.String getBenef_denominazione_sede() {
		return benef_denominazione_sede;
	}
	public void setBenef_denominazione_sede(java.lang.String benef_denominazione_sede) {
		this.benef_denominazione_sede = benef_denominazione_sede;
	}
	public java.lang.String getResp_denominazione_sede() {
		return resp_denominazione_sede;
	}
	public void setResp_denominazione_sede(java.lang.String resp_denominazione_sede) {
		this.resp_denominazione_sede = resp_denominazione_sede;
	}
	public java.lang.String getFirm_denominazione_sede() {
		return firm_denominazione_sede;
	}
	public void setFirm_denominazione_sede(java.lang.String firm_denominazione_sede) {
		this.firm_denominazione_sede = firm_denominazione_sede;
	}
	public java.lang.String getDs_tipo_norma() {
		return ds_tipo_norma;
	}
	public void setDs_tipo_norma(java.lang.String ds_tipo_norma) {
		this.ds_tipo_norma = ds_tipo_norma;
	}
	public java.lang.String getDs_proc_amm() {
		return ds_proc_amm;
	}
	public void setDs_proc_amm(java.lang.String ds_proc_amm) {
		this.ds_proc_amm = ds_proc_amm;
	}
	public java.lang.String getCd_tipo_attivita() {
		return cd_tipo_attivita;
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita) {
		this.cd_tipo_attivita = cd_tipo_attivita;
	}
	public java.sql.Timestamp getDt_dichiarazione() {
		return dt_dichiarazione;
	}
	public void setDt_dichiarazione(java.sql.Timestamp dt_dichiarazione) {
		this.dt_dichiarazione = dt_dichiarazione;
	}

	public Integer getIdPerla() {
		return idPerla;
	}

	public void setIdPerla(Integer idPerla) {
		this.idPerla = idPerla;
	}

	public String getCodiceAooIpa() {
		return codiceAooIpa;
	}

	public void setCodiceAooIpa(String codiceAooIpa) {
		this.codiceAooIpa = codiceAooIpa;
	}
}