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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_pdg_obbligazione_speBase extends V_pdg_obbligazione_speKey implements Keyed {

	// CD_CENTRO_RESPONSABILITA_CLGS VARCHAR(30)
	private java.lang.String cd_centro_responsabilita_clgs;

	// CD_ELEMENTO_VOCE_CLGS VARCHAR(20)
	private java.lang.String cd_elemento_voce_clgs;

	// CD_FUNZIONE VARCHAR(2)
	private java.lang.String cd_funzione;

	// CD_LINEA_ATTIVITA_CLGS VARCHAR(10)
	private java.lang.String cd_linea_attivita_clgs;

	// CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;

	// CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;

	// DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;


	// IM_RAC_A2_SPESE_ODC DECIMAL(22,0)
	private java.math.BigDecimal im_rac_a2_spese_odc;

	// IM_RAD_A2_SPESE_ODC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_rad_a2_spese_odc_altra_uo;

	// IM_RAE_A2_SPESE_OGC DECIMAL(22,0)
	private java.math.BigDecimal im_rae_a2_spese_ogc;

	// IM_RAF_A2_SPESE_OGC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_raf_a2_spese_ogc_altra_uo;

	// IM_RAG_A2_SPESE_COSTI_ALTRUI DECIMAL(22,0)
	private java.math.BigDecimal im_rag_a2_spese_costi_altrui;

	// IM_RAL_A3_SPESE_ODC DECIMAL(22,0)
	private java.math.BigDecimal im_ral_a3_spese_odc;

	// IM_RAM_A3_SPESE_ODC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_ram_a3_spese_odc_altra_uo;

	// IM_RAN_A3_SPESE_OGC DECIMAL(22,0)
	private java.math.BigDecimal im_ran_a3_spese_ogc;

	// IM_RAO_A3_SPESE_OGC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_rao_a3_spese_ogc_altra_uo;

	// IM_RAP_A3_SPESE_COSTI_ALTRUI DECIMAL(22,0)
	private java.math.BigDecimal im_rap_a3_spese_costi_altrui;

	// IM_RI_CCS_SPESE_ODC DECIMAL(22,0)
	private java.math.BigDecimal im_ri_ccs_spese_odc;

	// IM_RJ_CCS_SPESE_ODC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo;

	// IM_RK_CCS_SPESE_OGC DECIMAL(22,0)
	private java.math.BigDecimal im_rk_ccs_spese_ogc;

	// IM_RL_CCS_SPESE_OGC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo;

	// IM_RQ_SSC_COSTI_ODC DECIMAL(22,0)
	private java.math.BigDecimal im_rq_ssc_costi_odc;

	// IM_RR_SSC_COSTI_ODC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo;

	// IM_RS_SSC_COSTI_OGC DECIMAL(22,0)
	private java.math.BigDecimal im_rs_ssc_costi_ogc;

	// IM_RT_SSC_COSTI_OGC_ALTRA_UO DECIMAL(22,0)
	private java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo;

	// IM_RU_SPESE_COSTI_ALTRUI DECIMAL(22,0)
	private java.math.BigDecimal im_ru_spese_costi_altrui;

	// TI_APPARTENENZA_CLGS CHAR(1)
	private java.lang.String ti_appartenenza_clgs;

	// TI_GESTIONE_CLGS CHAR(1)
	private java.lang.String ti_gestione_clgs;

public V_pdg_obbligazione_speBase() {
	super();
}
public V_pdg_obbligazione_speBase(java.lang.String categoria_dettaglio,java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String stato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(categoria_dettaglio,cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,stato,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo cd_centro_responsabilita_clgs
 */
public java.lang.String getCd_centro_responsabilita_clgs() {
	return cd_centro_responsabilita_clgs;
}
/* 
 * Getter dell'attributo cd_elemento_voce_clgs
 */
public java.lang.String getCd_elemento_voce_clgs() {
	return cd_elemento_voce_clgs;
}
/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}
/* 
 * Getter dell'attributo cd_linea_attivita_clgs
 */
public java.lang.String getCd_linea_attivita_clgs() {
	return cd_linea_attivita_clgs;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
/* 
 * Getter dell'attributo ds_linea_attivita
 */
public java.lang.String getDs_linea_attivita() {
	return ds_linea_attivita;
}
/* 
 * Getter dell'attributo im_rac_a2_spese_odc
 */
public java.math.BigDecimal getIm_rac_a2_spese_odc() {
	return im_rac_a2_spese_odc;
}
/* 
 * Getter dell'attributo im_rad_a2_spese_odc_altra_uo
 */
public java.math.BigDecimal getIm_rad_a2_spese_odc_altra_uo() {
	return im_rad_a2_spese_odc_altra_uo;
}
/* 
 * Getter dell'attributo im_rae_a2_spese_ogc
 */
public java.math.BigDecimal getIm_rae_a2_spese_ogc() {
	return im_rae_a2_spese_ogc;
}
/* 
 * Getter dell'attributo im_raf_a2_spese_ogc_altra_uo
 */
public java.math.BigDecimal getIm_raf_a2_spese_ogc_altra_uo() {
	return im_raf_a2_spese_ogc_altra_uo;
}
/* 
 * Getter dell'attributo im_rag_a2_spese_costi_altrui
 */
public java.math.BigDecimal getIm_rag_a2_spese_costi_altrui() {
	return im_rag_a2_spese_costi_altrui;
}
/* 
 * Getter dell'attributo im_ral_a3_spese_odc
 */
public java.math.BigDecimal getIm_ral_a3_spese_odc() {
	return im_ral_a3_spese_odc;
}
/* 
 * Getter dell'attributo im_ram_a3_spese_odc_altra_uo
 */
public java.math.BigDecimal getIm_ram_a3_spese_odc_altra_uo() {
	return im_ram_a3_spese_odc_altra_uo;
}
/* 
 * Getter dell'attributo im_ran_a3_spese_ogc
 */
public java.math.BigDecimal getIm_ran_a3_spese_ogc() {
	return im_ran_a3_spese_ogc;
}
/* 
 * Getter dell'attributo im_rao_a3_spese_ogc_altra_uo
 */
public java.math.BigDecimal getIm_rao_a3_spese_ogc_altra_uo() {
	return im_rao_a3_spese_ogc_altra_uo;
}
/* 
 * Getter dell'attributo im_rap_a3_spese_costi_altrui
 */
public java.math.BigDecimal getIm_rap_a3_spese_costi_altrui() {
	return im_rap_a3_spese_costi_altrui;
}
/* 
 * Getter dell'attributo im_ri_ccs_spese_odc
 */
public java.math.BigDecimal getIm_ri_ccs_spese_odc() {
	return im_ri_ccs_spese_odc;
}
/* 
 * Getter dell'attributo im_rj_ccs_spese_odc_altra_uo
 */
public java.math.BigDecimal getIm_rj_ccs_spese_odc_altra_uo() {
	return im_rj_ccs_spese_odc_altra_uo;
}
/* 
 * Getter dell'attributo im_rk_ccs_spese_ogc
 */
public java.math.BigDecimal getIm_rk_ccs_spese_ogc() {
	return im_rk_ccs_spese_ogc;
}
/* 
 * Getter dell'attributo im_rl_ccs_spese_ogc_altra_uo
 */
public java.math.BigDecimal getIm_rl_ccs_spese_ogc_altra_uo() {
	return im_rl_ccs_spese_ogc_altra_uo;
}
/* 
 * Getter dell'attributo im_rq_ssc_costi_odc
 */
public java.math.BigDecimal getIm_rq_ssc_costi_odc() {
	return im_rq_ssc_costi_odc;
}
/* 
 * Getter dell'attributo im_rr_ssc_costi_odc_altra_uo
 */
public java.math.BigDecimal getIm_rr_ssc_costi_odc_altra_uo() {
	return im_rr_ssc_costi_odc_altra_uo;
}
/* 
 * Getter dell'attributo im_rs_ssc_costi_ogc
 */
public java.math.BigDecimal getIm_rs_ssc_costi_ogc() {
	return im_rs_ssc_costi_ogc;
}
/* 
 * Getter dell'attributo im_rt_ssc_costi_ogc_altra_uo
 */
public java.math.BigDecimal getIm_rt_ssc_costi_ogc_altra_uo() {
	return im_rt_ssc_costi_ogc_altra_uo;
}
/* 
 * Getter dell'attributo im_ru_spese_costi_altrui
 */
public java.math.BigDecimal getIm_ru_spese_costi_altrui() {
	return im_ru_spese_costi_altrui;
}
/* 
 * Getter dell'attributo ti_appartenenza_clgs
 */
public java.lang.String getTi_appartenenza_clgs() {
	return ti_appartenenza_clgs;
}
/* 
 * Getter dell'attributo ti_gestione_clgs
 */
public java.lang.String getTi_gestione_clgs() {
	return ti_gestione_clgs;
}
/* 
 * Setter dell'attributo cd_centro_responsabilita_clgs
 */
public void setCd_centro_responsabilita_clgs(java.lang.String cd_centro_responsabilita_clgs) {
	this.cd_centro_responsabilita_clgs = cd_centro_responsabilita_clgs;
}
/* 
 * Setter dell'attributo cd_elemento_voce_clgs
 */
public void setCd_elemento_voce_clgs(java.lang.String cd_elemento_voce_clgs) {
	this.cd_elemento_voce_clgs = cd_elemento_voce_clgs;
}
/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
/* 
 * Setter dell'attributo cd_linea_attivita_clgs
 */
public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs) {
	this.cd_linea_attivita_clgs = cd_linea_attivita_clgs;
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
/* 
 * Setter dell'attributo ds_linea_attivita
 */
public void setDs_linea_attivita(java.lang.String ds_linea_attivita) {
	this.ds_linea_attivita = ds_linea_attivita;
}
/* 
 * Setter dell'attributo im_rac_a2_spese_odc
 */
public void setIm_rac_a2_spese_odc(java.math.BigDecimal im_rac_a2_spese_odc) {
	this.im_rac_a2_spese_odc = im_rac_a2_spese_odc;
}
/* 
 * Setter dell'attributo im_rad_a2_spese_odc_altra_uo
 */
public void setIm_rad_a2_spese_odc_altra_uo(java.math.BigDecimal im_rad_a2_spese_odc_altra_uo) {
	this.im_rad_a2_spese_odc_altra_uo = im_rad_a2_spese_odc_altra_uo;
}
/* 
 * Setter dell'attributo im_rae_a2_spese_ogc
 */
public void setIm_rae_a2_spese_ogc(java.math.BigDecimal im_rae_a2_spese_ogc) {
	this.im_rae_a2_spese_ogc = im_rae_a2_spese_ogc;
}
/* 
 * Setter dell'attributo im_raf_a2_spese_ogc_altra_uo
 */
public void setIm_raf_a2_spese_ogc_altra_uo(java.math.BigDecimal im_raf_a2_spese_ogc_altra_uo) {
	this.im_raf_a2_spese_ogc_altra_uo = im_raf_a2_spese_ogc_altra_uo;
}
/* 
 * Setter dell'attributo im_rag_a2_spese_costi_altrui
 */
public void setIm_rag_a2_spese_costi_altrui(java.math.BigDecimal im_rag_a2_spese_costi_altrui) {
	this.im_rag_a2_spese_costi_altrui = im_rag_a2_spese_costi_altrui;
}
/* 
 * Setter dell'attributo im_ral_a3_spese_odc
 */
public void setIm_ral_a3_spese_odc(java.math.BigDecimal im_ral_a3_spese_odc) {
	this.im_ral_a3_spese_odc = im_ral_a3_spese_odc;
}
/* 
 * Setter dell'attributo im_ram_a3_spese_odc_altra_uo
 */
public void setIm_ram_a3_spese_odc_altra_uo(java.math.BigDecimal im_ram_a3_spese_odc_altra_uo) {
	this.im_ram_a3_spese_odc_altra_uo = im_ram_a3_spese_odc_altra_uo;
}
/* 
 * Setter dell'attributo im_ran_a3_spese_ogc
 */
public void setIm_ran_a3_spese_ogc(java.math.BigDecimal im_ran_a3_spese_ogc) {
	this.im_ran_a3_spese_ogc = im_ran_a3_spese_ogc;
}
/* 
 * Setter dell'attributo im_rao_a3_spese_ogc_altra_uo
 */
public void setIm_rao_a3_spese_ogc_altra_uo(java.math.BigDecimal im_rao_a3_spese_ogc_altra_uo) {
	this.im_rao_a3_spese_ogc_altra_uo = im_rao_a3_spese_ogc_altra_uo;
}
/* 
 * Setter dell'attributo im_rap_a3_spese_costi_altrui
 */
public void setIm_rap_a3_spese_costi_altrui(java.math.BigDecimal im_rap_a3_spese_costi_altrui) {
	this.im_rap_a3_spese_costi_altrui = im_rap_a3_spese_costi_altrui;
}
/* 
 * Setter dell'attributo im_ri_ccs_spese_odc
 */
public void setIm_ri_ccs_spese_odc(java.math.BigDecimal im_ri_ccs_spese_odc) {
	this.im_ri_ccs_spese_odc = im_ri_ccs_spese_odc;
}
/* 
 * Setter dell'attributo im_rj_ccs_spese_odc_altra_uo
 */
public void setIm_rj_ccs_spese_odc_altra_uo(java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo) {
	this.im_rj_ccs_spese_odc_altra_uo = im_rj_ccs_spese_odc_altra_uo;
}
/* 
 * Setter dell'attributo im_rk_ccs_spese_ogc
 */
public void setIm_rk_ccs_spese_ogc(java.math.BigDecimal im_rk_ccs_spese_ogc) {
	this.im_rk_ccs_spese_ogc = im_rk_ccs_spese_ogc;
}
/* 
 * Setter dell'attributo im_rl_ccs_spese_ogc_altra_uo
 */
public void setIm_rl_ccs_spese_ogc_altra_uo(java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo) {
	this.im_rl_ccs_spese_ogc_altra_uo = im_rl_ccs_spese_ogc_altra_uo;
}
/* 
 * Setter dell'attributo im_rq_ssc_costi_odc
 */
public void setIm_rq_ssc_costi_odc(java.math.BigDecimal im_rq_ssc_costi_odc) {
	this.im_rq_ssc_costi_odc = im_rq_ssc_costi_odc;
}
/* 
 * Setter dell'attributo im_rr_ssc_costi_odc_altra_uo
 */
public void setIm_rr_ssc_costi_odc_altra_uo(java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo) {
	this.im_rr_ssc_costi_odc_altra_uo = im_rr_ssc_costi_odc_altra_uo;
}
/* 
 * Setter dell'attributo im_rs_ssc_costi_ogc
 */
public void setIm_rs_ssc_costi_ogc(java.math.BigDecimal im_rs_ssc_costi_ogc) {
	this.im_rs_ssc_costi_ogc = im_rs_ssc_costi_ogc;
}
/* 
 * Setter dell'attributo im_rt_ssc_costi_ogc_altra_uo
 */
public void setIm_rt_ssc_costi_ogc_altra_uo(java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo) {
	this.im_rt_ssc_costi_ogc_altra_uo = im_rt_ssc_costi_ogc_altra_uo;
}
/* 
 * Setter dell'attributo im_ru_spese_costi_altrui
 */
public void setIm_ru_spese_costi_altrui(java.math.BigDecimal im_ru_spese_costi_altrui) {
	this.im_ru_spese_costi_altrui = im_ru_spese_costi_altrui;
}
/* 
 * Setter dell'attributo ti_appartenenza_clgs
 */
public void setTi_appartenenza_clgs(java.lang.String ti_appartenenza_clgs) {
	this.ti_appartenenza_clgs = ti_appartenenza_clgs;
}
/* 
 * Setter dell'attributo ti_gestione_clgs
 */
public void setTi_gestione_clgs(java.lang.String ti_gestione_clgs) {
	this.ti_gestione_clgs = ti_gestione_clgs;
}
	/**
	 * @return
	 */
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}

	/**
	 * @param string
	 */
	public void setCd_progetto(java.lang.String string) {
		cd_progetto = string;
	}

}
