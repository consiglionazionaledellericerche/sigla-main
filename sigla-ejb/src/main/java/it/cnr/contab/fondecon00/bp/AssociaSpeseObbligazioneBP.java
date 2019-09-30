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

package it.cnr.contab.fondecon00.bp;

import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.contab.fondecon00.ejb.FondoEconomaleComponentSession;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

/**
 * Gestione dell'associazione delle spese relative a un fondo economale
 * da reintegrare con un'obbligazioni (scadenza)
 */

public class AssociaSpeseObbligazioneBP extends it.cnr.jada.util.action.SelezionatoreListaBP {

	private Obbligazione_scadenzarioBulk obbscad;
	private Fondo_economaleBulk fondo;

	public AssociaSpeseObbligazioneBP() {
		super();
	}

	public AssociaSpeseObbligazioneBP(String function, Fondo_economaleBulk testata, Obbligazione_scadenzarioBulk obbliga) throws it.cnr.jada.action.BusinessProcessException {
		super(function + "Tr");
		setObbscad(obbliga);
		setFondo(testata);
		setMultiSelection(true);
		table.setOnselect(null);
	}

	public FondoEconomaleComponentSession createComponentSession() throws it.cnr.jada.action.BusinessProcessException {
		try {
			return (FondoEconomaleComponentSession)createComponentSession("CNRFONDECON00_EJB_FondoEconomaleComponentSession",FondoEconomaleComponentSession.class);
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.undo");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.selectAll");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.deselectAll");
		return toolbar;
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
	public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk bulk,it.cnr.jada.bulk.OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {

		try {
			FondoEconomaleComponentSession fpcs = (FondoEconomaleComponentSession)actionContext.getBusinessProcess().createComponentSession("CNRFONDECON00_EJB_FondoEconomaleComponentSession", FondoEconomaleComponentSession.class);
			it.cnr.jada.util.RemoteIterator ri = fpcs.cerca(actionContext.getUserContext(),clauses,bulk,context,property);
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext, ri);
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk getFondo() {
		return fondo;
	}

	public Obbligazione_scadenzarioBulk getObbscad() {
		return obbscad;
	}

public boolean isSelectAllEnabled() {
	
	return	getFondo() != null && 
			getFondo().getFl_associatata_for_search() != null &&
			!getFondo().getFl_associatata_for_search().booleanValue();
}

	public void setFondo(it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk newFondo) {
		fondo = newFondo;
	}

	public void setObbscad(Obbligazione_scadenzarioBulk newObbscad) {
		obbscad = newObbscad;
	}

	public void setPageContents(it.cnr.jada.bulk.OggettoBulk[] elements) {
		super.setPageContents(elements);
		for (int i = 0;i < elements.length;i++) {
			Fondo_spesaBulk spesa = (Fondo_spesaBulk)elements[i];
			if (spesa.getFl_obbligazione() != null && spesa.getFl_obbligazione().booleanValue())
				selection.setSelected(getCurrentPage()*getPageSize()+i);
			else
				selection.removeFromSelection(getCurrentPage()*getPageSize()+i);
		}
	}

}
