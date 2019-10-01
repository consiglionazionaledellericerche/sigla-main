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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Voce_f_saldi_cdr_lineaBase extends Voce_f_saldi_cdr_lineaKey implements Keyed {

//	CD_ELEMENTO_VOCE VARCHAR2(30) NULL
  	private String cd_elemento_voce;

//	  IM_STANZ_INIZIALE_A1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_stanz_iniziale_a1;
 
//	  IM_STANZ_INIZIALE_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_stanz_iniziale_a2;
 
//	  IM_STANZ_INIZIALE_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_stanz_iniziale_a3;
 
//	  VARIAZIONI_PIU DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variazioni_piu;
 
//	  VARIAZIONI_MENO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variazioni_meno;
 
//	  IM_STANZ_INIZIALE_CASSA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_stanz_iniziale_cassa;
 
//	  VARIAZIONI_PIU_CASSA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variazioni_piu_cassa;
 
//	  VARIAZIONI_MENO_CASSA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variazioni_meno_cassa;
 
//	  IM_OBBL_ACC_COMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbl_acc_comp;
 
//	  IM_STANZ_RES_IMPROPRIO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_stanz_res_improprio;
 
//	  VAR_PIU_STANZ_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_piu_stanz_res_imp;
 
//	  VAR_MENO_STANZ_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_meno_stanz_res_imp;
 
//	  IM_OBBL_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbl_res_imp;
 
//	  VAR_PIU_OBBL_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_piu_obbl_res_imp;
 
//	  VAR_MENO_OBBL_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_meno_obbl_res_imp;
 
//	  IM_OBBL_RES_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbl_res_pro;
 
//	  VAR_PIU_OBBL_RES_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_piu_obbl_res_pro;
 
//	  VAR_MENO_OBBL_RES_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_meno_obbl_res_pro;
 
//	  IM_MANDATI_REVERSALI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_mandati_reversali_pro;

//	IM_MANDATI_REVERSALI DECIMAL(15,2) NOT NULL
  private java.math.BigDecimal im_mandati_reversali_imp;
 
//	  IM_PAGAMENTI_INCASSI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_pagamenti_incassi;
 
	public Voce_f_saldi_cdr_lineaBase() {
		super();
	}
	public Voce_f_saldi_cdr_lineaBase(java.lang.Integer esercizio, java.lang.Integer esercizio_res, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce) {
		super(esercizio, esercizio_res, cd_centro_responsabilita, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_voce);
	}
	public java.math.BigDecimal getIm_stanz_iniziale_a1 () {
		return im_stanz_iniziale_a1;
	}
	public void setIm_stanz_iniziale_a1(java.math.BigDecimal im_stanz_iniziale_a1)  {
		this.im_stanz_iniziale_a1=im_stanz_iniziale_a1;
	}
	public java.math.BigDecimal getIm_stanz_iniziale_a2 () {
		return im_stanz_iniziale_a2;
	}
	public void setIm_stanz_iniziale_a2(java.math.BigDecimal im_stanz_iniziale_a2)  {
		this.im_stanz_iniziale_a2=im_stanz_iniziale_a2;
	}
	public java.math.BigDecimal getIm_stanz_iniziale_a3 () {
		return im_stanz_iniziale_a3;
	}
	public void setIm_stanz_iniziale_a3(java.math.BigDecimal im_stanz_iniziale_a3)  {
		this.im_stanz_iniziale_a3=im_stanz_iniziale_a3;
	}
	public java.math.BigDecimal getVariazioni_piu () {
		return variazioni_piu;
	}
	public void setVariazioni_piu(java.math.BigDecimal variazioni_piu)  {
		this.variazioni_piu=variazioni_piu;
	}
	public java.math.BigDecimal getVariazioni_meno () {
		return variazioni_meno;
	}
	public void setVariazioni_meno(java.math.BigDecimal variazioni_meno)  {
		this.variazioni_meno=variazioni_meno;
	}
	public java.math.BigDecimal getIm_stanz_iniziale_cassa () {
		return im_stanz_iniziale_cassa;
	}
	public void setIm_stanz_iniziale_cassa(java.math.BigDecimal im_stanz_iniziale_cassa)  {
		this.im_stanz_iniziale_cassa=im_stanz_iniziale_cassa;
	}
	public java.math.BigDecimal getVariazioni_piu_cassa () {
		return variazioni_piu_cassa;
	}
	public void setVariazioni_piu_cassa(java.math.BigDecimal variazioni_piu_cassa)  {
		this.variazioni_piu_cassa=variazioni_piu_cassa;
	}
	public java.math.BigDecimal getVariazioni_meno_cassa () {
		return variazioni_meno_cassa;
	}
	public void setVariazioni_meno_cassa(java.math.BigDecimal variazioni_meno_cassa)  {
		this.variazioni_meno_cassa=variazioni_meno_cassa;
	}
	public java.math.BigDecimal getIm_obbl_acc_comp () {
		return im_obbl_acc_comp;
	}
	public void setIm_obbl_acc_comp(java.math.BigDecimal im_obbl_acc_comp)  {
		this.im_obbl_acc_comp=im_obbl_acc_comp;
	}
	public java.math.BigDecimal getIm_stanz_res_improprio () {
		return im_stanz_res_improprio;
	}
	public void setIm_stanz_res_improprio(java.math.BigDecimal im_stanz_res_improprio)  {
		this.im_stanz_res_improprio=im_stanz_res_improprio;
	}
	public java.math.BigDecimal getVar_piu_stanz_res_imp () {
		return var_piu_stanz_res_imp;
	}
	public void setVar_piu_stanz_res_imp(java.math.BigDecimal var_piu_stanz_res_imp)  {
		this.var_piu_stanz_res_imp=var_piu_stanz_res_imp;
	}
	public java.math.BigDecimal getVar_meno_stanz_res_imp () {
		return var_meno_stanz_res_imp;
	}
	public void setVar_meno_stanz_res_imp(java.math.BigDecimal var_meno_stanz_res_imp)  {
		this.var_meno_stanz_res_imp=var_meno_stanz_res_imp;
	}
	public java.math.BigDecimal getIm_obbl_res_imp () {
		return im_obbl_res_imp;
	}
	public void setIm_obbl_res_imp(java.math.BigDecimal im_obbl_res_imp)  {
		this.im_obbl_res_imp=im_obbl_res_imp;
	}
	public java.math.BigDecimal getVar_piu_obbl_res_imp () {
		return var_piu_obbl_res_imp;
	}
	public void setVar_piu_obbl_res_imp(java.math.BigDecimal var_piu_obbl_res_imp)  {
		this.var_piu_obbl_res_imp=var_piu_obbl_res_imp;
	}
	public java.math.BigDecimal getVar_meno_obbl_res_imp () {
		return var_meno_obbl_res_imp;
	}
	public void setVar_meno_obbl_res_imp(java.math.BigDecimal var_meno_obbl_res_imp)  {
		this.var_meno_obbl_res_imp=var_meno_obbl_res_imp;
	}
	public java.math.BigDecimal getIm_obbl_res_pro () {
		return im_obbl_res_pro;
	}
	public void setIm_obbl_res_pro(java.math.BigDecimal im_obbl_res_pro)  {
		this.im_obbl_res_pro=im_obbl_res_pro;
	}
	public java.math.BigDecimal getVar_piu_obbl_res_pro () {
		return var_piu_obbl_res_pro;
	}
	public void setVar_piu_obbl_res_pro(java.math.BigDecimal var_piu_obbl_res_pro)  {
		this.var_piu_obbl_res_pro=var_piu_obbl_res_pro;
	}
	public java.math.BigDecimal getVar_meno_obbl_res_pro () {
		return var_meno_obbl_res_pro;
	}
	public void setVar_meno_obbl_res_pro(java.math.BigDecimal var_meno_obbl_res_pro)  {
		this.var_meno_obbl_res_pro=var_meno_obbl_res_pro;
	}
	public java.math.BigDecimal getIm_pagamenti_incassi () {
		return im_pagamenti_incassi;
	}
	public void setIm_pagamenti_incassi(java.math.BigDecimal im_pagamenti_incassi)  {
		this.im_pagamenti_incassi=im_pagamenti_incassi;
	}
/**
 * @return
 */
public java.math.BigDecimal getIm_mandati_reversali_imp() {
	return im_mandati_reversali_imp;
}

/**
 * @return
 */
public java.math.BigDecimal getIm_mandati_reversali_pro() {
	return im_mandati_reversali_pro;
}

/**
 * @param decimal
 */
public void setIm_mandati_reversali_imp(java.math.BigDecimal decimal) {
	im_mandati_reversali_imp = decimal;
}

/**
 * @param decimal
 */
public void setIm_mandati_reversali_pro(java.math.BigDecimal decimal) {
	im_mandati_reversali_pro = decimal;
}

/**
 * @return
 */
public String getCd_elemento_voce() {
	return cd_elemento_voce;
}

/**
 * @param string
 */
public void setCd_elemento_voce(String string) {
	cd_elemento_voce = string;
}

}