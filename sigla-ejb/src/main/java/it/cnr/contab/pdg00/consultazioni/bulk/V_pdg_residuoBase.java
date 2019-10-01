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
* Date 29/06/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_pdg_residuoBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
 
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;
 
//    STATO_RESIDUO CHAR(1) NOT NULL
	private java.lang.String stato_residuo;
 
//    IM_MASSA_SPENDIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_massa_spendibile;
 
//    PG_DETTAGLIO DECIMAL(7,0) NOT NULL
	private java.lang.Integer pg_dettaglio;
 
//    CD_CDR_LINEA VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_linea;
 
//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    LA_DENOMINAZIONE VARCHAR(300)
	private java.lang.String la_denominazione;
 
//    CD_FUNZIONE VARCHAR(2) NOT NULL
	private java.lang.String cd_funzione;
 
//    CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    CODICE_CLA_S VARCHAR(20)
	private java.lang.String codice_cla_s;
 
//    CLA_S_DESCRIZIONE VARCHAR(400)
	private java.lang.String cla_s_descrizione;
 
//    RES_DESCRIZIONE VARCHAR(300) NOT NULL
	private java.lang.String res_descrizione;
 
//    IM_RESIDUO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_residuo;
 
//    STATO_DETTAGLIO CHAR(1) NOT NULL
	private java.lang.String stato_dettaglio;
 
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
	public java.lang.String getDs_cdr () {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.String getStato_residuo () {
		return stato_residuo;
	}
	public void setStato_residuo(java.lang.String stato_residuo)  {
		this.stato_residuo=stato_residuo;
	}
	public java.math.BigDecimal getIm_massa_spendibile () {
		return im_massa_spendibile;
	}
	public void setIm_massa_spendibile(java.math.BigDecimal im_massa_spendibile)  {
		this.im_massa_spendibile=im_massa_spendibile;
	}
	public java.lang.Integer getPg_dettaglio () {
		return pg_dettaglio;
	}
	public void setPg_dettaglio(java.lang.Integer pg_dettaglio)  {
		this.pg_dettaglio=pg_dettaglio;
	}
	public java.lang.String getCd_cdr_linea () {
		return cd_cdr_linea;
	}
	public void setCd_cdr_linea(java.lang.String cd_cdr_linea)  {
		this.cd_cdr_linea=cd_cdr_linea;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getLa_denominazione () {
		return la_denominazione;
	}
	public void setLa_denominazione(java.lang.String la_denominazione)  {
		this.la_denominazione=la_denominazione;
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
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getCd_dipartimento () {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
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
	public java.lang.String getCodice_cla_s () {
		return codice_cla_s;
	}
	public void setCodice_cla_s(java.lang.String codice_cla_s)  {
		this.codice_cla_s=codice_cla_s;
	}
	public java.lang.String getCla_s_descrizione () {
		return cla_s_descrizione;
	}
	public void setCla_s_descrizione(java.lang.String cla_s_descrizione)  {
		this.cla_s_descrizione=cla_s_descrizione;
	}
	public java.lang.String getRes_descrizione () {
		return res_descrizione;
	}
	public void setRes_descrizione(java.lang.String res_descrizione)  {
		this.res_descrizione=res_descrizione;
	}
	public java.math.BigDecimal getIm_residuo () {
		return im_residuo;
	}
	public void setIm_residuo(java.math.BigDecimal im_residuo)  {
		this.im_residuo=im_residuo;
	}
	public java.lang.String getStato_dettaglio () {
		return stato_dettaglio;
	}
	public void setStato_dettaglio(java.lang.String stato_dettaglio)  {
		this.stato_dettaglio=stato_dettaglio;
	}
}