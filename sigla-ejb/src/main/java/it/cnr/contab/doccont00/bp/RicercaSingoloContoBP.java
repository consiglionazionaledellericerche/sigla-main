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

package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;


import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.SelectionListener;

public class RicercaSingoloContoBP
	extends it.cnr.jada.util.action.SimpleCRUDBP
	implements SelectionListener {

	//private int status = SEARCH;
	private java.math.BigDecimal pg_stampa = null;
	private java.math.BigDecimal currentSequence = null;
	public RicercaSingoloContoBP() throws it.cnr.jada.action.BusinessProcessException {
		this("Tr");
	}

	public RicercaSingoloContoBP(String function) throws it.cnr.jada.action.BusinessProcessException {
		super(function+"Tr");
	}

/**
 * clearSelection method comment.
 */
public void clearSelection(it.cnr.jada.action.ActionContext context)
	throws it.cnr.jada.action.BusinessProcessException {

	try {
		((it.cnr.contab.doccont00.ejb.StampaSingoloContoComponentSession)createComponentSession()).annullaModificaSelezione(
			context.getUserContext(),
			(V_voce_f_sing_contoBulk)getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * deselectAll method comment.
 */
public void deselectAll(it.cnr.jada.action.ActionContext context) {}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
protected it.cnr.jada.persistency.sql.CompoundFindClause getCompoundClauses(
	it.cnr.jada.action.ActionContext actionContext) {
	
	V_voce_f_sing_contoBulk filtro = (V_voce_f_sing_contoBulk)getModel();
	if (filtro == null) return null;

	CompoundFindClause clauses = new CompoundFindClause();
	try {
	//E' ovvio che il codice seguente è ripetitivo, ma data la complessità delle
	//possiblità risulta piu' ordinato alla lettura. Pertanto ho separato per
	//casistica le varie clausole.
	if (!filtro.isEnteInScrivania()) {
		if (filtro.getFl_ente() != null && filtro.getFl_ente().booleanValue()) {
		// filtro per entrata non consentito 
			/*if (filtro.isEntrata()) {
				clauses.addClause("AND", "cd_voce", SQLBuilder.EQUALS, filtro.getCd_voce());
				clauses.addClause("AND", "ti_competenza_residuo", SQLBuilder.EQUALS, filtro.getTi_competenza_residuo());
			} else {
				clauses.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, filtro.getFl_partita_giro());
			}*/
			clauses.addClause("AND", "cd_voce", SQLBuilder.EQUALS, filtro.getCd_voce());
			clauses.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, filtro.getFl_partita_giro());
			clauses.addClause("AND", "cd_cds", SQLBuilder.EQUALS, filtro.getCd_cds_ente());
			clauses.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, "C");
			clauses.addClause("AND", "cd_cds_proprio", SQLBuilder.EQUALS, filtro.getCd_cds_scrivania());
		} else {
				clauses.addClause("AND", "cd_voce", SQLBuilder.EQUALS, filtro.getCd_voce());
			clauses.addClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, filtro.getCd_elemento_voce());
				clauses.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, filtro.getFl_partita_giro());
			if (filtro.isEntrata()) {
					clauses.addClause("AND", "cd_cds", SQLBuilder.EQUALS, filtro.getCd_cds_ente());
					String uo=(((CNRUserContext)actionContext.getUserContext()).getCd_unita_organizzativa());
					Unita_organizzativaBulk bulk= (Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findByPrimaryKey(actionContext.getUserContext(),new Unita_organizzativaBulk(uo));
					if(!bulk.getFl_uo_cds().booleanValue())
					    	clauses.addClause("AND", "cd_unita_organizzativa",SQLBuilder.EQUALS,((CNRUserContext)actionContext.getUserContext()).getCd_unita_organizzativa());
				
				if(filtro.getFl_partita_giro())
					clauses.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, "D");
				else{
					clauses.addClause("AND", "cd_cds_proprio", SQLBuilder.EQUALS, filtro.getCd_cds_scrivania());
					clauses.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, "C");
				}
			}
			else {
				if(!filtro.getFl_partita_giro()){
					clauses.addClause("AND", "cd_cds", SQLBuilder.EQUALS, filtro.getCd_cds_scrivania());
					String uo=(((CNRUserContext)actionContext.getUserContext()).getCd_unita_organizzativa());
					Unita_organizzativaBulk bulk= (Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findByPrimaryKey(actionContext.getUserContext(),new Unita_organizzativaBulk(uo));
					if(!bulk.getFl_uo_cds().booleanValue())
					    	clauses.addClause("AND", "cd_unita_organizzativa",SQLBuilder.EQUALS,((CNRUserContext)actionContext.getUserContext()).getCd_unita_organizzativa());
				}
			clauses.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, "D");
		}
		}
	} else {
		if (filtro.isEntrata()) {
			clauses.addClause("AND", "cd_voce", SQLBuilder.EQUALS, filtro.getCd_voce());
			clauses.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, filtro.getCd_unita_organizzativa());
			clauses.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, filtro.getFl_partita_giro());
		} else {
			clauses.addClause("AND", "cd_voce", SQLBuilder.EQUALS, filtro.getCd_voce());
			clauses.addClause("AND", "cd_natura", SQLBuilder.EQUALS, filtro.getCd_natura());
			clauses.addClause("AND", "cd_cds_proprio", SQLBuilder.EQUALS, filtro.getCd_cds_proprio());
			clauses.addClause("AND", "cd_proprio_voce", SQLBuilder.EQUALS, filtro.getCd_proprio_voce());
			clauses.addClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, filtro.getFl_partita_giro());
			
		}
		clauses.addClause("AND", "cd_cds", SQLBuilder.EQUALS, filtro.getCd_cds_ente());
		clauses.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, "C");
	}

	//Esercizio di scrivania
	clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, filtro.getEsercizio());
	clauses.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, filtro.getTi_gestione());

	filtro.setSqlClauses(clauses);
	} catch (Exception e) {
		   handleException(e);
		}
	return clauses;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 17.01.03)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getCurrentSequence() {
	return currentSequence;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 14.17.45)
 * @return java.lang.Long
 */
public java.math.BigDecimal getPg_stampa() {
	return pg_stampa;
}
/**
 * getSelection method comment.
 */
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) throws it.cnr.jada.action.BusinessProcessException {

	return currentSelection;
}
/**
 * initializeSelection method comment.
 */
public void initializeSelection(it.cnr.jada.action.ActionContext context) 
	throws it.cnr.jada.action.BusinessProcessException {
		
	try {
		it.cnr.contab.doccont00.ejb.StampaSingoloContoComponentSession session = 
				(it.cnr.contab.doccont00.ejb.StampaSingoloContoComponentSession)
						createComponentSession();

		session.inizializzaSelezionePerModifica(
									context.getUserContext(),
									(V_voce_f_sing_contoBulk)getModel());

		setPg_stampa(session.getPgStampa(context.getUserContext()));
		setCurrentSequence(new java.math.BigDecimal(0));
		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isDeleteButtonHidden() {

	return true;
}
public boolean isFreeSearchButtonHidden() {
	
	return true;
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isNewButtonHidden() {
	return true;
}
public boolean isPrintButtonHidden() {
	return true;
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isSaveButtonHidden() {
	return true;
}
/**
 * Inzializza il ricevente nello stato di INSERT.
 */
public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	resetForSearch(context);
}
public void resetIdReport(it.cnr.jada.action.ActionContext context) 
	throws it.cnr.jada.action.BusinessProcessException {

	setPg_stampa(null);
	setCurrentSequence(null);
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator ricercaSingoliConti (it.cnr.jada.action.ActionContext actionContext) 
	throws BusinessProcessException, java.rmi.RemoteException {

	try {
		return find(actionContext,
					getCompoundClauses(actionContext),
					(it.cnr.jada.bulk.OggettoBulk)getModel());
	} catch (Throwable t) {
		throw handleException(t);
	}
}
/**
 * selectAll method comment.
 */
public void selectAll(it.cnr.jada.action.ActionContext context)
	throws it.cnr.jada.action.BusinessProcessException {
		
	try {
		((it.cnr.contab.doccont00.ejb.StampaSingoloContoComponentSession)createComponentSession()).associaTutti(
			context.getUserContext(),
			(V_voce_f_sing_contoBulk)getModel(),
			getPg_stampa());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
	
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 17.01.03)
 * @param newCurrentSequence java.math.BigDecimal
 */
public void setCurrentSequence(java.math.BigDecimal newCurrentSequence) {
	currentSequence = newCurrentSequence;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 14.17.45)
 * @param newPg_stampa java.lang.Long
 */
public void setPg_stampa(java.math.BigDecimal newPg_stampa) {
	pg_stampa = newPg_stampa;
}
/**
 * setSelection method comment.
 */
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {

	try {
		java.math.BigDecimal newSequence = ((it.cnr.contab.doccont00.ejb.StampaSingoloContoComponentSession)createComponentSession()).modificaSelezione(
			context.getUserContext(),
			(V_voce_f_sing_contoBulk)getModel(),
			bulks,
			oldSelection,
			newSelection,
			getPg_stampa(),
			getCurrentSequence());
		setCurrentSequence(newSequence);
		return newSelection;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
}
