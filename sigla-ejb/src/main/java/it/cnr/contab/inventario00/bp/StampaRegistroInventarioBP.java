package it.cnr.contab.inventario00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

public class StampaRegistroInventarioBP extends it.cnr.contab.reports.bp.ParametricPrintBP {

	private boolean ufficiale;
	
	public boolean isUfficiale() {
		return ufficiale;
	}
	public void setUfficiale(boolean ufficiale) {
		this.ufficiale = ufficiale;
	}
protected void initialize(ActionContext context) throws BusinessProcessException {
	try {
		setUfficiale(UtenteBulk.isInventarioUfficiale(context.getUserContext()));
	} catch (ComponentException e1) {
		throw handleException(e1);
	} catch (RemoteException e1) {
		throw handleException(e1);
	}
	super.initialize(context);
}
public StampaRegistroInventarioBP(){
	super();
}
public StampaRegistroInventarioBP(String function){
	super(function);
}
}
