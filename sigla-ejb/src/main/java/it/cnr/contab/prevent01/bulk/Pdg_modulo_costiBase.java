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
* Date 28/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_modulo_costiBase extends Pdg_modulo_costiKey implements Keyed {
//    RIS_ES_PREC_TIT_I DECIMAL(15,2)
	private java.math.BigDecimal ris_es_prec_tit_i;
 
//    RIS_ES_PREC_TIT_II DECIMAL(15,2)
	private java.math.BigDecimal ris_es_prec_tit_ii;
 
//    RIS_PRES_ES_PREC_TIT_I DECIMAL(15,2)
	private java.math.BigDecimal ris_pres_es_prec_tit_i;
 
//    RIS_PRES_ES_PREC_TIT_II DECIMAL(15,2)
	private java.math.BigDecimal ris_pres_es_prec_tit_ii;
 
//    IM_COSTI_GENERALI DECIMAL(15,2)
	private java.math.BigDecimal im_costi_generali;
 
//    IM_CF_TFR DECIMAL(15,2)
	private java.math.BigDecimal im_cf_tfr;

//	IM_CF_TFR_DET DECIMAL(15,2)
  private java.math.BigDecimal im_cf_tfr_det;
 
//    IM_CF_AMM_IMMOBILI DECIMAL(15,2)
	private java.math.BigDecimal im_cf_amm_immobili;
 
//    IM_CF_AMM_ATTREZZ DECIMAL(15,2)
	private java.math.BigDecimal im_cf_amm_attrezz;
 
//    IM_CF_AMM_ALTRO DECIMAL(15,2)
	private java.math.BigDecimal im_cf_amm_altro;
 
	public Pdg_modulo_costiBase() {
		super();
	}
	public Pdg_modulo_costiBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto) {
		super(esercizio, cd_centro_responsabilita, pg_progetto);
	}
	public java.math.BigDecimal getRis_es_prec_tit_i () {
		return ris_es_prec_tit_i;
	}
	public void setRis_es_prec_tit_i(java.math.BigDecimal ris_es_prec_tit_i)  {
		this.ris_es_prec_tit_i=ris_es_prec_tit_i;
	}
	public java.math.BigDecimal getRis_es_prec_tit_ii () {
		return ris_es_prec_tit_ii;
	}
	public void setRis_es_prec_tit_ii(java.math.BigDecimal ris_es_prec_tit_ii)  {
		this.ris_es_prec_tit_ii=ris_es_prec_tit_ii;
	}
	public java.math.BigDecimal getRis_pres_es_prec_tit_i () {
		return ris_pres_es_prec_tit_i;
	}
	public void setRis_pres_es_prec_tit_i(java.math.BigDecimal ris_pres_es_prec_tit_i)  {
		this.ris_pres_es_prec_tit_i=ris_pres_es_prec_tit_i;
	}
	public java.math.BigDecimal getRis_pres_es_prec_tit_ii () {
		return ris_pres_es_prec_tit_ii;
	}
	public void setRis_pres_es_prec_tit_ii(java.math.BigDecimal ris_pres_es_prec_tit_ii)  {
		this.ris_pres_es_prec_tit_ii=ris_pres_es_prec_tit_ii;
	}
	public java.math.BigDecimal getIm_costi_generali () {
		return im_costi_generali;
	}
	public void setIm_costi_generali(java.math.BigDecimal im_costi_generali)  {
		this.im_costi_generali=im_costi_generali;
	}
	public java.math.BigDecimal getIm_cf_tfr () {
		return im_cf_tfr;
	}
	public void setIm_cf_tfr(java.math.BigDecimal im_cf_tfr)  {
		this.im_cf_tfr=im_cf_tfr;
	}
	public java.math.BigDecimal getIm_cf_tfr_det () {
		return im_cf_tfr_det;
	}
	public void setIm_cf_tfr_det(java.math.BigDecimal im_cf_tfr_det)  {
		this.im_cf_tfr_det=im_cf_tfr_det;
	}
	
	public java.math.BigDecimal getIm_cf_amm_immobili () {
		return im_cf_amm_immobili;
	}
	public void setIm_cf_amm_immobili(java.math.BigDecimal im_cf_amm_immobili)  {
		this.im_cf_amm_immobili=im_cf_amm_immobili;
	}
	public java.math.BigDecimal getIm_cf_amm_attrezz () {
		return im_cf_amm_attrezz;
	}
	public void setIm_cf_amm_attrezz(java.math.BigDecimal im_cf_amm_attrezz)  {
		this.im_cf_amm_attrezz=im_cf_amm_attrezz;
	}
	public java.math.BigDecimal getIm_cf_amm_altro () {
		return im_cf_amm_altro;
	}
	public void setIm_cf_amm_altro(java.math.BigDecimal im_cf_amm_altro)  {
		this.im_cf_amm_altro=im_cf_amm_altro;
	}
}