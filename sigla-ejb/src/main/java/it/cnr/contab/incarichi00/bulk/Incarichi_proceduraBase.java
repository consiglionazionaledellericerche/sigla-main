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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.jada.persistency.Keyed;
public class Incarichi_proceduraBase extends Incarichi_proceduraKey implements Keyed {
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_cancellazione;
 
//    DT_RESPINTA TIMESTAMP(7)
	private java.sql.Timestamp dt_respinta;
 
//    DT_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_pubblicazione;
 
//    DT_FINE_PUBBLICAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_pubblicazione;
 
//    DT_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dt_scadenza;

//    PG_COMUNE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_comune;
 
//    ESERCIZIO_RICHIESTA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_richiesta;
 
//    PG_RICHIESTA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_richiesta;
 
//    CD_TERZO_RESP DECIMAL(8,0)
	private java.lang.Integer cd_terzo_resp;

//    CD_FIRMATARIO DECIMAL(8,0)
	private java.lang.Integer cd_firmatario;
 
//    CD_PROC_AMM VARCHAR(5)
	private java.lang.String cd_proc_amm;
 
//    CD_PROC_AMM_BENEF VARCHAR(5)
	private java.lang.String cd_proc_amm_benef;

//    CD_TIPO_ATTO VARCHAR(5)
	private java.lang.String cd_tipo_atto;
 
//    DS_ATTO VARCHAR(100)
	private java.lang.String ds_atto;
 
//    CD_PROTOCOLLO VARCHAR(50)
	private java.lang.String cd_protocollo;
 
//    OGGETTO VARCHAR(500) NOT NULL
	private java.lang.String oggetto;
 
//    CD_TIPO_INCARICO VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_incarico;
 
//    CD_TIPO_ATTIVITA VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_attivita;
 
//    TIPO_NATURA VARCHAR(3) NOT NULL
	private java.lang.String tipo_natura;
 
//  FL_MERAMENTE_OCCASIONALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_meramente_occasionale;

//    ESERCIZIO_PADRE DECIMAL(4,0)
	private java.lang.Integer esercizio_padre;
 
//    PG_PROCEDURA_PADRE DECIMAL(10,0)
	private java.lang.Long pg_procedura_padre;
 
//    IMPORTO_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_lordo;

//    IMPORTO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_complessivo;

//    FL_PUBBLICA_CONTRATTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pubblica_contratto;

//    NR_CONTRATTI DECIMAL(3,0)
	private java.lang.Integer nr_contratti;

//    NR_CONTRATTI_INIZIALE DECIMAL(3,0)
	private java.lang.Integer nr_contratti_iniziale;
	
//    FL_ART51 CHAR(1) NOT NULL
	private java.lang.Boolean fl_art51;

//    FL_SBLOCCATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_sbloccato;

//    DS_MOTIVAZIONE_SBLOCCO VARCHAR(500)
	private java.lang.String ds_motivazione_sblocco;

//    CD_TIPO_PRESTAZIONE VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_prestazione;

//    CD_TIPO_ATTIVITA_FP VARCHAR(5)
	private java.lang.String cd_tipo_attivita_fp;

//    FL_APPLICAZIONE_NORMA CHAR(1) NOT NULL
	private String fl_applicazione_norma;

//    CD_TIPO_NORMA_PERLA VARCHAR(3) NOT NULL
	private java.lang.String cd_tipo_norma_perla;

//    DS_LIBERA_NORMA_PERLA VARCHAR(3) NOT NULL
	private java.lang.String ds_libera_norma_perla;

//    FL_MIGRATA_TO_CMIS CHAR(1) NOT NULL
	private java.lang.Boolean fl_migrata_to_cmis;

	public Incarichi_proceduraBase() {
		super();
	}
	public Incarichi_proceduraBase(java.lang.Integer esercizio, java.lang.Long pg_procedura) {
		super(esercizio, pg_procedura);
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
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.sql.Timestamp getDt_cancellazione() {
		return dt_cancellazione;
	}
	public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione)  {
		this.dt_cancellazione=dt_cancellazione;
	}
	public java.sql.Timestamp getDt_respinta() {
		return dt_respinta;
	}
	public void setDt_respinta(java.sql.Timestamp dt_respinta)  {
		this.dt_respinta=dt_respinta;
	}
	public java.lang.Integer getEsercizio_richiesta() {
		return esercizio_richiesta;
	}
	public void setEsercizio_richiesta(java.lang.Integer esercizio_richiesta)  {
		this.esercizio_richiesta=esercizio_richiesta;
	}
	public java.lang.Long getPg_richiesta() {
		return pg_richiesta;
	}
	public void setPg_richiesta(java.lang.Long pg_richiesta)  {
		this.pg_richiesta=pg_richiesta;
	}
	public java.lang.Integer getCd_terzo_resp() {
		return cd_terzo_resp;
	}
	public void setCd_terzo_resp(java.lang.Integer cd_terzo_resp)  {
		this.cd_terzo_resp=cd_terzo_resp;
	}
	public java.lang.Integer getCd_firmatario() {
		return cd_firmatario;
	}
	public void setCd_firmatario(java.lang.Integer cd_firmatario)  {
		this.cd_firmatario=cd_firmatario;
	}
	public java.lang.String getCd_proc_amm() {
		return cd_proc_amm;
	}
	public void setCd_proc_amm(java.lang.String cd_proc_amm)  {
		this.cd_proc_amm=cd_proc_amm;
	}
	public java.lang.String getCd_proc_amm_benef() {
		return cd_proc_amm_benef;
	}
	public void setCd_proc_amm_benef(java.lang.String cd_proc_amm_benef) {
		this.cd_proc_amm_benef = cd_proc_amm_benef;
	}
	public java.lang.String getCd_tipo_atto() {
		return cd_tipo_atto;
	}
	public void setCd_tipo_atto(java.lang.String cd_tipo_atto)  {
		this.cd_tipo_atto=cd_tipo_atto;
	}
	public java.lang.String getDs_atto() {
		return ds_atto;
	}
	public void setDs_atto(java.lang.String ds_atto)  {
		this.ds_atto=ds_atto;
	}
	public java.lang.String getCd_protocollo() {
		return cd_protocollo;
	}
	public void setCd_protocollo(java.lang.String cd_protocollo)  {
		this.cd_protocollo=cd_protocollo;
	}
	public java.lang.String getOggetto() {
		return oggetto;
	}
	public void setOggetto(java.lang.String oggetto)  {
		this.oggetto=oggetto;
	}
	public java.lang.String getCd_tipo_incarico() {
		return cd_tipo_incarico;
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico)  {
		this.cd_tipo_incarico=cd_tipo_incarico;
	}
	public java.lang.String getCd_tipo_attivita() {
		return cd_tipo_attivita;
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita)  {
		this.cd_tipo_attivita=cd_tipo_attivita;
	}
	public java.lang.String getTipo_natura() {
		return tipo_natura;
	}
	public void setTipo_natura(java.lang.String tipo_natura)  {
		this.tipo_natura=tipo_natura;
	}
	public java.lang.Integer getEsercizio_padre() {
		return esercizio_padre;
	}
	public void setEsercizio_padre(java.lang.Integer esercizio_padre)  {
		this.esercizio_padre=esercizio_padre;
	}
	public java.lang.Long getPg_procedura_padre() {
		return pg_procedura_padre;
	}
	public void setPg_procedura_padre(java.lang.Long pg_procedura_padre)  {
		this.pg_procedura_padre=pg_procedura_padre;
	}
	public java.math.BigDecimal getImporto_lordo() {
		return importo_lordo;
	}
	public void setImporto_lordo(java.math.BigDecimal importo_lordo)  {
		this.importo_lordo=importo_lordo;
	}
	public java.math.BigDecimal getImporto_complessivo() {
		return importo_complessivo;
	}
	public void setImporto_complessivo(java.math.BigDecimal importo_complessivo)  {
		this.importo_complessivo=importo_complessivo;
	}
	public java.sql.Timestamp getDt_fine_pubblicazione() {
		return dt_fine_pubblicazione;
	}
	public void setDt_fine_pubblicazione(java.sql.Timestamp dt_fine_pubblicazione) {
		this.dt_fine_pubblicazione = dt_fine_pubblicazione;
	}
	public java.sql.Timestamp getDt_pubblicazione() {
		return dt_pubblicazione;
	}
	public void setDt_pubblicazione(java.sql.Timestamp dt_pubblicazione) {
		this.dt_pubblicazione = dt_pubblicazione;
	}
	public java.sql.Timestamp getDt_scadenza() {
		return dt_scadenza;
	}
	public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
		this.dt_scadenza = dt_scadenza;
	}
	public java.lang.Long getPg_comune() {
		return pg_comune;
	}
	public void setPg_comune(java.lang.Long pg_comune)  {
		this.pg_comune=pg_comune;
	}
	public java.lang.Boolean getFl_pubblica_contratto() {
		return fl_pubblica_contratto;
	}
	public void setFl_pubblica_contratto(java.lang.Boolean fl_pubblica_contratto) {
		this.fl_pubblica_contratto = fl_pubblica_contratto;
	}
	public java.lang.Integer getNr_contratti() {
		return nr_contratti;
	}
	public void setNr_contratti(java.lang.Integer nr_contratti) {
		this.nr_contratti = nr_contratti;
	}
	public java.lang.Integer getNr_contratti_iniziale() {
		return nr_contratti_iniziale;
	}
	public void setNr_contratti_iniziale(java.lang.Integer nr_contratti_iniziale) {
		this.nr_contratti_iniziale = nr_contratti_iniziale;
	}
	public java.lang.Boolean getFl_meramente_occasionale() {
		return fl_meramente_occasionale;
	}
	public void setFl_meramente_occasionale(
			java.lang.Boolean fl_meramente_occasionale) {
		this.fl_meramente_occasionale = fl_meramente_occasionale;
	}
	public java.lang.Boolean getFl_art51() {
		return fl_art51;
	}
	public void setFl_art51(java.lang.Boolean fl_art51) {
		this.fl_art51 = fl_art51;
	}
	public java.lang.Boolean getFl_sbloccato() {
		return fl_sbloccato;
	}
	public void setFl_sbloccato(java.lang.Boolean fl_sbloccato) {
		this.fl_sbloccato = fl_sbloccato;
	}
	public java.lang.String getDs_motivazione_sblocco() {
		return ds_motivazione_sblocco;
	}
	public void setDs_motivazione_sblocco(java.lang.String ds_motivazione_sblocco) {
		this.ds_motivazione_sblocco = ds_motivazione_sblocco;
	}
	public java.lang.String getCd_tipo_prestazione() {
		return cd_tipo_prestazione;
	}
	public void setCd_tipo_prestazione(java.lang.String cd_tipo_prestazione) {
		this.cd_tipo_prestazione = cd_tipo_prestazione;
	}
	public java.lang.String getCd_tipo_attivita_fp() {
		return cd_tipo_attivita_fp;
	}
	public void setCd_tipo_attivita_fp(java.lang.String cd_tipo_attivita_fp) {
		this.cd_tipo_attivita_fp = cd_tipo_attivita_fp;
	}
	public String getFl_applicazione_norma() {
		return fl_applicazione_norma;
	}
	public void setFl_applicazione_norma(String fl_applicazione_norma) {
		this.fl_applicazione_norma = fl_applicazione_norma;
	}
	public java.lang.String getCd_tipo_norma_perla() {
		return cd_tipo_norma_perla;
	}
	public void setCd_tipo_norma_perla(java.lang.String cd_tipo_norma_perla) {
		this.cd_tipo_norma_perla = cd_tipo_norma_perla;
	}
	public java.lang.String getDs_libera_norma_perla() {
		return ds_libera_norma_perla;
	}
	public void setDs_libera_norma_perla(java.lang.String ds_libera_norma_perla) {
		this.ds_libera_norma_perla = ds_libera_norma_perla;
	}
	public java.lang.Boolean getFl_migrata_to_cmis() {
		return fl_migrata_to_cmis;
	}
	public void setFl_migrata_to_cmis(java.lang.Boolean fl_migrata_to_cmis) {
		this.fl_migrata_to_cmis = fl_migrata_to_cmis;
	}
}