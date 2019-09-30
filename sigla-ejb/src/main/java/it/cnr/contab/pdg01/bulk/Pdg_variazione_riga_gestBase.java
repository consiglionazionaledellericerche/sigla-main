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
* Date 21/04/2006
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_variazione_riga_gestBase extends Pdg_variazione_riga_gestKey implements Keyed {
//    CD_CDR_ASSEGNATARIO VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_assegnatario;
 
//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    CD_CDS_AREA VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_area;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_registrazione;
 
//    DESCRIZIONE VARCHAR(300)
	private java.lang.String descrizione;
 
//    CATEGORIA_DETTAGLIO VARCHAR(3) NOT NULL
	private java.lang.String categoria_dettaglio;
 
//    IM_SPESE_GEST_DECENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_int;
 
//    IM_SPESE_GEST_DECENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_est;
 
//    IM_SPESE_GEST_ACCENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_int;
 
//    IM_SPESE_GEST_ACCENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_est;
 
//    IM_ENTRATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata;

//    PG_RIGA_CLGS DECIMAL(5,0)
	private java.lang.Integer pg_riga_clgs;
 
//    CD_CDR_ASSEGNATARIO_CLGS VARCHAR(30)
	private java.lang.String cd_cdr_assegnatario_clgs;
 
//    CD_LINEA_ATTIVITA_CLGS VARCHAR(10)
	private java.lang.String cd_linea_attivita_clgs;
 
//    FL_VISTO_DIP_VARIAZIONI VARCHAR(1)
	private Boolean fl_visto_dip_variazioni;

	public Pdg_variazione_riga_gestBase() {
		super();
	}
	public Pdg_variazione_riga_gestBase(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Integer pg_riga) {
		super(esercizio, pg_variazione_pdg, pg_riga);
	}
	public java.lang.String getCd_cdr_assegnatario () {
		return cd_cdr_assegnatario;
	}
	public void setCd_cdr_assegnatario(java.lang.String cd_cdr_assegnatario)  {
		this.cd_cdr_assegnatario=cd_cdr_assegnatario;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getCd_cds_area () {
		return cd_cds_area;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
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
	public java.lang.String getCategoria_dettaglio () {
		return categoria_dettaglio;
	}
	public void setCategoria_dettaglio(java.lang.String categoria_dettaglio)  {
		this.categoria_dettaglio=categoria_dettaglio;
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
	public java.math.BigDecimal getIm_entrata () {
		return im_entrata;
	}
	public void setIm_entrata(java.math.BigDecimal im_entrata)  {
		this.im_entrata=im_entrata;
	}
	public java.lang.Integer getPg_riga_clgs () {
		return pg_riga_clgs;
	}
	public void setPg_riga_clgs(java.lang.Integer pg_riga_clgs)  {
		this.pg_riga_clgs=pg_riga_clgs;
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
	public Boolean getFl_visto_dip_variazioni() {
		return fl_visto_dip_variazioni;
	}
	public void setFl_visto_dip_variazioni(Boolean fl_visto_dip_variazioni) {
		this.fl_visto_dip_variazioni = fl_visto_dip_variazioni;
	}
}