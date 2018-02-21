/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.bp;

import java.rmi.RemoteException;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDTipoVariazioneBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	private boolean flVariazioniTrasferimento = false;

	public CRUDTipoVariazioneBP() {
		super();
	}

	public CRUDTipoVariazioneBP(String function) {
		super(function);
	}

	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		try {
			Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext());
			setFlVariazioniTrasferimento(parEnte.getFl_variazioni_trasferimento().booleanValue());
			super.initialize(actioncontext);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} 
	}

	public boolean isFlVariazioniTrasferimento() {
		return flVariazioniTrasferimento;
	}
	
	public void setFlVariazioniTrasferimento(boolean flVariazioniTrasferimento) {
		this.flVariazioniTrasferimento = flVariazioniTrasferimento;
	}
}
