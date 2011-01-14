/*
 * Created on Feb 16, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bp;

import java.util.List;

import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util00.ejb.ProcedureComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public class PdgVarApponiVistoListaBP extends PdGVarSelezionatoreListaBP {

	public PdgVarApponiVistoListaBP() {
		super();
	}

	public PdgVarApponiVistoListaBP(String function) {
		super(function);
	}

	public String getFormTitle() {
		return "Variazioni al Piano di Gestione - Apposizione Visto";
	}

	public void aggiornaStato(ActionContext context, List list) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ProcedureComponentSession comp = (ProcedureComponentSession)createProcedureComponentSession();
			comp.aggiornaApponiVisto(context.getUserContext(), list, CNRUserInfo.getDipartimento(context));
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}

	public void aggiornaStato(ActionContext context, CompoundFindClause clause) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ProcedureComponentSession comp = (ProcedureComponentSession)createProcedureComponentSession();
			comp.aggiornaApponiVisto(context.getUserContext(), clause, CNRUserInfo.getDipartimento(context));
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
}
