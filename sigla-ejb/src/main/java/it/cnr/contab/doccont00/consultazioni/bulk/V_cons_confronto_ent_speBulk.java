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
* Date 14/02/2007
*/
package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;


public class V_cons_confronto_ent_speBulk extends OggettoBulk implements Persistent{
		
	//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CDS VARCHAR(4000)
	private java.lang.String cds;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    DS_CDR VARCHAR(300)
	private java.lang.String ds_cdr;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    DENOMINAZIONE VARCHAR(300)
	private java.lang.String denominazione;
 
//    CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String ds_modulo;
 
//    TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    IM_STANZ_INIZIALE_A1 DECIMAL(22,0)
	private java.lang.Long im_stanz_iniziale_a1;
 
//    VARIAZIONI_PIU DECIMAL(22,0)
	private java.lang.Long variazioni_piu;
 
//    VARIAZIONI_MENO DECIMAL(22,0)
	private java.lang.Long variazioni_meno;
 
//    ASSESTATO_COMP DECIMAL(22,0)
	private java.lang.Long assestato_comp;
	private java.lang.Long assestato_comp_etr;
	private java.lang.Long assestato_comp_spe;
 
//    IM_OBBL_ACC_COMP DECIMAL(22,0)
	private java.lang.Long im_obbl_acc_comp;
	private java.lang.Long im_obbl_acc_comp_etr;
	private java.lang.Long im_obbl_acc_comp_spe;
 
//    IM_ASS_DOC_AMM_SPE DECIMAL(22,0)
	private java.lang.Long im_ass_doc_amm_spe;
 
//    IM_ASS_DOC_AMM_ETR DECIMAL(22,0)
	private java.lang.Long im_ass_doc_amm_etr;
	private java.lang.Long im_ass_doc_amm;
 
//    IM_MANDATI_REVERSALI_PRO DECIMAL(22,0)
	private java.lang.Long im_mandati_reversali_pro;
	private java.lang.Long im_mandati_reversali_pro_etr;
	private java.lang.Long im_mandati_reversali_pro_spe;
 
//    TRATT_ECON_INT DECIMAL(22,0)
	private java.lang.Long tratt_econ_int;
 
//    TRATT_ECON_EST DECIMAL(22,0)
	private java.lang.Long tratt_econ_est;
 
	public V_cons_confronto_ent_speBulk() {
		super();
	}
	
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCds () {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getDs_cdr () {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getDenominazione () {
		return denominazione;
	}
	public void setDenominazione(java.lang.String denominazione)  {
		this.denominazione=denominazione;
	}
	public java.lang.String getCd_natura () {
		return cd_natura;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
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
	public java.lang.String getTi_appartenenza () {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza)  {
		this.ti_appartenenza=ti_appartenenza;
	}
	public java.lang.String getTi_gestione () {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_elemento_voce () {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	public java.lang.Long getIm_stanz_iniziale_a1 () {
		return im_stanz_iniziale_a1;
	}
	public void setIm_stanz_iniziale_a1(java.lang.Long im_stanz_iniziale_a1)  {
		this.im_stanz_iniziale_a1=im_stanz_iniziale_a1;
	}
	public java.lang.Long getVariazioni_piu () {
		return variazioni_piu;
	}
	public void setVariazioni_piu(java.lang.Long variazioni_piu)  {
		this.variazioni_piu=variazioni_piu;
	}
	public java.lang.Long getVariazioni_meno () {
		return variazioni_meno;
	}
	public void setVariazioni_meno(java.lang.Long variazioni_meno)  {
		this.variazioni_meno=variazioni_meno;
	}
	public java.lang.Long getAssestato_comp () {
		return assestato_comp;
	}
	public void setAssestato_comp(java.lang.Long assestato_comp)  {
		this.assestato_comp=assestato_comp;
	}
	public java.lang.Long getIm_obbl_acc_comp () {
		return im_obbl_acc_comp;
	}
	public void setIm_obbl_acc_comp(java.lang.Long im_obbl_acc_comp)  {
		this.im_obbl_acc_comp=im_obbl_acc_comp;
	}
	public java.lang.Long getIm_ass_doc_amm_spe () {
		return im_ass_doc_amm_spe;
	}
	public void setIm_ass_doc_amm_spe(java.lang.Long im_ass_doc_amm_spe)  {
		this.im_ass_doc_amm_spe=im_ass_doc_amm_spe;
	}
	public java.lang.Long getIm_ass_doc_amm_etr () {
		return im_ass_doc_amm_etr;
	}
	public void setIm_ass_doc_amm_etr(java.lang.Long im_ass_doc_amm_etr)  {
		this.im_ass_doc_amm_etr=im_ass_doc_amm_etr;
	}
	public java.lang.Long getIm_mandati_reversali_pro () {
		return im_mandati_reversali_pro;
	}
	public void setIm_mandati_reversali_pro(java.lang.Long im_mandati_reversali_pro)  {
		this.im_mandati_reversali_pro=im_mandati_reversali_pro;
	}
	public java.lang.Long getTratt_econ_int () {
		return tratt_econ_int;
	}
	public void setTratt_econ_int(java.lang.Long tratt_econ_int)  {
		this.tratt_econ_int=tratt_econ_int;
	}
	public java.lang.Long getTratt_econ_est () {
		return tratt_econ_est;
	}
	public void setTratt_econ_est(java.lang.Long tratt_econ_est)  {
		this.tratt_econ_est=tratt_econ_est;
	}

	public java.lang.Long getAssestato_comp_etr() {
		return assestato_comp_etr;
	}

	public void setAssestato_comp_etr(java.lang.Long assestato_comp_etr) {
		this.assestato_comp_etr = assestato_comp_etr;
	}

	public java.lang.Long getAssestato_comp_spe() {
		return assestato_comp_spe;
	}

	public void setAssestato_comp_spe(java.lang.Long assestato_comp_spe) {
		this.assestato_comp_spe = assestato_comp_spe;
	}

	public java.lang.Long getIm_mandati_reversali_pro_etr() {
		return im_mandati_reversali_pro_etr;
	}

	public void setIm_mandati_reversali_pro_etr(
			java.lang.Long im_mandati_reversali_pro_etr) {
		this.im_mandati_reversali_pro_etr = im_mandati_reversali_pro_etr;
	}

	public java.lang.Long getIm_mandati_reversali_pro_spe() {
		return im_mandati_reversali_pro_spe;
	}

	public void setIm_mandati_reversali_pro_spe(
			java.lang.Long im_mandati_reversali_pro_spe) {
		this.im_mandati_reversali_pro_spe = im_mandati_reversali_pro_spe;
	}

	public java.lang.Long getIm_obbl_acc_comp_etr() {
		return im_obbl_acc_comp_etr;
	}

	public void setIm_obbl_acc_comp_etr(java.lang.Long im_obbl_acc_comp_etr) {
		this.im_obbl_acc_comp_etr = im_obbl_acc_comp_etr;
	}

	public java.lang.Long getIm_obbl_acc_comp_spe() {
		return im_obbl_acc_comp_spe;
	}

	public void setIm_obbl_acc_comp_spe(java.lang.Long im_obbl_acc_comp_spe) {
		this.im_obbl_acc_comp_spe = im_obbl_acc_comp_spe;
	}

	public java.lang.Long getIm_ass_doc_amm() {
		return im_ass_doc_amm;
	}

	public void setIm_ass_doc_amm(java.lang.Long im_ass_doc_amm) {
		this.im_ass_doc_amm = im_ass_doc_amm;
	}

	
}