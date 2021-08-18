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
* Creted by Generator 1.0
* Date 09/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.persistency.Keyed;
public class ContrattoBase extends ContrattoKey implements Keyed {
//    ESERCIZIO_PADRE DECIMAL(4,0)
	private java.lang.Integer esercizio_padre;

//	STATO_PADRE CHAR(1)
    private java.lang.Long stato_padre;
 
//    PG_CONTRATTO_PADRE DECIMAL(10,0)
	private java.lang.Long pg_contratto_padre;

//	  CD_UNITA_ORGANIZZATIVA VARCHAR(30)
    private java.lang.String cd_unita_organizzativa;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_registrazione;
 
//    FIG_GIUR_INT DECIMAL(8,0) NOT NULL
	private java.lang.Integer fig_giur_int;
 
//    FIG_GIUR_EST DECIMAL(8,0) NOT NULL
	private java.lang.Integer fig_giur_est;
 
//    CD_TERZO_RESP DECIMAL(8,0)
	private java.lang.Integer cd_terzo_resp;

//	  CD_TERZO_FIRMATARIO DECIMAL(8,0)
    private java.lang.Integer cd_terzo_firmatario;
 
//    RESP_ESTERNO VARCHAR(300)
	private java.lang.String resp_esterno;
 
//    TIPO VARCHAR(1) NOT NULL
	private java.lang.String natura_contabile;
 
//    CD_TIPO_CONTRATTO VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_contratto;
 
//  CD_CIG_FATTURA_ATTIVA VARCHAR(10) NOT NULL
	private java.lang.String cdCigFatturaAttiva;

//    PROC_AMM VARCHAR(5) NOT NULL
	private java.lang.String cd_proc_amm;
 
//    OGGETTO VARCHAR(500) NOT NULL
	private java.lang.String oggetto;
 
//    CD_PROTOCOLLO VARCHAR(50)
	private java.lang.String cd_protocollo;
 
//    DT_STIPULA TIMESTAMP(7)
	@StorageProperty(name="sigla_contratti:data_stipula")
	private java.sql.Timestamp dt_stipula;
 
//    DT_INIZIO_VALIDITA TIMESTAMP(7)
	@StorageProperty(name="sigla_contratti:data_inizio")
	private java.sql.Timestamp dt_inizio_validita;
 
//    DT_FINE_VALIDITA TIMESTAMP(7)
	@StorageProperty(name="sigla_contratti:data_fine")
	private java.sql.Timestamp dt_fine_validita;

//	  DT_PROROGA TIMESTAMP(7)
    private java.sql.Timestamp dt_proroga;
 
//    IM_CONTRATTO_ATTIVO DECIMAL(15,2)
	@StorageProperty(name="sigla_contratti:importo_attivo_appalto")
	private java.math.BigDecimal im_contratto_attivo;

//	  IM_CONTRATTO_PASSIVO DECIMAL(15,2)
	@StorageProperty(name="sigla_contratti:importo_passivo_appalto")
	private java.math.BigDecimal im_contratto_passivo;
 
//	  IM_CONTRATTO_PASSIVO DECIMAL(15,2)
//	@StorageProperty(name="sigla_contratti:importo_passivo_appalto_netto")
	private java.math.BigDecimal im_contratto_passivo_netto;	
//	CD_TIPO_ATTO VARCHAR(5)
	private java.lang.String cd_tipo_atto;

//	DS_ATTO_NON_DEFINITO VARCHAR(100)
	private java.lang.String ds_atto_non_definito;

//	DS_ATTO VARCHAR(100)
	private java.lang.String ds_atto;
	
//	CD_ORGANO VARCHAR(5)
	private java.lang.String cd_organo;
	
//	DS_ORGANO_NON_DEFINITO VARCHAR(100)
	private java.lang.String ds_organo_non_definito;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dt_annullamento;
 
//    DS_ANNULLAMENTO VARCHAR(300)
	private java.lang.String ds_annullamento;

//	CD_TIPO_ATTO_ANN VARCHAR(5)
	private java.lang.String cd_tipo_atto_ann;

//	DS_ATTO_ANN_NON_DEFINITO VARCHAR(100)
	private java.lang.String ds_atto_ann_non_definito;

//	DS_ATTO_ANN VARCHAR(100)
	private java.lang.String ds_atto_ann;

//	CD_TIPO_ORGANO_ANN VARCHAR(5)
	private java.lang.String cd_organo_ann;

//	DS_ORGANO_ANN_NON_DEFINITO VARCHAR(100)
	private java.lang.String ds_organo_ann_non_definito;

//	ESERCIZIO_PROTOCOLLO DECIMAL(4,0)
    private java.lang.Integer esercizio_protocollo;

//	CD_PROTOCOLLO_GENERALE VARCHAR(50)
    private java.lang.String cd_protocollo_generale;
    
//	FL_ART82 CHAR(1) NOT NULL
	private java.lang.Boolean fl_art82;

//	FL_MEPA CHAR(1) 
	private java.lang.Boolean fl_mepa;

//	FL_PUBBLICA_CONTRATTO CHAR(1)
	private java.lang.Boolean fl_pubblica_contratto;
	
	private String cdCigExt;
	private String codfisPivaRupExt;
	private String codfisPivaFirmatarioExt;
	private String codfisPivaAggiudicatarioExt;
	private String codiceFlussoAcquisti;

	// PG_PROGETTO DECIMAL(10,0) NOT NULL
	private java.lang.Integer pg_progetto;

	// PG_PROGETTO VARCHAR2(3)
	private String tipo_dettaglio_contratto;

	public ContrattoBase() {
		super();
	}
	public ContrattoBase(java.lang.Integer esercizio, java.lang.String stato, java.lang.Long pg_contratto) {
		super(esercizio, stato, pg_contratto);
	}
	public java.lang.Integer getEsercizio_padre () {
		return esercizio_padre;
	}
	public void setEsercizio_padre(java.lang.Integer esercizio_padre)  {
		this.esercizio_padre=esercizio_padre;
	}
	public java.lang.Long getPg_contratto_padre () {
		return pg_contratto_padre;
	}
	public void setPg_contratto_padre(java.lang.Long pg_contratto_padre)  {
		this.pg_contratto_padre=pg_contratto_padre;
	}
	public java.sql.Timestamp getDt_registrazione () {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.lang.Integer getFig_giur_int () {
		return fig_giur_int;
	}
	public void setFig_giur_int(java.lang.Integer fig_giur_int)  {
		this.fig_giur_int=fig_giur_int;
	}
	public java.lang.Integer getFig_giur_est () {
		return fig_giur_est;
	}
	public void setFig_giur_est(java.lang.Integer fig_giur_est)  {
		this.fig_giur_est=fig_giur_est;
	}
	public java.lang.Integer getCd_terzo_resp () {
		return cd_terzo_resp;
	}
	public void setCd_terzo_resp(java.lang.Integer cd_terzo_resp)  {
		this.cd_terzo_resp=cd_terzo_resp;
	}
	public java.lang.String getResp_esterno () {
		return resp_esterno;
	}
	public void setResp_esterno(java.lang.String resp_esterno)  {
		this.resp_esterno=resp_esterno;
	}
	public java.lang.String getNatura_contabile () {
		return natura_contabile;
	}
	public void setNatura_contabile(java.lang.String tipo)  {
		this.natura_contabile=tipo;
	}
	public java.lang.String getCd_tipo_contratto () {
		return cd_tipo_contratto;
	}
	public void setCd_tipo_contratto(java.lang.String cd_tipo_contratto)  {
		this.cd_tipo_contratto=cd_tipo_contratto;
	}
	public java.lang.String getCd_proc_amm () {
		return cd_proc_amm;
	}
	public void setCd_proc_amm(java.lang.String proc_amm)  {
		this.cd_proc_amm=proc_amm;
	}
	public java.lang.String getOggetto () {
		return oggetto;
	}
	public void setOggetto(java.lang.String oggetto)  {
		this.oggetto=oggetto;
	}
	public java.lang.String getCd_protocollo () {
		return cd_protocollo;
	}
	public void setCd_protocollo(java.lang.String cd_protocollo)  {
		this.cd_protocollo=cd_protocollo;
	}
	public java.sql.Timestamp getDt_stipula () {
		return dt_stipula;
	}
	public void setDt_stipula(java.sql.Timestamp dt_stipula)  {
		this.dt_stipula=dt_stipula;
	}
	public java.sql.Timestamp getDt_inizio_validita () {
		return dt_inizio_validita;
	}
	public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita)  {
		this.dt_inizio_validita=dt_inizio_validita;
	}
	public java.sql.Timestamp getDt_fine_validita () {
		return dt_fine_validita;
	}
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita)  {
		this.dt_fine_validita=dt_fine_validita;
	}
	public java.math.BigDecimal getIm_contratto_attivo () {
		return im_contratto_attivo;
	}
	public void setIm_contratto_attivo(java.math.BigDecimal im_contratto)  {
		this.im_contratto_attivo=im_contratto;
	}
	public java.sql.Timestamp getDt_annullamento () {
		return dt_annullamento;
	}
	public void setDt_annullamento(java.sql.Timestamp dt_annullamento)  {
		this.dt_annullamento=dt_annullamento;
	}
	public java.lang.String getDs_annullamento () {
		return ds_annullamento;
	}
	public void setDs_annullamento(java.lang.String ds_annullamento)  {
		this.ds_annullamento=ds_annullamento;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_protocollo_generale() {
		return cd_protocollo_generale;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_organo() {
		return cd_organo;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_organo_ann() {
		return cd_organo_ann;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_tipo_atto() {
		return cd_tipo_atto;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_tipo_atto_ann() {
		return cd_tipo_atto_ann;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_protocollo() {
		return esercizio_protocollo;
	}

	/**
	 * @param string
	 */
	public void setCd_protocollo_generale(java.lang.String string) {
		cd_protocollo_generale = string;
	}

	/**
	 * @param string
	 */
	public void setCd_organo(java.lang.String string) {
		cd_organo = string;
	}

	/**
	 * @param string
	 */
	public void setCd_organo_ann(java.lang.String string) {
		cd_organo_ann = string;
	}

	/**
	 * @param string
	 */
	public void setCd_tipo_atto(java.lang.String string) {
		cd_tipo_atto = string;
	}

	/**
	 * @param string
	 */
	public void setCd_tipo_atto_ann(java.lang.String string) {
		cd_tipo_atto_ann = string;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio_protocollo(java.lang.Integer integer) {
		esercizio_protocollo = integer;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	/**
	 * 
	 * @param string
	 */
	public void setCd_unita_organizzativa(java.lang.String string) {
		cd_unita_organizzativa = string;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getCd_terzo_firmatario() {
		return cd_terzo_firmatario;
	}

	/**
	 * @param integer
	 */
	public void setCd_terzo_firmatario(java.lang.Integer integer) {
		cd_terzo_firmatario = integer;
	}

	/**
	 * @return
	 */
	public java.sql.Timestamp getDt_proroga() {
		return dt_proroga;
	}

	/**
	 * @param timestamp
	 */
	public void setDt_proroga(java.sql.Timestamp timestamp) {
		dt_proroga = timestamp;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_atto() {
		return ds_atto;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_atto_non_definito() {
		return ds_atto_non_definito;
	}

	/**
	 * @param string
	 */
	public void setDs_atto(java.lang.String string) {
		ds_atto = string;
	}

	/**
	 * @param string
	 */
	public void setDs_atto_non_definito(java.lang.String string) {
		ds_atto_non_definito = string;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_atto_ann() {
		return ds_atto_ann;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_atto_ann_non_definito() {
		return ds_atto_ann_non_definito;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_organo_ann_non_definito() {
		return ds_organo_ann_non_definito;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_organo_non_definito() {
		return ds_organo_non_definito;
	}

	/**
	 * @return
	 */
	public java.lang.Boolean getFl_art82() {
		return fl_art82;
	}

	/**
	 * @param string
	 */
	public void setDs_atto_ann(java.lang.String string) {
		ds_atto_ann = string;
	}

	/**
	 * @param string
	 */
	public void setDs_atto_ann_non_definito(java.lang.String string) {
		ds_atto_ann_non_definito = string;
	}

	/**
	 * @param string
	 */
	public void setDs_organo_ann_non_definito(java.lang.String string) {
		ds_organo_ann_non_definito = string;
	}

	/**
	 * @param string
	 */
	public void setDs_organo_non_definito(java.lang.String string) {
		ds_organo_non_definito = string;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_art82(java.lang.Boolean boolean1) {
		fl_art82 = boolean1;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_contratto_passivo() {
		return im_contratto_passivo;
	}

	/**
	 * @param decimal
	 */
	public void setIm_contratto_passivo(java.math.BigDecimal decimal) {
		im_contratto_passivo = decimal;
	}

	/**
	 * @return
	 */
	public java.lang.Long getStato_padre() {
		return stato_padre;
	}

	/**
	 * @param long1
	 */
	public void setStato_padre(java.lang.Long long1) {
		stato_padre = long1;
	}

	public java.lang.Boolean getFl_mepa() {
		return fl_mepa;
	}

	public void setFl_mepa(java.lang.Boolean fl_mepa) {
		this.fl_mepa = fl_mepa;
	}

	public java.lang.Boolean getFl_pubblica_contratto() {
		return fl_pubblica_contratto;
	}

	public void setFl_pubblica_contratto(java.lang.Boolean fl_pubblica_contratto) {
		this.fl_pubblica_contratto = fl_pubblica_contratto;
	}
	
	public java.lang.String getCdCigFatturaAttiva() {
		return cdCigFatturaAttiva;
	}
	public void setCdCigFatturaAttiva(java.lang.String cdCigFatturaAttiva) {
		this.cdCigFatturaAttiva = cdCigFatturaAttiva;
	}
	public java.math.BigDecimal getIm_contratto_passivo_netto() {
		return im_contratto_passivo_netto;
	}
	public void setIm_contratto_passivo_netto(
			java.math.BigDecimal im_contratto_passivo_netto) {
		this.im_contratto_passivo_netto = im_contratto_passivo_netto;
	}
	public String getCdCigExt() {
		return cdCigExt;
	}
	public void setCdCigExt(String cdCigExt) {
		this.cdCigExt = cdCigExt;
	}
	public String getCodfisPivaRupExt() {
		return codfisPivaRupExt;
	}
	public void setCodfisPivaRupExt(String codfisPivaRupExt) {
		this.codfisPivaRupExt = codfisPivaRupExt;
	}
	public String getCodfisPivaFirmatarioExt() {
		return codfisPivaFirmatarioExt;
	}
	public void setCodfisPivaFirmatarioExt(String codfisPivaFirmatarioExt) {
		this.codfisPivaFirmatarioExt = codfisPivaFirmatarioExt;
	}
	public String getCodfisPivaAggiudicatarioExt() {
		return codfisPivaAggiudicatarioExt;
	}
	public void setCodfisPivaAggiudicatarioExt(String codfisPivaAggiudicatarioExt) {
		this.codfisPivaAggiudicatarioExt = codfisPivaAggiudicatarioExt;
	}
	public String getCodiceFlussoAcquisti() {
		return codiceFlussoAcquisti;
	}
	public void setCodiceFlussoAcquisti(String codiceFlussoAcquisti) {
		this.codiceFlussoAcquisti = codiceFlussoAcquisti;
	}
	
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}
	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}

	public String getTipo_dettaglio_contratto() {
		return tipo_dettaglio_contratto;
	}

	public void setTipo_dettaglio_contratto(String tipo_dettaglio_contratto) {
		this.tipo_dettaglio_contratto = tipo_dettaglio_contratto;
	}
}