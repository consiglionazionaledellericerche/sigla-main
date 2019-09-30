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

package it.cnr.contab.fondecon00.action;

import it.cnr.contab.fondecon00.bp.AssociaSpeseObbligazioneBP;
import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.jada.action.*;

public class AssociaSpeseObbligazioneAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
	public AssociaSpeseObbligazioneAction() {
		super();
	}

/**
 * Gestisce la chiusura del pannello
 */
 
public Forward doCloseForm(ActionContext context) throws BusinessProcessException {

	return doUndo(context);
}
/**
 * Disassocia tutte le scadenze del fondo alla scadenza di obbligazione
 */
 
public it.cnr.jada.action.Forward doDeselectAll(it.cnr.jada.action.ActionContext context) {
	try {
		AssociaSpeseObbligazioneBP bp = (AssociaSpeseObbligazioneBP)context.getBusinessProcess();
		bp.createComponentSession().dissociaTutteSpese(context.getUserContext(),bp.getFondo(), bp.getObbscad());
		bp.refresh(context);

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Salva i cambiamenti eseguiti sulla pagina corrente e carica la pagina richiesta
 * dall'utente
 */

public Forward doGotoPage(ActionContext context,int pagina) {
		try {
			savePage(context);
			return super.doGotoPage(context,pagina);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

/**
 * Salva i cambiamenti eseguiti sulla pagina corrente e carica la pagina richiesta
 * dall'utente
 */

public Forward doNextFrame(ActionContext context) {
	try {
		savePage(context);
		return super.doNextFrame(context);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Salva i cambiamenti eseguiti sulla pagina corrente e carica la pagina successiva
 */

public Forward doNextPage(ActionContext context) {
	try {
		savePage(context);
		return super.doNextPage(context);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Salva i cambiamenti eseguiti sulla pagina corrente e carica la pagina precedente
 */

public Forward doPreviousFrame(ActionContext context) {
	try {
		savePage(context);
		return super.doPreviousFrame(context);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

	public Forward doPreviousPage(ActionContext context) {
		try {
			savePage(context);
			return super.doPreviousFrame(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

/**
 * Salva i cambiamenti eseguiti associando le spese del fondo
 * selezionate all'obbligazione scelta precedentemente dall'utente
 */

public it.cnr.jada.action.Forward doSave(it.cnr.jada.action.ActionContext context) {
	try {
		AssociaSpeseObbligazioneBP bp = (AssociaSpeseObbligazioneBP)context.getBusinessProcess();
		bp.setSelection(context);
		savePage(context);
		bp.createComponentSession().associazione(context.getUserContext(), bp.getFondo(), bp.getObbscad());
		bp.commitUserTransaction();
		context.closeBusinessProcess();
		return context.findForward("bringback");
	} catch(Throwable e)  {
		return handleException(context,e);
	}
}

/**
 * Associa tutte le scadenze del fondo alla scadenza di obbligazione
 */
 
public it.cnr.jada.action.Forward doSelectAll(it.cnr.jada.action.ActionContext context) {
	try {
		AssociaSpeseObbligazioneBP bp = (AssociaSpeseObbligazioneBP)context.getBusinessProcess();
		bp.createComponentSession().associaTutteSpese(context.getUserContext(), bp.getFondo(), bp.getObbscad());
		bp.refresh(context);
		bp.selectAll(context);

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Annulla tutte le modifiche effettuate
 */
 
public it.cnr.jada.action.Forward doUndo(it.cnr.jada.action.ActionContext context) {
	try {
		AssociaSpeseObbligazioneBP bp = (AssociaSpeseObbligazioneBP)context.getBusinessProcess();
		bp.clearSelection(context);
		bp.rollbackUserTransaction();
		context.closeBusinessProcess();
		context.getBusinessProcess().rollbackUserTransaction();
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Salva i cambiamenti eseguiti sulla pagina corrente associando le spese del fondo
 * selezionate all'obbligazione scelta precedentemente dall'utente
 */
 
private void savePage(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException,it.cnr.jada.action.BusinessProcessException {
	AssociaSpeseObbligazioneBP bp = (AssociaSpeseObbligazioneBP)context.getBusinessProcess();
	bp.setSelection(context);
	it.cnr.jada.bulk.OggettoBulk[] page = bp.getPageContents();
	boolean[] associati = new boolean[page.length];
	Fondo_spesaBulk[] spese = new Fondo_spesaBulk[page.length];
	boolean dirty = false;
	for (int i = 0;i < page.length;i++) {
		spese[i] = (Fondo_spesaBulk)page[i];
		associati[i] = bp.getSelection().isSelected(bp.getPageSize()*bp.getCurrentPage() + i);
		dirty = dirty || spese[i].getFl_obbligazione().booleanValue() != associati[i];
	}
	if (dirty)
		bp.setPageContents(
			bp.createComponentSession().modificaSpe_associate(
				context.getUserContext(),
				spese,
				associati
			)
		);
}

}
