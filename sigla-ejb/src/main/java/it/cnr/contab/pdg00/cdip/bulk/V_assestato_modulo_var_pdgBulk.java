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
* Date 12/07/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;

import java.math.BigDecimal;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_assestato_modulo_var_pdgBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Integer esercizio;
 
//	  PG_VARIAZIONE_PDG DECIMAL(9,0)
    private java.lang.Long pg_variazione_pdg;

//    TI_GESTIONE VARCHAR(1)
    private java.lang.String ti_gestione;
    
//    DIPART VARCHAR(30)
	private java.lang.String dipart;
 
//	  PROGETTO VARCHAR(30)
    private java.lang.String progetto;

//    COMMESSA VARCHAR(30)
	private java.lang.String commessa;
 
//    MODULO VARCHAR(30)
	private java.lang.String modulo;
 
//    DS_MODULO VARCHAR(400)
	private java.lang.String ds_modulo;
 
//    TIPO_PROGETTO VARCHAR(10)
	private java.lang.String tipo_progetto;
 
//    SPESE_INI DECIMAL(22,0)
	private BigDecimal spese_ini;
 
//    COSTI_INI DECIMAL(22,0)
	private BigDecimal costi_ini;
 
//    PRE_SPESE_VAR_APP DECIMAL(22,0)
	private BigDecimal pre_spese_var_app;
 
//    PRE_COSTI_VAR_APP DECIMAL(22,0)
	private BigDecimal pre_costi_var_app;
 
//    PRE_SPESE_VAR_PRD DECIMAL(22,0)
	private BigDecimal pre_spese_var_prd;
 
//    PRE_COSTI_VAR_PRD DECIMAL(22,0)
	private BigDecimal pre_costi_var_prd;
 
//	  SPESE_VAR_APP DECIMAL(22,0)
    private BigDecimal spese_var_app;
 
//	  COSTI_VAR_APP DECIMAL(22,0)
    private BigDecimal costi_var_app;
 
//	  SPESE_VAR_PRD DECIMAL(22,0)
    private BigDecimal spese_var_prd;
 
//	  COSTI_VAR_PRD DECIMAL(22,0)
    private BigDecimal costi_var_prd;

//    POST_SPESE_VAR_APP DECIMAL(22,0)
    private BigDecimal post_spese_var_app;
 
//    POST_COSTI_VAR_APP DECIMAL(22,0)
    private BigDecimal post_costi_var_app;
 
//    POST_SPESE_VAR_PRD DECIMAL(22,0)
    private BigDecimal post_spese_var_prd;
 
//    POST_COSTI_VAR_PRD DECIMAL(22,0)
    private BigDecimal post_costi_var_prd;

//	  SPESE_ASSESTATO_APP DECIMAL(22,0)
    private BigDecimal spese_assestato_app;

//	  COSTI_ASSESTATO_APP DECIMAL(22,0)
    private BigDecimal costi_assestato_app;

//    ENTRATE_INI DECIMAL(22,0)
	private BigDecimal entrate_ini;
 
//    RICAVI_INI DECIMAL(22,0)
	private BigDecimal ricavi_ini;
 
//	  PRE_ENTRATE_VAR_APP DECIMAL(22,0)
    private BigDecimal pre_entrate_var_app;
 
//	  PRE_RICAVI_VAR_APP DECIMAL(22,0)
    private BigDecimal pre_ricavi_var_app;
 
//	  PRE_ENTRATE_VAR_PRD DECIMAL(22,0)
    private BigDecimal pre_entrate_var_prd;
  
//	  PRE_RICAVI_VAR_PRD DECIMAL(22,0)
    private BigDecimal pre_ricavi_var_prd;

//    ENTRATE_VAR_APP DECIMAL(22,0)
	private BigDecimal entrate_var_app;
 
//    RICAVI_VAR_APP DECIMAL(22,0)
	private BigDecimal ricavi_var_app;
 
//    ENTRATE_VAR_PRD DECIMAL(22,0)
	private BigDecimal entrate_var_prd;
 
//    RICAVI_VAR_PRD DECIMAL(22,0)
	private BigDecimal ricavi_var_prd;

//    POST_ENTRATE_VAR_APP DECIMAL(22,0)
    private BigDecimal post_entrate_var_app;
 
//    POST_RICAVI_VAR_APP DECIMAL(22,0)
    private BigDecimal post_ricavi_var_app;
 
//    POST_ENTRATE_VAR_PRD DECIMAL(22,0)
    private BigDecimal post_entrate_var_prd;
 
//    POST_RICAVI_VAR_PRD DECIMAL(22,0)
    private BigDecimal post_ricavi_var_prd;

//	  ENTRATE_ASSESTATO_APP DECIMAL(22,0)
    private BigDecimal entrate_assestato_app;

//	  RICAVI_ASSESTATO_APP DECIMAL(22,0)
    private BigDecimal ricavi_assestato_app;

	public V_assestato_modulo_var_pdgBulk() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_variazione_pdg () {
		return pg_variazione_pdg;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
	public java.lang.String getTi_gestione () {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getProgetto () {
		return progetto;
	}
	public void setProgetto(java.lang.String progetto)  {
		this.progetto=progetto;
	}
	public java.lang.String getDipart () {
		return dipart;
	}
	public void setDipart(java.lang.String dipart)  {
		this.dipart=dipart;
	}
	public java.lang.String getCommessa () {
		return commessa;
	}
	public void setCommessa(java.lang.String commessa)  {
		this.commessa=commessa;
	}
	public java.lang.String getModulo () {
		return modulo;
	}
	public void setModulo(java.lang.String modulo)  {
		this.modulo=modulo;
	}
	public java.lang.String getDs_modulo () {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getTipo_progetto () {
		return tipo_progetto;
	}
	public void setTipo_progetto(java.lang.String tipo_progetto)  {
		this.tipo_progetto=tipo_progetto;
	}
	public BigDecimal getSpese_ini () {
		return spese_ini;
	}
	public void setSpese_ini(BigDecimal spese_ini)  {
		this.spese_ini=spese_ini;
	}
	public BigDecimal getCosti_ini () {
		return costi_ini;
	}
	public void setCosti_ini(BigDecimal costi_ini)  {
		this.costi_ini=costi_ini;
	}
	public BigDecimal getPre_spese_var_app () {
		return pre_spese_var_app;
	}
	public void setPre_spese_var_app(BigDecimal pre_spese_var_app)  {
		this.pre_spese_var_app=pre_spese_var_app;
	}
	public BigDecimal getPre_costi_var_app () {
		return pre_costi_var_app;
	}
	public void setPre_costi_var_app(BigDecimal pre_costi_var_app)  {
		this.pre_costi_var_app=pre_costi_var_app;
	}
	public BigDecimal getPre_spese_var_prd () {
		return pre_spese_var_prd;
	}
	public void setPre_spese_var_prd(BigDecimal pre_spese_var_prd)  {
		this.pre_spese_var_prd=pre_spese_var_prd;
	}
	public BigDecimal getPre_costi_var_prd () {
		return pre_costi_var_prd;
	}
	public void setPre_costi_var_prd(BigDecimal pre_costi_var_prd)  {
		this.pre_costi_var_prd=pre_costi_var_prd;
	}
	public BigDecimal getSpese_var_app () {
		return spese_var_app;
	}
	public void setSpese_var_app(BigDecimal spese_var_app)  {
		this.spese_var_app=spese_var_app;
	}
	public BigDecimal getCosti_var_app () {
		return costi_var_app;
	}
	public void setCosti_var_app(BigDecimal costi_var_app)  {
		this.costi_var_app=costi_var_app;
	}
	public BigDecimal getSpese_var_prd () {
		return spese_var_prd;
	}
	public void setSpese_var_prd(BigDecimal spese_var_prd)  {
		this.spese_var_prd=spese_var_prd;
	}
	public BigDecimal getCosti_var_prd () {
		return costi_var_prd;
	}
	public void setCosti_var_prd(BigDecimal costi_var_prd)  {
		this.costi_var_prd=costi_var_prd;
	}
	public BigDecimal getPost_spese_var_app () {
		return post_spese_var_app;
	}
	public void setPost_spese_var_app(BigDecimal post_spese_var_app)  {
		this.post_spese_var_app=post_spese_var_app;
	}
	public BigDecimal getPost_costi_var_app () {
		return post_costi_var_app;
	}
	public void setPost_costi_var_app(BigDecimal post_costi_var_app)  {
		this.post_costi_var_app=post_costi_var_app;
	}
	public BigDecimal getPost_spese_var_prd () {
		return post_spese_var_prd;
	}
	public void setPost_spese_var_prd(BigDecimal post_spese_var_prd)  {
		this.post_spese_var_prd=post_spese_var_prd;
	}
	public BigDecimal getPost_costi_var_prd () {
		return post_costi_var_prd;
	}
	public void setPost_costi_var_prd(BigDecimal post_costi_var_prd)  {
		this.post_costi_var_prd=post_costi_var_prd;
	}
	public BigDecimal getSpese_assestato_app () {
		return spese_assestato_app;
	}
	public void setSpese_assestato_app(BigDecimal spese_assestato_app)  {
		this.spese_assestato_app=spese_assestato_app;
	}
	public BigDecimal getCosti_assestato_app () {
		return costi_assestato_app;
	}
	public void setCosti_assestato_app(BigDecimal costi_assestato_app)  {
		this.costi_assestato_app=costi_assestato_app;
	}
	public BigDecimal getEntrate_ini () {
		return entrate_ini;
	}
	public void setEntrate_ini(BigDecimal entrate_ini)  {
		this.entrate_ini=entrate_ini;
	}
	public BigDecimal getRicavi_ini () {
		return ricavi_ini;
	}
	public void setRicavi_ini(BigDecimal ricavi_ini)  {
		this.ricavi_ini=ricavi_ini;
	}
	public BigDecimal getPre_entrate_var_app () {
		return pre_entrate_var_app;
	}
	public void setPre_entrate_var_app(BigDecimal pre_entrate_var_app)  {
		this.pre_entrate_var_app=pre_entrate_var_app;
	}
	public BigDecimal getPre_ricavi_var_app () {
		return pre_ricavi_var_app;
	}
	public void setPre_ricavi_var_app(BigDecimal pre_ricavi_var_app)  {
		this.pre_ricavi_var_app=pre_ricavi_var_app;
	}
	public BigDecimal getPre_entrate_var_prd () {
		return pre_entrate_var_prd;
	}
	public void setPre_entrate_var_prd(BigDecimal pre_entrate_var_prd)  {
		this.pre_entrate_var_prd=pre_entrate_var_prd;
	}
	public BigDecimal getPre_ricavi_var_prd () {
		return pre_ricavi_var_prd;
	}
	public void setPre_ricavi_var_prd(BigDecimal pre_ricavi_var_prd)  {
		this.pre_ricavi_var_prd=pre_ricavi_var_prd;
	}
	public BigDecimal getEntrate_var_app () {
		return entrate_var_app;
	}
	public void setEntrate_var_app(BigDecimal entrate_var_app)  {
		this.entrate_var_app=entrate_var_app;
	}
	public BigDecimal getRicavi_var_app () {
		return ricavi_var_app;
	}
	public void setRicavi_var_app(BigDecimal ricavi_var_app)  {
		this.ricavi_var_app=ricavi_var_app;
	}
	public BigDecimal getEntrate_var_prd () {
		return entrate_var_prd;
	}
	public void setEntrate_var_prd(BigDecimal entrate_var_prd)  {
		this.entrate_var_prd=entrate_var_prd;
	}
	public BigDecimal getRicavi_var_prd () {
		return ricavi_var_prd;
	}
	public void setRicavi_var_prd(BigDecimal ricavi_var_prd)  {
		this.ricavi_var_prd=ricavi_var_prd;
	}
	public BigDecimal getPost_entrate_var_app () {
		return post_entrate_var_app;
	}
	public void setPost_entrate_var_app(BigDecimal post_entrate_var_app)  {
		this.post_entrate_var_app=post_entrate_var_app;
	}
	public BigDecimal getPost_ricavi_var_app () {
		return post_ricavi_var_app;
	}
	public void setPost_ricavi_var_app(BigDecimal post_ricavi_var_app)  {
		this.post_ricavi_var_app=post_ricavi_var_app;
	}
	public BigDecimal getPost_entrate_var_prd () {
		return post_entrate_var_prd;
	}
	public void setPost_entrate_var_prd(BigDecimal post_entrate_var_prd)  {
		this.post_entrate_var_prd=post_entrate_var_prd;
	}
	public BigDecimal getPost_ricavi_var_prd () {
		return post_ricavi_var_prd;
	}
	public void setPost_ricavi_var_prd(BigDecimal post_ricavi_var_prd)  {
		this.post_ricavi_var_prd=post_ricavi_var_prd;
	}	
	public BigDecimal getEntrate_assestato_app () {
		return entrate_assestato_app;
	}
	public void setEntrate_assestato_app(BigDecimal entrate_assestato_app)  {
		this.entrate_assestato_app=entrate_assestato_app;
	}
	public BigDecimal getRicavi_assestato_app () {
		return ricavi_assestato_app;
	}
	public void setRicavi_assestato_app(BigDecimal ricavi_assestato_app)  {
		this.ricavi_assestato_app=ricavi_assestato_app;
	}
}