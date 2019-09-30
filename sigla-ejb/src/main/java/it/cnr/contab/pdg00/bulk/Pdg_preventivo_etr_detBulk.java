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

package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Pdg_preventivo_etr_det
 */

public class Pdg_preventivo_etr_detBulk extends Pdg_preventivo_etr_detBase implements Pdg_preventivo_detBulk {

	protected WorkpackageBulk linea_attivita;
	protected Elemento_voceBulk elemento_voce;
	protected CdrBulk centro_responsabilita;
	protected CdrBulk centro_responsabilita_clgs;
	protected it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita_clgs;
	protected Classificazione_entrateBulk classificazione_entrate;
	protected Pdg_variazioneBulk pdg_variazione;
	
	public final static java.util.Dictionary STATO;
	static {
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(ST_NESSUNA_AZIONE,"Nessuna azione");
		STATO.put(ST_CONFERMA,"Conferma");
		STATO.put(ST_ANNULLA,"Annulla");
	}

	protected it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs;
	private it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura;
	private it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione;

	protected java.util.Collection nature;
	protected java.util.Collection funzioni;
public Pdg_preventivo_etr_detBulk() {
	super();
}
public Pdg_preventivo_etr_detBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Long pg_entrata,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,pg_entrata,ti_appartenenza,ti_gestione);
	setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione));
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
}
public void completaImportiNulli() {

	if(getIm_ra_rce() == null)
		setIm_ra_rce(IM_ZERO);

	if(getIm_rb_rse() == null)
		setIm_rb_rse(IM_ZERO);

	if(getIm_rc_esr() == null)
		setIm_rc_esr(IM_ZERO);

	if(getIm_rd_a2_ricavi() == null)
		setIm_rd_a2_ricavi(IM_ZERO);

	if(getIm_re_a2_entrate() == null)
		setIm_re_a2_entrate(IM_ZERO);

	if(getIm_rf_a3_ricavi() == null)
		setIm_rf_a3_ricavi(IM_ZERO);

	if(getIm_rg_a3_entrate() == null)
		setIm_rg_a3_entrate(IM_ZERO);
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = this.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
public java.lang.String getCd_centro_responsabilita_clgs() {
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita_clgs = this.getCentro_responsabilita_clgs();
	if (centro_responsabilita_clgs == null)
		return null;
	return centro_responsabilita_clgs.getCd_centro_responsabilita();
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
public java.lang.String getCd_elemento_voce_clgs() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs = this.getElemento_voce_clgs();
	if (elemento_voce_clgs == null)
		return null;
	return elemento_voce_clgs.getCd_elemento_voce();
}
public java.lang.String getCd_funzione() {
	it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione = this.getFunzione();
	if (funzione == null)
		return null;
	return funzione.getCd_funzione();
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	return linea_attivita.getCd_linea_attivita();
}
public java.lang.String getCd_linea_attivita_clgs() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita_clgs = this.getLinea_attivita_clgs();
	if (linea_attivita_clgs == null)
		return null;
	return linea_attivita_clgs.getCd_linea_attivita();
}
public java.lang.String getCd_natura() {
	it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura = this.getNatura();
	if (natura == null)
		return null;
	return natura.getCd_natura();
}
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
		return centro_responsabilita;
	}
public CdrBulk getCentro_responsabilita_clgs() {
		return centro_responsabilita_clgs;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'classificazione_entrate'
	 *
	 * @return Il valore della proprietà 'classificazione_entrate'
	 */
	public Classificazione_entrateBulk getClassificazione_entrate() {
		return (getElemento_voce() != null && getElemento_voce().getClassificazione_entrate() != null && getElemento_voce().getClassificazione_entrate().getEsercizio() != null && getElemento_voce().getClassificazione_entrate().getCodice_cla_e() != null) ? getElemento_voce().getClassificazione_entrate() : classificazione_entrate;
	}

	/* 
	 * Getter dell'attributo ti_appartenenza_clgs
	 */
	public java.lang.String getDs_cdr() {
		if(centro_responsabilita == null) return null;
		return centro_responsabilita.getCd_centro_responsabilita() + " - " + centro_responsabilita.getDs_cdr();
	}

public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce_clgs() {
	return elemento_voce_clgs;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'funzione'
 *
 * @return Il valore della proprietà 'funzione'
 */
public it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk getFunzione() {
	return funzione;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'funzioni'
 *
 * @return Il valore della proprietà 'funzioni'
 */
public java.util.Collection getFunzioni() {
	return funzioni;
}
public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}
public WorkpackageBulk getLinea_attivita_clgs() {
		return linea_attivita_clgs;
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'natura'
 *
 * @return Il valore della proprietà 'natura'
 */
public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
	return natura;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nature'
 *
 * @return Il valore della proprietà 'nature'
 */
public java.util.Collection getNature() {
	return nature;
}
public java.util.Dictionary getOrigineKeys() {
	return Pdg_preventivoBulk.origineKeys;
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione degli stati.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getStatoKeys() {
		return STATO;
	}

public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
public java.lang.String getTi_appartenenza_clgs() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs = this.getElemento_voce_clgs();
	if (elemento_voce_clgs == null)
		return null;
	return elemento_voce_clgs.getTi_appartenenza();
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
public java.lang.String getTi_gestione_clgs() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce_clgs = this.getElemento_voce_clgs();
	if (elemento_voce_clgs == null)
		return null;
	return elemento_voce_clgs.getTi_gestione();
}
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setCentro_responsabilita(
			((it.cnr.contab.pdg00.bp.CRUDEtrDetPdGBP)bp).getCentro_responsabilita()
		);
		setPdg_variazione(((it.cnr.contab.pdg00.bp.CRUDEtrDetPdGBP)bp).getPdg_variazione());
		return super.initialize(bp, context);
	}

	public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setLinea_attivita(new WorkpackageBulk());
		setElemento_voce(new Elemento_voceBulk());
		return super.initializeForFreeSearch(bp,context);
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setLinea_attivita(new WorkpackageBulk());
		return super.initializeForInsert(bp,context);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setLinea_attivita(new WorkpackageBulk());
		return super.initializeForSearch(bp,context);
	}

public boolean isOrigineDefinitivo() {
	return OR_UTENTE.equals(getOrigine());
}
public boolean isOriginePropostaVariazione() {
	return OR_PROPOSTA_VARIAZIONE.equals(getOrigine());
}
public boolean isOrigineStipendi() {
	return OR_STIPENDI.equals(getOrigine());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rODettaglio'
 *
 * @return Il valore della proprietà 'rODettaglio'
 */
public boolean isRODettaglio() {
		if(getFl_sola_lettura() == null)
			return false;
		else
			return getFl_sola_lettura().booleanValue();
	}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOelemento_voce'
 *
 * @return Il valore della proprietà 'rOelemento_voce'
 */
public boolean isROelemento_voce() {
		if(isRODettaglio()) {
			return true;
		} else {
			return (linea_attivita == null
					|| linea_attivita.getCd_linea_attivita() == null
					|| "".equals( linea_attivita.getCd_linea_attivita() )
					);
		}
	}
	public boolean isDaVariazione(){
		return getPdg_variazione()!=null && getPdg_variazione().getPg_variazione_pdg()!=null;	
	}	
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public void setCd_centro_responsabilita_clgs(java.lang.String cd_centro_responsabilita_clgs) {
	this.getCentro_responsabilita_clgs().setCd_centro_responsabilita(cd_centro_responsabilita_clgs);
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_elemento_voce_clgs(java.lang.String cd_elemento_voce_clgs) {
	this.getElemento_voce_clgs().setCd_elemento_voce(cd_elemento_voce_clgs);
}
public void setCd_funzione(java.lang.String cd_funzione) {
	this.getFunzione().setCd_funzione(cd_funzione);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
public void setCd_linea_attivita_clgs(java.lang.String cd_linea_attivita_clgs) {
	this.getLinea_attivita_clgs().setCd_linea_attivita(cd_linea_attivita_clgs);
}
public void setCd_natura(java.lang.String cd_natura) {
	this.getNatura().setCd_natura(cd_natura);
}
public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
		centro_responsabilita = newCentro_responsabilita;
	}
public void setCentro_responsabilita_clgs(CdrBulk newCentro_responsabilita_clgs) {
		centro_responsabilita_clgs = newCentro_responsabilita_clgs;
	}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'classificazione_entrate'
 *
 * @param newClassificazione_spese	Il valore da assegnare a 'classificazione_entrate'
 */
public void setClassificazione_entrate(Classificazione_entrateBulk newClassificazione_entrate) {
	classificazione_entrate = newClassificazione_entrate;
	}
public void setElemento_voce(Elemento_voceBulk newElemento_voce) {
		elemento_voce = newElemento_voce;
	}
public void setElemento_voce_clgs(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce_clgs) {
	elemento_voce_clgs = newElemento_voce_clgs;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'funzione'
 *
 * @param newFunzione	Il valore da assegnare a 'funzione'
 */
public void setFunzione(it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk newFunzione) {
	funzione = newFunzione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'funzioni'
 *
 * @param newFunzioni	Il valore da assegnare a 'funzioni'
 */
public void setFunzioni(java.util.Collection newFunzioni) {
	funzioni = newFunzioni;
}
public void setLinea_attivita(WorkpackageBulk newLinea_attivita) {
		linea_attivita = newLinea_attivita;
		setNatura(linea_attivita.getNatura());
		setFunzione(linea_attivita.getFunzione());
	}
public void setLinea_attivita_clgs(WorkpackageBulk newLinea_attivita_clgs) {
		linea_attivita_clgs = newLinea_attivita_clgs;
	}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'natura'
 *
 * @param newNatura	Il valore da assegnare a 'natura'
 */
public void setNatura(it.cnr.contab.config00.pdcfin.bulk.NaturaBulk newNatura) {
	natura = newNatura;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'nature'
 *
 * @param newNature	Il valore da assegnare a 'nature'
 */
public void setNature(java.util.Collection newNature) {
	nature = newNature;
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_appartenenza_clgs(java.lang.String ti_appartenenza_clgs) {
	this.getElemento_voce_clgs().setTi_appartenenza(ti_appartenenza_clgs);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
public void setTi_gestione_clgs(java.lang.String ti_gestione_clgs) {
	this.getElemento_voce_clgs().setTi_gestione(ti_gestione_clgs);
}
/**
 * @return
 */
public Pdg_variazioneBulk getPdg_variazione() {
	return pdg_variazione;
}

/**
 * @param bulk
 */
public void setPdg_variazione(Pdg_variazioneBulk bulk) {
	pdg_variazione = bulk;
}
/**
 * @return
 */
public java.lang.Integer getEsercizio_pdg_variazione() {
	return getPdg_variazione().getEsercizio();
}

/**
 * @return
 */
public java.lang.Long getPg_variazione_pdg() {
	return getPdg_variazione().getPg_variazione_pdg();
}

/**
 * @param integer
 */
public void setEsercizio_pdg_variazione(java.lang.Integer integer) {
	getPdg_variazione().setEsercizio(integer);
}

/**
 * @param long1
 */
public void setPg_variazione_pdg(java.lang.Long long1) {
	getPdg_variazione().setPg_variazione_pdg(long1);
}
}
