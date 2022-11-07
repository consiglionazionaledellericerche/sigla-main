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
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.AllegatoAccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
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

public abstract class CRUDVirtualAccertamentoBP 
	extends AllegatiCRUDBP<AllegatoAccertamentoBulk, AccertamentoBulk>
	implements IDocumentoContabileBP {
		
	private boolean deleting = false;
	protected boolean annoSolareInScrivania;
	protected boolean riportaAvantiIndietro;	
	private boolean attivoRegolamento_2006 = false;
	private boolean flNuovoPdg = false;
	private boolean ribaltato;

	private boolean attivaAccertamentoPluriennale = false;

	public boolean isAttivaAccertamentoPluriennale() {
		return attivaAccertamentoPluriennale;
	}

public CRUDVirtualAccertamentoBP() {

	super();	
}
public CRUDVirtualAccertamentoBP(String function) {
	
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
		throw new BusinessProcessException( "Impossibile aggiornare saldi dei docuemnti contabili in differita! (Il parent BusinessProcess non implementa interfaccia IDefferUpdateSaldiBP)" );
	IDefferUpdateSaldi docAmm = ((IDefferedUpdateSaldiBP) getParent()).getDefferedUpdateSaldiBulk();
	IDocumentoContabileBulk docCont = (IDocumentoContabileBulk) getModel();
	docAmm.addToDefferredSaldi( docCont, docCont.getSaldiInfo() );
}
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
{
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW)
	{
		AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
		if ( "Y".equals(accertamento.getRiportato()) )
		{
	//		setStatus(VIEW);
			setMessage("Il documento è stato riportato all'esercizio successivo. Non consentita la modifica.");
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
	newToolbar[ toolbar.length] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.riportaIndietro");
	newToolbar[ toolbar.length + 1 ] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.riportaAvanti");		

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
	
public abstract OggettoBulk getBringBackModel();
/**
 * @param context Il contesto dell'azione
 * @param accertamento it.cnr.contab.doccont00.core.bulk.AccertamentoBulk
 * @param mode java.lang.String
 * @return it.cnr.jada.util.action.CRUDBP
 */
public static CRUDVirtualAccertamentoBP getBusinessProcessFor(
	it.cnr.jada.action.ActionContext context,
	AccertamentoBulk accertamento,
	String mode) throws it.cnr.jada.action.BusinessProcessException {

	if (accertamento == null) return null;
	else if (accertamento.getFl_pgiro().booleanValue())
		return (it.cnr.contab.doccont00.bp.CRUDAccertamentoPGiroBP)context.getUserInfo().createBusinessProcess(context,"CRUDAccertamentoPGiroBP",new Object[] { mode });
	else if ( accertamento.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES ))
		return (it.cnr.contab.doccont00.bp.CRUDAccertamentoResiduoBP)context.getUserInfo().createBusinessProcess(context,"CRUDAccertamentoResiduoBP",new Object[] { mode });
	else
		return (it.cnr.contab.doccont00.bp.CRUDAccertamentoBP)context.getUserInfo().createBusinessProcess(context,"CRUDAccertamentoBP",new Object[] { mode });		
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public static AccertamentoAbstractComponentSession getVirtualComponentSession(
	it.cnr.jada.action.ActionContext context,
	boolean setSavePoint) 
	throws it.cnr.jada.action.BusinessProcessException {

	if (context == null) return null;
	if (setSavePoint)
		return setSafePoint(context);
	else {
		BusinessProcess bp = context.getBusinessProcess();
		return (AccertamentoAbstractComponentSession)bp.createComponentSession(
				"CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
				AccertamentoAbstractComponentSession.class);
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
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(),CNRUserContext.getEsercizio(context.getUserContext()));
		setAttivoRegolamento_2006(parCnr.getFl_regolamento_2006().booleanValue());
		setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());

		attivaAccertamentoPluriennale = Utility.createConfigurazioneCnrComponentSession().isAccertamentoPluriennaleAttivo(context.getUserContext());

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
					  (esSucc!=null && esSucc.getSt_apertura_chiusura().equals(es.STATO_APERTO)) &&
					  isRibaltato())
					setRiportaAvantiIndietro(true);
				else
					setRiportaAvantiIndietro(false);				
			} catch (Throwable t) 
			{
				throw new BusinessProcessException(t);
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
 * Creation date: (03/07/2003 17.36.54)
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
				!((AccertamentoBulk)getModel()).isDocRiportato() && !((AccertamentoBulk)getModel()).isControparteRiportatata();
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
//	return RicercaAccertamentiBP.class.isAssignableFrom( getParent().getClass()) ||
//	    	IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass());
	try{
		return IDefferedUpdateSaldiBP.class.isAssignableFrom( getParent().getClass());	
	}catch(NullPointerException e){
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
				!((AccertamentoBulk)doc).isDocRiportato(); 

}
public boolean isRiportaAvantiButtonHidden() 
{
	return isAnnoSolareInScrivania() ||
			 !isRiportaAvantiIndietro() ||
			 isFromDocAmm();
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.36.54)
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
				((AccertamentoBulk)doc).isDocRiportato(); 
}
public boolean isRiportaIndietroButtonHidden() 
{
	return isRiportaAvantiButtonHidden();
}
public boolean isSaveButtonEnabled() 
{
	return super.isSaveButtonEnabled() && getModel() != null && 
			!((AccertamentoBulk)getModel()).isDocRiportato() && !((AccertamentoBulk)getModel()).isControparteRiportatata();
	
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
		this.setDirty(true);
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
		this.setDirty(true);
		edit( context, getModel(), true );
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
public static AccertamentoAbstractComponentSession rollbackToSafePoint (
	ActionContext context) 
	throws it.cnr.jada.action.BusinessProcessException {

	if (context == null) return null;

	BusinessProcess bp = context.getBusinessProcess();
	AccertamentoAbstractComponentSession compSession = (AccertamentoAbstractComponentSession)bp.createComponentSession(
												"CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
												AccertamentoAbstractComponentSession.class);
	try {
		compSession.rollbackToSavePoint(context.getUserContext());
	} catch (Throwable t) {
		throw new it.cnr.jada.action.BusinessProcessException(t);
	}
	return compSession;
}
public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException , it.cnr.jada.bulk.ValidationException
{
	boolean isCheckDispContrattoEseguito = ((AccertamentoBulk)getModel()).isCheckDisponibilitaContrattoEseguito();
	Accertamento_modificaBulk acrMod = null;
	// salvo il valore della modifica per reimpostarlo dopo
	if (((AccertamentoBulk)getModel()).isAccertamentoResiduo() && ((AccertamentoBulk)getModel()) instanceof AccertamentoResiduoBulk)
		acrMod = ((AccertamentoResiduoBulk)getModel()).getAccertamento_modifica();
	super.save( context );
	((AccertamentoBulk)getModel()).setCheckDisponibilitaContrattoEseguito( isCheckDispContrattoEseguito);
	if (((AccertamentoBulk)getModel()).isAccertamentoResiduo() && ((AccertamentoBulk)getModel()) instanceof AccertamentoResiduoBulk) {
		((AccertamentoResiduoBulk)getModel()).setAccertamento_modifica(acrMod);
	}
	if ( getUserTransaction() != null )
		if (!(((AccertamentoBulk)getModel()).isAccertamentoResiduo() && !(getParent() instanceof IDefferedUpdateSaldiBP)))
			aggiornaDefferUpdateSaldi( context );
		/*
	try
	{	OggettoBulk oggetto = ((BulkBP)getParent()).getModel();
		if (oggetto instanceof it.cnr.contab.docamm00.docs.bulk.IFiltroRicercaDocCont)
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
public abstract void selezionaScadenza(it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza, it.cnr.jada.action.ActionContext context );
/**
 * Insert the method's description here.
 * Creation date: (03/07/2003 17.36.54)
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
 * Creation date: (03/07/2003 17.36.54)
 * @param newRiportaAvantiIndietro boolean
 */
public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
	riportaAvantiIndietro = newRiportaAvantiIndietro;
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public static AccertamentoAbstractComponentSession setSafePoint (
	ActionContext context) 
	throws it.cnr.jada.action.BusinessProcessException {

	if (context == null) return null;

	BusinessProcess bp = context.getBusinessProcess();
	AccertamentoAbstractComponentSession compSession = (AccertamentoAbstractComponentSession)bp.createComponentSession(
												"CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
												AccertamentoAbstractComponentSession.class);
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

	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
	
	private void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
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
	protected String getStorePath(AccertamentoBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegatoParentBulk.getUnita_organizzativa())
						.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
						.orElse(""),
				"Accertamenti",
				Optional.ofNullable(allegatoParentBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				String.valueOf(allegatoParentBulk.getPg_accertamento())
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	@Override
	protected Class<AllegatoAccertamentoBulk> getAllegatoClass() {
		return AllegatoAccertamentoBulk.class;
	}
	
	public String [][] getTabs() {
		if(attivaAccertamentoPluriennale){
			if(!isSearching()) {
				return new String[][]{
						{"tabAccertamento", "Accertamento", "/doccont00/tab_accertamento.jsp"},
						{"tabImputazioneFin", "Imputazione Finanziaria", "/doccont00/tab_imputazione_fin_accertamento.jsp"},
						{"tabScadenziario", "Scadenziario", "/doccont00/tab_scadenziario_accertamento.jsp"},
						{"tabAccertamentiPluriennali", "Accertamenti Pluriennali", "/doccont00/tab_acc_pluriennali.jsp"}
				};
			}
		}
		return new String[][] {
				{ "tabAccertamento","Accertamento","/doccont00/tab_accertamento.jsp" } ,
				{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_accertamento.jsp" },
				{ "tabScadenziario","Scadenziario","/doccont00/tab_scadenziario_accertamento.jsp" }
		};
	}
}
