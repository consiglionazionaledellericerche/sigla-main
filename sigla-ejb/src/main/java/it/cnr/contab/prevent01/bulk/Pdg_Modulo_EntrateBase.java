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
* Date 16/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_Modulo_EntrateBase extends Pdg_Modulo_EntrateKey implements Keyed {
//	CDR_LINEA VARCHAR(30)
    private java.lang.String cdr_linea;
 
//	CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;

//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;
 
//    DS_DETTAGLIO VARCHAR(300) NOT NULL
	private java.lang.String ds_dettaglio;
 
//    IM_ENTRATA_TOT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata_tot;

//	IM_ENTRATA_APP DECIMAL(15,2) NOT NULL
  private java.math.BigDecimal im_entrata_app;
 
//    ESERCIZIO_INIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_inizio;
 
//    ESERCIZIO_FINE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_fine;
 
//    IM_ENTRATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata;
 
//    IM_ENTRATA_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata_a2;
 
//    IM_ENTRATA_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata_a3;

//	IM_SPESE_VIVE DECIMAL(15,2) NOT NULL
  	private java.math.BigDecimal im_spese_vive;

//	DS_SPESE_VIVE VARCHAR(300) NOT NULL
  	private java.lang.String ds_spese_vive;

//  CD_UNITA_PIANO VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_piano;

//  CD_VOCE_PIANO VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_piano;
  	
	public Pdg_Modulo_EntrateBase() {
		super();
	}
	public Pdg_Modulo_EntrateBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.String cd_natura, java.lang.Integer id_classificazione, java.lang.Long pg_dettaglio,java.lang.String cd_cds_area) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, cd_natura, id_classificazione, pg_dettaglio,cd_cds_area);
	}
	public java.lang.String getCdr_linea () {
		return cdr_linea;
	}
	public void setCdr_linea(java.lang.String cdr_linea)  {
		this.cdr_linea=cdr_linea;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.Integer getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDs_dettaglio () {
		return ds_dettaglio;
	}
	public void setDs_dettaglio(java.lang.String ds_dettaglio)  {
		this.ds_dettaglio=ds_dettaglio;
	}
	public java.math.BigDecimal getIm_entrata_tot () {
		return im_entrata_tot;
	}
	public void setIm_entrata_tot(java.math.BigDecimal im_entrata_tot)  {
		this.im_entrata_tot=im_entrata_tot;
	}
	public java.lang.Integer getEsercizio_inizio () {
		return esercizio_inizio;
	}
	public void setEsercizio_inizio(java.lang.Integer esercizio_inizio)  {
		this.esercizio_inizio=esercizio_inizio;
	}
	public java.lang.Integer getEsercizio_fine () {
		return esercizio_fine;
	}
	public void setEsercizio_fine(java.lang.Integer esercizio_fine)  {
		this.esercizio_fine=esercizio_fine;
	}
	public java.math.BigDecimal getIm_entrata () {
		return im_entrata;
	}
	public void setIm_entrata(java.math.BigDecimal im_entrata)  {
		this.im_entrata=im_entrata;
	}
	public java.math.BigDecimal getIm_entrata_a2 () {
		return im_entrata_a2;
	}
	public void setIm_entrata_a2(java.math.BigDecimal im_entrata_a2)  {
		this.im_entrata_a2=im_entrata_a2;
	}
	public java.math.BigDecimal getIm_entrata_a3 () {
		return im_entrata_a3;
	}
	public void setIm_entrata_a3(java.math.BigDecimal im_entrata_a3)  {
		this.im_entrata_a3=im_entrata_a3;
	}

	public java.lang.String getDs_spese_vive() {
		return ds_spese_vive;
	}

	public java.math.BigDecimal getIm_spese_vive() {
		return im_spese_vive;
	}

	public void setDs_spese_vive(java.lang.String string) {
		ds_spese_vive = string;
	}

	public void setIm_spese_vive(java.math.BigDecimal decimal) {
		im_spese_vive = decimal;
	}
	public java.math.BigDecimal getIm_entrata_app() {
		return im_entrata_app;
	}
	
	public void setIm_entrata_app(java.math.BigDecimal decimal) {
		im_entrata_app = decimal;
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