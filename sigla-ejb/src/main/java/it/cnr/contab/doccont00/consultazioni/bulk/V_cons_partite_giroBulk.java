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
* Date 19/05/2005
*/
package it.cnr.contab.doccont00.consultazioni.bulk;

import java.math.BigDecimal;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_partite_giroBulk extends OggettoBulk implements Persistent {
//	  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//	  TI_ORIGINE CHAR(1) NOT NULL
	private java.lang.String ti_origine;
 
//	  CD_CDS_OBB VARCHAR(30)
	private java.lang.String cd_cds_obb;
 
//	  ESERCIZIO_OBB DECIMAL(4,0)
	private java.lang.Integer esercizio_obb;
 
//	  CD_UNITA_ORGANIZZATIVA_OBB VARCHAR(30)
	private java.lang.String cd_unita_organizzativa_obb;
 
//	  CD_CDS_ORIGINE_OBB VARCHAR(30)
	private java.lang.String cd_cds_origine_obb;
 
//	  CD_UO_ORIGINE_OBB VARCHAR(30)
	private java.lang.String cd_uo_origine_obb;
 
//	  CD_ELEMENTO_VOCE_OBB VARCHAR(20)
	private java.lang.String cd_elemento_voce_obb;
 
//	  ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Integer esercizio_ori_obbligazione;

//	  PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;
 
//	  IM_SCADENZA_COMP_OBB DECIMAL(15,2)
	private BigDecimal im_scadenza_comp_obb;
 
//	  IM_SCADENZA_RES_OBB DECIMAL(15,2)
	private BigDecimal im_scadenza_res_obb;
 
//	  IM_ASSOCIATO_DOC_AMM_COMP_OBB DECIMAL(15,2)
	private BigDecimal im_associato_doc_amm_comp_obb;
 
//	  IM_ASSOCIATO_DOC_AMM_RES_OBB DECIMAL(15,2)
    private BigDecimal im_associato_doc_amm_res_obb;
 
//	  IM_PAGATO_COMP DECIMAL(15,2)
    private BigDecimal im_pagato_comp;

//	  IM_PAGATO DECIMAL(15,2)
	private BigDecimal im_pagato_res;
 
//	  CD_CDS_ACR VARCHAR(30)
	private java.lang.String cd_cds_acr;
 
//	  ESERCIZIO_ACR DECIMAL(4,0)
	private java.lang.Integer esercizio_acr;
 
//	  CD_UNITA_ORGANIZZATIVA_ACR VARCHAR(30)
	private java.lang.String cd_unita_organizzativa_acr;
 
//	  CD_CDS_ORIGINE_ACR VARCHAR(30)
	private java.lang.String cd_cds_origine_acr;
 
//	  CD_UO_ORIGINE_ACR VARCHAR(30)
	private java.lang.String cd_uo_origine_acr;
 
//	  CD_ELEMENTO_VOCE_ACR VARCHAR(20)
	private java.lang.String cd_elemento_voce_acr;
 
//	 ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Integer esercizio_ori_accertamento;

//	  PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;
 
//	  IM_SCADENZA_COMP_ACR DECIMAL(15,2)
	private BigDecimal im_scadenza_comp_acr;
 
//	  IM_SCADENZA_RES_ACR DECIMAL(15,2)
	private BigDecimal im_scadenza_res_acr;
 
//	  IM_ASSOCIATO_DOC_COMP_ACR DECIMAL(15,2)
	private BigDecimal im_associato_doc_comp_acr;
 
//	  IM_ASSOCIATO_DOC_RES_ACR DECIMAL(15,2)
    private BigDecimal im_associato_doc_res_acr;
 
// 	  IM_INCASSATO_COMP DECIMAL(15,2)
    private BigDecimal im_incassato_comp;

//	  IM_INCASSATO_RES DECIMAL(15,2)
	private BigDecimal im_incassato_res;
 
	public V_cons_partite_giroBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getTi_origine () {
		return ti_origine;
	}
	public void setTi_origine(java.lang.String ti_origine)  {
		this.ti_origine=ti_origine;
	}
	public java.lang.String getCd_cds_obb () {
		return cd_cds_obb;
	}
	public void setCd_cds_obb(java.lang.String cd_cds_obb)  {
		this.cd_cds_obb=cd_cds_obb;
	}
	public java.lang.Integer getEsercizio_obb () {
		return esercizio_obb;
	}
	public void setEsercizio_obb(java.lang.Integer esercizio_obb)  {
		this.esercizio_obb=esercizio_obb;
	}
	public java.lang.String getCd_unita_organizzativa_obb () {
		return cd_unita_organizzativa_obb;
	}
	public void setCd_unita_organizzativa_obb(java.lang.String cd_unita_organizzativa_obb)  {
		this.cd_unita_organizzativa_obb=cd_unita_organizzativa_obb;
	}
	public java.lang.String getCd_cds_origine_obb () {
		return cd_cds_origine_obb;
	}
	public void setCd_cds_origine_obb(java.lang.String cd_cds_origine_obb)  {
		this.cd_cds_origine_obb=cd_cds_origine_obb;
	}
	public java.lang.String getCd_uo_origine_obb () {
		return cd_uo_origine_obb;
	}
	public void setCd_uo_origine_obb(java.lang.String cd_uo_origine_obb)  {
		this.cd_uo_origine_obb=cd_uo_origine_obb;
	}
	public java.lang.String getCd_elemento_voce_obb () {
		return cd_elemento_voce_obb;
	}
	public void setCd_elemento_voce_obb(java.lang.String cd_elemento_voce_obb)  {
		this.cd_elemento_voce_obb=cd_elemento_voce_obb;
	}
	public java.lang.Integer getEsercizio_ori_obbligazione () {
		return esercizio_ori_obbligazione;
	}
	public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione)  {
		this.esercizio_ori_obbligazione=esercizio_ori_obbligazione;
	}
	public java.lang.Long getPg_obbligazione () {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public BigDecimal getIm_scadenza_comp_obb () {
		return im_scadenza_comp_obb;
	}
	public void setIm_scadenza_comp_obb(BigDecimal im_scadenza_comp_obb)  {
		this.im_scadenza_comp_obb=im_scadenza_comp_obb;
	}
	public BigDecimal getIm_scadenza_res_obb () {
		return im_scadenza_res_obb;
	}
	public void setIm_scadenza_res_obb(BigDecimal im_scadenza_res_obb)  {
		this.im_scadenza_res_obb=im_scadenza_res_obb;
	}
	public BigDecimal getIm_associato_doc_amm_comp_obb () {
		return im_associato_doc_amm_comp_obb;
	}
	public void setIm_associato_doc_amm_comp_obb(BigDecimal im_associato_doc_amm_comp_obb)  {
		this.im_associato_doc_amm_comp_obb=im_associato_doc_amm_comp_obb;
	}
	public BigDecimal getIm_associato_doc_amm_res_obb () {
		return im_associato_doc_amm_res_obb;
	}
	public void setIm_associato_doc_amm_res_obb(BigDecimal im_associato_doc_amm_res_obb)  {
		this.im_associato_doc_amm_res_obb=im_associato_doc_amm_res_obb;
	}
	public BigDecimal getIm_pagato_comp () {
		return im_pagato_comp;
	}
	public void setIm_pagato_comp(BigDecimal im_pagato_comp)  {
		this.im_pagato_comp=im_pagato_comp;
	}
	public BigDecimal getIm_pagato_res () {
		return im_pagato_res;
	}
	public void setIm_pagato_res(BigDecimal im_pagato_res)  {
		this.im_pagato_res=im_pagato_res;
	}
	public java.lang.String getCd_cds_acr () {
		return cd_cds_acr;
	}
	public void setCd_cds_acr(java.lang.String cd_cds_acr)  {
		this.cd_cds_acr=cd_cds_acr;
	}
	public java.lang.Integer getEsercizio_acr () {
		return esercizio_acr;
	}
	public void setEsercizio_acr(java.lang.Integer esercizio_acr)  {
		this.esercizio_acr=esercizio_acr;
	}
	public java.lang.String getCd_unita_organizzativa_acr () {
		return cd_unita_organizzativa_acr;
	}
	public void setCd_unita_organizzativa_acr(java.lang.String cd_unita_organizzativa_acr)  {
		this.cd_unita_organizzativa_acr=cd_unita_organizzativa_acr;
	}
	public java.lang.String getCd_cds_origine_acr () {
		return cd_cds_origine_acr;
	}
	public void setCd_cds_origine_acr(java.lang.String cd_cds_origine_acr)  {
		this.cd_cds_origine_acr=cd_cds_origine_acr;
	}
	public java.lang.String getCd_uo_origine_acr () {
		return cd_uo_origine_acr;
	}
	public void setCd_uo_origine_acr(java.lang.String cd_uo_origine_acr)  {
		this.cd_uo_origine_acr=cd_uo_origine_acr;
	}
	public java.lang.String getCd_elemento_voce_acr () {
		return cd_elemento_voce_acr;
	}
	public void setCd_elemento_voce_acr(java.lang.String cd_elemento_voce_acr)  {
		this.cd_elemento_voce_acr=cd_elemento_voce_acr;
	}
	public java.lang.Integer getEsercizio_ori_accertamento () {
		return esercizio_ori_accertamento;
	}
	public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento)  {
		this.esercizio_ori_accertamento=esercizio_ori_accertamento;
	}
	public java.lang.Long getPg_accertamento () {
		return pg_accertamento;
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento)  {
		this.pg_accertamento=pg_accertamento;
	}
	public BigDecimal getIm_scadenza_comp_acr () {
		return im_scadenza_comp_acr;
	}
	public void setIm_scadenza_comp_acr(BigDecimal im_scadenza_comp_acr)  {
		this.im_scadenza_comp_acr=im_scadenza_comp_acr;
	}
	public BigDecimal getIm_scadenza_res_acr () {
		return im_scadenza_res_acr;
	}
	public void setIm_scadenza_res_acr(BigDecimal im_scadenza_res_acr)  {
		this.im_scadenza_res_acr=im_scadenza_res_acr;
	}
	public BigDecimal getIm_associato_doc_comp_acr () {
		return im_associato_doc_comp_acr;
	}
	public void setIm_associato_doc_comp_acr(BigDecimal im_associato_doc_comp_acr)  {
		this.im_associato_doc_comp_acr=im_associato_doc_comp_acr;
	}
	public BigDecimal getIm_associato_doc_res_acr () {
		return im_associato_doc_res_acr;
	}
	public void setIm_associato_doc_res_acr(BigDecimal im_associato_doc_res_acr)  {
		this.im_associato_doc_res_acr=im_associato_doc_res_acr;
	}
	public BigDecimal getIm_incassato_comp () {
		return im_incassato_comp;
	}
	public void setIm_incassato_comp(BigDecimal im_incassato_comp)  {
		this.im_incassato_comp=im_incassato_comp;
	}
	public BigDecimal getIm_incassato_res () {
		return im_incassato_res;
	}
	public void setIm_incassato_res(BigDecimal im_incassato_res)  {
		this.im_incassato_res=im_incassato_res;
	}
}