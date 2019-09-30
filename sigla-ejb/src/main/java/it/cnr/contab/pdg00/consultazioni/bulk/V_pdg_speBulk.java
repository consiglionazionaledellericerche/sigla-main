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
* Date 20/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_pdg_speBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    DS_DIPARTIMENTO VARCHAR(300)
	private java.lang.String ds_dipartimento;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(400)
	private java.lang.String ds_progetto;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(400)
	private java.lang.String ds_commessa;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(400)
	private java.lang.String ds_modulo;
 
//    TIPO_PROGETTO VARCHAR(10)
	private java.lang.String tipo_progetto;
 
//    CDS VARCHAR(30)
	private java.lang.String cds;
 
//    DS_CDS VARCHAR(300)
	private java.lang.String ds_cds;
 
//    UO VARCHAR(30)
	private java.lang.String uo;
 
//    CDR VARCHAR(30)
	private java.lang.String cdr;
 
//    TITOLO VARCHAR(20)
	private java.lang.String titolo;
 
//    DS_TITOLO VARCHAR(100)
	private java.lang.String ds_titolo;
 
//    CODICE_CLAS_SPESA VARCHAR(20)
	private java.lang.String codice_clas_spesa;
 
//    DS_CLASSIFICAZIONE_SPESA VARCHAR(400)
	private java.lang.String ds_classificazione_spesa;
 
//    ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String elemento_voce;
 
//    ORIGINE VARCHAR(3)
	private java.lang.String origine;
 
//    CATEGORIA_DETTAGLIO VARCHAR(3)
	private java.lang.String categoria_dettaglio;
 
//    NATURA VARCHAR(1)
	private java.lang.String natura;
 
//    IM_RI_CCS_SPESE_ODC DECIMAL(15,2)
	private java.math.BigDecimal im_ri_ccs_spese_odc;
 
//    IM_RK_CCS_SPESE_OGC DECIMAL(15,2)
	private java.math.BigDecimal im_rk_ccs_spese_ogc;
 
//    IM_RQ_SSC_COSTI_ODC DECIMAL(15,2)
	private java.math.BigDecimal im_rq_ssc_costi_odc;
 
//    IM_RS_SSC_COSTI_OGC DECIMAL(15,2)
	private java.math.BigDecimal im_rs_ssc_costi_ogc;
 
//    IM_RJ_CCS_SPESE_ODC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo;
 
//    IM_RL_CCS_SPESE_OGC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo;
 
//    IM_RR_SSC_COSTI_ODC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo;
 
//    IM_RT_SSC_COSTI_OGC_ALTRA_UO DECIMAL(15,2)
	private java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo;
 
//    IM_RM_CSS_AMMORTAMENTI DECIMAL(15,2)
	private java.math.BigDecimal im_rm_css_ammortamenti;
 
//    IM_RN_CSS_RIMANENZE DECIMAL(15,2)
	private java.math.BigDecimal im_rn_css_rimanenze;
 
//    IM_RO_CSS_ALTRI_COSTI DECIMAL(15,2)
	private java.math.BigDecimal im_ro_css_altri_costi;
 
//    IM_RP_CSS_VERSO_ALTRO_CDR DECIMAL(15,2)
	private java.math.BigDecimal im_rp_css_verso_altro_cdr;
 
//    IM_RU_SPESE_COSTI_ALTRUI DECIMAL(15,2)
	private java.math.BigDecimal im_ru_spese_costi_altrui;
 
	public V_pdg_speBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_dipartimento () {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getDs_dipartimento () {
		return ds_dipartimento;
	}
	public void setDs_dipartimento(java.lang.String ds_dipartimento)  {
		this.ds_dipartimento=ds_dipartimento;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getDs_progetto () {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.String getCd_commessa () {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa () {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.String getCd_modulo () {
		return cd_modulo;
	}
	public void setCd_modulo(java.lang.String cd_modulo)  {
		this.cd_modulo=cd_modulo;
	}
	public java.lang.String getDs_modulo () {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getTipo_progetto () {
		return tipo_progetto;
	}
	public void setTipo_progetto(java.lang.String tipo_progetto)  {
		this.tipo_progetto=tipo_progetto;
	}
	public java.lang.String getCds () {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getDs_cds () {
		return ds_cds;
	}
	public void setDs_cds(java.lang.String ds_cds)  {
		this.ds_cds=ds_cds;
	}
	public java.lang.String getUo () {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.String getCdr () {
		return cdr;
	}
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	public java.lang.String getTitolo () {
		return titolo;
	}
	public void setTitolo(java.lang.String titolo)  {
		this.titolo=titolo;
	}
	public java.lang.String getDs_titolo () {
		return ds_titolo;
	}
	public void setDs_titolo(java.lang.String ds_titolo)  {
		this.ds_titolo=ds_titolo;
	}
	public java.lang.String getCodice_clas_spesa () {
		return codice_clas_spesa;
	}
	public void setCodice_clas_spesa(java.lang.String codice_clas_spesa)  {
		this.codice_clas_spesa=codice_clas_spesa;
	}
	public java.lang.String getDs_classificazione_spesa () {
		return ds_classificazione_spesa;
	}
	public void setDs_classificazione_spesa(java.lang.String ds_classificazione_spesa)  {
		this.ds_classificazione_spesa=ds_classificazione_spesa;
	}
	public java.lang.String getElemento_voce () {
		return elemento_voce;
	}
	public void setElemento_voce(java.lang.String elemento_voce)  {
		this.elemento_voce=elemento_voce;
	}
	public java.lang.String getOrigine () {
		return origine;
	}
	public void setOrigine(java.lang.String origine)  {
		this.origine=origine;
	}
	public java.lang.String getCategoria_dettaglio () {
		return categoria_dettaglio;
	}
	public void setCategoria_dettaglio(java.lang.String categoria_dettaglio)  {
		this.categoria_dettaglio=categoria_dettaglio;
	}
	public java.lang.String getNatura () {
		return natura;
	}
	public void setNatura(java.lang.String natura)  {
		this.natura=natura;
	}
	public java.math.BigDecimal getIm_ri_ccs_spese_odc () {
		return im_ri_ccs_spese_odc;
	}
	public void setIm_ri_ccs_spese_odc(java.math.BigDecimal im_ri_ccs_spese_odc)  {
		this.im_ri_ccs_spese_odc=im_ri_ccs_spese_odc;
	}
	public java.math.BigDecimal getIm_rk_ccs_spese_ogc () {
		return im_rk_ccs_spese_ogc;
	}
	public void setIm_rk_ccs_spese_ogc(java.math.BigDecimal im_rk_ccs_spese_ogc)  {
		this.im_rk_ccs_spese_ogc=im_rk_ccs_spese_ogc;
	}
	public java.math.BigDecimal getIm_rq_ssc_costi_odc () {
		return im_rq_ssc_costi_odc;
	}
	public void setIm_rq_ssc_costi_odc(java.math.BigDecimal im_rq_ssc_costi_odc)  {
		this.im_rq_ssc_costi_odc=im_rq_ssc_costi_odc;
	}
	public java.math.BigDecimal getIm_rs_ssc_costi_ogc () {
		return im_rs_ssc_costi_ogc;
	}
	public void setIm_rs_ssc_costi_ogc(java.math.BigDecimal im_rs_ssc_costi_ogc)  {
		this.im_rs_ssc_costi_ogc=im_rs_ssc_costi_ogc;
	}
	public java.math.BigDecimal getIm_rj_ccs_spese_odc_altra_uo () {
		return im_rj_ccs_spese_odc_altra_uo;
	}
	public void setIm_rj_ccs_spese_odc_altra_uo(java.math.BigDecimal im_rj_ccs_spese_odc_altra_uo)  {
		this.im_rj_ccs_spese_odc_altra_uo=im_rj_ccs_spese_odc_altra_uo;
	}
	public java.math.BigDecimal getIm_rl_ccs_spese_ogc_altra_uo () {
		return im_rl_ccs_spese_ogc_altra_uo;
	}
	public void setIm_rl_ccs_spese_ogc_altra_uo(java.math.BigDecimal im_rl_ccs_spese_ogc_altra_uo)  {
		this.im_rl_ccs_spese_ogc_altra_uo=im_rl_ccs_spese_ogc_altra_uo;
	}
	public java.math.BigDecimal getIm_rr_ssc_costi_odc_altra_uo () {
		return im_rr_ssc_costi_odc_altra_uo;
	}
	public void setIm_rr_ssc_costi_odc_altra_uo(java.math.BigDecimal im_rr_ssc_costi_odc_altra_uo)  {
		this.im_rr_ssc_costi_odc_altra_uo=im_rr_ssc_costi_odc_altra_uo;
	}
	public java.math.BigDecimal getIm_rt_ssc_costi_ogc_altra_uo () {
		return im_rt_ssc_costi_ogc_altra_uo;
	}
	public void setIm_rt_ssc_costi_ogc_altra_uo(java.math.BigDecimal im_rt_ssc_costi_ogc_altra_uo)  {
		this.im_rt_ssc_costi_ogc_altra_uo=im_rt_ssc_costi_ogc_altra_uo;
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
	public java.math.BigDecimal getIm_ru_spese_costi_altrui () {
		return im_ru_spese_costi_altrui;
	}
	public void setIm_ru_spese_costi_altrui(java.math.BigDecimal im_ru_spese_costi_altrui)  {
		this.im_ru_spese_costi_altrui=im_ru_spese_costi_altrui;
	}
}