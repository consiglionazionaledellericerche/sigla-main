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
* Date 23/11/2005
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_modulo_spese_gestBase extends Pdg_modulo_spese_gestKey implements Keyed {
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_registrazione;
 
//    DESCRIZIONE VARCHAR(300) NOT NULL
	private java.lang.String descrizione;
 
//    ORIGINE VARCHAR(3) NOT NULL
	private java.lang.String origine;
 
//    CATEGORIA_DETTAGLIO VARCHAR(3) NOT NULL
	private java.lang.String categoria_dettaglio;
 
//    FL_SOLA_LETTURA CHAR(1) NOT NULL
	private java.lang.Boolean fl_sola_lettura;
 
//    IM_SPESE_GEST_DECENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_int;
 
//    IM_SPESE_GEST_DECENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_est;
 
//    IM_SPESE_GEST_ACCENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_int;

//    IM_SPESE_GEST_ACCENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_est;

//    IM_PAGAMENTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_pagamenti;

//    CD_CENTRO_RESPONSABILITA_CLGS VARCHAR(30)
	private java.lang.String cd_cdr_assegnatario_clgs;
 
//    CD_LINEA_ATTIVITA_CLGS VARCHAR(10)
	private java.lang.String cd_linea_attivita_clgs;
 
//    PG_PROGETTO_CLGS DECIMAL(10,0)
	private java.lang.Integer pg_progetto_clgs;

//    ESERCIZIO_PDG_VARIAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_pdg_variazione;
 
//    PG_VARIAZIONE_PDG DECIMAL(10,0)
	private java.lang.Long pg_variazione_pdg;
	
	 
	public Pdg_modulo_spese_gestBase() {
		super();
	}
	public Pdg_modulo_spese_gestBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer id_classificazione, java.lang.String cd_cds_area, java.lang.String cd_cdr_assegnatario, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce,Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, id_classificazione, cd_cds_area, cd_cdr_assegnatario, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_elemento_voce,pg_dettaglio);
	}
	public java.sql.Timestamp getDt_registrazione () {
		return dt_registrazione;
	}
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione)  {
		this.dt_registrazione=dt_registrazione;
	}
	public java.lang.String getDescrizione () {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.lang.String getOrigine () {
		return origine;
	}
	public void setOrigine(java.lang.String origine)  {
		this.origine=origine;
	}
	public java.lang.String getCategoria_dettaglio () {
		return categoria_dettaglio;
	}
	public void setCategoria_dettaglio(java.lang.String categoria_dettaglio)  {
		this.categoria_dettaglio=categoria_dettaglio;
	}
	public java.lang.Boolean getFl_sola_lettura () {
		return fl_sola_lettura;
	}
	public void setFl_sola_lettura(java.lang.Boolean fl_sola_lettura)  {
		this.fl_sola_lettura=fl_sola_lettura;
	}
	public void setIm_spese_gest_decentrata_int(java.math.BigDecimal im_spese_gest_decentrata_int)  {
		this.im_spese_gest_decentrata_int=im_spese_gest_decentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_int () {
		return im_spese_gest_decentrata_int;
	}
	public void setIm_spese_gest_decentrata_est(java.math.BigDecimal im_spese_gest_decentrata_est)  {
		this.im_spese_gest_decentrata_est=im_spese_gest_decentrata_est;
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_est () {
		return im_spese_gest_decentrata_est;
	}
	public void setIm_spese_gest_accentrata_int(java.math.BigDecimal im_spese_gest_accentrata_int)  {
		this.im_spese_gest_accentrata_int=im_spese_gest_accentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_int () {
		return im_spese_gest_accentrata_int;
	}
	public void setIm_spese_gest_accentrata_est(java.math.BigDecimal im_spese_gest_accentrata_est)  {
		this.im_spese_gest_accentrata_est=im_spese_gest_accentrata_est;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_est () {
		return im_spese_gest_accentrata_est;
	}
	public void setIm_pagamenti(java.math.BigDecimal im_pagamenti)  {
		this.im_pagamenti=im_pagamenti;
	}
	public java.math.BigDecimal getIm_pagamenti () {
		return im_pagamenti;
	}
	public java.lang.String getCd_cdr_assegnatario_clgs () {
		return cd_cdr_assegnatario_clgs;
	}
	public void setCd_cdr_assegnatario_clgs(java.lang.String cd_cdr_assegnatario_clgs)  {
		this.cd_cdr_assegnatario_clgs=cd_cdr_assegnatario_clgs;
	}
	public java.lang.String getCd_linea_attivita_clgs () {
		return cd_linea_attivita_clgs;
	}
	public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs)  {
		this.cd_linea_attivita_clgs=cd_linea_attivita_clgs;
	}
	public void setPg_progetto_clgs(java.lang.Integer pg_progetto_clgs)  {
		this.pg_progetto_clgs=pg_progetto_clgs;
	}
	public java.lang.Integer getPg_progetto_clgs () {
		return pg_progetto_clgs;
	}
	public java.lang.Integer getEsercizio_pdg_variazione () {
		return esercizio_pdg_variazione;
	}
	public void setEsercizio_pdg_variazione(java.lang.Integer esercizio_pdg_variazione)  {
		this.esercizio_pdg_variazione=esercizio_pdg_variazione;
	}
	public java.lang.Long getPg_variazione_pdg () {
		return pg_variazione_pdg;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
}