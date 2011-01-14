package it.cnr.contab.docamm00.bp;

import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Gestisce le catene di elementi correlate con la fattura passiva in uso.
 */
public abstract class CRUDFatturaPassivaBP 
	extends SimpleCRUDBP 
	implements	IDocumentoAmministrativoBP, 
				IGenericSearchDocAmmBP,
				VoidableBP,
				IDefferedUpdateSaldiBP {
					
	private final SimpleDetailCRUDController crudRiferimentiBanca = new SimpleDetailCRUDController("RifBanca",Fattura_passiva_rigaBulk.class,"riferimenti_bancari",this);
	private final SimpleDetailCRUDController consuntivoController = new SimpleDetailCRUDController("Consuntivo",Consuntivo_rigaVBulk.class,"fattura_passiva_consuntivoColl",this);	
	private final ObbligazioniCRUDController obbligazioniController = new ObbligazioniCRUDController("Obbligazioni",it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class,"fattura_passiva_obbligazioniHash",this);
	private final SimpleDetailCRUDController dettaglioObbligazioneController;
	private final FatturaPassivaRigaIntrastatCRUDController dettaglioIntrastatController = new FatturaPassivaRigaIntrastatCRUDController("Intrastat",Fattura_passiva_intraBulk.class,"fattura_passiva_intrastatColl", this);
	
	protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
	private boolean isDeleting = false;
	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean annoDiCompetenza = true;
	private boolean annoSolareInScrivania = true;
	private boolean riportaAvantiIndietro = false;
	private boolean carryingThrough = false;
	private boolean ribaltato;
/**
 * CRUDAnagraficaBP constructor comment.
 */
public CRUDFatturaPassivaBP() {
	
	this(Fattura_passiva_rigaBulk.class);	
}
/**
 * CRUDAnagraficaBP constructor comment.
 */
public CRUDFatturaPassivaBP(Class dettObbligazioniControllerClass) {
	super("Tr");

	dettaglioObbligazioneController = new SimpleDetailCRUDController("DettaglioObbligazioni",dettObbligazioniControllerClass,"fattura_passiva_obbligazioniHash",obbligazioniController) {

			public java.util.List getDetails() {

				Fattura_passivaBulk fattura = (Fattura_passivaBulk)CRUDFatturaPassivaBP.this.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (fattura != null) {
					java.util.Hashtable h = fattura.getFattura_passiva_obbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector)h.get(getParentModel());
				}
				return lista;
			}
			public boolean isGrowable() {
				
				return	super.isGrowable() && !((it.cnr.jada.util.action.CRUDBP)getParentController().getParentController()).isSearching();
			}
			public boolean isShrinkable() {

				return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController().getParentController()).isSearching();
			}	
		};
}
/**
 * CRUDAnagraficaBP constructor comment.
 * @param function java.lang.String
 */
public CRUDFatturaPassivaBP(String function, Class dettObbligazioniControllerClass) throws BusinessProcessException{
	super(function+"Tr");

	dettaglioObbligazioneController = new SimpleDetailCRUDController("DettaglioObbligazioni",dettObbligazioniControllerClass,"fattura_passiva_obbligazioniHash",obbligazioniController) {

			public java.util.List getDetails() {

				Fattura_passivaBulk fattura = (Fattura_passivaBulk)CRUDFatturaPassivaBP.this.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (fattura != null) {
					java.util.Hashtable h = fattura.getFattura_passiva_obbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector)h.get(getParentModel());
				}
				return lista;
			}
			public boolean isGrowable() {
				
				return	super.isGrowable() && !((it.cnr.jada.util.action.CRUDBP)getParentController().getParentController()).isSearching();
			}
			public boolean isShrinkable() {

				return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController().getParentController()).isSearching();
			}	
		};
}
protected void basicEdit(it.cnr.jada.action.ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	try {
		Fattura_passivaBulk fp = (Fattura_passivaBulk)bulk;
		setAnnoDiCompetenza(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() == fp.getEsercizio().intValue());
		super.basicEdit(context, bulk, doInitializeForEdit);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
protected boolean basicRiportaButtonHidden() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	return	isAnnoSolareInScrivania() ||
			!isRiportaAvantiIndietro() ||
			isDeleting() ||
			isModelVoided() ||
			(fp != null && (fp.isPagata() || fp.isCongelata())) ||
			!isEditing();
}
public void create(it.cnr.jada.action.ActionContext context)
	throws	it.cnr.jada.action.BusinessProcessException {
		
	try {
		getModel().setToBeCreated();
		setModel(
				context,
				((FatturaPassivaComponentSession)createComponentSession()).creaConBulk(
																			context.getUserContext(),
																			getModel(),
																			getUserConfirm()));
	} catch(Exception e) {
		throw handleException(e);
	} finally {
		setUserConfirm(null);
	}
}
public OggettoBulk createEmptyModel(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	setAnnoDiCompetenza(true);
	return super.createEmptyModel(context);
}
public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	int crudStatus = getModel().getCrudStatus();
	try {
		getModel().setToBeDeleted();
		createComponentSession().eliminaConBulk(context.getUserContext(),getModel());
	} catch(Exception e) {
		getModel().setCrudStatus(crudStatus);
		throw handleException(e);
	}
}
public void edit(
	it.cnr.jada.action.ActionContext context,
	OggettoBulk bulk,
	boolean doInitializeForEdit) 
	throws it.cnr.jada.action.BusinessProcessException {

	setCarryingThrough(false);
	super.edit(context, bulk, doInitializeForEdit);
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 *
 * @param userContext	lo userContext che ha generato la richiesta
 * @param filtro		modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 						controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 *
**/
public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.action.BusinessProcessException {

	try {

		it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession fpcs = (it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession)createComponentSession();
		return fpcs.cercaObbligazioni(userContext, filtro);

	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 *
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 *
**/
public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {

	try {

		it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession fpcs = (it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession)createComponentSession();
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
												actionContext,
												fpcs.cerca(
														actionContext.getUserContext(),
														clauses,
														bulk,
														context,
														property));
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/18/2001 12:50:31 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public abstract Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente();
public IDocumentoAmministrativoBulk getBringBackDocAmm() {
	
	return getDocumentoAmministrativoCorrente();
}
public String getColumnsetForGenericSearch() {

	return "default";
}
/**
 * Insert the method's description here.
 * Creation date: (9/12/2001 10:35:52 AM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final SimpleDetailCRUDController getConsuntivoController() {
	return consuntivoController;
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2001 14:55:11)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final SimpleDetailCRUDController getCrudRiferimentiBanca() {
	return crudRiferimentiBanca;
}
public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {

	if (isDeleting() && getParent() != null)
		return getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
	return (IDefferUpdateSaldi)getDocumentoAmministrativoCorrente();
}
public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

	if (isDeleting() && getParent() != null)
		return ((IDefferedUpdateSaldiBP)getParent()).getDefferedUpdateSaldiParentBP();
	return this;
}
public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {

	if (deleteManager == null)
		deleteManager = new it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk();
	else
		deleteManager.reset();
	return deleteManager;
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2001 14:55:11)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public abstract FatturaPassivaRigaCRUDController getDettaglio();
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:11:03 AM)
 * @return it.cnr.contab.docamm00.bp.FatturaPassivaRigaIntrastatCRUDController
 */
public final FatturaPassivaRigaIntrastatCRUDController getDettaglioIntrastatController() {
	return dettaglioIntrastatController;
}
/**
 * Insert the method's description here.
 * Creation date: (10/18/2001 12:50:31 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglioObbligazioneController() {
	return dettaglioObbligazioneController;
}
/**
 * Insert the method's description here.
 * Creation date: (9/12/2001 10:35:52 AM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {
	
	return (IDocumentoAmministrativoBulk)getModel();
}
/**
 * Insert the method's description here.
 * Creation date: (10/18/2001 12:50:31 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
	if (getObbligazioniController() == null)
		return null;
	return (Obbligazione_scadenzarioBulk)getObbligazioniController().getModel();
}
/**
 * Insert the method's description here.
 * Creation date: (10/18/2001 12:50:31 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getObbligazioniController() {
	return obbligazioniController;
}
public String getPropertyForGenericSearch() {

	return "fornitore";
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 12:59:29 PM)
 * @return it.cnr.contab.doccont00.core.bulk.OptionRequestParameter
 */
public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
	return userConfirm;
}
	/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context <code>ActionContext</code>
	 */

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	
	try {
		int solaris = Fattura_passivaBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR);
		int esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
		setAnnoSolareInScrivania(solaris == esercizioScrivania);
		setRibaltato(initRibaltato(context));
		if (!isAnnoSolareInScrivania()) {
			String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
			try {
				FatturaPassivaComponentSession session = (FatturaPassivaComponentSession)createComponentSession();
				boolean esercizioScrivaniaAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania)));
				boolean esercizioSuccessivoAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania+1)));
				setRiportaAvantiIndietro(esercizioScrivaniaAperto && esercizioSuccessivoAperto && isRibaltato());
			} catch (Throwable t) {
				handleException(t);
			}
		} else
			setRiportaAvantiIndietro(false);
	} catch (javax.ejb.EJBException e) {
		setAnnoSolareInScrivania(false);
	}
	
	resetTabs();
	
}
public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {

	try {
		if (bulk != null) {
			Fattura_passivaBulk fp = (Fattura_passivaBulk)bulk;
			fp.setDettagliCancellati(new java.util.Vector());
			fp.setDocumentiContabiliCancellati(new java.util.Vector());
		}
		FatturaPassivaComponentSession h = (FatturaPassivaComponentSession)createComponentSession();
		if (isEditing()) {
			if (h.hasFatturaPassivaARowNotInventoried(context.getUserContext(), (Fattura_passivaBulk)getModel())) {
				setErrorMessage("Attenzione: sebbene il salvataggio sia stato effettuato correttamente, si ricorda che alcuni beni devono ancora essere inventariati!");
			} else if (getDettaglio().isInventoriedChildDeleted()) {
				getDettaglio().setInventoriedChildDeleted(false);
				setErrorMessage("Attenzione: sebbene il salvataggio sia stato effettuato correttamente, si ricorda che sono stati eliminati beni inventariati. Provvedere all'aggiornamento dell'inventario!");
			}
		}
		return super.initializeModelForEdit(context,bulk);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
public it.cnr.jada.ejb.CRUDComponentSession initializeModelForGenericSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context)
	throws BusinessProcessException {

	return createComponentSession();
}
/**
 * Insert the method's description here.
 * Creation date: (06/06/2003 15.53.41)
 * @return boolean
 */
public boolean isAnnoDiCompetenza() {
	return annoDiCompetenza;
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2003 17.06.48)
 * @return boolean
 */
public boolean isAnnoSolareInScrivania() {
	return annoSolareInScrivania;
}
public boolean isAssociaInventarioButtonEnabled() {
	
	return	 	(isEditing()||isInserting()) &&
			getModel() != null &&
			!getDettaglio().getDetails().isEmpty() &&
			!((Fattura_passivaBulk)getModel()).isGenerataDaCompenso() &&
			(isAnnoDiCompetenza());
}
public boolean isAssociaInventarioButtonHidden() {
	
	return isSearching() || isDeleting();
}
public abstract boolean isAutoGenerated();
public boolean isBringbackButtonEnabled() {
	return super.isBringbackButtonEnabled() || isDeleting();
}
public boolean isBringbackButtonHidden() {
	return super.isBringbackButtonHidden() || !isDeleting();
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2003 9.38.09)
 * @return boolean
 */
public boolean isCarryingThrough() {
	return carryingThrough;
}
public boolean isDeleteButtonEnabled() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	return super.isDeleteButtonEnabled() &&
			!isModelVoided() && 
			!fp.isCongelata() &&
			!fp.isGenerataDaCompenso() &&
			((isAnnoDiCompetenza() && !fp.isRiportata()) ||
				// Gennaro Borriello - (02/11/2004 16.48.21)
				// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
				(!isAnnoDiCompetenza() && fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania())));
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 2:40:22 PM)
 * @return boolean
 */
public boolean isDeleting() {
	return isDeleting;
}
/*
 * Gennaro Borriello - (03/11/2004 19.04.48)
 * Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato 
 * DA UN ES. PRECEDENTE a quello di scrivania.
 */
public boolean isInputReadonly() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	
	return super.isInputReadonly() || isDeleting() || isModelVoided() || (!isAnnoDiCompetenza() && isEditing()) ||
			(fp != null && ((fp.isPagata() || 
				(
				 (isAnnoDiCompetenza() && fp.isRiportata()))
				|| fp.isCongelata()) && !isSearching()));
}
public boolean isInputReadonlyDoc1210() {
	
	return super.isInputReadonly();
}
public boolean isInventariaButtonEnabled() {
	
	return	 	(isEditing()||isInserting()) &&
	        getModel() != null &&
			!getDettaglio().getDetails().isEmpty() &&
			!((Fattura_passivaBulk)getModel()).isGenerataDaCompenso() &&
			(isAnnoDiCompetenza());
}
public boolean isInventariaButtonHidden() {
	
	return isSearching() || isDeleting();
}
public boolean isInventariaPerAumentoButtonEnabled() {
	
	return	 	(isEditing()||isInserting()) &&
			getModel() != null &&
			!getDettaglio().getDetails().isEmpty() &&
			!((Fattura_passivaBulk)getModel()).isGenerataDaCompenso() &&
			(isAnnoDiCompetenza());
}
public boolean isInventariaPerAumentoButtonHidden() {
	
	return isSearching() || isDeleting();
}
public boolean isBeni_collButtonEnabled() {
	Fattura_passivaBulk fattura=(Fattura_passivaBulk) getModel();
	 if (fattura.getHa_beniColl()==null) 
	    return false;
	 else   
		return	/*isInserting() &&*/ 
			getModel() != null &&
			!getDettaglio().getDetails().isEmpty() &&
			!((Fattura_passivaBulk)getModel()).isGenerataDaCompenso()&&
			(fattura.getHa_beniColl().booleanValue());
}
public boolean isBeni_collButtonHidden() {
	
	return isSearching() || isDeleting();
}
public boolean isManualModify() {

	return !((Fattura_passivaBulk)getModel()).isCongelata();
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/2001 11:45:16)
 * @return boolean
 */
public boolean isModelVoided() {

	return !isSearching() && getModel() != null && ((Voidable)getModel()).isAnnullato();
}
public boolean isNewButtonEnabled() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	return super.isNewButtonEnabled() && 
		((fp != null && fp.getEsercizio()!= null &&
			Fattura_passivaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR) <= fp.getEsercizio().intValue() 
			&& isAnnoDiCompetenza()) ||
				!isAnnoDiCompetenza());
}
public boolean isRiportaAvantiButtonEnabled() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	return	isCarryingThrough() || !fp.isRiportata();
}
public boolean isRiportaAvantiButtonHidden() {

	return	basicRiportaButtonHidden();
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2003 17.28.47)
 * @return boolean
 */
public boolean isRiportaAvantiIndietro() {
	return riportaAvantiIndietro;
}
public boolean isRiportaIndietroButtonEnabled() {

    Fattura_passivaBulk fp = (Fattura_passivaBulk) getModel();
    return isEditing() &&
    		!isDeleting() &&
    		!isModelVoided() && 
    		!isDirty() &&
    		!fp.isPagata() &&
    		!isCarryingThrough();
}
public boolean isRiportaIndietroButtonHidden() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	return basicRiportaButtonHidden() ||
			(fp != null && !(fp.isRiportata() || isCarryingThrough()));
}
public boolean isSaveButtonEnabled() {

	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	return super.isSaveButtonEnabled() && 
			!isModelVoided() &&
			!fp.isGenerataDaCompenso() &&
			/* RP per consentire salvataggio delle associazioni con l'inventario
			 * tutti i dati risultano comunque non aggiornabili
			 !fp.isPagata() && */
			!fp.isCongelata() &&
				// Gennaro Borriello - (02/11/2004 16.48.21)
				// Fix sul controllo dello "Stato Riportato": 
			((!fp.isRiportata() && isAnnoDiCompetenza()) || isCarryingThrough() ||
			(!isAnnoDiCompetenza() && fp.COMPLETAMENTE_RIPORTATO.equals(fp.getRiportataInScrivania())) );
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isUndoBringBackButtonEnabled() {
	
	return super.isUndoBringBackButtonEnabled() || isDeleting() || isViewing();
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isUndoBringBackButtonHidden() {
	
	return super.isUndoBringBackButtonHidden() || !isDeleting();
}
/**
 * Attiva oltre al normale reset il metodo di set dei tab di default.
 *
 * @param context <code>ActionContext</code>
 *
 * @see resetTabs
 */

public void reset(ActionContext context) throws BusinessProcessException {

	if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() != 
		Fattura_passivaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR))
		resetForSearch(context);
	else {
		setCarryingThrough(false);
		super.reset(context);
		resetTabs();
	}
}
	/**
	 * Attiva oltre al normale reset il metodo di set dei tab di default.
	 *
	 * @param context <code>ActionContext</code>
	 *
	 * @see resetTabs
	 */

	public void resetForSearch(ActionContext context) throws BusinessProcessException {
		setCarryingThrough(false);
		super.resetForSearch(context);
		resetTabs();
	}
	/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context <code>ActionContext</code>
	 */

	public void resetTabs() {
		setTab("tab","tabFatturaPassiva");
	}
public void riportaAvanti(ActionContext context)
	throws ValidationException,BusinessProcessException {

	try {
		FatturaPassivaComponentSession session = (FatturaPassivaComponentSession)createComponentSession();
		Fattura_passivaBulk fpCarried = 
				(Fattura_passivaBulk)session.riportaAvanti(
										context.getUserContext(),
										(IDocumentoAmministrativoBulk)getModel(),
										getUserConfirm());
		setModel(context, fpCarried);
	} catch(Exception e) {
		throw handleException(e);
	} finally {
		setUserConfirm(null);
	}
}
public void riportaIndietro(ActionContext context)
	throws it.cnr.jada.action.BusinessProcessException {
	
	if (isDirty()) {
		setMessage("Il documento è stato modificato! Operazione annullata.");
		return;
	}
	
	rollbackUserTransaction();
	setCarryingThrough(true);

	try {
		FatturaPassivaComponentSession session = (FatturaPassivaComponentSession)createComponentSession();
		Fattura_passivaBulk fpCarried = (Fattura_passivaBulk)session.riportaIndietro(
											context.getUserContext(),
											(IDocumentoAmministrativoBulk)getModel());
		basicEdit(context, fpCarried, true);
		setDirty(true);
		
	} catch (Throwable t) {
		setCarryingThrough(false);
		rollbackUserTransaction();
		throw handleException(t);
	}
}
/**
 * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo che ha aperto il compenso
 * @param	context			il Context che ha generato la richiesta
 * @param	savePointName	il nome del savePoint
 *
 */	
public void rollbackToSavePoint(ActionContext context, String savePointName) throws BusinessProcessException{

	try {

		FatturaPassivaComponentSession sess = (FatturaPassivaComponentSession)createComponentSession();
		sess.rollbackToSavePoint(context.getUserContext(), savePointName);

	} catch (java.rmi.RemoteException e) {
			throw handleException(e);
	} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
	}
}
public void salvaRiportandoAvanti(ActionContext context)
	throws ValidationException,BusinessProcessException {

	Fattura_passivaBulk fpClone = (Fattura_passivaBulk)getModel();
	try {
		setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
		completeSearchTools(context,this);
		validate(context);
		saveChildren(context);

		update(context);
		riportaAvanti(context);
	} catch (BusinessProcessException e) {
		rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
		//Il setModel è necessario perchè update setta il modello. se riportaAvanti fallisce il pg_ver_rec
		//rimane disallineato.
		setModel(context, fpClone);
		throw handleException(e);
	}
	
	if (getMessage() == null)
		setMessage("Salvataggio e riporto all'esercizio successivo eseguito in modo corretto.");
	
	commitUserTransaction();
	setCarryingThrough(false);
	
	try {
		basicEdit(context,getModel(),true);
	} catch (BusinessProcessException e) {
		setModel(context,null);
		setDirty(false);
		throw e;
	}
}
public void save(ActionContext context) 
	throws ValidationException,BusinessProcessException {

	super.save(context);
	setCarryingThrough(false);
}
/**
 * Insert the method's description here.
 * Creation date: (06/06/2003 15.53.41)
 * @param newIsAnnoDiCompetenza boolean
 */
public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
	annoDiCompetenza = newAnnoDiCompetenza;
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2003 17.06.48)
 * @param newAnnoSolareInScrivania boolean
 */
public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
	annoSolareInScrivania = newAnnoSolareInScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (25/06/2003 9.38.09)
 * @param newCarryingThrough boolean
 */
public void setCarryingThrough(boolean newCarryingThrough) {
	carryingThrough = newCarryingThrough;
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 2:40:22 PM)
 * @param newIsDeleting boolean
 */
public void setIsDeleting(boolean newIsDeleting) {
	isDeleting = newIsDeleting;
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2003 17.28.47)
 * @param newRiportaAvantiIndietro boolean
 */
public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
	riportaAvantiIndietro = newRiportaAvantiIndietro;
}
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	context			il Context che ha generato la richiesta
 * @param	savePointName	il nome del savePoint
 *
 */	
public void setSavePoint(ActionContext context, String savePointName) throws BusinessProcessException{

	try {

		FatturaPassivaComponentSession sess = (FatturaPassivaComponentSession)createComponentSession();
		sess.setSavePoint(context.getUserContext(), savePointName);

	} catch (java.rmi.RemoteException e) {
			throw handleException(e);
	} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 12:59:29 PM)
 * @param newUserConfirm it.cnr.contab.doccont00.core.bulk.OptionRequestParameter
 */
public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter newUserConfirm) {
	userConfirm = newUserConfirm;
}
public void update(ActionContext context)
	throws it.cnr.jada.action.BusinessProcessException {
		
	try {
		getModel().setToBeUpdated();
		setModel(
				context,
				((FatturaPassivaComponentSession)createComponentSession()).modificaConBulk(
																			context.getUserContext(),
																			getModel(),
																			getUserConfirm()));
	} catch(Exception e) {
		throw handleException(e);
	} finally {
		setUserConfirm(null);
	}
}
/**
  * Validazione dell'obbligazione in fase di creazione di una nuova obbligazione
  * o modifica di una già creata.
  * Il metodo viene chiamato sul riporta dell'Obbligazione in modo da validare
  * istantaneamente l'oggetto creato.
  * Chi non ne ha bisogno lo lasci vuoto.
  *
**/
public void validaObbligazionePerDocAmm(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
	return;
}
protected void writeToolbar(javax.servlet.jsp.JspWriter writer,it.cnr.jada.util.jsp.Button[] buttons) throws java.io.IOException,javax.servlet.ServletException {

	it.cnr.jada.util.jsp.Button riportaAvantiButton = buttons[buttons.length-1];
	riportaAvantiButton.setSeparator(isRiportaIndietroButtonHidden() && !isRiportaAvantiButtonHidden());
	super.writeToolbar(writer, buttons);
}
public boolean initRibaltato(it.cnr.jada.action.ActionContext context)  throws it.cnr.jada.action.BusinessProcessException
{
	try 
	{
		return (((RicercaDocContComponentSession)createComponentSession("CNRCHIUSURA00_EJB_RicercaDocContComponentSession", RicercaDocContComponentSession.class)).isRibaltato(context.getUserContext()));
	} catch(Exception e) 
	{
		throw handleException(e);
	} 
}
public boolean isRibaltato() {
	return ribaltato;
}
public void setRibaltato(boolean b) {
	ribaltato = b;
}
}
