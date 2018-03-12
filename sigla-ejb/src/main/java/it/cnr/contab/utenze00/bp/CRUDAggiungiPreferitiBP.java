package it.cnr.contab.utenze00.bp;

import it.cnr.contab.utenze00.bulk.AssBpAccessoBulk;
import it.cnr.contab.utenze00.bulk.PreferitiBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleCRUDBP;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class CRUDAggiungiPreferitiBP extends SimpleCRUDBP {
	private String bpName;
	private Character funzione;
	private String descrizione;
	
	public CRUDAggiungiPreferitiBP() {
		super();
	}

	public CRUDAggiungiPreferitiBP(String s) {
		super(s);
	}

	public CRUDAggiungiPreferitiBP(String bpName, Character funzione, String descrizione) {
		super("M");
		this.bpName = bpName;
		this.funzione = funzione;
		this.descrizione = descrizione;
	}
	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		super.init(config, actioncontext);
		if (bpName != null && funzione != null){
			setStatus(FormController.INSERT);
			PreferitiBulk preferiti = (PreferitiBulk)getModel();
			preferiti.setCd_utente(CNRUserContext.getUser(actioncontext.getUserContext()));
            preferiti.setBusiness_process(bpName);
			preferiti.setDescrizione(descrizione);
			preferiti.setTi_funzione(String.valueOf(funzione));
			preferiti.setUrl_icona(PreferitiBulk.LINK1);
			preferiti.setToBeCreated();
			initializeModelForInsert(actioncontext, preferiti);		
		}
	}
	
	@Override
	public boolean isNewButtonEnabled() {
		return false;
	}
	@Override
	public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
		super.save(actioncontext);
		actioncontext.closeBusinessProcess();
	}

	@Override
	public void writeForm(JspWriter jspwriter) throws IOException {
		if (this.getParentRoot().isBootstrap())
			super.writeForm(jspwriter, "bootstrap");
		else
		    super.writeForm(jspwriter);
	}

    @Override
    public String getSearchResultColumnSet() {
        if (this.getParentRoot().isBootstrap())
            return "bootstrap";
        else
            return super.getSearchResultColumnSet();
    }

    @Override
    public String getFreeSearchSet() {
        if (this.getParentRoot().isBootstrap())
            return "bootstrap";
        else
            return super.getFreeSearchSet();
    }
}
