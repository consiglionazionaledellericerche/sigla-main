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
* Date 13/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_dpdg_aggregato_spe_det_dBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    CD_FUNZIONE VARCHAR(2) NOT NULL
	private java.lang.String cd_funzione;
 
//    CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;
 
//    IM_RH_CCS_COSTI DECIMAL(15,2)
	private java.math.BigDecimal im_rh_ccs_costi;
 
//    IM_RI_CCS_SPESE_ODC DECIMAL(15,2)
	private java.math.BigDecimal im_ri_ccs_spese_odc;
 
//    IM_RJ_CCS_SPESE_ODC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo;
 
//    IM_RK_CCS_SPESE_OGC DECIMAL(15,2)
	private java.math.BigDecimal im_rk_ccs_spese_ogc;
 
//    IM_RL_CCS_SPESE_OGC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo;
 
//    IM_RM_CSS_AMMORTAMENTI DECIMAL(15,2)
	private java.math.BigDecimal im_rm_css_ammortamenti;
 
//    IM_RN_CSS_RIMANENZE DECIMAL(15,2)
	private java.math.BigDecimal im_rn_css_rimanenze;
 
//    IM_RO_CSS_ALTRI_COSTI DECIMAL(15,2)
	private java.math.BigDecimal im_ro_css_altri_costi;
 
//    IM_RP_CSS_VERSO_ALTRO_CDR DECIMAL(15,2)
	private java.math.BigDecimal im_rp_css_verso_altro_cdr;
 
//    IM_RQ_SSC_COSTI_ODC DECIMAL(15,2)
	private java.math.BigDecimal im_rq_ssc_costi_odc;
 
//    IM_RR_SSC_COSTI_ODC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo;
 
//    IM_RS_SSC_COSTI_OGC DECIMAL(15,2)
	private java.math.BigDecimal im_rs_ssc_costi_ogc;
 
//    IM_RT_SSC_COSTI_OGC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo;
 
//    IM_RU_SPESE_COSTI_ALTRUI DECIMAL(15,2)
	private java.math.BigDecimal im_ru_spese_costi_altrui;
 
//    IM_RV_PAGAMENTI DECIMAL(15,2)
	private java.math.BigDecimal im_rv_pagamenti;
 
	public V_dpdg_aggregato_spe_det_dBase() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getCd_funzione () {
		return cd_funzione;
	}
	public void setCd_funzione(java.lang.String cd_funzione)  {
		this.cd_funzione=cd_funzione;
	}
	public java.lang.String getCd_natura () {
		return cd_natura;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
	}
	public java.math.BigDecimal getIm_rh_ccs_costi () {
		return im_rh_ccs_costi;
	}
	public void setIm_rh_ccs_costi(java.math.BigDecimal im_rh_ccs_costi)  {
		this.im_rh_ccs_costi=im_rh_ccs_costi;
	}
	public java.math.BigDecimal getIm_ri_ccs_spese_odc () {
		return im_ri_ccs_spese_odc;
	}
	public void setIm_ri_ccs_spese_odc(java.math.BigDecimal im_ri_ccs_spese_odc)  {
		this.im_ri_ccs_spese_odc=im_ri_ccs_spese_odc;
	}
	public java.math.BigDecimal getIm_rj_ccs_spese_odc_altra_uo () {
		return im_rj_ccs_spese_odc_altra_uo;
	}
	public void setIm_rj_ccs_spese_odc_altra_uo(java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo)  {
		this.im_rj_ccs_spese_odc_altra_uo=im_rj_ccs_spese_odc_altra_uo;
	}
	public java.math.BigDecimal getIm_rk_ccs_spese_ogc () {
		return im_rk_ccs_spese_ogc;
	}
	public void setIm_rk_ccs_spese_ogc(java.math.BigDecimal im_rk_ccs_spese_ogc)  {
		this.im_rk_ccs_spese_ogc=im_rk_ccs_spese_ogc;
	}
	public java.math.BigDecimal getIm_rl_ccs_spese_ogc_altra_uo () {
		return im_rl_ccs_spese_ogc_altra_uo;
	}
	public void setIm_rl_ccs_spese_ogc_altra_uo(java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo)  {
		this.im_rl_ccs_spese_ogc_altra_uo=im_rl_ccs_spese_ogc_altra_uo;
	}
	public java.math.BigDecimal getIm_rm_css_ammortamenti () {
		return im_rm_css_ammortamenti;
	}
	public void setIm_rm_css_ammortamenti(java.math.BigDecimal im_rm_css_ammortamenti)  {
		this.im_rm_css_ammortamenti=im_rm_css_ammortamenti;
	}
	public java.math.BigDecimal getIm_rn_css_rimanenze () {
		return im_rn_css_rimanenze;
	}
	public void setIm_rn_css_rimanenze(java.math.BigDecimal im_rn_css_rimanenze)  {
		this.im_rn_css_rimanenze=im_rn_css_rimanenze;
	}
	public java.math.BigDecimal getIm_ro_css_altri_costi () {
		return im_ro_css_altri_costi;
	}
	public void setIm_ro_css_altri_costi(java.math.BigDecimal im_ro_css_altri_costi)  {
		this.im_ro_css_altri_costi=im_ro_css_altri_costi;
	}
	public java.math.BigDecimal getIm_rp_css_verso_altro_cdr () {
		return im_rp_css_verso_altro_cdr;
	}
	public void setIm_rp_css_verso_altro_cdr(java.math.BigDecimal im_rp_css_verso_altro_cdr)  {
		this.im_rp_css_verso_altro_cdr=im_rp_css_verso_altro_cdr;
	}
	public java.math.BigDecimal getIm_rq_ssc_costi_odc () {
		return im_rq_ssc_costi_odc;
	}
	public void setIm_rq_ssc_costi_odc(java.math.BigDecimal im_rq_ssc_costi_odc)  {
		this.im_rq_ssc_costi_odc=im_rq_ssc_costi_odc;
	}
	public java.math.BigDecimal getIm_rr_ssc_costi_odc_altra_uo () {
		return im_rr_ssc_costi_odc_altra_uo;
	}
	public void setIm_rr_ssc_costi_odc_altra_uo(java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo)  {
		this.im_rr_ssc_costi_odc_altra_uo=im_rr_ssc_costi_odc_altra_uo;
	}
	public java.math.BigDecimal getIm_rs_ssc_costi_ogc () {
		return im_rs_ssc_costi_ogc;
	}
	public void setIm_rs_ssc_costi_ogc(java.math.BigDecimal im_rs_ssc_costi_ogc)  {
		this.im_rs_ssc_costi_ogc=im_rs_ssc_costi_ogc;
	}
	public java.math.BigDecimal getIm_rt_ssc_costi_ogc_altra_uo () {
		return im_rt_ssc_costi_ogc_altra_uo;
	}
	public void setIm_rt_ssc_costi_ogc_altra_uo(java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo)  {
		this.im_rt_ssc_costi_ogc_altra_uo=im_rt_ssc_costi_ogc_altra_uo;
	}
	public java.math.BigDecimal getIm_ru_spese_costi_altrui () {
		return im_ru_spese_costi_altrui;
	}
	public void setIm_ru_spese_costi_altrui(java.math.BigDecimal im_ru_spese_costi_altrui)  {
		this.im_ru_spese_costi_altrui=im_ru_spese_costi_altrui;
	}
	public java.math.BigDecimal getIm_rv_pagamenti () {
		return im_rv_pagamenti;
	}
	public void setIm_rv_pagamenti(java.math.BigDecimal im_rv_pagamenti)  {
		this.im_rv_pagamenti=im_rv_pagamenti;
	}
}