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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.doccont00.bp.ListaAccertamentiBP;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Lista di Accertamenti)
 */
public class ListaAccertamentiAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
public ListaAccertamentiAction() {
	super();
}
/**
 * Gestisce un comando "Edit".
     * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doEdit(ActionContext context) {
	
	try 
	{
		SelezionatoreListaBP listaBp = (SelezionatoreListaBP) context.getBusinessProcess();
		AccertamentoBulk accertamento = (AccertamentoBulk) listaBp.getFocusedElement(context);
			
		CRUDAccertamentoBP bp = (CRUDAccertamentoBP)context.createBusinessProcess("CRUDAccertamentoBP");
		bp.setEditable( true );
		bp.edit( context, accertamento );
		return context.addBusinessProcess(bp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando "Elimina".
     * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		ListaAccertamentiBP bp = (ListaAccertamentiBP)context.getBusinessProcess();
		bp.setSelection(context);
		bp.delete(context);
		bp.setMessage("Cancellazione effettuata");
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando "ricerca libera".
 */
public Forward doRicercaLibera(ActionContext context) {
	try {
		RicercaLiberaBP bp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
		bp.setFindbp( (ListaAccertamentiBP)context.getBusinessProcess());
		bp.setPrototype( ((ListaAccertamentiBP)context.getBusinessProcess()).createEmptyModelForFreeSearch(context));
		context.addHookForward("seleziona",this,"doRiportaSelezione");
		return context.addBusinessProcess(bp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
     * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doRiportaSelezione(ActionContext context)  throws java.rmi.RemoteException {
	try {
		HookForward caller = (HookForward)context.getCaller();
		OggettoBulk selezione = (OggettoBulk)caller.getParameter("focusedElement");
		if (selezione != null)
		{	
			CRUDAccertamentoBP bp = (CRUDAccertamentoBP)context.createBusinessProcess("CRUDAccertamentoBP");
			bp.setEditable( true );
			bp.edit( context, selezione );
			return context.addBusinessProcess(bp);
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doSelection(ActionContext context,String name) {
	try 
	{
//		fillModel(context);
		AbstractSelezionatoreBP bp = (AbstractSelezionatoreBP)context.getBusinessProcess();
		bp.setFocus(context);
		bp.setSelection(context);
		return context.findDefaultForward();
	}
	catch(Exception e) 
	{
		return handleException(context,e);
	}
}
}
