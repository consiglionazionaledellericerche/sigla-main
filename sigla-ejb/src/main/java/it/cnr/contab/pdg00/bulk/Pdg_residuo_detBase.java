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
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_residuo_detBase extends Pdg_residuo_detKey implements Keyed {
//	CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
  	private java.lang.String cd_cdr_linea;

//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    CD_FUNZIONE VARCHAR(2) NOT NULL
	private java.lang.String cd_funzione;
 
//    CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    DT_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_registrazione;
 
//    DESCRIZIONE VARCHAR(300) NOT NULL
	private java.lang.String descrizione;
 
//    IM_RESIDUO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_residuo;
 
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
	public Pdg_residuo_detBase() {
		super();
	}
	public Pdg_residuo_detBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_dettaglio);
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getCd_funzione () {
		return cd_funzione;
	}
	public void setCd_funzione(java.lang.String cd_funzione)  {
		this.cd_funzione=cd_funzione;
	}
	public java.lang.String getCd_natura () {
		return cd_natura;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
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
	public java.math.BigDecimal getIm_residuo () {
		return im_residuo;
	}
	public void setIm_residuo(java.math.BigDecimal im_residuo)  {
		this.im_residuo=im_residuo;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getCd_cdr_linea() {
		return cd_cdr_linea;
	}

	public void setCd_cdr_linea(java.lang.String string) {
		cd_cdr_linea = string;
	}

}