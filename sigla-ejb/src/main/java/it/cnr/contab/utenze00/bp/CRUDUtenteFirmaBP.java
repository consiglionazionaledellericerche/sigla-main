package it.cnr.contab.utenze00.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDUtenteFirmaBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	public CRUDUtenteFirmaBP() {
		super();
	}

	public CRUDUtenteFirmaBP(String s) {
		super(s);
	}
	
	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		super.initialize(actioncontext);
	}
	
	@Override
	public boolean isInputReadonly() {
		return super.isInputReadonly();
	}

}
