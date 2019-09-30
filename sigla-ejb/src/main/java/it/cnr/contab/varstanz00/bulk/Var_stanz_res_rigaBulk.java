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
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
public class Var_stanz_res_rigaBulk extends Var_stanz_res_rigaBase {
	private static final long serialVersionUID = 1L;

	private Voce_fBulk voce_f;
	private WorkpackageBulk linea_di_attivita;
	private Var_stanz_resBulk var_stanz_res; 
	private Elemento_voceBulk elemento_voce;
	private CdrBulk centroTestata;
	private BigDecimal disponibilita_stanz_res;
	private ProgettoBulk progetto = new ProgettoBulk();
	private Pdg_missioneBulk missione = new Pdg_missioneBulk();
	public Pdg_missioneBulk getMissione() {
		return missione;
	}
	public void setMissione(Pdg_missioneBulk missione) {
		this.missione = missione;
	}
	public Pdg_programmaBulk programma =new Pdg_programmaBulk();
	public Var_stanz_res_rigaBulk() {
		super();
	}
	public Var_stanz_res_rigaBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione, java.lang.Long pg_riga) {
		super(esercizio, pg_variazione, pg_riga);
		setVar_stanz_res(new Var_stanz_resBulk(esercizio, pg_variazione));
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.bulk.OggettoBulk#initializeForInsert(it.cnr.jada.util.action.CRUDBP, it.cnr.jada.action.ActionContext)
	 */
	public OggettoBulk initializeForInsert(CRUDBP crudbp,ActionContext actioncontext) {
		setDisponibilita_stanz_res(Utility.ZERO);
		return super.initializeForInsert(crudbp, actioncontext);
	}

	/**
	 * @return
	 */
	public WorkpackageBulk getLinea_di_attivita() {
		return linea_di_attivita;
	}

	/**
	 * @return
	 */
	public Var_stanz_resBulk getVar_stanz_res() {
		return var_stanz_res;
	}

	/**
	 * @return
	 */
	public Voce_fBulk getVoce_f() {
		return voce_f;
	}

	/**
	 * @param bulk
	 */
	public void setLinea_di_attivita(WorkpackageBulk bulk) {
		linea_di_attivita = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setVar_stanz_res(Var_stanz_resBulk bulk) {
		var_stanz_res = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setVoce_f(Voce_fBulk bulk) {
		voce_f = bulk;
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setCd_linea_attivita(java.lang.String)
	 */
	public void setCd_linea_attivita(String cd_linea_attivita) {
		getLinea_di_attivita().setCd_linea_attivita(cd_linea_attivita);
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getCd_linea_attivita()
	 */
	public String getCd_linea_attivita() {
		return getLinea_di_attivita().getCd_linea_attivita();
	}


	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#setEsercizio(java.lang.Integer)
	 */
	public void setEsercizio(Integer esercizio) {
		getVar_stanz_res().setEsercizio(esercizio);
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#getEsercizio()
	 */
	public Integer getEsercizio() {
		return getVar_stanz_res().getEsercizio();
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#setPg_variazione(java.lang.Long)
	 */
	public void setPg_variazione(Long pg_variazione) {
		getVar_stanz_res().setPg_variazione(pg_variazione);
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrKey#getPg_variazione()
	 */
	public Long getPg_variazione() {
		return getVar_stanz_res().getPg_variazione();
	}    
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setCd_cdr(java.lang.String)
	 */
	public void setCd_cdr(String cd_cdr) {
		  getLinea_di_attivita().setCd_centro_responsabilita(cd_cdr);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getCd_cdr()
	 */
	public String getCd_cdr() {
		return getLinea_di_attivita().getCd_centro_responsabilita();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setEsercizio_voce(java.lang.Integer)
	 */
	public void setEsercizio_voce(Integer esercizio_voce) {
		getElemento_voce().setEsercizio(esercizio_voce);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setTi_appartenenza(java.lang.String)
	 */
	public void setTi_appartenenza(String ti_appartenenza) {
		getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setTi_gestione(java.lang.String)
	 */
	public void setTi_gestione(String ti_gestione) {
		getElemento_voce().setTi_gestione(ti_gestione);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setCd_voce(java.lang.String)
	 */
	public void setCd_voce(String cd_voce) {
		if (getVoce_f()!=null)
			getVoce_f().setCd_voce(cd_voce);
		else
			super.setCd_voce(cd_voce);
	}	
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getEsercizio_voce()
	 */
	public Integer getEsercizio_voce() {
		return getElemento_voce().getEsercizio();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getTi_appartenenza()
	 */
	public String getTi_appartenenza() {
		return getElemento_voce().getTi_appartenenza();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getTi_gestione()
	 */
	public String getTi_gestione() {
		return getElemento_voce().getTi_gestione();
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getCd_voce()
	 */
	public String getCd_voce() {
		if (this.getVoce_f()!=null)
			return getVoce_f().getCd_voce();
		return super.getCd_voce();
	}
	/**
	 * @return
	 */
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}

	/**
	 * @param bulk
	 */
	public void setElemento_voce(Elemento_voceBulk bulk) {
		elemento_voce = bulk;
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#setCd_elemento_voce(java.lang.String)
	 */
	public void setCd_elemento_voce(String string) {
		getElemento_voce().setCd_elemento_voce(string);
	}
    /* (non-Javadoc)
	 * @see it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBase#getCd_elemento_voce()
	 */
	public String getCd_elemento_voce() {
		if(this.getElemento_voce()!=null)
			return getElemento_voce().getCd_elemento_voce();
		else
			return null;
	}


	/**
	 * @return
	 */
	public CdrBulk getCentroTestata() {
		return centroTestata;
	}

	/**
	 * @param bulk
	 */
	public void setCentroTestata(CdrBulk bulk) {
		centroTestata = bulk;
	}

	/**
	 * @return
	 */
	public BigDecimal getDisponibilita_stanz_res() {
		return disponibilita_stanz_res;
	}

	/**
	 * @param decimal
	 */
	public void setDisponibilita_stanz_res(BigDecimal decimal) {
		disponibilita_stanz_res = decimal;
	}
	public void validate(OggettoBulk oggettoBulk) throws ValidationException {		
		if (getVar_stanz_res().getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO)){
			if(getIm_variazione() != null && getIm_variazione().compareTo(Utility.ZERO) > 0) 
			  throw new ValidationException("L'importo delle righe di variazione deve essere negativo per le Economie.");
		}
		if(this.getElemento_voce()!=null && this.getElemento_voce().getFl_azzera_residui()!=null && this.getElemento_voce().getFl_azzera_residui() &&  this.getIm_variazione()!=null && this.getIm_variazione().compareTo(BigDecimal.ZERO)>0)
			throw new ValidationException ("Attenzione non può essere predisposta una variazione positiva sulla voce "+this.getElemento_voce().getCd_elemento_voce());
		 
		for (java.util.Iterator i = var_stanz_res.getRigaVariazione().iterator();i.hasNext();) {
			Var_stanz_res_rigaBulk riga = (Var_stanz_res_rigaBulk)i.next();
			if (!riga.equals(this) &&
					this.getEsercizio()!= null && riga.getEsercizio().compareTo(this.getEsercizio())==0 &&
					this.getEsercizio_res()!= null && riga.getEsercizio_res().compareTo(this.getEsercizio_res())==0 &&
					this.getLinea_di_attivita()!= null && riga.getLinea_di_attivita()!= null &&
					this.getCd_cdr()!= null &&  riga.getCd_cdr()!= null &&
					riga.getCd_cdr().compareTo(this.getCd_cdr())==0 &&
					this.getCd_linea_attivita()!= null && riga.getCd_linea_attivita()!=null && 
					riga.getCd_linea_attivita().compareTo(this.getCd_linea_attivita())==0 &&
					this.getCd_elemento_voce()!= null && riga.getCd_elemento_voce()!=null && 
					riga.getCd_elemento_voce().compareTo(this.getCd_elemento_voce())==0)
				throw new ValidationException ("Attenzione: combinazione Esercizio/Esercizio residuo/CdR/G.A.E./Voce già inserita!");
			}
		super.validate();
	}

	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	public Pdg_programmaBulk getProgramma() {
		return programma;
	}
	public void setProgramma(Pdg_programmaBulk programma) {
		this.programma = programma;
	}
}