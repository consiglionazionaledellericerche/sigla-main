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
 * Date 15/10/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_disp_comp_res_entBulk extends OggettoBulk implements Persistent {

//  PROGETTO VARCHAR(30)
	private java.lang.String progetto;
 
//    DS_PROGETTO VARCHAR(433)
	private java.lang.String ds_progetto;
 
//    COMMESSA VARCHAR(30)
	private java.lang.String commessa;
 
//    DS_COMMESSA VARCHAR(433)
	private java.lang.String ds_commessa;
 
//    MODULO VARCHAR(30)
	private java.lang.String modulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String ds_modulo;
 
//    DIPARTIMENTO VARCHAR(30)
	private java.lang.String dipartimento;
 
//    DS_DIPARTIMENTO VARCHAR(300)
	private java.lang.String ds_dipartimento;
 
//    CDS VARCHAR(30)
	private java.lang.String cds;
 
//    DS_CDS VARCHAR(300)
	private java.lang.String ds_cds;
 
//    UO VARCHAR(30)
	private java.lang.String uo;
 
//    CDR VARCHAR(30)
	private java.lang.String cdr;
 
//    DS_CDR VARCHAR(300)
	private java.lang.String ds_cdr;
 
//    LDA VARCHAR(10)
	private java.lang.String lda;
 
//    DS_LDA VARCHAR(300)
	private java.lang.String ds_lda;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    ESERCIZIO_RES DECIMAL(4,0)
	private java.lang.Integer esercizio_res;
 
//    CD_VOCE VARCHAR(50)
	private java.lang.String cd_voce;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_VOCE VARCHAR(100)
	private java.lang.String ds_voce;
 
//    STANZ_INI DECIMAL(15,2)
	private java.math.BigDecimal stanz_ini;
 
//    VAR_PIU DECIMAL(15,2)
	private java.math.BigDecimal var_piu;
 
//    VAR_MENO DECIMAL(15,2)
	private java.math.BigDecimal var_meno;
 
//    ASSESTATO_COMP DECIMAL(22,0)
	private java.lang.Long assestato_comp;
 
//    ACC_COMP DECIMAL(15,2)
	private java.math.BigDecimal acc_comp;
 
//    DISP_COMP DECIMAL(22,0)
	private java.lang.Long disp_comp;
 
//    RISCOSSO_COMP DECIMAL(22,0)
	private java.lang.Long riscosso_comp;
 
//    ACC_RES_PRO DECIMAL(15,2)
	private java.math.BigDecimal acc_res_pro;
 
//    VAR_PIU_RES_PRO DECIMAL(15,2)
	private java.math.BigDecimal var_piu_res_pro;
 
//    VAR_MENO_RES_PRO DECIMAL(15,2)
	private java.math.BigDecimal var_meno_res_pro;
 
//    RISCOSSO_RES DECIMAL(22,0)
	private java.lang.Long riscosso_res;
 
//    CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;
	
//  DS_NATURA VARCHAR(100)
	private java.lang.String ds_natura;
	
//  TIPO VARCHAR(3)
	private java.lang.String tipo;
	
//	Colonne calcolate	
	private java.math.BigDecimal acc_res_ass;
	private java.math.BigDecimal rimasti_da_riscuotere_res;
	private java.math.BigDecimal rimasti_da_riscuotere_comp;
	
	public V_cons_disp_comp_res_entBulk() {
		super();
	}
	
	public java.lang.String getProgetto() {
		return progetto;
	}
	public void setProgetto(java.lang.String progetto)  {
		this.progetto=progetto;
	}
	public java.lang.String getDs_progetto() {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.String getCommessa() {
		return commessa;
	}
	public void setCommessa(java.lang.String commessa)  {
		this.commessa=commessa;
	}
	public java.lang.String getDs_commessa() {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.String getModulo() {
		return modulo;
	}
	public void setModulo(java.lang.String modulo)  {
		this.modulo=modulo;
	}
	public java.lang.String getDs_modulo() {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getDipartimento() {
		return dipartimento;
	}
	public void setDipartimento(java.lang.String dipartimento)  {
		this.dipartimento=dipartimento;
	}
	public java.lang.String getDs_dipartimento() {
		return ds_dipartimento;
	}
	public void setDs_dipartimento(java.lang.String ds_dipartimento)  {
		this.ds_dipartimento=ds_dipartimento;
	}
	public java.lang.String getCds() {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
	public java.lang.String getDs_cds() {
		return ds_cds;
	}
	public void setDs_cds(java.lang.String ds_cds)  {
		this.ds_cds=ds_cds;
	}
	public java.lang.String getUo() {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.String getCdr() {
		return cdr;
	}
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.String getLda() {
		return lda;
	}
	public void setLda(java.lang.String lda)  {
		this.lda=lda;
	}
	public java.lang.String getDs_lda() {
		return ds_lda;
	}
	public void setDs_lda(java.lang.String ds_lda)  {
		this.ds_lda=ds_lda;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}
	public void setEsercizio_res(java.lang.Integer esercizio_res)  {
		this.esercizio_res=esercizio_res;
	}
	public java.lang.String getCd_voce() {
		return cd_voce;
	}
	public void setCd_voce(java.lang.String cd_voce)  {
		this.cd_voce=cd_voce;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_voce() {
		return ds_voce;
	}
	public void setDs_voce(java.lang.String ds_voce)  {
		this.ds_voce=ds_voce;
	}
	public java.math.BigDecimal getStanz_ini() {
		return stanz_ini;
	}
	public void setStanz_ini(java.math.BigDecimal stanz_ini)  {
		this.stanz_ini=stanz_ini;
	}
	public java.math.BigDecimal getVar_piu() {
		return var_piu;
	}
	public void setVar_piu(java.math.BigDecimal var_piu)  {
		this.var_piu=var_piu;
	}
	public java.math.BigDecimal getVar_meno() {
		return var_meno;
	}
	public void setVar_meno(java.math.BigDecimal var_meno)  {
		this.var_meno=var_meno;
	}
	public java.lang.Long getAssestato_comp() {
		return assestato_comp;
	}
	public void setAssestato_comp(java.lang.Long assestato_comp)  {
		this.assestato_comp=assestato_comp;
	}
	public java.math.BigDecimal getAcc_comp() {
		return acc_comp;
	}
	public void setAcc_comp(java.math.BigDecimal acc_comp)  {
		this.acc_comp=acc_comp;
	}
	public java.lang.Long getDisp_comp() {
		return disp_comp;
	}
	public void setDisp_comp(java.lang.Long disp_comp)  {
		this.disp_comp=disp_comp;
	}
	public java.lang.Long getRiscosso_comp() {
		return riscosso_comp;
	}
	public void setRiscosso_comp(java.lang.Long riscosso_comp)  {
		this.riscosso_comp=riscosso_comp;
	}
	public java.math.BigDecimal getAcc_res_pro() {
		return acc_res_pro;
	}
	public void setAcc_res_pro(java.math.BigDecimal acc_res_pro)  {
		this.acc_res_pro=acc_res_pro;
	}
	public java.math.BigDecimal getVar_piu_res_pro() {
		return var_piu_res_pro;
	}
	public void setVar_piu_res_pro(java.math.BigDecimal var_piu_res_pro)  {
		this.var_piu_res_pro=var_piu_res_pro;
	}
	public java.math.BigDecimal getVar_meno_res_pro() {
		return var_meno_res_pro;
	}
	public void setVar_meno_res_pro(java.math.BigDecimal var_meno_res_pro)  {
		this.var_meno_res_pro=var_meno_res_pro;
	}
	public java.lang.Long getRiscosso_res() {
		return riscosso_res;
	}
	public void setRiscosso_res(java.lang.Long riscosso_res)  {
		this.riscosso_res=riscosso_res;
	}
	public java.lang.String getCd_natura() {
		return cd_natura;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
	}

	public java.math.BigDecimal getAcc_res_ass() {
		return acc_res_ass;
	}

	public void setAcc_res_ass(java.math.BigDecimal acc_res_ass) {
		this.acc_res_ass = acc_res_ass;
	}

	public java.math.BigDecimal getRimasti_da_riscuotere_comp() {
		return rimasti_da_riscuotere_comp;
	}

	public void setRimasti_da_riscuotere_comp(
			java.math.BigDecimal rimasti_da_riscuotere_comp) {
		this.rimasti_da_riscuotere_comp = rimasti_da_riscuotere_comp;
	}

	public java.math.BigDecimal getRimasti_da_riscuotere_res() {
		return rimasti_da_riscuotere_res;
	}

	public void setRimasti_da_riscuotere_res(
			java.math.BigDecimal rimasti_da_riscuotere_res) {
		this.rimasti_da_riscuotere_res = rimasti_da_riscuotere_res;
	}

	public java.lang.String getDs_natura() {
		return ds_natura;
	}

	public void setDs_natura(java.lang.String ds_natura) {
		this.ds_natura = ds_natura;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}

	
}