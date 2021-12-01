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
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

public class Pdg_variazioneBase extends Pdg_variazioneKey implements Keyed {
//	CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL	
	private java.lang.String cd_centro_responsabilita;
	
//    DT_APERTURA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_apertura;
 
//    DT_CHIUSURA TIMESTAMP(7)
	private java.sql.Timestamp dt_chiusura;
 
//    DT_APPROVAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_approvazione;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_annullamento;
 
//    DS_VARIAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_variazione;
 
//    DS_DELIBERA VARCHAR(200) NOT NULL
	private java.lang.String ds_delibera;
 
//  TIPOLOGIA VARCHAR(15) NOT NULL
	private java.lang.String tipologia;

//	TIPOLOGIA_FIN VARCHAR(3) NOT NULL
	private java.lang.String tipologia_fin;

//    STATO CHAR(3) NOT NULL
	private java.lang.String stato;
	
//  STATO_INVIO CHAR(3) NOT NULL
	private java.lang.String stato_invio;	
 
//	  RIFERIMENTI VARCHAR(200) NULL
    private java.lang.String riferimenti;

//	  CD_CAUSALE_RESPINTA VARCHAR(200) NULL
    private java.lang.String cd_causale_respinta;

//	  DS_CAUSALE_RESPINTA VARCHAR(200) NULL
    private java.lang.String ds_causale_respinta;

//    DT_APPROVAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_app_formale;

	// TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;

	// CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;

	//  FL_VISTO_DIP_VARIAZIONI VARCHAR(1)
	private Boolean fl_visto_dip_variazioni;

	private java.sql.Timestamp dt_firma;
	
	// TI_MOTIVAZIONE_VARIAZIONE VARCHAR2(10)
	private java.lang.String tiMotivazioneVariazione;

	// ID_MATRICOLA VARCHAR2(10)
	private java.lang.Long idMatricola;

	// ID_BANDO VARCHAR2(30)
	private java.lang.String idBando;
	
	// DS_CAUSALE VARCHAR2(50)
    private java.lang.String ds_causale;

	// PG_PROGETTO_RIMODULAZIONE NUMBER (10) NULL
	private java.lang.Integer pg_progetto_rimodulazione;

	// PG_RIMODULAZIONE NUMBER (10) NULL
	private java.lang.Integer pg_rimodulazione;

	//IMPORTO_INCASSATO NUMBER(15,2)
	private java.math.BigDecimal im_incassato;

	//FL_CDA VARCHAR2(1)
	private java.lang.Boolean fl_cda;

	// ASSEGNAZIONE VARCHAR2(50)
	private java.lang.String assegnazione;

	// NOTE VARCHAR2(2000)
	private java.lang.String note;

	public Pdg_variazioneBase() {
		super();
	}
	public Pdg_variazioneBase(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg) {
		super(esercizio, pg_variazione_pdg);
	}
	public java.sql.Timestamp getDt_apertura () {
		return dt_apertura;
	}
	public void setDt_apertura(java.sql.Timestamp dt_apertura)  {
		this.dt_apertura=dt_apertura;
	}
	public java.sql.Timestamp getDt_chiusura () {
		return dt_chiusura;
	}
	public void setDt_chiusura(java.sql.Timestamp dt_chiusura)  {
		this.dt_chiusura=dt_chiusura;
	}
	public java.sql.Timestamp getDt_approvazione () {
		return dt_approvazione;
	}
	public void setDt_approvazione(java.sql.Timestamp dt_approvazione)  {
		this.dt_approvazione=dt_approvazione;
	}
	public java.sql.Timestamp getDt_annullamento () {
		return dt_annullamento;
	}
	public void setDt_annullamento(java.sql.Timestamp dt_annullamento)  {
		this.dt_annullamento=dt_annullamento;
	}
	public java.lang.String getDs_variazione () {
		return ds_variazione;
	}
	public void setDs_variazione(java.lang.String ds_variazione)  {
		this.ds_variazione=ds_variazione;
	}
	public java.lang.String getDs_delibera () {
		return ds_delibera;
	}
	public void setDs_delibera(java.lang.String ds_delibera)  {
		this.ds_delibera=ds_delibera;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getRiferimenti () {
		return riferimenti;
	}
	public void setRiferimenti(java.lang.String riferimenti)  {
		this.riferimenti=riferimenti;
	}
	public java.lang.String getCd_causale_respinta () {
		return cd_causale_respinta;
	}
	public void setCd_causale_respinta(java.lang.String cd_causale_respinta)  {
		this.cd_causale_respinta=cd_causale_respinta;
	}
	public java.lang.String getDs_causale_respinta () {
		return ds_causale_respinta;
	}
	public void setDs_causale_respinta(java.lang.String ds_causale_respinta)  {
		this.ds_causale_respinta=ds_causale_respinta;
	}
	/**
	 * @return
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	
	/**
	 * @param string
	 */
	public void setCd_centro_responsabilita(java.lang.String string) {
		cd_centro_responsabilita = string;
	}
	
	public java.sql.Timestamp getDt_app_formale () {
		return dt_app_formale;
	}
	public void setDt_app_formale(java.sql.Timestamp dt_app_formale)  {
		this.dt_app_formale=dt_app_formale;
	}
	public java.lang.String getTipologia () {
		return tipologia;
	}
	public void setTipologia(java.lang.String tipologia)  {
		this.tipologia=tipologia;
	}
	public java.lang.String getTipologia_fin() {
		return tipologia_fin;
	}
	public void setTipologia_fin(java.lang.String string) {
		tipologia_fin = string;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}
	
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}
	
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
	public Boolean getFl_visto_dip_variazioni() {
		return fl_visto_dip_variazioni;
	}
	public void setFl_visto_dip_variazioni(Boolean fl_visto_dip_variazioni) {
		this.fl_visto_dip_variazioni = fl_visto_dip_variazioni;
	}
	public java.lang.String getStato_invio() {
		return stato_invio;
	}
	public void setStato_invio(java.lang.String statoInvio) {
		stato_invio = statoInvio;
	}
	public java.sql.Timestamp getDt_firma() {
		return dt_firma;
	}
	public void setDt_firma(java.sql.Timestamp dt_firma) {
		this.dt_firma = dt_firma;
	}

	public java.lang.String getTiMotivazioneVariazione() {
		return tiMotivazioneVariazione;
	}
	
	public void setTiMotivazioneVariazione(java.lang.String tiMotivazioneVariazione) {
		this.tiMotivazioneVariazione = tiMotivazioneVariazione;
	}
	
	public java.lang.Long getIdMatricola() {
		return idMatricola;
	}
	
	public void setIdMatricola(java.lang.Long idMatricola) {
		this.idMatricola = idMatricola;
	}

	public java.lang.String getIdBando() {
		return idBando;
	}
	
	public void setIdBando(java.lang.String idBando) {
		this.idBando = idBando;
	}
	
	public java.lang.String getDs_causale() {
		return ds_causale;
	}
	
	public void setDs_causale(java.lang.String ds_causale) {
		this.ds_causale = ds_causale;
	}
	
	public java.lang.Integer getPg_progetto_rimodulazione() {
		return pg_progetto_rimodulazione;
	}
	
	public void setPg_progetto_rimodulazione(java.lang.Integer pg_progetto_rimodulazione) {
		this.pg_progetto_rimodulazione = pg_progetto_rimodulazione;
	}
	
	public java.lang.Integer getPg_rimodulazione() {
		return pg_rimodulazione;
	}

	public void setPg_rimodulazione(java.lang.Integer pg_rimodulazione) {
		this.pg_rimodulazione = pg_rimodulazione;
	}

	public BigDecimal getIm_incassato() {
		return im_incassato;
	}

	public void setIm_incassato(BigDecimal im_incassato) {
		this.im_incassato = im_incassato;
	}

	public Boolean getFl_cda() {
		return fl_cda;
	}

	public void setFl_cda(Boolean fl_cda) {
		this.fl_cda = fl_cda;
	}

	public String getAssegnazione() {
		return assegnazione;
	}

	public void setAssegnazione(String assegnazione) {
		this.assegnazione = assegnazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}