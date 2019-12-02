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
public class Pdg_modulo_entrate_gestBase extends Pdg_modulo_entrate_gestKey implements Keyed {
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
 
//    IM_ENTRATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_entrata;
 
//    IM_INCASSI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_incassi;
 
//    CD_CENTRO_RESPONSABILITA_CLGE VARCHAR(30)
	private java.lang.String cd_cdr_assegnatario_clge;
 
//    CD_LINEA_ATTIVITA_CLGE VARCHAR(10)
	private java.lang.String cd_linea_attivita_clge;
 
//    ESERCIZIO_PDG_VARIAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_pdg_variazione;
 
//    PG_VARIAZIONE_PDG DECIMAL(10,0)
	private java.lang.Long pg_variazione_pdg;
 
	public Pdg_modulo_entrate_gestBase() {
		super();
	}
	public Pdg_modulo_entrate_gestBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.String cd_natura, java.lang.Integer id_classificazione, java.lang.String cd_cds_area, java.lang.Long pg_dettaglio, java.lang.String cdr_assegnatario, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, cd_natura, id_classificazione, cd_cds_area, pg_dettaglio, cdr_assegnatario, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_elemento_voce);
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
	public java.lang.String getCd_cdr_assegnatario_clge () {
		return cd_cdr_assegnatario_clge;
	}
	public void setCd_cdr_assegnatario_clge(java.lang.String cd_cdr_assegnatario_clge)  {
		this.cd_cdr_assegnatario_clge=cd_cdr_assegnatario_clge;
	}
	public java.lang.String getCd_linea_attivita_clge () {
		return cd_linea_attivita_clge;
	}
	public void setCd_linea_attivita_clge(java.lang.String cd_linea_attivita_clge)  {
		this.cd_linea_attivita_clge=cd_linea_attivita_clge;
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