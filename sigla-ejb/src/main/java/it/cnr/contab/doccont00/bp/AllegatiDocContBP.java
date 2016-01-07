package it.cnr.contab.doccont00.bp;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;

import org.apache.chemistry.opencmis.client.api.CmisObject;

public class AllegatiDocContBP extends AllegatiCRUDBP<AllegatoGenericoBulk, StatoTrasmissione> {
	private static final long serialVersionUID = 1L;

	public AllegatiDocContBP() {
		super();
	}

	public AllegatiDocContBP(String s) {
		super(s);
	}

	@Override
	public boolean isSearchButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isFreeSearchButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}
	@Override
	protected boolean excludeChild(CmisObject cmisObject) {
		if (cmisObject.getType().getId().equalsIgnoreCase("D:doccont:document"))
			return true;
		return super.excludeChild(cmisObject);
	}

	@Override
	public boolean isInputReadonly() {
		return super.isInputReadonly();
	}
	@Override
	public boolean isNewButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled();
	}
	
	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			archiviaAllegati(actioncontext, null);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
	}
	
	@Override
	protected CMISPath getCMISPath(StatoTrasmissione allegatoParentBulk,
			boolean create) throws BusinessProcessException {
		try {
			return allegatoParentBulk.getCMISPath(cmisService);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
	}

	@Override
	protected Class<AllegatoGenericoBulk> getAllegatoClass() {
		return AllegatoGenericoBulk.class;
	}
}
