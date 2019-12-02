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
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.persistency.Keyed;
public class Pdg_modulo_speseBase extends Pdg_modulo_speseKey implements Keyed {
//    IM_SPESE_GEST_DECENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_int;
 
//    IM_SPESE_GEST_DECENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_est;
 
//    IM_SPESE_GEST_ACCENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_int;
 
//    IM_SPESE_GEST_ACCENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_est;
 
//    IM_SPESE_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_a2;
 
//    IM_SPESE_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_a3;
	
	private String cd_cofog;
	private String cd_missione;
 
	// CD_UNITA_PIANO VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_piano;

	// CD_VOCE_PIANO VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_piano;

	public Pdg_modulo_speseBase() {
		super();
	}
	public Pdg_modulo_speseBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer id_classificazione, java.lang.String cd_cds_area,Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, id_classificazione, cd_cds_area,pg_dettaglio);
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_int () {
		return im_spese_gest_decentrata_int;
	}
	public void setIm_spese_gest_decentrata_int(java.math.BigDecimal im_spese_gest_decentrata_int)  {
		this.im_spese_gest_decentrata_int=im_spese_gest_decentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_est () {
		return im_spese_gest_decentrata_est;
	}
	public void setIm_spese_gest_decentrata_est(java.math.BigDecimal im_spese_gest_decentrata_est)  {
		this.im_spese_gest_decentrata_est=im_spese_gest_decentrata_est;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_int () {
		return im_spese_gest_accentrata_int;
	}
	public void setIm_spese_gest_accentrata_int(java.math.BigDecimal im_spese_gest_accentrata_int)  {
		this.im_spese_gest_accentrata_int=im_spese_gest_accentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_est () {
		return im_spese_gest_accentrata_est;
	}
	public void setIm_spese_gest_accentrata_est(java.math.BigDecimal im_spese_gest_accentrata_est)  {
		this.im_spese_gest_accentrata_est=im_spese_gest_accentrata_est;
	}
	public java.math.BigDecimal getIm_spese_a2 () {
		return im_spese_a2;
	}
	public void setIm_spese_a2(java.math.BigDecimal im_spese_a2)  {
		this.im_spese_a2=im_spese_a2;
	}
	public java.math.BigDecimal getIm_spese_a3 () {
		return im_spese_a3;
	}
	public void setIm_spese_a3(java.math.BigDecimal im_spese_a3)  {
		this.im_spese_a3=im_spese_a3;
	}
	public String getCd_cofog() {
		return cd_cofog;
	}
	public void setCd_cofog(String cd_cofog) {
		this.cd_cofog = cd_cofog;
	}
	public String getCd_missione() {
		return cd_missione;
	}
	public void setCd_missione(String cd_missione) {
		this.cd_missione = cd_missione;
	}

	public java.lang.String getCd_unita_piano() {
		return cd_unita_piano;
	}
	
	public void setCd_unita_piano(java.lang.String cd_unita_piano) {
		this.cd_unita_piano = cd_unita_piano;
	}
	
	public java.lang.String getCd_voce_piano() {
		return cd_voce_piano;
	}
	
	public void setCd_voce_piano(java.lang.String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}
}