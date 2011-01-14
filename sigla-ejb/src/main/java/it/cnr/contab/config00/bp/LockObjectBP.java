package it.cnr.contab.config00.bp;

import java.util.ArrayList;
import java.util.List;

import it.cnr.contab.config00.ejb.LockObjectSession;
import it.cnr.contab.utenze00.bulk.LockedObjectBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

public class LockObjectBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController utenti = new SimpleDetailCRUDController("utenti",UtenteBulk.class,"utenti",this);	
	private final SimpleDetailCRUDController oggetti = new SimpleDetailCRUDController("oggetti",LockedObjectBulk.class,"oggetti",this);
	
	@Override
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {		
		super.initialize(actioncontext);
		getOggetti().setEnabled(false);
	}
	
	public LockObjectBP() {
		super();
	}
	public LockObjectBP(String s) {
		super(s);
	}
	
	public SimpleDetailCRUDController getUtenti() {
		return utenti;
	}
	public SimpleDetailCRUDController getOggetti() {
		return oggetti;
	}
	
	public void riempiListaUtenti(it.cnr.jada.action.ActionContext actionContext) throws BusinessProcessException {
		try {
			setModel(actionContext, ((LockObjectSession)createComponentSession()).riempiListaUtenti(actionContext.getUserContext(), getModel()));
		}catch (ComponentException e) {
			throw handleException(e);
		}
	}
	public void selezionaUtente(ActionContext actionContext) throws BusinessProcessException {
		try {
			setModel(actionContext, ((LockObjectSession)createComponentSession()).riempiListaOggetti(actionContext.getUserContext(), getModel()));
		}catch (ComponentException e) {
			throw handleException(e);
		}
	}	
	@Override
	protected Button[] createToolbar() {
		Button bottone = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
		bottone.setStyle("width:100px");
		bottone.setLabel("<u>T</u>ermina sessioni selezionate");
		bottone.setAccessKey("T");
		bottone.setTitle("Termina sessioni selezionate");
        return new Button[]{bottone};
	}
	
	@Override
	public boolean isDeleteButtonEnabled() {
		return true;
	}

	public void disconnettiUtenti(ActionContext actioncontext) throws BusinessProcessException{
		try {
			BulkList utentiSelezionati = new BulkList(getUtenti().getSelectedModels(actioncontext));
			((LockObjectSession)createComponentSession()).terminaSessioni(actioncontext.getUserContext(), utentiSelezionati);
		} catch (ValidationException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
		
	}
}
