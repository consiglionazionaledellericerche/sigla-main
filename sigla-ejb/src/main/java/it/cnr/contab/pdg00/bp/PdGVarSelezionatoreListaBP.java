/*
 * Created on Feb 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.pdg00.bp;

import java.util.List;

import it.cnr.contab.util00.ejb.ProcedureComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

/**
 * @author mincarnato
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PdGVarSelezionatoreListaBP  extends ConsultazioniBP {

	
	public PdGVarSelezionatoreListaBP() {
		super();
	}

	public PdGVarSelezionatoreListaBP(String function) {
		super(function);
	}

	public String getFormTitle() {
		return "Variazioni al Piano di Gestione - Approvazione Formale";
	}

	public it.cnr.contab.util00.ejb.ProcedureComponentSession createProcedureComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (it.cnr.contab.util00.ejb.ProcedureComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTIL00_EJB_ProcedureComponentSession", it.cnr.contab.util00.ejb.ProcedureComponentSession.class);
	}

	public Button[] createToolbar()
	{
		Button abutton[] = new Button[5];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.deselectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.aggiorna");
		abutton[i-1].setSeparator(true);
		//abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearch");		
		return abutton;
	}

	public boolean isAggiornaButtonHidden()
	{
		return false;
	}

	public void aggiornaStato(ActionContext context, List list) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ProcedureComponentSession comp = (ProcedureComponentSession)createProcedureComponentSession();
			comp.aggiornaApprovazioneFormale(context.getUserContext(), list);
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}

	public void aggiornaStato(ActionContext context, CompoundFindClause clause) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ProcedureComponentSession comp = (ProcedureComponentSession)createProcedureComponentSession();
			comp.aggiornaApprovazioneFormale(context.getUserContext(), clause);
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
}
