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
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.doccont00.bp.ListaSospesiBP;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListaSospesiAction extends SelezionatoreListaAction {
	/**
	 * Gestisce un comando "ricerca libera".
	 */
	public Forward doRicercaLibera(ActionContext context) {
		try {
			ListaSospesiBP bp = (ListaSospesiBP)context.getBusinessProcess();
			RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			ricercaLiberaBP.setSearchProvider(bp);
			ricercaLiberaBP.setFreeSearchSet(null);
			ricercaLiberaBP.setShowSearchResult(false);
			ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
			ricercaLiberaBP.setPrototype( bp.createEmptyModelForFreeSearch(context));
			context.addHookForward("seleziona",this,"doRiportaSelezioneSospesi");
			context.addHookForward("searchResult",this,"doAddToCRUDMain_SospesiSelezionati");			
			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public Forward doAddToCRUDMain_SospesiSelezionati(ActionContext context)
	{
	
		try 
		{
			ListaSospesiBP bp = (ListaSospesiBP)context.getBusinessProcess();
			HookForward hook = (HookForward)context.getCaller();
			bp.setIterator(context,(it.cnr.jada.util.RemoteIterator)hook.getParameter("remoteIterator"));
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(SospesoBulk.class);
			bp.setColumns( bulkInfo.getColumnFieldPropertyDictionary("SospesiReversale"));
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	/**
	 * Gestisce la selezione dei sospesi
	 *
	 */
	public Forward doRiportaSelezioneSospesi(ActionContext context)
	{
	
		try 
		{
			if (context.getBusinessProcess() instanceof ListaSospesiBP)
			   context.closeBusinessProcess();
			CRUDReversaleBP bp = (CRUDReversaleBP)context.getBusinessProcess();
			bp.aggiungiSospesi( context );
			return context.findDefaultForward();
		} catch(Throwable e) 
		{
			return handleException(context,e);
		}
	
	}		
}
