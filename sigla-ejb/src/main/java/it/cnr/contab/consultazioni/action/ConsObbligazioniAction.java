/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.action;

import it.cnr.contab.consultazioni.bp.ConsObbligazioniBP;
import it.cnr.contab.consultazioni.bulk.ConsObbligazioniBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsObbligazioniAction extends SelezionatoreListaAction {

	public Forward doBringBack(ActionContext actioncontext) {
		return null;
	}

	public Forward doFiltraFiles(ActionContext context) {
		try {
			ConsObbligazioniBP bp = (ConsObbligazioniBP)context.getBusinessProcess();
			RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			ricercaLiberaBP.setSearchProvider(bp);
			ricercaLiberaBP.setFreeSearchSet(null);
			ricercaLiberaBP.setShowSearchResult(false);
			ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
			ricercaLiberaBP.setPrototype( bp.createEmptyModelForFreeSearch(context));
			//context.addHookForward("seleziona",this,"doSelezionaObbligazioni");
			context.addHookForward("searchResult",this,"doObbligazioniSelezionate");			
			//context.addHookForward("filter",this,"doRiportaFiltraFiles");
			//context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doCloseForm(ActionContext context)
		throws BusinessProcessException
	{
		try
		{
			ConsObbligazioniBP bp = (ConsObbligazioniBP)context.getBusinessProcess();
			bp.setFindclause(null);
			return super.doCloseForm(context);
		}
		catch(Throwable throwable)
		{
			return handleException(context, throwable);
		}
	}

	public Forward doCancellaFiltro(ActionContext context) {
		try 
		{
			ConsObbligazioniBP bp = (ConsObbligazioniBP)context.getBusinessProcess();
			it.cnr.jada.util.RemoteIterator ri = bp.search(context,(CompoundFindClause) null,(OggettoBulk) new ConsObbligazioniBulk()); 
			bp.setIterator(context,ri);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doObbligazioniSelezionate(ActionContext context)
	{
	
		try 
		{
			ConsObbligazioniBP bp = (ConsObbligazioniBP)context.getBusinessProcess();
			HookForward hook = (HookForward)context.getCaller();
			it.cnr.jada.util.RemoteIterator ri = (it.cnr.jada.util.RemoteIterator)hook.getParameter("remoteIterator");
			bp.setIterator(context,ri);
			bp.setDirty(true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

}
