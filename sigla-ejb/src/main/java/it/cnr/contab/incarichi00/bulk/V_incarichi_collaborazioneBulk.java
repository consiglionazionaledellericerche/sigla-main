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
 * Date 29/10/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_incarichi_collaborazioneBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_PROCEDURA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_procedura;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    DS_CDS VARCHAR(300) NOT NULL
	private java.lang.String ds_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    DS_UNITA_ORGANIZZATIVA VARCHAR(300) NOT NULL
	private java.lang.String ds_unita_organizzativa;
 
//    STATO VARCHAR(2) NOT NULL
	private java.lang.String stato;
 
//    NR_CONTRATTI DECIMAL(10,0)
	private java.lang.Integer nr_contratti;

//    DT_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_pubblicazione;
 
//    DT_FINE_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_pubblicazione;
 
//    DT_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dt_scadenza;
 
//  non esiste su DB, verrà riempito con la Sede del CDS
	private java.lang.String sede;

	private java.lang.String oggetto;

//  non esiste su DB, contiene la procedura di conferimento incarico
	private Incarichi_proceduraBulk incaricoProcedura;

//  non esiste su DB, verrà riempito con ll URL dove scaricare il pdf del bando
	private java.lang.String downloadUrl;

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_procedura() {
		return pg_procedura;
	}
	public void setPg_procedura(java.lang.Long pg_procedura)  {
		this.pg_procedura=pg_procedura;
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
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.sql.Timestamp getDt_pubblicazione() {
		return dt_pubblicazione;
	}
	public void setDt_pubblicazione(java.sql.Timestamp dt_pubblicazione)  {
		this.dt_pubblicazione=dt_pubblicazione;
	}
	public java.sql.Timestamp getDt_fine_pubblicazione() {
		return dt_fine_pubblicazione;
	}
	public void setDt_fine_pubblicazione(java.sql.Timestamp dt_fine_pubblicazione)  {
		this.dt_fine_pubblicazione=dt_fine_pubblicazione;
	}
	public java.sql.Timestamp getDt_scadenza() {
		return dt_scadenza;
	}
	public void setDt_scadenza(java.sql.Timestamp dt_scadenza)  {
		this.dt_scadenza=dt_scadenza;
	}
	public java.lang.String getSede() {
		return sede;
	}
	public void setSede(java.lang.String sede) {
		this.sede = sede;
	}
	public java.lang.String getOggetto() {
		return oggetto;
	}
	public void setOggetto(java.lang.String oggetto) {
		this.oggetto = oggetto;
	}
	public java.lang.String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(java.lang.String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public java.lang.Integer getNr_contratti() {
		return nr_contratti;
	}
	public void setNr_contratti(java.lang.Integer nr_contratti) {
		this.nr_contratti = nr_contratti;
	}
	public Incarichi_proceduraBulk getIncaricoProcedura() {
		return incaricoProcedura;
	}
	public void setIncaricoProcedura(Incarichi_proceduraBulk incaricoProcedura) {
		this.incaricoProcedura = incaricoProcedura;
	}
}