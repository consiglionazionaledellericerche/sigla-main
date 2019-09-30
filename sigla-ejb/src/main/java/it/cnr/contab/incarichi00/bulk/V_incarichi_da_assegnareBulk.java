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
 * Date 26/03/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_incarichi_da_assegnareBulk extends OggettoBulk implements Persistent {
	private Incarichi_proceduraBulk incarichi_procedura = new Incarichi_proceduraBulk();
	private Incarichi_repertorioBulk incarichi_repertorio = new Incarichi_repertorioBulk();
	
//    ESERCIZIO_LIMITE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_limite;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_PROCEDURA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_procedura;

//    ESERCIZIO_REPERTORIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_repertorio;
 
//    PG_REPERTORIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_repertorio;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;
 
//    DT_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_pubblicazione;
 
//    DT_FINE_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_pubblicazione;
 
//    DT_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dt_scadenza;
 
//    STATO VARCHAR(2) NOT NULL
	private java.lang.String stato;
 
//    CD_TIPO_LIMITE VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_limite;
 
//    IM_INCARICHI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_incarichi;
 
	public java.lang.Integer getEsercizio_limite() {
		return esercizio_limite;
	}
	public void setEsercizio_limite(java.lang.Integer esercizio_limite)  {
		this.esercizio_limite=esercizio_limite;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
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
	public java.lang.Integer getEsercizio_repertorio() {
		return esercizio_repertorio;
	}
	public void setEsercizio_repertorio(java.lang.Integer esercizio_repertorio)  {
		this.esercizio_repertorio=esercizio_repertorio;
	}
	public java.lang.Long getPg_repertorio() {
		return pg_repertorio;
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio)  {
		this.pg_repertorio=pg_repertorio;
	}
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
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
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getCd_tipo_limite() {
		return cd_tipo_limite;
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite)  {
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public java.math.BigDecimal getIm_incarichi() {
		return im_incarichi;
	}
	public void setIm_incarichi(java.math.BigDecimal im_incarichi)  {
		this.im_incarichi=im_incarichi;
	}
	public Incarichi_proceduraBulk getIncarichi_procedura() {
		return incarichi_procedura;
	}
	public void setIncarichi_procedura(
			Incarichi_proceduraBulk incarichi_procedura) {
		this.incarichi_procedura = incarichi_procedura;
	}
	public Incarichi_repertorioBulk getIncarichi_repertorio() {
		return incarichi_repertorio;
	}
	public void setIncarichi_repertorio(
			Incarichi_repertorioBulk incarichi_repertorio) {
		this.incarichi_repertorio = incarichi_repertorio;
	}
	public String getStatoText(){
		return getIncarichi_procedura().getStatoText();
	}	
}