package it.cnr.contab.config00.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

public class CRUDConfigAnagContrattoMasterBP extends CRUDConfigAnagContrattoBP {

	public CRUDConfigAnagContrattoMasterBP() {
		super();
	}

	public CRUDConfigAnagContrattoMasterBP(String s) {
		super(s);
	}

	@Override
	public boolean isPublishCRUDButtonHidden() {
		return false;
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
	public boolean isSalvaDefinitivoButtonEnabled() {
		return true;
	}
	
	@Override
	public void basicEdit(ActionContext context, OggettoBulk bulk,
			boolean doInitializeForEdit) throws BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);
		setStatus(EDIT);
	}
}
