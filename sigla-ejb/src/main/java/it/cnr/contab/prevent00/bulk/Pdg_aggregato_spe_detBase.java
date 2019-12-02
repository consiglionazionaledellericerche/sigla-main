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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_aggregato_spe_detBase extends Pdg_aggregato_spe_detKey implements Keyed {
	// IM_RAA_A2_COSTI_FINALI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_raa_a2_costi_finali;

	// IM_RAB_A2_COSTI_ALTRO_CDR DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rab_a2_costi_altro_cdr;

	// IM_RAC_A2_SPESE_ODC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rac_a2_spese_odc;

	// IM_RAD_A2_SPESE_ODC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rad_a2_spese_odc_altra_uo;

	// IM_RAE_A2_SPESE_OGC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rae_a2_spese_ogc;

	// IM_RAF_A2_SPESE_OGC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_raf_a2_spese_ogc_altra_uo;

	// IM_RAG_A2_SPESE_COSTI_ALTRUI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rag_a2_spese_costi_altrui;

	// IM_RAH_A3_COSTI_FINALI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rah_a3_costi_finali;

	// IM_RAI_A3_COSTI_ALTRO_CDR DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rai_a3_costi_altro_cdr;

	// IM_RAL_A3_SPESE_ODC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ral_a3_spese_odc;

	// IM_RAM_A3_SPESE_ODC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ram_a3_spese_odc_altra_uo;

	// IM_RAN_A3_SPESE_OGC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ran_a3_spese_ogc;

	// IM_RAO_A3_SPESE_OGC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rao_a3_spese_ogc_altra_uo;

	// IM_RAP_A3_SPESE_COSTI_ALTRUI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rap_a3_spese_costi_altrui;

	// IM_RH_CCS_COSTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rh_ccs_costi;

	// IM_RI_CCS_SPESE_ODC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ri_ccs_spese_odc;

	// IM_RJ_CCS_SPESE_ODC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo;

	// IM_RK_CCS_SPESE_OGC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rk_ccs_spese_ogc;

	// IM_RL_CCS_SPESE_OGC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo;

	// IM_RM_CSS_AMMORTAMENTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rm_css_ammortamenti;

	// IM_RN_CSS_RIMANENZE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rn_css_rimanenze;

	// IM_RO_CSS_ALTRI_COSTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ro_css_altri_costi;

	// IM_RP_CSS_VERSO_ALTRO_CDR DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rp_css_verso_altro_cdr;

	// IM_RQ_SSC_COSTI_ODC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rq_ssc_costi_odc;

	// IM_RR_SSC_COSTI_ODC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo;

	// IM_RS_SSC_COSTI_OGC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rs_ssc_costi_ogc;

	// IM_RT_SSC_COSTI_OGC_ALTRA_UO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo;

	// IM_RU_SPESE_COSTI_ALTRUI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ru_spese_costi_altrui;

	// IM_RV_PAGAMENTI DECIMAL(15,2)
	private java.math.BigDecimal im_rv_pagamenti;

public Pdg_aggregato_spe_detBase() {
	super();
}
public Pdg_aggregato_spe_detBase(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_funzione,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_aggregato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_cds,cd_centro_responsabilita,cd_elemento_voce,cd_funzione,cd_natura,esercizio,ti_aggregato,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo im_raa_a2_costi_finali
 */
public java.math.BigDecimal getIm_raa_a2_costi_finali() {
	return im_raa_a2_costi_finali;
}
/* 
 * Getter dell'attributo im_rab_a2_costi_altro_cdr
 */
public java.math.BigDecimal getIm_rab_a2_costi_altro_cdr() {
	return im_rab_a2_costi_altro_cdr;
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
 * Getter dell'attributo im_rah_a3_costi_finali
 */
public java.math.BigDecimal getIm_rah_a3_costi_finali() {
	return im_rah_a3_costi_finali;
}
/* 
 * Getter dell'attributo im_rai_a3_costi_altro_cdr
 */
public java.math.BigDecimal getIm_rai_a3_costi_altro_cdr() {
	return im_rai_a3_costi_altro_cdr;
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
 * Getter dell'attributo im_rh_ccs_costi
 */
public java.math.BigDecimal getIm_rh_ccs_costi() {
	return im_rh_ccs_costi;
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
 * Getter dell'attributo im_rm_css_ammortamenti
 */
public java.math.BigDecimal getIm_rm_css_ammortamenti() {
	return im_rm_css_ammortamenti;
}
/* 
 * Getter dell'attributo im_rn_css_rimanenze
 */
public java.math.BigDecimal getIm_rn_css_rimanenze() {
	return im_rn_css_rimanenze;
}
/* 
 * Getter dell'attributo im_ro_css_altri_costi
 */
public java.math.BigDecimal getIm_ro_css_altri_costi() {
	return im_ro_css_altri_costi;
}
/* 
 * Getter dell'attributo im_rp_css_verso_altro_cdr
 */
public java.math.BigDecimal getIm_rp_css_verso_altro_cdr() {
	return im_rp_css_verso_altro_cdr;
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
 * Getter dell'attributo im_rv_pagamenti
 */
public java.math.BigDecimal getIm_rv_pagamenti() {
	return im_rv_pagamenti;
}
/* 
 * Setter dell'attributo im_raa_a2_costi_finali
 */
public void setIm_raa_a2_costi_finali(java.math.BigDecimal im_raa_a2_costi_finali) {
	this.im_raa_a2_costi_finali = im_raa_a2_costi_finali;
}
/* 
 * Setter dell'attributo im_rab_a2_costi_altro_cdr
 */
public void setIm_rab_a2_costi_altro_cdr(java.math.BigDecimal im_rab_a2_costi_altro_cdr) {
	this.im_rab_a2_costi_altro_cdr = im_rab_a2_costi_altro_cdr;
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
 * Setter dell'attributo im_rah_a3_costi_finali
 */
public void setIm_rah_a3_costi_finali(java.math.BigDecimal im_rah_a3_costi_finali) {
	this.im_rah_a3_costi_finali = im_rah_a3_costi_finali;
}
/* 
 * Setter dell'attributo im_rai_a3_costi_altro_cdr
 */
public void setIm_rai_a3_costi_altro_cdr(java.math.BigDecimal im_rai_a3_costi_altro_cdr) {
	this.im_rai_a3_costi_altro_cdr = im_rai_a3_costi_altro_cdr;
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
 * Setter dell'attributo im_rh_ccs_costi
 */
public void setIm_rh_ccs_costi(java.math.BigDecimal im_rh_ccs_costi) {
	this.im_rh_ccs_costi = im_rh_ccs_costi;
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
 * Setter dell'attributo im_rm_css_ammortamenti
 */
public void setIm_rm_css_ammortamenti(java.math.BigDecimal im_rm_css_ammortamenti) {
	this.im_rm_css_ammortamenti = im_rm_css_ammortamenti;
}
/* 
 * Setter dell'attributo im_rn_css_rimanenze
 */
public void setIm_rn_css_rimanenze(java.math.BigDecimal im_rn_css_rimanenze) {
	this.im_rn_css_rimanenze = im_rn_css_rimanenze;
}
/* 
 * Setter dell'attributo im_ro_css_altri_costi
 */
public void setIm_ro_css_altri_costi(java.math.BigDecimal im_ro_css_altri_costi) {
	this.im_ro_css_altri_costi = im_ro_css_altri_costi;
}
/* 
 * Setter dell'attributo im_rp_css_verso_altro_cdr
 */
public void setIm_rp_css_verso_altro_cdr(java.math.BigDecimal im_rp_css_verso_altro_cdr) {
	this.im_rp_css_verso_altro_cdr = im_rp_css_verso_altro_cdr;
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
 * Setter dell'attributo im_rv_pagamenti
 */
public void setIm_rv_pagamenti(java.math.BigDecimal im_rv_pagamenti) {
	this.im_rv_pagamenti = im_rv_pagamenti;
}
}
