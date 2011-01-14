/*
 * Created on Jan 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario00.actions;

import it.cnr.contab.inventario00.bp.AssBeneFatturaBP;
import it.cnr.contab.inventario00.bp.ListaBeniBP;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListaBeniAction extends SelezionatoreListaAction {
	/**
	 * Gestisce un comando "ricerca libera".
	 */
	public Forward doRicercaLibera(ActionContext context) {
		try {
			ListaBeniBP bp = (ListaBeniBP)context.getBusinessProcess();
			RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			ricercaLiberaBP.setSearchProvider(bp);
			ricercaLiberaBP.setFreeSearchSet(null);
			ricercaLiberaBP.setShowSearchResult(false);
			ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
			ricercaLiberaBP.setPrototype( bp.createEmptyModelForFreeSearch(context));
			context.addHookForward("seleziona",this,"doSelezionaBeni_associati");
			context.addHookForward("searchResult",this,"doAddToCRUDMain_BeniSelezionati");			
			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public Forward doAddToCRUDMain_BeniSelezionati(ActionContext context)
	{
	
		try 
		{
			ListaBeniBP bp = (ListaBeniBP)context.getBusinessProcess();
			AssBeneFatturaBP assBeneFattureBP = (AssBeneFatturaBP)bp.getParent();
			HookForward hook = (HookForward)context.getCaller();
			SelezionatoreListaBP slbp =null;
			it.cnr.jada.util.RemoteIterator ri = (it.cnr.jada.util.RemoteIterator)hook.getParameter("remoteIterator");
			context.closeBusinessProcess();			
			//if(!assBeneFattureBP.isDaDocumento())
				 slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class),null,"doSelezionaBeni_associati",null,bp);
			//else
				// slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class),null,"doSelezionaBeni_associatiDoc",null,bp);
			
			slbp.setMultiSelection(true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	/**
	 * Gestisce la selezione dei sospesi
	 *
	 */
	public Forward doSelezionaBeni_associati(ActionContext context)
	{
		try 
		{
			if (context.getBusinessProcess() instanceof ListaBeniBP)
			   context.closeBusinessProcess();
			AssBeneFatturaBP bp = (AssBeneFatturaBP)context.getBusinessProcess();
			if (bp.isDaDocumento())
				bp.getRigheInventarioDaDocumento().reset(context);
			else
				bp.getRigheInventarioDaFattura().reset(context);
		    bp.setDirty(true);
			return context.findDefaultForward();
		} catch(Throwable e) 
		{
			return handleException(context,e);
		}
	
	}
}
