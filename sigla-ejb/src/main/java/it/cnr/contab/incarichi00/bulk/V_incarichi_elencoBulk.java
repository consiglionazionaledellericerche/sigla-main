/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 05/11/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.util.List;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_incarichi_elencoBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_REPERTORIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_repertorio;
 
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

//    nominativo VARCHAR(100)
	private java.lang.String nominativo;
 
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

//    non esiste su DB, contiene l'anagrafica del terzo dell'incarico
	private AnagraficoBulk anagraficoTerzo;

//    non esiste su DB, contiene la procedura di conferimento incarico
	private Incarichi_repertorioBulk incaricoRepertorio;

//  non esiste su DB, verrà riempito con ll URL dove scaricare il pdf del bando
	private List<Incarichi_archivioBulk> listDownloadUrl;

//  non esiste su DB, contiene l'ultima variazione valida dell'incarico
	private Incarichi_repertorio_varBulk incaricoVariazione;

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
	public java.lang.String getNominativo() {
		return nominativo;
	}
	public void setNominativo(java.lang.String nominativo) {
		this.nominativo = nominativo;
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
}