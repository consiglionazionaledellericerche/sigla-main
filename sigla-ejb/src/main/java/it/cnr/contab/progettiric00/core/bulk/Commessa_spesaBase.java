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
* Date 24/02/2005
*/
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Commessa_spesaBase extends Commessa_spesaKey implements Keyed {
//    GENERALE_AFFITTO DECIMAL(15,2)
	private java.math.BigDecimal generale_affitto;
 
//    GESTIONE_NAVE DECIMAL(15,2)
	private java.math.BigDecimal gestione_nave;
 
//    CC_BREV_PI DECIMAL(15,2)
	private java.math.BigDecimal cc_brev_pi;
 
//    EDILIZIA DECIMAL(15,2)
	private java.math.BigDecimal edilizia;
 
//    AMM_IMMOBILI DECIMAL(15,2)
	private java.math.BigDecimal amm_immobili;
 
//    ACC_TFR DECIMAL(15,2)
	private java.math.BigDecimal acc_tfr;
 
//    AMM_TECNICO DECIMAL(15,2)
	private java.math.BigDecimal amm_tecnico;
 
//    AMM_ALTRI_BENI DECIMAL(15,2)
	private java.math.BigDecimal amm_altri_beni;
 
//    RES_FO DECIMAL(15,2)
	private java.math.BigDecimal res_fo;
 
//    RES_MIN DECIMAL(15,2)
	private java.math.BigDecimal res_min;
 
//    RES_UE_INT DECIMAL(15,2)
	private java.math.BigDecimal res_ue_int;
 
//    RES_PRIVATI DECIMAL(15,2)
	private java.math.BigDecimal res_privati;
 
	public Commessa_spesaBase() {
		super();
	}
	public Commessa_spesaBase(java.lang.Long pg_progetto, java.lang.Integer esercizio) {
		super(pg_progetto, esercizio);
	}
	public java.math.BigDecimal getGenerale_affitto () {
		return generale_affitto;
	}
	public void setGenerale_affitto(java.math.BigDecimal generale_affitto)  {
		this.generale_affitto=generale_affitto;
	}
	public java.math.BigDecimal getGestione_nave () {
		return gestione_nave;
	}
	public void setGestione_nave(java.math.BigDecimal gestione_nave)  {
		this.gestione_nave=gestione_nave;
	}
	public java.math.BigDecimal getCc_brev_pi () {
		return cc_brev_pi;
	}
	public void setCc_brev_pi(java.math.BigDecimal cc_brev_pi)  {
		this.cc_brev_pi=cc_brev_pi;
	}
	public java.math.BigDecimal getEdilizia () {
		return edilizia;
	}
	public void setEdilizia(java.math.BigDecimal edilizia)  {
		this.edilizia=edilizia;
	}
	public java.math.BigDecimal getAmm_immobili () {
		return amm_immobili;
	}
	public void setAmm_immobili(java.math.BigDecimal amm_immobili)  {
		this.amm_immobili=amm_immobili;
	}
	public java.math.BigDecimal getAcc_tfr () {
		return acc_tfr;
	}
	public void setAcc_tfr(java.math.BigDecimal acc_tfr)  {
		this.acc_tfr=acc_tfr;
	}
	public java.math.BigDecimal getAmm_tecnico () {
		return amm_tecnico;
	}
	public void setAmm_tecnico(java.math.BigDecimal amm_tecnico)  {
		this.amm_tecnico=amm_tecnico;
	}
	public java.math.BigDecimal getAmm_altri_beni () {
		return amm_altri_beni;
	}
	public void setAmm_altri_beni(java.math.BigDecimal amm_altri_beni)  {
		this.amm_altri_beni=amm_altri_beni;
	}
	public java.math.BigDecimal getRes_fo () {
		return res_fo;
	}
	public void setRes_fo(java.math.BigDecimal res_fo)  {
		this.res_fo=res_fo;
	}
	public java.math.BigDecimal getRes_min () {
		return res_min;
	}
	public void setRes_min(java.math.BigDecimal res_min)  {
		this.res_min=res_min;
	}
	public java.math.BigDecimal getRes_ue_int () {
		return res_ue_int;
	}
	public void setRes_ue_int(java.math.BigDecimal res_ue_int)  {
		this.res_ue_int=res_ue_int;
	}
	public java.math.BigDecimal getRes_privati () {
		return res_privati;
	}
	public void setRes_privati(java.math.BigDecimal res_privati)  {
		this.res_privati=res_privati;
	}
}