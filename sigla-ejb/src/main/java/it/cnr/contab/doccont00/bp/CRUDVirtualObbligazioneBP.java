/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.AllegatoObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.jsp.Button;

public abstract class CRUDVirtualObbligazioneBP 
	extends AllegatiCRUDBP<AllegatoObbligazioneBulk, ObbligazioneBulk>
	implements IDocumentoContabileBP {
		
	private boolean deleting = false;
	protected boolean annoSolareInScrivania;
	protected boolean riportaAvantiIndietro;
	private boolean attivoRegolamento_2006 = false;
	private boolean ribaltato;

public CRUDVirtualObbligazioneBP() {

	super();	
}
public CRUDVirtualObbligazioneBP(String function) {

	super(function);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 * @throws ValidationException	
 */
protected void aggiornaDefferUpdateSaldi(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException , it.cnr.jada.bulk.ValidationException
{
	if ( ! (getParent() instanceof IDefferedUpdateSaldiBP) )
		throw new BusinessProcessException( "Impossibile aggiornare saldi dei documenti contabili in differita! (Il parent BusinessProcess non implementa interfaccia IDefferUpdateSaldiBP)" );
	IDefferUpdateSaldi docAmm = ((IDefferedUpdateSaldiBP) getParent()).getDefferedUpdateSaldiBulk();
	IDocumentoContabileBulk docCont = (IDocumentoContabileBulk) getModel();
	docAmm.addToDefferredSaldi( docCont, docCont.getSaldiInfo() );
}
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
{
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW)
	{
		ObbligazioneBulk obb = (ObbligazioneBulk)getModel();
		if ( "Y".equals(obb.getRiportato()) )
		{
	//		setStatus(VIEW);
			setMessage("Il documento è stato riportato all'esercizio successivo. Non consentita la modifica.");
		}

		// Se l'impoegno è stato stornato, NON è possibile apportare alcuna modifica.
		//	27/02/2004 - BORRIELLO - Segnalazione Interna
		if ( obb.getStato_obbligazione().equals(ObbligazioneBulk.STATO_OBB_STORNATO)) {
			setStatus(VIEW);
			setMessage("Impegno Residuo stornato. Non consentita la modifica.");
		}
	
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public ObbligazioneComponentSession createObbligazioneComponentSession() throws BusinessProcessException 
{
	return (ObbligazioneComponentSession)createComponentSession("CNRDOCCONT00_EJB_ObbligazioneComponentSession",CRUDComponentSession.class);
}
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 * @return newToolbar La nuova toolbar creata
 */

protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
		
	Button[] toolbar = super.createToolbar();
	Button[] newToolbar = new Button[ toolbar.length + 2 ];
	for ( int i = 0; i< toolbar.length; i++ )
		newToolbar[ i ] = toolbar[ i ];
	newToolbar[ toolbar.length] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.riportaIndietro");
	newToolbar[ toolbar.length + 1 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.riportaAvanti");		

	return newToolbar;
}
public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException 
{
	try
	{
		completeSearchTools( context, this );
		return super.find( context, clauses, model );
	}
	catch ( Exception e )
	{
		throw handleException(e);		
	}	
}
public abstract OggettoBulk getBringBackModel() ;
/**
 * @param context Il contesto dell'azione
 * @param obbligazione it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk
 * @param mode java.lang.String
 * @return it.cnr.jada.util.action.CRUDBP
 */
public static CRUDVirtualObbligazioneBP getBusinessProcessFor(
	it.cnr.jada.action.ActionContext context,
	ObbligazioneBulk obbligazione,
	String mode) throws it.cnr.jada.action.BusinessProcessException {

	if (obbligazione == null)
		return null;
	else if (obbligazione.getFl_pgiro().booleanValue())
		return (it.cnr.contab.doccont00.bp.CRUDImpegnoPGiroBP)context.getUserInfo().createBusinessProcess(context,"CRUDImpegnoPGiroBP",new Object[] { mode });
	else if (obbligazione.isObbligazioneResiduo())
		return (CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneResBP",new Object[] { mode });
	else if (obbligazione.isObbligazioneResiduoImproprio())
		return (CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneResImpropriaBP",new Object[] { mode });
	else 
		return (CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneBP",new Object[] { mode });
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public static ObbligazioneAbstractComponentSession getVirtualComponentSession(
		it.cnr.jada.action.ActionContext context,
		boolean setSavePoint) 
		throws it.cnr.jada.action.BusinessProcessException {

	if (context == null) return null;
	
	if (setSavePoint)
		return setSafePoint(context);
	else {
		it.cnr.jada.action.BusinessProcess bp = context.getBusinessProcess();
		return (ObbligazioneAbstractComponentSession)bp.createComponentSession(
													"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
													ObbligazioneAbstractComponentSession.class);
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public DocumentoContabileComponentSession getVirtualSession(
	it.cnr.jada.action.ActionContext context,
	boolean setSavePoint) 
	throws it.cnr.jada.action.BusinessProcessException {
	return getVirtualComponentSession( context, setSavePoint );
} 
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	if (getUserTransaction() != null)
		setSafePoint(context);
	super.init(config,context);
	try {
		setAttivoRegolamento_2006(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(),CNRUserContext.getEsercizio(context.getUserContext())).getFl_regolamento_2006().booleanValue());
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	} catch (RemoteException e) {
		throw new BusinessProcessException(e);
	} 
	try 
	{
		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
		calendar.setTime( today );
		Integer solaris = new Integer(calendar.get(java.util.Calendar.YEAR));
		Integer esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
		setAnnoSolareInScrivania(solaris == esercizioScrivania);
		setRibaltato(initRibaltato(context));
		if (!isAnnoSolareInScrivania()) {
			String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
			try 
			{
				ObbligazioneComponentSession session = createObbligazioneComponentSession();
				EsercizioBulk es = session.verificaStatoEsercizio(context.getUserContext(), cds, esercizioScrivania);
				EsercizioBulk esSucc = session.verificaStatoEsercizio(context.getUserContext(), cds, new Integer(esercizioScrivania.intValue()+1));
				if ( es.getSt_apertura_chiusura().equals(es.STATO_APERTO) &&
					  esSucc.getSt_apertura_chiusura().equals(es.STATO_APERTO) &&
					  isRibaltato())
					setRiportaAvantiIndietro(true);
				else
					setRiportaAvantiIndietro(false);				
			} catch (Throwable t) 
			{
				handleException(t);
			}
		}
		else
			setRiportaAvantiIndietro(false);
	} catch (javax.ejb.EJBException e) 
	{
		setAnnoSolareInScrivania(false);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 14.16.31)
 * @return boolean
 */
public boolean isAnnoSolareInScrivania() {
	return annoSolareInScrivania;
}
//
//	Abilito il bottone di ANNULLA RIPORTA documento solo se non ho scadenze in fase di modifica/inserimento
//

public boolean isBringbackButtonEnabled()
{
	return super.isBringbackButtonEnabled() || isDeleting();
}
public boolean isDeleteButtonEnabled() 
{
	
	return super.isDeleteButtonEnabled() && getModel() != null && 
					!((ObbligazioneBulk)getModel()).isDocRiportato() && !((ObbligazioneBulk)getModel()).isControparteRiportatata();

}
public boolean isDeleting() {
	return deleting;
}
public boolean isEditable() {
	return super.isEditable() || isDeleting();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'fromDocAmm'
 *
 * @return Il valore della proprietà 'fromDocAmm'
 */
public boolean isFromDocAmm() 
{
  try{	
	return IDefferedUpdateSaldiBP.class.isAssignableFrom( getParent().getClass());
  }catch(java.lang.NullPointerException e){
  	return false;
  }
}
public boolean isRiportaAvantiButtonEnabled() 
{
	IDocumentoContabileBulk doc = ((IDocumentoContabileBulk)getModel());
	
	return !isRiportaAvantiButtonHidden() &&
				isEditing() &&
//				!isDirty() &&
				doc != null &&
				!((ObbligazioneBulk)doc).isDocRiportato();

}
public boolean isRiportaAvantiButtonHidden() 
{
	return isAnnoSolareInScrivania() ||
			 !isRiportaAvantiIndietro() ||
			 isFromDocAmm();
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 14.16.31)
 * @return boolean
 */
public boolean isRiportaAvantiIndietro() {
	return riportaAvantiIndietro;
}
public boolean isRiportaIndietroButtonEnabled() 
{
	IDocumentoContabileBulk doc = ((IDocumentoContabileBulk)getModel());
	
	return !isRiportaIndietroButtonHidden() &&
				isEditing() &&
				!isDirty() &&
				doc != null &&
				((ObbligazioneBulk)doc).isDocRiportato();
}
public boolean isRiportaIndietroButtonHidden() 
{
	return isRiportaAvantiButtonHidden();
}
public boolean isSaveButtonEnabled() 
{
	return super.isSaveButtonEnabled() && getModel() != null && 
			!((ObbligazioneBulk)getModel()).isDocRiportato() && !((ObbligazioneBulk)getModel()).isControparteRiportatata();
}
//
//	Abilito il bottone di RIPORTA documento solo se non ho scadenze in fase di modifica/inserimento
//

public boolean isUndoBringBackButtonEnabled() 
{
	return super.isUndoBringBackButtonEnabled() || isDeleting() || isViewing();
}
public void riportaAvanti(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		((DocumentoContabileComponentSession)createComponentSession()).callRiportaAvanti( context.getUserContext(), (IDocumentoContabileBulk) getModel());
		edit( context, getModel(), true );
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
public void riportaIndietro(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		((DocumentoContabileComponentSession)createComponentSession()).callRiportaIndietro( context.getUserContext(), (IDocumentoContabileBulk) getModel());
		edit( context, getModel(), true );
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public static ObbligazioneAbstractComponentSession rollbackToSafePoint (
	ActionContext context) 
	throws it.cnr.jada.action.BusinessProcessException {

	if (context == null) return null;

	BusinessProcess bp = context.getBusinessProcess();
	ObbligazioneAbstractComponentSession compSession = (ObbligazioneAbstractComponentSession)bp.createComponentSession(
												"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
												ObbligazioneAbstractComponentSession.class);
	try {
		compSession.rollbackToSavePoint(context.getUserContext());
	} catch (Throwable t) {
		throw new it.cnr.jada.action.BusinessProcessException(t);
	}
	return compSession;
}
public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException , it.cnr.jada.bulk.ValidationException
{
	/*27.5.2002 - salvo il valore della disp cassa per reimpostarlo */
	boolean isCheckDispCassEseguito = ((ObbligazioneBulk)getModel()).isCheckDisponibilitaCassaEseguito();
	ObbligazioneBulk obbligazione = (ObbligazioneBulk)getModel();
	Obbligazione_modificaBulk obbMod=null;
	boolean saldiDaAggiornare=false;
	if (obbligazione instanceof ObbligazioneResBulk && obbligazione.isObbligazioneResiduo()) {
		obbMod = ((ObbligazioneResBulk)obbligazione).getObbligazione_modifica();
		saldiDaAggiornare = ((ObbligazioneResBulk)obbligazione).isSaldiDaAggiornare();
	}
	super.save( context );
	((ObbligazioneBulk)getModel()).setCheckDisponibilitaCassaEseguito( isCheckDispCassEseguito);
	((ObbligazioneBulk)getModel()).setCheckDisponibilitaContrattoEseguito( false);
	((ObbligazioneBulk)getModel()).setCheckDisponibilitaCdrGAEEseguito( false);

	// reimposto i valori
	if (obbligazione instanceof ObbligazioneResBulk && obbligazione.isObbligazioneResiduo()) {
		((ObbligazioneResBulk)getModel()).setObbligazione_modifica(obbMod);
		((ObbligazioneResBulk)getModel()).setSaldiDaAggiornare(saldiDaAggiornare);
	}
	
	if ( getUserTransaction() != null )
		if (!(((ObbligazioneBulk)getModel()).isObbligazioneResiduo() && !(getParent() instanceof IDefferedUpdateSaldiBP)))
			aggiornaDefferUpdateSaldi( context );
		/*
	try
	{	OggettoBulk oggetto = ((BulkBP)getParent()).getModel();
		if (oggetto instanceof it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk)
			oggetto = ((BulkBP)getParent().getParent()).getModel();
		IDefferUpdateSaldi docAmm = (IDefferUpdateSaldi) oggetto;
		Map values = (Map) docAmm.getDefferredSaldi().get(getModel());
		((ObbligazioneComponentSession)createComponentSession()).aggiornaSaldiInDifferita( context.getUserContext(), (IDocumentoContabileBulk)getModel(), values );
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}*/
		
}		
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param scadenza	
 * @param context	L'ActionContext della richiesta
 */
public abstract void selezionaScadenza(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza, it.cnr.jada.action.ActionContext context );
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 14.16.31)
 * @param newAnnoSolareInScrivania boolean
 */
public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
	annoSolareInScrivania = newAnnoSolareInScrivania;
}
public void setDeleting(boolean newDeleting) {
	deleting = newDeleting;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 14.16.31)
 * @param newRiportaAvantiIndietro boolean
 */
public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
	riportaAvantiIndietro = newRiportaAvantiIndietro;
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public static ObbligazioneAbstractComponentSession setSafePoint (
	ActionContext context) 
	throws it.cnr.jada.action.BusinessProcessException {

	if (context == null) return null;

	BusinessProcess bp = context.getBusinessProcess();
	ObbligazioneAbstractComponentSession compSession = (ObbligazioneAbstractComponentSession)bp.createComponentSession(
												"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
												ObbligazioneAbstractComponentSession.class);
	try {
		compSession.setSavePoint(context.getUserContext());
	} catch (Throwable t) {
		throw new it.cnr.jada.action.BusinessProcessException(t);
	}
	return compSession;
}
	/**
	 * @return
	 */
	public boolean isAttivoRegolamento_2006() {
		return attivoRegolamento_2006;
	}

	/**
	 * @param b
	 */
	public void setAttivoRegolamento_2006(boolean b) {
		attivoRegolamento_2006 = b;
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

	protected String getStorePath(ObbligazioneBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegatoParentBulk.getUnita_organizzativa())
						.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
						.orElse(""),
				"Obbligazioni",
				Optional.ofNullable(allegatoParentBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				String.valueOf(allegatoParentBulk.getPg_obbligazione())
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	@Override
	protected Class<AllegatoObbligazioneBulk> getAllegatoClass() {
		return AllegatoObbligazioneBulk.class;
	}
}
