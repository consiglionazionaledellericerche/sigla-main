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
* Date 23/05/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_cons_var_bilancioBase extends OggettoBulk  implements Persistent {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;
 
//    PG_VARIAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_variazione;
 
//    DS_VARIAZIONE VARCHAR(300)
	private java.lang.String ds_variazione;
 
//    DS_DELIBERA VARCHAR(300)
	private java.lang.String ds_delibera;
 
//    TI_VARIAZIONE VARCHAR(10) NOT NULL
	private java.lang.String ti_variazione;
 
//    CD_CAUSALE_VAR_BILANCIO VARCHAR(10)
	private java.lang.String cd_causale_var_bilancio;
 
//  DS_CAUSALE_VAR_BILANCIO VARCHAR(200)
	private java.lang.String ds_causale_var_bilancio;

	//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
//    ESERCIZIO_IMPORTI DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_importi;
 
//    ESERCIZIO_PDG_VARIAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_pdg_variazione;
 
//    PG_VARIAZIONE_PDG DECIMAL(10,0)
	private java.lang.Long pg_variazione_pdg;
 
//    ESERCIZIO_VAR_STANZ_RES DECIMAL(4,0)
	private java.lang.Integer esercizio_var_stanz_res;
 
//    PG_VAR_STANZ_RES DECIMAL(10,0)
	private java.lang.Long pg_var_stanz_res;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;
 
//    IM_VARIAZIONE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_variazione;
 
	public V_cons_var_bilancioBase() {
		super();
	}
	public java.lang.String getCd_cds () {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getTi_appartenenza () {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza)  {
		this.ti_appartenenza=ti_appartenenza;
	}
	public java.lang.Long getPg_variazione () {
		return pg_variazione;
	}
	public void setPg_variazione(java.lang.Long pg_variazione)  {
		this.pg_variazione=pg_variazione;
	}
	public java.lang.String getDs_variazione () {
		return ds_variazione;
	}
	public void setDs_variazione(java.lang.String ds_variazione)  {
		this.ds_variazione=ds_variazione;
	}
	public java.lang.String getDs_delibera () {
		return ds_delibera;
	}
	public void setDs_delibera(java.lang.String ds_delibera)  {
		this.ds_delibera=ds_delibera;
	}
	public java.lang.String getTi_variazione () {
		return ti_variazione;
	}
	public void setTi_variazione(java.lang.String ti_variazione)  {
		this.ti_variazione=ti_variazione;
	}
	public java.lang.String getCd_causale_var_bilancio () {
		return cd_causale_var_bilancio;
	}
	public void setCd_causale_var_bilancio(java.lang.String cd_causale_var_bilancio)  {
		this.cd_causale_var_bilancio=cd_causale_var_bilancio;
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.Integer getEsercizio_importi () {
		return esercizio_importi;
	}
	public void setEsercizio_importi(java.lang.Integer esercizio_importi)  {
		this.esercizio_importi=esercizio_importi;
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
	public java.lang.Integer getEsercizio_var_stanz_res () {
		return esercizio_var_stanz_res;
	}
	public void setEsercizio_var_stanz_res(java.lang.Integer esercizio_var_stanz_res)  {
		this.esercizio_var_stanz_res=esercizio_var_stanz_res;
	}
	public java.lang.Long getPg_var_stanz_res () {
		return pg_var_stanz_res;
	}
	public void setPg_var_stanz_res(java.lang.Long pg_var_stanz_res)  {
		this.pg_var_stanz_res=pg_var_stanz_res;
	}
	public java.lang.String getTi_gestione () {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getCd_voce () {
		return cd_voce;
	}
	public void setCd_voce(java.lang.String cd_voce)  {
		this.cd_voce=cd_voce;
	}
	public java.math.BigDecimal getIm_variazione () {
		return im_variazione;
	}
	public void setIm_variazione(java.math.BigDecimal im_variazione)  {
		this.im_variazione=im_variazione;
	}
	public java.lang.String getDs_causale_var_bilancio() {
		return ds_causale_var_bilancio;
	}
	public void setDs_causale_var_bilancio(java.lang.String ds_causale_var_bilancio) {
		this.ds_causale_var_bilancio = ds_causale_var_bilancio;
	}
}