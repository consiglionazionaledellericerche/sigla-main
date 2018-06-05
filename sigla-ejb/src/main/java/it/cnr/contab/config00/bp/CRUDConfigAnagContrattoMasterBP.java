package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.jsp.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CRUDConfigAnagContrattoMasterBP extends CRUDConfigAnagContrattoBP {
	private static final long serialVersionUID = 1L;

	public CRUDConfigAnagContrattoMasterBP() {
		super();
	}

	public CRUDConfigAnagContrattoMasterBP(String s) {
		super(s);
	}

	@Override
	public boolean isPublishCRUDButtonHidden() {
		return true;
	}

	public boolean isUpublishCRUDButtonHidden() {
		return true;
	}

	@Override
	public boolean isPublishHidden() {
		return false;
	}
	
	@Override
	public boolean isAllegatiEnabled() {
		return true;
	}

	@Override
	public boolean isContrattoDefinitivo() {
		return false;
	}
	
	@Override
	protected Button[] createToolbar() {
		Button[] baseToolbar = super.createToolbar();
		List<Button> newToolbar = new ArrayList<Button>();
		newToolbar.addAll(Arrays.asList(baseToolbar));
		Button unPublish = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.publish");
		unPublish.setLabel("<u>A</u>nnulla<br>Pubblicazione");
		unPublish.setTitle("Annulla Pubblicazione");
		unPublish.setStyle("width:100px");
		unPublish.setHiddenProperty("upublishCRUDButtonHidden");
		unPublish.setHref("javascript:submitForm('doUnpublishContratto')");
		newToolbar.add(unPublish);
		return newToolbar.toArray(new Button[newToolbar.size()]);
	}
	
	@Override
	public void basicEdit(ActionContext context, OggettoBulk bulk,
			boolean doInitializeForEdit) throws BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);
		setStatus(EDIT);
	}

	public void unpublishContratto(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		try {
			StorageObject storageObject = contrattoService.getFolderContratto((ContrattoBulk)getModel());
			contratto.setFl_pubblica_contratto(Boolean.FALSE);
			contratto.setToBeUpdated();
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			comp.modificaConBulk(context.getUserContext(), contratto);
			if (storageObject != null){
				contrattoService.removeConsumer(storageObject,"GROUP_CONTRATTI");
			}
			edit(context,contratto);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}

}
