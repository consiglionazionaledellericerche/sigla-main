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
* Date 02/05/2005
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_pdg_accertamento_etrBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    ESERCIZIO_RES DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_res;

//    CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    CD_PROGETTO_PADRE VARCHAR(30)
	private java.lang.String cd_progetto_padre;
 
//    CD_FUNZIONE VARCHAR(2)
	private java.lang.String cd_funzione;
 
//    CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;
 
//    IM_RA_RCE DECIMAL(22,0)
	private java.math.BigDecimal im_ra_rce;
 
//    IM_RB_RSE DECIMAL(22,0)
	private java.math.BigDecimal im_rb_rse;
 
//    IM_RC_ESR DECIMAL(22,0)
	private java.math.BigDecimal im_rc_esr;
 
//    IM_RD_A2_RICAVI DECIMAL(22,0)
	private java.math.BigDecimal im_rd_a2_ricavi;
 
//    IM_RE_A2_ENTRATE DECIMAL(22,0)
	private java.math.BigDecimal im_re_a2_entrate;
 
//    IM_RF_A3_RICAVI DECIMAL(22,0)
	private java.math.BigDecimal im_rf_a3_ricavi;
 
//    IM_RG_A3_ENTRATE DECIMAL(22,0)
	private java.math.BigDecimal im_rg_a3_entrate;
 
//    CD_CENTRO_RESPONSABILITA_CLGS VARCHAR(30)
	private java.lang.String cd_centro_responsabilita_clgs;
 
//    CD_LINEA_ATTIVITA_CLGS VARCHAR(10)
	private java.lang.String cd_linea_attivita_clgs;
 
//    TI_APPARTENENZA_CLGS CHAR(1)
	private java.lang.String ti_appartenenza_clgs;
 
//    TI_GESTIONE_CLGS CHAR(1)
	private java.lang.String ti_gestione_clgs;
 
//    CD_ELEMENTO_VOCE_CLGS VARCHAR(20)
	private java.lang.String cd_elemento_voce_clgs;
 
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
//    CATEGORIA_DETTAGLIO VARCHAR(3) NOT NULL
	private java.lang.String categoria_dettaglio;
 
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
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
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getCd_progetto_padre () {
		return cd_progetto_padre;
	}
	public void setCd_progetto_padre(java.lang.String cd_progetto_padre)  {
		this.cd_progetto_padre=cd_progetto_padre;
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
	public java.lang.String getDs_linea_attivita () {
		return ds_linea_attivita;
	}
	public void setDs_linea_attivita(java.lang.String ds_linea_attivita)  {
		this.ds_linea_attivita=ds_linea_attivita;
	}
	public java.math.BigDecimal getIm_ra_rce () {
		return im_ra_rce;
	}
	public void setIm_ra_rce(java.math.BigDecimal im_ra_rce)  {
		this.im_ra_rce=im_ra_rce;
	}
	public java.math.BigDecimal getIm_rb_rse () {
		return im_rb_rse;
	}
	public void setIm_rb_rse(java.math.BigDecimal im_rb_rse)  {
		this.im_rb_rse=im_rb_rse;
	}
	public java.math.BigDecimal getIm_rc_esr () {
		return im_rc_esr;
	}
	public void setIm_rc_esr(java.math.BigDecimal im_rc_esr)  {
		this.im_rc_esr=im_rc_esr;
	}
	public java.math.BigDecimal getIm_rd_a2_ricavi () {
		return im_rd_a2_ricavi;
	}
	public void setIm_rd_a2_ricavi(java.math.BigDecimal im_rd_a2_ricavi)  {
		this.im_rd_a2_ricavi=im_rd_a2_ricavi;
	}
	public java.math.BigDecimal getIm_re_a2_entrate () {
		return im_re_a2_entrate;
	}
	public void setIm_re_a2_entrate(java.math.BigDecimal im_re_a2_entrate)  {
		this.im_re_a2_entrate=im_re_a2_entrate;
	}
	public java.math.BigDecimal getIm_rf_a3_ricavi () {
		return im_rf_a3_ricavi;
	}
	public void setIm_rf_a3_ricavi(java.math.BigDecimal im_rf_a3_ricavi)  {
		this.im_rf_a3_ricavi=im_rf_a3_ricavi;
	}
	public java.math.BigDecimal getIm_rg_a3_entrate () {
		return im_rg_a3_entrate;
	}
	public void setIm_rg_a3_entrate(java.math.BigDecimal im_rg_a3_entrate)  {
		this.im_rg_a3_entrate=im_rg_a3_entrate;
	}
	public java.lang.String getCd_centro_responsabilita_clgs () {
		return cd_centro_responsabilita_clgs;
	}
	public void setCd_centro_responsabilita_clgs(java.lang.String cd_centro_responsabilita_clgs)  {
		this.cd_centro_responsabilita_clgs=cd_centro_responsabilita_clgs;
	}
	public java.lang.String getCd_linea_attivita_clgs () {
		return cd_linea_attivita_clgs;
	}
	public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs)  {
		this.cd_linea_attivita_clgs=cd_linea_attivita_clgs;
	}
	public java.lang.String getTi_appartenenza_clgs () {
		return ti_appartenenza_clgs;
	}
	public void setTi_appartenenza_clgs(java.lang.String ti_appartenenza_clgs)  {
		this.ti_appartenenza_clgs=ti_appartenenza_clgs;
	}
	public java.lang.String getTi_gestione_clgs () {
		return ti_gestione_clgs;
	}
	public void setTi_gestione_clgs(java.lang.String ti_gestione_clgs)  {
		this.ti_gestione_clgs=ti_gestione_clgs;
	}
	public java.lang.String getCd_elemento_voce_clgs () {
		return cd_elemento_voce_clgs;
	}
	public void setCd_elemento_voce_clgs(java.lang.String cd_elemento_voce_clgs)  {
		this.cd_elemento_voce_clgs=cd_elemento_voce_clgs;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getCategoria_dettaglio () {
		return categoria_dettaglio;
	}
	public void setCategoria_dettaglio(java.lang.String categoria_dettaglio)  {
		this.categoria_dettaglio=categoria_dettaglio;
	}
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}
	public void setEsercizio_res(java.lang.Integer integer) {
		esercizio_res = integer;
	}
}