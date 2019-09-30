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
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Var_stanz_res_rigaBase extends Var_stanz_res_rigaKey implements Keyed {
//    ESERCIZIO_VOCE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_voce;
 
//    ESERCIZIO_RES DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_res;
 
//    CD_CDR VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr;
 
//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;
 
//	CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

//    IM_VARIAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_variazione;
 
	public Var_stanz_res_rigaBase() {
		super();
	}
	public Var_stanz_res_rigaBase(java.lang.Integer esercizio, java.lang.Long pg_variazione, java.lang.Long pg_riga) {
		super(esercizio, pg_variazione, pg_riga);
	}
	public java.lang.Integer getEsercizio_voce () {
		return esercizio_voce;
	}
	public void setEsercizio_voce(java.lang.Integer esercizio_voce)  {
		this.esercizio_voce=esercizio_voce;
	}
	public java.lang.Integer getEsercizio_res () {
		return esercizio_res;
	}
	public void setEsercizio_res(java.lang.Integer esercizio_res)  {
		this.esercizio_res=esercizio_res;
	}
	public java.lang.String getCd_cdr () {
		return cd_cdr;
	}
	public void setCd_cdr(java.lang.String cd_cdr)  {
		this.cd_cdr=cd_cdr;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
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
	public java.lang.String getCd_voce () {
		return cd_voce;
	}
	public void setCd_voce(java.lang.String cd_voce)  {
		this.cd_voce=cd_voce;
	}
	public java.math.BigDecimal getIm_variazione () {
		return im_variazione;
	}
	public void setIm_variazione(java.math.BigDecimal im_variazione)  {
		this.im_variazione=im_variazione;
	}
/**
 * @return
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}

/**
 * @param string
 */
public void setCd_elemento_voce(java.lang.String string) {
	cd_elemento_voce = string;
}

}