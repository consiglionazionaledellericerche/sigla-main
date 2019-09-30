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
public class Pdg_contrattazione_speseBase extends Pdg_contrattazione_speseKey implements Keyed {

	private java.lang.Integer id_classificazione;

	private java.lang.String cd_cds_area;

//    TOT_SPESE_DECENTR_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal tot_spese_decentr_int;
 
//    TOT_SPESE_DECENTR_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal tot_spese_decentr_est;
 
//    APPR_TOT_SPESE_DECENTR_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal appr_tot_spese_decentr_int;
 
//    APPR_TOT_SPESE_DECENTR_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal appr_tot_spese_decentr_est;
 
	private java.lang.Integer pg_esercizio_dip;
	
	private java.lang.String cd_dipartimento;
	
	private java.lang.Integer pg_dettaglio_dip;

	public Pdg_contrattazione_speseBase() {
		super();
	}
	public Pdg_contrattazione_speseBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, pg_dettaglio);
	}
	public java.math.BigDecimal getTot_spese_decentr_int () {
		return tot_spese_decentr_int;
	}
	public void setTot_spese_decentr_int(java.math.BigDecimal tot_spese_decentr_int)  {
		this.tot_spese_decentr_int=tot_spese_decentr_int;
	}
	public java.math.BigDecimal getTot_spese_decentr_est () {
		return tot_spese_decentr_est;
	}
	public void setTot_spese_decentr_est(java.math.BigDecimal tot_spese_decentr_est)  {
		this.tot_spese_decentr_est=tot_spese_decentr_est;
	}
	public java.math.BigDecimal getAppr_tot_spese_decentr_int () {
		return appr_tot_spese_decentr_int;
	}
	public void setAppr_tot_spese_decentr_int(java.math.BigDecimal appr_tot_spese_decentr_int)  {
		this.appr_tot_spese_decentr_int=appr_tot_spese_decentr_int;
	}
	public java.math.BigDecimal getAppr_tot_spese_decentr_est () {
		return appr_tot_spese_decentr_est;
	}
	public void setAppr_tot_spese_decentr_est(java.math.BigDecimal appr_tot_spese_decentr_est)  {
		this.appr_tot_spese_decentr_est=appr_tot_spese_decentr_est;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	public java.lang.Integer getId_classificazione () {
		return id_classificazione;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
	}
	public java.lang.String getCd_cds_area () {
		return cd_cds_area;
	}
	public java.lang.Integer getEsercizio_dip() {
		return pg_esercizio_dip;
	}
	public void setPg_esercizio_dip(java.lang.Integer pg_esercizio_dip) {
		this.pg_esercizio_dip = pg_esercizio_dip;
	}
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento) {
		this.cd_dipartimento = cd_dipartimento;
	}
	public java.lang.Integer getPg_dettaglio_dip() {
		return pg_dettaglio_dip;
	}
	public void setPg_dettaglio_dip(java.lang.Integer pg_dettaglio_dip) {
		this.pg_dettaglio_dip = pg_dettaglio_dip;
	}
}