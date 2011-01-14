/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import java.util.Iterator;

import it.cnr.contab.consultazioni.bp.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.AbstractSelezionatoreBP;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author Marco Spasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DettagliFileStipendiAction extends SelezionatoreListaAction {

	public Forward doSelection(ActionContext actioncontext, String s)
	throws BusinessProcessException
	{
		return actioncontext.findDefaultForward();
	}
	/*
	public Forward doBringBack(ActionContext actioncontext) {
		return null;
	}
	
	public Forward doFiltraFiles(ActionContext context) {
		try {
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			ricercaLiberaBP.setSearchProvider(bp);
			ricercaLiberaBP.setFreeSearchSet(bp.getFreeSearchSet());
			ricercaLiberaBP.setShowSearchResult(false);
			ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
			ricercaLiberaBP.setPrototype( bp.createEmptyModelForFreeSearch(context));
			context.addHookForward("searchResult",this,"doRigheSelezionate");
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
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
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
		ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
		it.cnr.jada.util.RemoteIterator ri = bp.search(context,(CompoundFindClause) null,(OggettoBulk) bp.getBulkInfo().getBulkClass().newInstance()); 
		bp.setIterator(context,ri);
		return context.findDefaultForward();
	  } catch(Throwable e) {
		return handleException(context,e);
	  }
    }

	public Forward doChiudiRicerca(ActionContext context)
	{
		try 
		{
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doRigheSelezionate(ActionContext context)
	{
	
		try 
		{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			HookForward hook = (HookForward)context.getCaller();
			it.cnr.jada.util.RemoteIterator ri = (it.cnr.jada.util.RemoteIterator)hook.getParameter("remoteIterator");
			bp.setIterator(context,ri);
			bp.setDirty(true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doSelectAll(it.cnr.jada.action.ActionContext context) {
		try {
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			bp.selectAll(context);
            
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doDeselectAll(it.cnr.jada.action.ActionContext context) {
		try {
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			bp.deselectAll(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	*/
}
