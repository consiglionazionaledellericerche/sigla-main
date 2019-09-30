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
public class V_cons_pdgg_dipfoBulk extends OggettoBulk implements Persistent {
	public V_cons_pdgg_dipfoBulk() {
		super();
	}
	//	ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
	
	private java.lang.String  cd_dipartimento;
	 
	private java.lang.String  ds_dipartimento;
	
	private java.lang.String  cd_centro_responsabilita;
	 
	private java.lang.String  ds_cdr; 
	 
	private java.lang.String  cd_classificazione; 
	 
	private java.lang.String  ds_classificazione; 
	 
	private java.lang.String  cd_livello1; 
	 
	private java.lang.String  cd_livello2; 	
		
	private java.lang.String  cd_livello3; 
	 
	private java.lang.Integer  pg_progetto; 	
	 
	private java.lang.String  cd_progetto; 	
		
	private java.lang.String  ds_progetto; 
	 
	private java.lang.String  cd_commessa; 	
		
	private java.lang.String  ds_commessa; 
	 
	private java.lang.String  cd_modulo; 	
			
	private java.lang.String  ds_modulo; 
	 
	private java.lang.String cd_tipo_modulo;
	
	private java.lang.String ds_tipo_modulo;
	
	private java.lang.String cd_elemento_voce;
	 
	private java.lang.String ds_elemento_voce;
 
	private java.lang.String cd_linea_attivita;

	private java.lang.String ds_linea;

	/**
	 * @return
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_classificazione() {
		return cd_classificazione;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_commessa() {
		return cd_commessa;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_livello1() {
		return cd_livello1;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_livello2() {
		return cd_livello2;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_livello3() {
		return cd_livello3;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_modulo() {
		return cd_modulo;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_tipo_modulo() {
		return cd_tipo_modulo;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_classificazione() {
		return ds_classificazione;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_commessa() {
		return ds_commessa;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_dipartimento() {
		return ds_dipartimento;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_modulo() {
		return ds_modulo;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_progetto() {
		return ds_progetto;
	}

	/**
	 * @return
	 */
	public java.lang.String getDs_tipo_modulo() {
		return ds_tipo_modulo;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	/**
	 * @param string
	 */
	public void setCd_centro_responsabilita(java.lang.String string) {
		cd_centro_responsabilita = string;
	}

	/**
	 * @param string
	 */
	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}

	/**
	 * @param string
	 */
	public void setCd_commessa(java.lang.String string) {
		cd_commessa = string;
	}

	/**
	 * @param string
	 */
	public void setCd_dipartimento(java.lang.String string) {
		cd_dipartimento = string;
	}

	/**
	 * @param string
	 */
	public void setCd_livello1(java.lang.String string) {
		cd_livello1 = string;
	}

	/**
	 * @param string
	 */
	public void setCd_livello2(java.lang.String string) {
		cd_livello2 = string;
	}

	/**
	 * @param string
	 */
	public void setCd_livello3(java.lang.String string) {
		cd_livello3 = string;
	}

	/**
	 * @param string
	 */
	public void setCd_modulo(java.lang.String string) {
		cd_modulo = string;
	}

	/**
	 * @param string
	 */
	public void setCd_progetto(java.lang.String string) {
		cd_progetto = string;
	}

	/**
	 * @param string
	 */
	public void setCd_tipo_modulo(java.lang.String string) {
		cd_tipo_modulo = string;
	}

	/**
	 * @param string
	 */
	public void setDs_cdr(java.lang.String string) {
		ds_cdr = string;
	}

	/**
	 * @param string
	 */
	public void setDs_classificazione(java.lang.String string) {
		ds_classificazione = string;
	}

	/**
	 * @param string
	 */
	public void setDs_commessa(java.lang.String string) {
		ds_commessa = string;
	}

	/**
	 * @param string
	 */
	public void setDs_dipartimento(java.lang.String string) {
		ds_dipartimento = string;
	}

	/**
	 * @param string
	 */
	public void setDs_modulo(java.lang.String string) {
		ds_modulo = string;
	}

	/**
	 * @param string
	 */
	public void setDs_progetto(java.lang.String string) {
		ds_progetto = string;
	}

	/**
	 * @param string
	 */
	public void setDs_tipo_modulo(java.lang.String string) {
		ds_tipo_modulo = string;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}

	/**
	 * @param Integer
	 */
	public void setPg_progetto(java.lang.Integer integer) {
		pg_progetto = integer;
	}

	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}

	public java.lang.String getDs_elemento_voce() {
		return ds_elemento_voce;
	}

	public void setDs_elemento_voce(java.lang.String ds_elemento_voce) {
		this.ds_elemento_voce = ds_elemento_voce;
	}

	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}

	public java.lang.String getDs_linea() {
		return ds_linea;
	}

	public void setDs_linea(java.lang.String ds_linea) {
		this.ds_linea = ds_linea;
	}

}