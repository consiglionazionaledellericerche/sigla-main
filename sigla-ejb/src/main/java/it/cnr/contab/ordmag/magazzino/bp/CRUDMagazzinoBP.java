package it.cnr.contab.ordmag.magazzino.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDMagazzinoBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	public CRUDMagazzinoBP() {
		super();
	}

	public CRUDMagazzinoBP(String function) {
		super(function);
	}

	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabMagazzino");
		super.init(config, actioncontext);
	}
}