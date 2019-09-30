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
* Date 23/02/2006
*/
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Utente_indirizzi_mailBase extends Utente_indirizzi_mailKey implements Keyed {
	//    FL_ERR_APPR_VAR_BIL_CNR_RES CHAR(1) NOT NULL
	private java.lang.Boolean fl_err_appr_var_bil_cnr_res;
	//  FL_COM_APP_VAR_STANZ_RES CHAR(1) NOT NULL
	private java.lang.Boolean fl_com_app_var_stanz_res;
	//    FL_ERR_APPR_VAR_BIL_CNR_COMP CHAR(1) NOT NULL
	private java.lang.Boolean fl_err_appr_var_bil_cnr_comp;
	//  FL_COM_APP_VAR_STANZ_COMP CHAR(1) NOT NULL
	private java.lang.Boolean fl_com_app_var_stanz_comp;
	//  FL_ESITO_POS_FATT_ELETTR CHAR(1) NOT NULL
	private java.lang.Boolean flEsitoPosFattElettr;
	//  FL_ESITO_NEG_FATT_ELETTR CHAR(1) NOT NULL
	private java.lang.Boolean flEsitoNegFattElettr;
	//  FL_FEP_NOTIFICA_RICEZIONE CHAR(1) NOT NULL
	private java.lang.Boolean flFepNotificaRicezione;
 
	public Utente_indirizzi_mailBase() {
		super();
	}
	public Utente_indirizzi_mailBase(java.lang.String cd_utente, java.lang.String indirizzo_mail) {
		super(cd_utente, indirizzo_mail);
	}
	public java.lang.Boolean getFl_err_appr_var_bil_cnr_res () {
		return fl_err_appr_var_bil_cnr_res;
	}
	public void setFl_err_appr_var_bil_cnr_res(java.lang.Boolean fl_err_appr_var_bil_cnr_res)  {
		this.fl_err_appr_var_bil_cnr_res=fl_err_appr_var_bil_cnr_res;
	}
	public java.lang.Boolean getFl_com_app_var_stanz_res() {
		return fl_com_app_var_stanz_res;
	}
	
	public void setFl_com_app_var_stanz_res(
			java.lang.Boolean fl_com_app_var_stanz_res) {
		this.fl_com_app_var_stanz_res = fl_com_app_var_stanz_res;
	}
	public java.lang.Boolean getFl_err_appr_var_bil_cnr_comp () {
		return fl_err_appr_var_bil_cnr_comp;
	}
	public void setFl_err_appr_var_bil_cnr_comp(java.lang.Boolean fl_err_appr_var_bil_cnr_comp)  {
		this.fl_err_appr_var_bil_cnr_comp=fl_err_appr_var_bil_cnr_comp;
	}
	public java.lang.Boolean getFl_com_app_var_stanz_comp() {
		return fl_com_app_var_stanz_comp;
	}
	
	public void setFl_com_app_var_stanz_comp(
			java.lang.Boolean fl_com_app_var_stanz_comp) {
		this.fl_com_app_var_stanz_comp = fl_com_app_var_stanz_comp;
	}
	public java.lang.Boolean getFlEsitoPosFattElettr() {
		return flEsitoPosFattElettr;
	}
	public void setFlEsitoPosFattElettr(java.lang.Boolean flEsitoPosFattElettr) {
		this.flEsitoPosFattElettr = flEsitoPosFattElettr;
	}
	public java.lang.Boolean getFlEsitoNegFattElettr() {
		return flEsitoNegFattElettr;
	}
	public void setFlEsitoNegFattElettr(java.lang.Boolean flEsitoNegFattElettr) {
		this.flEsitoNegFattElettr = flEsitoNegFattElettr;
	}
	public java.lang.Boolean getFlFepNotificaRicezione() {
		return flFepNotificaRicezione;
	}
	public void setFlFepNotificaRicezione(java.lang.Boolean flFepNotificaRicezione) {
		this.flFepNotificaRicezione = flFepNotificaRicezione;
	}	
}