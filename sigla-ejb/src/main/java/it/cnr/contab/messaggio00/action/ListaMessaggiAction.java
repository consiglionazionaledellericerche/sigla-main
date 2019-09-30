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

package it.cnr.contab.messaggio00.action;

import it.cnr.contab.messaggio00.bulk.*;
import it.cnr.contab.messaggio00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
/**
 * Insert the type's description here.
 * Creation date: (10/09/2002 16:35:53)
 * @author: CNRADM
 */
public class ListaMessaggiAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
	private static final long serialVersionUID = 1L;

	/**
	 * ListaMessaggiAction constructor comment.
	 */
	public ListaMessaggiAction() {
		super();
	}
	public Forward doBringBack(ActionContext context) throws BusinessProcessException {
		return context.findDefaultForward();
	}
	public Forward doRimuoviCorrente(ActionContext context) throws BusinessProcessException {
		ListaMessaggiBP bp = (ListaMessaggiBP)context.getBusinessProcess();
		bp.saveSelection(context);
		it.cnr.contab.messaggio00.bulk.MessaggioBulk messaggio = (it.cnr.contab.messaggio00.bulk.MessaggioBulk)bp.getFocusedElement();
		if (messaggio != null) 
			try {
				EJBCommonServices.closeRemoteIterator(context, bp.getIterator());
				bp.createComponentSession().leggiMessaggi(context.getUserContext(),new it.cnr.contab.messaggio00.bulk.MessaggioBulk[] { messaggio });
				bp.refresh(context);
			} catch(Throwable e) {
				return handleException(context,e);
			}
		return context.findDefaultForward();
	}
	
	@SuppressWarnings("unchecked")
	public Forward doRimuoviSelezionati(ActionContext context) throws BusinessProcessException {
		ListaMessaggiBP bp = (ListaMessaggiBP)context.getBusinessProcess();
		bp.saveSelection(context);
		java.util.List<MessaggioBulk> messaggi = bp.getSelectedElements(context);

		if (!messaggi.isEmpty()) 
			try {
				EJBCommonServices.closeRemoteIterator(context, bp.getIterator());
				bp.createComponentSession().leggiMessaggi(context.getUserContext(),(MessaggioBulk[])messaggi.toArray(new MessaggioBulk[messaggi.size()]));
				bp.refresh(context);
			} catch(Throwable e) {
				return handleException(context,e);
			}
		return context.findDefaultForward();
	}
	
	public Forward doSort(ActionContext actioncontext, String s, String s1)
			throws BusinessProcessException {
		if (s1.equals("iconaOpenClose"))
			return super.doSort(actioncontext, s, "visionato");
		return super.doSort(actioncontext, s, s1);
	}
}
