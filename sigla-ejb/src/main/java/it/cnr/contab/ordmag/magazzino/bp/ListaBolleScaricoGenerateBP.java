package it.cnr.contab.ordmag.magazzino.bp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

public class ListaBolleScaricoGenerateBP extends SelezionatoreListaBP {
	private transient final static Logger logger = LoggerFactory.getLogger(ListaBolleScaricoGenerateBP.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		super.init(config, context);
	}


	public ListaBolleScaricoGenerateBP() {
		this("");
	}

	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.excel");
		return toolbar;
	}

	public ListaBolleScaricoGenerateBP(String function) {
		super(function+"Tr");
	}
	
}
