package it.cnr.contab.incarichi00.action;

import java.rmi.RemoteException;

import it.cnr.jada.action.*;

public class RepertorioLimitiAction extends it.cnr.jada.util.action.CRUDAction{

public RepertorioLimitiAction() {
	super();
}

public Forward doOnFlRaggruppaChange(ActionContext actioncontext) throws RemoteException {
	try {
		getBusinessProcess(actioncontext).setTab( "tab", "tabRepertorio");
		return super.doDefault(actioncontext);
	}catch (Throwable ex) {
		return handleException(actioncontext, ex);
	}
}
}
