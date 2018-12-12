package it.cnr.contab.chiusura00.bp;

import it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk;
import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.ejb.RicercaComponentSession;
import it.cnr.jada.util.RemoteIterator;

/**
 * BP che gestisce il riporto avanti massivo di documenti contabili all'esercizio successivo;
 */

public class RiportoEsSuccessivoBP extends it.cnr.jada.util.action.BulkBP implements it.cnr.jada.util.action.SelectionListener
{

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private java.lang.Class searchBulkClass;
	private it.cnr.jada.bulk.BulkInfo searchBulkInfo;
	private java.lang.String searchResultColumnSet;
	private boolean ribaltato;
	private boolean confRibaltato;
	private Unita_organizzativaBulk uoSrivania;
	private boolean isRiaccertamentoChiuso = true;
	private boolean isRiobbligazioneChiusa= true;
	private boolean isGaeCollegateProgetti = true;
	private boolean isProgettiCollegatiGaeApprovati = true;
	
public RiportoEsSuccessivoBP() {
		super("Tn");
//		super();
}
public RiportoEsSuccessivoBP(String function) {
	super(function+"Tn");
//	super(function);	
}
/**
 * Ricerca di documenti contabili idonei al riporto avanti
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param model	
 * @return 
 * @throws BusinessProcessException	
 */
public it.cnr.jada.util.RemoteIterator cercaDocDaRiportare(ActionContext context,V_obb_acc_xxxBulk model) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		validaRibaltamento(context);
		RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,sessione.cercaPerRiportaAvanti(context.getUserContext(),model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * annulla la selezione dei doc. contabili effettuata fino ad ora
 */
public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).clearSelectionPerRiportaAvanti(context.getUserContext(), (V_obb_acc_xxxBulk)getModel());

	} catch(Exception e) {
		throw handleException(e);
	} 
}
/**
 * richiama la stored procedure che effettua il riporto massivo sui documenti
 * selezionati dall'utente
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 */
public void confermaRiporto(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		validaRibaltamento(context);
		RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
		sessione.callRiportoNextEsDocCont(context.getUserContext(), ((V_obb_acc_xxxBulk)getModel()).getPg_call());
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
 */
public RicercaComponentSession createComponentSession() throws BusinessProcessException {
	return (RicercaComponentSession)createComponentSession(componentSessioneName,RicercaComponentSession.class);
}
/**
 * crea una istanza di V_obb_acc_xxxBulk per effettuare la ricerca dei doc. contabili 
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return 
 * @throws BusinessProcessException	
 */
public OggettoBulk createEmptyModelForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try
	{
		V_obb_acc_xxxBulk doc = new V_obb_acc_xxxBulk();
		return ((it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession)createComponentSession()).inizializzaBulkPerRicerca( context.getUserContext(), doc );
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}		
}
/**
 * deselectAll method comment.
 */
public void deselectAll(it.cnr.jada.action.ActionContext context) {}
/**
 * non usato
 * 
 *
 */
public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,createComponentSession().cerca(context.getUserContext(),clauses,model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * non usato
 * 
 *
 */

public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * Restituisce il valore della proprietà 'bulkClass'
 *
 * @return Il valore della proprietà 'bulkClass'
 */
public java.lang.Class getBulkClass() {
	return bulkClass;
}
/**
 * Restituisce il valore della proprietà 'bulkInfo'
 *
 * @return Il valore della proprietà 'bulkInfo'
 */

public it.cnr.jada.bulk.BulkInfo getBulkInfo() {
	return bulkInfo;
}
/**
 * Restituisce il valore della proprietà 'componentSessioneName'
 *
 * @return Il valore della proprietà 'componentSessioneName'
 */
public java.lang.String getComponentSessioneName() {
	return componentSessioneName;
}
/**
 * Restituisce il valore della proprietà 'searchBulkClass'
 *
 * @return Il valore della proprietà 'searchBulkClass'
 */
public java.lang.Class getSearchBulkClass() {
	return searchBulkClass;
}
/**
 * Restituisce il valore della proprietà 'searchBulkInfo'
 *
 * @return Il valore della proprietà 'searchBulkInfo'
 */
public it.cnr.jada.bulk.BulkInfo getSearchBulkInfo() {
	return searchBulkInfo;
}
/**
 * Restituisce il valore della proprietà 'searchResultColumns'
 *
 * @return Il valore della proprietà 'searchResultColumns'
 */
public java.util.Dictionary getSearchResultColumns() {
	if (getSearchResultColumnSet() == null)
		return getModel().getBulkInfo().getColumnFieldPropertyDictionary();
	return getModel().getBulkInfo().getColumnFieldPropertyDictionary(getSearchResultColumnSet());
}
/**
 * Restituisce il valore della proprietà 'searchResultColumnSet'
 *
 * @return Il valore della proprietà 'searchResultColumnSet'
 */
public java.lang.String getSearchResultColumnSet() {
	return searchResultColumnSet;
}
/**
 * ritorna la currentSelection
 */
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	//for (int i = 0;i < bulks.length;i++) {
		//if (Boolean.TRUE.equals(((Cdr_ass_tipo_laBulk)bulks[i]).getFl_associato()))
			//currentSelection.set(i);
	//}
	return currentSelection;
}
/**
 * Inizializza il BP coi parametri di inizializzazione presenti nel file di
 * configurazione delle action.
 * @param config l'elenco dei parametri di configurazione del business
 * @param context	L'ActionContext della richiesta 
 */

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setBulkClassName(config.getInitParameter("bulkClassName"));
		setComponentSessioneName(config.getInitParameter("componentSessionName"));
		if (searchBulkClass == null)
			setSearchBulkClass(bulkClass);
		setSearchResultColumnSet(config.getInitParameter("searchResultColumnSet"));			
		setModel( context, createEmptyModelForSearch( context));
	} catch(ClassNotFoundException e) {
		throw new RuntimeException("Non trovata la classe bulk");
	}
	super.init(config,context);
	setRibaltato(initRibaltato(context));
	setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
}
/**
 * inizializza il pg_call del protocollo VSX in modo da consentire l'inserimento dei doc. contabili
 * selezionat dall'utente nella tabella VSX_CHIUSURA
 */
public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		V_obb_acc_xxxBulk doc = ((RicercaDocContComponentSession)createComponentSession()).initializeSelectionPerRiportaAvanti(
						context.getUserContext(),(V_obb_acc_xxxBulk)getModel());
		setModel( context, doc );

	} catch(Exception e) 
	{
		throw handleException(e);
	} 
}

/**
 * gestisce la selezione massiva di tutti i doc.contabili visualizzati all'utente
 */
public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).selectAllPerRiportaAvanti(
						context.getUserContext(),(V_obb_acc_xxxBulk)getModel());
	} catch(Exception e) {
		throw handleException(e);
	} 
	
}
/**
 * Imposta il valore della proprietà 'bulkClass'
 *
 * @param newClass	Il valore da assegnare a 'bulkClass'
 */
public void setBulkClass(java.lang.Class newClass) {
	bulkInfo = BulkInfo.getBulkInfo(this.bulkClass = newClass);
}
/**
 * Imposta il valore della proprietà 'bulkClassName'
 *
 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
 * @throws ClassNotFoundException	
 */
public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
	setBulkClass(getClass().getClassLoader().loadClass(bulkClassName));
}
/**
 * Imposta il valore della proprietà 'bulkInfo'
 *
 * @param newInfo	Il valore da assegnare a 'bulkInfo'
 */
public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newInfo) {
	bulkInfo = newInfo;
}
/**
 * Imposta il valore della proprietà 'componentSessioneName'
 *
 * @param newSessioneName	Il valore da assegnare a 'componentSessioneName'
 */
public void setComponentSessioneName(java.lang.String newSessioneName) {
	componentSessioneName = newSessioneName;
}
/**
 * Imposta il valore delle proprietà 'searchBulkClass' e 'searchBulkInfo'
 *
 * @param searchBulkClass	Il valore da assegnare a 'searchBulkClass'
 */

private void setSearchBulkClass(java.lang.Class searchBulkClass) {
	searchBulkInfo = BulkInfo.getBulkInfo(this.searchBulkClass = searchBulkClass);
}
/**
 * Imposta il valore della proprietà 'searchBulkClassName'
 *
 * @param searchBulkClassName	Il valore da assegnare a 'searchBulkClassName'
 * @throws ClassNotFoundException	
 */
public void setSearchBulkClassName(String searchBulkClassName) throws ClassNotFoundException {
	if (searchBulkClassName != null)
		setSearchBulkClass(getClass().getClassLoader().loadClass(searchBulkClassName));
}
/**
 * Imposta il valore della proprietà 'searchBulkInfo'
 *
 * @param newSearchBulkInfo	Il valore da assegnare a 'searchBulkInfo'
 */
public void setSearchBulkInfo(it.cnr.jada.bulk.BulkInfo newSearchBulkInfo) {
	searchBulkInfo = newSearchBulkInfo;
}
/**
 * Imposta il valore della proprietà 'searchResultColumnSet'
 *
 * @param newSearchResultColumnSet	Il valore da assegnare a 'searchResultColumnSet'
 */
public void setSearchResultColumnSet(java.lang.String newSearchResultColumnSet) {
	searchResultColumnSet = newSearchResultColumnSet;
}
/**
 * inserisce/elimina dalla tabella VSX_CHIUSURA le chiavi dei doc.contabili
 * selezionati/deselezionati dall'utente
 */
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).setSelectionPerRiportaAvanti(
			context.getUserContext(),
			(V_obb_acc_xxxBulk)getModel(),
			bulks,
			oldSelection,
			newSelection);
		return newSelection;
	} catch(Exception e) {
		throw handleException(e);
	} 
}
/**
 * Imposta il valore della proprietà 'sessioneName'
 *
 * @param newSessioneName	Il valore da assegnare a 'sessioneName'
 */
public void setSessioneName(java.lang.String newSessioneName) {
	componentSessioneName = newSessioneName;
}

public boolean initRibaltato(it.cnr.jada.action.ActionContext context)  throws it.cnr.jada.action.BusinessProcessException
{
	try 
	{
		return (((RicercaDocContComponentSession)createComponentSession()).isRibaltato(context.getUserContext()));
	} catch(Exception e) 
	{
		throw handleException(e);
	} 
}
	/**
	 * @return
	 */
	public boolean isRibaltato() {
		return ribaltato;
	}

	/**
	 * @param b
	 */
	public void setRibaltato(boolean b) {
		ribaltato = b;
	}

	public boolean isUoEnte(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
	}

	public void setUoSrivania(Unita_organizzativaBulk bulk) {
		uoSrivania = bulk;
	}
	public Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}
	
	public boolean confermaRibaltamentoDispImproprie(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			sessione.callRibaltaDispImproprie(context.getUserContext());
		} catch(Exception e) {
			throw handleException(e);
		}
		return confRibaltato;
	}
	
	public boolean isCdsRibaltato(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			return sessione.getCdsRibaltato(context.getUserContext());
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	public void aggiornaParametriCds(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			sessione.updateParametriCds(context.getUserContext());
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	private void validaRibaltamento(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			if (!sessione.isRicosResiduiChiusa(context.getUserContext())) {
				throw new ApplicationException("Impossibile procedere al ribaltamento. Non è stata chiusa l'attività di ricostruzione dei residui per tutti i CdR del CdS "+CNRUserContext.getCd_cds((CNRUserContext)context.getUserContext())+".");
			}
			if (sessione.isSfondataDispCdS(context.getUserContext())) {
				throw new ApplicationException("Impossibile procedere al ribaltamento. La somma degli impegni, per alcuni GAE/Voce, è superiore alla disponibilità ad impegnare.");
			}
			setRiaccertamentoChiuso(sessione.isRiaccertamentoChiuso(context.getUserContext()));
			setRiobbligazioneChiusa(sessione.isRiobbligazioneChiusa(context.getUserContext()));
			setGaeCollegateProgetti(sessione.isGaeCollegateProgetti(context.getUserContext()));
			setProgettiCollegatiGaeApprovati(sessione.isProgettiCollegatiGaeApprovati(context.getUserContext()));
			
			if (!isRiaccertamentoChiuso())
				throw new ApplicationException("Impossibile procedere al ribaltamento. Esistono accertamenti residui da ribaltare privi dello stato.");
			if (!isRiobbligazioneChiusa())
				throw new ApplicationException("Impossibile procedere al ribaltamento. Esistono obbligazioni residue da ribaltare privi dello stato.");
			if (!isRiaccertamentoChiuso() && !isGaeCollegateProgetti()) 
				throw new ApplicationException("Impossibile procedere al ribaltamento. Esistono anomalie da verificare.");
			if (!isGaeCollegateProgetti())
				throw new ApplicationException("Impossibile procedere al ribaltamento. Esistono GAE su impegni e/o accertamenti da ribaltare prive dell''indicazione del progetto nell''anno del ribaltamento.");
			if (!isProgettiCollegatiGaeApprovati())
				throw new ApplicationException("Impossibile procedere al ribaltamento. Esistono GAE su impegni e/o accertamenti da ribaltare associate a progetti non approvati.");
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	private void setRiaccertamentoChiuso(boolean isRiaccertamentoChiuso) {
		this.isRiaccertamentoChiuso = isRiaccertamentoChiuso;
	}
	public boolean isRiaccertamentoChiuso() {
		return isRiaccertamentoChiuso;
	}
	
	public void setRiobbligazioneChiusa(boolean isRiobbligazioneChiusa) {
		this.isRiobbligazioneChiusa = isRiobbligazioneChiusa;
	}
	
	public boolean isRiobbligazioneChiusa() {
		return isRiobbligazioneChiusa;
	}
	
	private void setGaeCollegateProgetti(boolean isGaeCollegateProgetti) {
		this.isGaeCollegateProgetti = isGaeCollegateProgetti;
	}
	public boolean isGaeCollegateProgetti() {
		return isGaeCollegateProgetti;
	}
	
	public void setProgettiCollegatiGaeApprovati(boolean isProgettiCollegatiGaeApprovati) {
		this.isProgettiCollegatiGaeApprovati = isProgettiCollegatiGaeApprovati;
	}
	public boolean isProgettiCollegatiGaeApprovati() {
		return isProgettiCollegatiGaeApprovati;
	}
	
	public it.cnr.jada.util.RemoteIterator cercaResiduiForRiaccertamento(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,sessione.cercaResiduiForRiaccertamento(context.getUserContext()));
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.util.RemoteIterator cercaGaeSenzaProgettiForRibaltamento(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,sessione.cercaGaeSenzaProgettiForRibaltamento(context.getUserContext()));
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.util.RemoteIterator cercaProgettiCollegatiGaeNonApprovatiForRibaltamento(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try 
		{
			RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,sessione.cercaProgettiCollegatiGaeNonApprovatiForRibaltamento(context.getUserContext()));
		} catch(Exception e) {
			throw handleException(e);
		}
	}
}