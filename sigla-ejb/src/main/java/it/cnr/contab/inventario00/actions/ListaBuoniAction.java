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
 * Created on Jan 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario00.actions;

import it.cnr.contab.inventario00.bp.AssBeneFatturaBP;
import it.cnr.contab.inventario00.bp.ListaBuoniBP;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListaBuoniAction extends SelezionatoreListaAction {
	/**
	 * Gestisce un comando "ricerca libera".
	 */
	public Forward doRicercaLibera(ActionContext context) {
		try {
			ListaBuoniBP bp = (ListaBuoniBP)context.getBusinessProcess();
			RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			ricercaLiberaBP.setSearchProvider(bp);
			ricercaLiberaBP.setFreeSearchSet(null);
			ricercaLiberaBP.setShowSearchResult(false);
			ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
			ricercaLiberaBP.setPrototype( bp.createEmptyModelForFreeSearch(context));
			context.addHookForward("seleziona",this,"doSelezionaBuoni_associati");
			context.addHookForward("searchResult",this,"doAddToCRUDMain_BuoniSelezionati");			
			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public Forward doAddToCRUDMain_BuoniSelezionati(ActionContext context)
	{
	
		try 
		{
			ListaBuoniBP bp = (ListaBuoniBP)context.getBusinessProcess();
			AssBeneFatturaBP assBeneFattureBP = (AssBeneFatturaBP)bp.getParent();
			HookForward hook = (HookForward)context.getCaller();
			it.cnr.jada.util.RemoteIterator ri = (it.cnr.jada.util.RemoteIterator)hook.getParameter("remoteIterator");
			context.closeBusinessProcess();		
			SelezionatoreListaBP slbp=null;
			//if(!assBeneFattureBP.isDaDocumento())
				 slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class),null,"doSelezionaBuoni_associati",null,bp);
			//else
				// slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class),null,"doSelezionaBuoni_associatiDoc",null,bp);
			slbp.setMultiSelection(true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	/**
	 * Gestisce la selezione dei buoni
	 *
	 */
	public Forward doSelezionaBuoni_associati(ActionContext context)
	{
	
		try 
		{
			if (context.getBusinessProcess() instanceof ListaBuoniBP)
			   context.closeBusinessProcess();
			AssBeneFatturaBP bp = (AssBeneFatturaBP)context.getBusinessProcess();
			if (bp.isDaDocumento())
				bp.getRigheDaDocumento().reset(context);
			else
				bp.getRigheDaFattura().reset(context);
		    bp.setDirty(true);
			return context.findDefaultForward();
		} catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	
}
