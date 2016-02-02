/*
 * Created on Feb 1, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.bp;

import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDObbligazioneResBP extends CRUDObbligazioneBP{

	boolean scadenzaModificabile = false;
	boolean scadenzaModificata = false;
	boolean statusOriginarioSaveButtonEnabled = false;

	public CRUDObbligazioneResBP() {
		super("Tr");
	}
	public CRUDObbligazioneResBP(String function) {
		super(newFunction(function));
	}
	private static String newFunction(String function) {
		int n = function.indexOf("T");
		String s = null;
		String newFunction = null;
		if (n>-1) {
			s = function.substring(n, n+2);
			newFunction = function.replace(s, "Tr");
		}
		else
			newFunction = function+"Tr";
		return newFunction;
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP#init(it.cnr.jada.action.Config, it.cnr.jada.action.ActionContext)
	 */
	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		super.init(config, context);
		setStatusOriginarioSaveButtonEnabled(super.isSaveButtonEnabled());
		setModel( context, createEmptyModelForSearch(context) );
		setStatus(SEARCH);
	}

	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDObbligazioneBP#basicEdit(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk, boolean)
	 */
	public void basicEdit(ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit)	throws BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);
		setScadenzaModificata(false);
		if (getStatus()!=VIEW && isEditable())
			setScadenzaModificabile(true);
		setStatusAndEditableMap();
	}
	/**
	 * Metodo utilizzato per la conferma dei dati selezionati o immessi, relativi
	 * alla scadenza.
	 * @param context Il contesto dell'azione
	 */
	public void confermaScadenza(ActionContext context)	throws BusinessProcessException {
		Obbligazione_scadenzarioBulk os = ((Obbligazione_scadenzarioBulk)getScadenzario().getModel());
		if (os.getScadenza_iniziale()==null||os.getScadenza_iniziale().getIm_scadenza().compareTo(Utility.nvl(os.getIm_scadenza()))!=0)
			setScadenzaModificata(true);
			
		super.confermaScadenza(context);
	}
	public boolean isEditScadenzaButtonEnabled() throws BusinessProcessException {
		return (isScadenzaModificabile() && getScadenzario().getModel() != null && !isEditingScadenza());
	}
	/**
	 * @return
	 */
	public boolean isScadenzaModificabile() {
		return scadenzaModificabile;
	}

	/**
	 * @param b
	 */
	private void setScadenzaModificabile(boolean b) {
		scadenzaModificabile = b;
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.CRUDBP#isNewButtonHidden()
	 */
	public boolean isNewButtonHidden() {
		return true;
	}

	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDObbligazioneBP#isDeleteButtonEnabled()
	 */
	public boolean isDeleteButtonHidden() {
		return true;
	}
	/**
	 *	Abilito il bottone di salvataggio documento solo se non ho scadenze in fase di modifica/inserimento
	 */
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled() && isStatusOriginarioSaveButtonEnabled() && !isEditingScadenza();
	}
	/**
	 * @return
	 */
	public boolean isStatusOriginarioSaveButtonEnabled() {
		return statusOriginarioSaveButtonEnabled;
	}

	/**
	 * @param b
	 */
	private void setStatusOriginarioSaveButtonEnabled(boolean b) {
		statusOriginarioSaveButtonEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isScadenzaModificata() {
		return scadenzaModificata;
	}

	/**
	 * @param b
	 */
	private void setScadenzaModificata(boolean b) {
		scadenzaModificata = b;
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDObbligazioneBP#save(it.cnr.jada.action.ActionContext)
	 */
	public void save(ActionContext context)	throws BusinessProcessException, ValidationException {
		setStatusAndEditableMap(EDIT);
		try {
			super.save(context);
		} catch(BusinessProcessException e) {
			setStatusAndEditableMap();
			throw e;
		} catch(ValidationException e) {
			setStatusAndEditableMap();
			throw e;
		}
		setStatusAndEditableMap();
	}
	private void setStatusAndEditableMap(int status){
		setStatus(status==VIEW?VIEW:EDIT);
		setEditable(status==VIEW?false:true);
	}
	public void setStatusAndEditableMap(){
		if (getModel()!=null && ((ObbligazioneBulk)getModel()).isObbligazioneResiduo()) {
			if (getTab( "tab" )!=null && (getTab( "tab" ).equalsIgnoreCase("tabScadenzario") && isScadenzaModificabile() && !((ObbligazioneBulk)getModel()).isDocRiportato()
					|| getTab( "tab" ).equalsIgnoreCase("tabObbligazione") && isScadenzaModificabile() && !((ObbligazioneBulk)getModel()).isDocRiportato())) 
				setStatusAndEditableMap(EDIT);
			else
				setStatusAndEditableMap(VIEW);
		}
	} 

	public boolean isCopiaObbligazioneButtonHidden() {
		return true;
	}

	public void selezionaScadenza(Obbligazione_scadenzarioBulk scadenza, ActionContext context) {
		super.selezionaScadenza(scadenza, context);
		setStatusAndEditableMap();
	}
	public boolean isROImporto() {
		if (getStatus()!=VIEW || isScadenzaModificabile())
			return false;
		return true;
	}
	public void cancellaObbligazioneModTemporanea(ActionContext context, Obbligazione_modificaBulk obbMod) throws BusinessProcessException {
		try {
			if (obbMod!=null && obbMod.isTemporaneo())
				((ObbligazioneResComponentSession)createComponentSession()).cancellaObbligazioneModTemporanea(context.getUserContext(), obbMod);
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
	public boolean isRiportaAvantiButtonEnabled() 
	{
		ObbligazioneBulk doc = ((ObbligazioneBulk)getModel());
		
		return !isRiportaAvantiButtonHidden() &&
					(isEditing() || isScadenzaModificabile()) &&
					doc != null &&
					!((ObbligazioneBulk)doc).isDocRiportato();

	}
	public boolean isRiportaIndietroButtonEnabled() 
	{
		ObbligazioneBulk doc = ((ObbligazioneBulk)getModel());
		
		return !isRiportaIndietroButtonHidden() &&
					(isEditing() || isScadenzaModificabile()) &&
					!isDirty() &&
					doc != null &&
					((ObbligazioneBulk)doc).isDocRiportato();
	}

	public void riportaAvanti(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			if (this.isDirty()) {
				this.setMessage("Confermare le modifiche apportate prima di effettuare l'operazione di Riporta Avanti.");
			} else {
				rollbackUserTransaction();
				((ObbligazioneComponentSession)EJBCommonServices.createEJB(getComponentSessioneName())).callRiportaAvantiRequiresNew( context.getUserContext(), (IDocumentoContabileBulk) getModel());
				edit( context, getModel(), true );
			}			
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	public void riportaIndietro(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			if (this.isDirty()) {
				this.setMessage("Confermare le modifiche apportate prima di effettuare l'operazione di Riporta Indietro.");
			} else {
				rollbackUserTransaction();
				((ObbligazioneComponentSession)EJBCommonServices.createEJB(getComponentSessioneName())).callRiportaIndietroRequiresNew( context.getUserContext(), (IDocumentoContabileBulk) getModel());
				edit( context, getModel(), true );
			}			
		} catch(Exception e) {
			throw handleException(e);
		}
	}
}
