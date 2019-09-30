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

package it.cnr.contab.prevent01.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_pdgg_istBulk extends OggettoBulk implements Persistent {
	public V_cons_pdgg_istBulk() {
		super();
	}
	//	ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
	
	private java.lang.String  cd_cds_area;
	 
	private java.lang.String  ds_unita_organizzativa;
	
	private java.lang.String  cd_cdr_assegnatario;
	 
	private java.lang.String  ds_cdr; 
	 
	private java.lang.String  cd_classificazione; 
	 
	private java.lang.String  ds_classificazione; 
	 
	private java.lang.String  cd_livello1; 
	 
	private java.lang.String  cd_livello2; 	
		
	private java.lang.String  cd_livello3; 
	 
	private java.lang.String  cd_progetto; 	
		
	private java.lang.String  ds_progetto; 
	 
	private java.lang.String  cd_commessa; 	
		
	private java.lang.String  ds_commessa; 
	 
	private java.lang.String  cd_modulo; 	
			
	private java.lang.String  ds_modulo; 
	 
	private java.lang.String cd_linea_attivita;
	 
	private java.lang.String ti_appartenenza;
 
	private java.lang.String ti_gestione;
 
	private java.lang.String cd_elemento_voce;
 
	private java.lang.String ds_elemento_voce;
 
	private java.math.BigDecimal im_entrata;
 
	private java.math.BigDecimal im_incassi;

	private java.math.BigDecimal im_dec_fonte_int;
	
	private java.math.BigDecimal im_dec_fonte_est;
	
	private java.math.BigDecimal tot_decentrato;
	
	private java.math.BigDecimal im_pagamenti;

	 public java.lang.String getCd_cds_area() {
		return cd_cds_area;
	}

	public java.lang.String getCd_cdr_assegnatario() {
		return cd_cdr_assegnatario;
	}

	
	public java.lang.String getCd_classificazione() {
		return cd_classificazione;
	}

	public java.lang.String getCd_commessa() {
		return cd_commessa;
	}

	
	public java.lang.String getCd_livello1() {
		return cd_livello1;
	}

	
	public java.lang.String getCd_livello2() {
		return cd_livello2;
	}

	public java.lang.String getCd_livello3() {
		return cd_livello3;
	}

	
	public java.lang.String getCd_modulo() {
		return cd_modulo;
	}

	
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}

	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}
	
	public java.lang.String getDs_classificazione() {
		return ds_classificazione;
	}

	
	public java.lang.String getDs_commessa() {
		return ds_commessa;
	}

	public java.lang.String getDs_modulo() {
		return ds_modulo;
	}

	public java.lang.String getDs_progetto() {
		return ds_progetto;
	}

	public java.lang.String getDs_unita_organizzativa() {
		return ds_unita_organizzativa;
	}

	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public java.math.BigDecimal getIm_dec_fonte_est() {
		return im_dec_fonte_est;
	}

	
	public java.math.BigDecimal getIm_dec_fonte_int() {
		return im_dec_fonte_int;
	}

	
	public void setCd_cds_area(java.lang.String string) {
		cd_cds_area = string;
	}

	
	public void setCd_cdr_assegnatario(java.lang.String string) {
		cd_cdr_assegnatario = string;
	}

	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}

	
	public void setCd_commessa(java.lang.String string) {
		cd_commessa = string;
	}

	
	public void setCd_livello1(java.lang.String string) {
		cd_livello1 = string;
	}

	
	public void setCd_livello2(java.lang.String string) {
		cd_livello2 = string;
	}

	public void setCd_livello3(java.lang.String string) {
		cd_livello3 = string;
	}

	
	public void setCd_modulo(java.lang.String string) {
		cd_modulo = string;
	}

	
	public void setCd_progetto(java.lang.String string) {
		cd_progetto = string;
	}

	
	public void setDs_cdr(java.lang.String string) {
		ds_cdr = string;
	}

	
	public void setDs_classificazione(java.lang.String string) {
		ds_classificazione = string;
	}

	public void setDs_commessa(java.lang.String string) {
		ds_commessa = string;
	}

	public void setDs_modulo(java.lang.String string) {
		ds_modulo = string;
	}

	public void setDs_progetto(java.lang.String string) {
		ds_progetto = string;
	}

	
	public void setDs_unita_organizzativa(java.lang.String string) {
		ds_unita_organizzativa = string;
	}

	
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	
	public void setIm_dec_fonte_est(java.math.BigDecimal decimal) {
		im_dec_fonte_est = decimal;
	}

	
	public void setIm_dec_fonte_int(java.math.BigDecimal decimal) {
		im_dec_fonte_int = decimal;
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
	public java.math.BigDecimal getIm_entrata () {
		return im_entrata;
	}
	public void setIm_entrata(java.math.BigDecimal im_entrata)  {
		this.im_entrata=im_entrata;
	}
	public java.math.BigDecimal getIm_incassi () {
		return im_incassi;
	}
	public void setIm_incassi(java.math.BigDecimal im_incassi)  {
		this.im_incassi=im_incassi;
	}

	public java.math.BigDecimal getTot_decentrato() {
		return tot_decentrato;
	}

	public void setTot_decentrato(java.math.BigDecimal tot_decentrato) {
		this.tot_decentrato = tot_decentrato;
	}

	public java.math.BigDecimal getIm_pagamenti() {
		return im_pagamenti;
	}

	public void setIm_pagamenti(java.math.BigDecimal im_pagamenti) {
		this.im_pagamenti = im_pagamenti;
	}

}