package it.cnr.contab.ordmag.richieste.bp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.SecondaryType;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaDettaglioBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.ejb.RichiestaUopComponentSession;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.upload.UploadedFile;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDRichiestaUopBP 
	extends AllegatiCRUDBP<AllegatoRichiestaBulk, RichiestaUopBulk>  {
		
    private final SimpleDetailCRUDController righe= new RichiestaUopRigaCRUDController("Righe", RichiestaUopRigaBulk.class, "righeRichiestaColl", this){
		@Override
		public OggettoBulk removeDetail(int i) {
			List list = getDetails();
			RichiestaUopRigaBulk dettaglio =(RichiestaUopRigaBulk)list.get(i);
			BulkList<AllegatoRichiestaDettaglioBulk> listaDettagliAllegati = dettaglio.getDettaglioAllegati();
			if (listaDettagliAllegati != null && !listaDettagliAllegati.isEmpty()){
				int k;
				for ( k = 0; k < listaDettagliAllegati.size(); k++ ){
					AllegatoRichiestaDettaglioBulk all = listaDettagliAllegati.get(k);
					all.setToBeDeleted();
				}
			}
			return super.removeDetail(i);
		}

    };

	private final SimpleDetailCRUDController dettaglioAllegatiController = new SimpleDetailCRUDController("AllegatiDettaglio", AllegatoRichiestaDettaglioBulk.class,"dettaglioAllegati",righe)
	{
		@Override
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			AllegatoRichiestaDettaglioBulk allegato = (AllegatoRichiestaDettaglioBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.Righe.AllegatiDettaglio.file");
			if (!(file == null || file.getName().equals(""))) {
				allegato.setFile(file.getFile());
				allegato.setContentType(file.getContentType());
				allegato.setNome(allegato.parseFilename(file.getName()));
				allegato.setAspectName(RichiesteCMISService.ASPECT_RICHIESTA_ORDINI_DETTAGLIO);
				allegato.setToBeUpdated();
				getParentController().setDirty(true);
			}
			oggettobulk.validate();		
			super.validate(actioncontext, oggettobulk);
		}
		@Override
		public OggettoBulk removeDetail(int i) {
			if (!getModel().isNew()){	
				List list = getDetails();
				AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)list.get(i);
				if (isPossibileCancellazioneDettaglioAllegato(all)) {
					return super.removeDetail(i);
				} else {
					return null;
				}
			}
			return super.removeDetail(i);
		}
		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int add = super.addDetail(oggettobulk);
			AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)oggettobulk;
			all.setIsDetailAdded(true);
			return add;
		}
	};	
				

	private boolean isDeleting= false;
	private boolean annoDiCompetenza = true;
	private boolean annoSolareInScrivania = true;
	private boolean riportaAvantiIndietro = false;
	private boolean carryingThrough = false;
	private boolean ribaltato;
	private RichiesteCMISService richiesteCMISService;
public CRUDRichiestaUopBP() {
	super();

	setTab("tab","tabRichiestaUop");
	setTab("tabRichiestaUopDettaglio","tabRichiestaDettaglio");

//	dettaglioObbligazioneController = new RichiestaUopRigaCRUDController("DettaglioObbligazioni",Documento_generico_rigaBulk.class,"documento_generico_obbligazioniHash",obbligazioniController) {
//
//			public java.util.List getDetails() {
//
//				RichiestaUopBulk doc= (RichiestaUopBulk)CRUDRichiestaUopBP.this.getModel();
//				java.util.Vector lista = new java.util.Vector();
//				if (doc != null) {
//					java.util.Hashtable h = doc.getDocumento_generico_obbligazioniHash();
//					if (h != null && getParentModel() != null)
//						lista = (java.util.Vector)h.get(getParentModel());
//				}
//				return lista;
//			}
//		};
//	
	
}
public CRUDRichiestaUopBP(String function) throws BusinessProcessException{
	super(function+"Tr");

	setTab("tab","tabRichiestaUop");
	setTab("tabRichiestaUopDettaglio","tabRichiestaDettaglio");
	
//	dettaglioObbligazioneController = new SimpleDetailCRUDController("DettaglioObbligazioni",Documento_generico_rigaBulk.class,"documento_generico_obbligazioniHash",obbligazioniController) {
//
//			public java.util.List getDetails() {
//
//				RichiestaUopBulk doc= (RichiestaUopBulk)CRUDRichiestaUopBP.this.getModel();
//				java.util.Vector lista = new java.util.Vector();
//				if (doc != null) {
//					java.util.Hashtable h = doc.getDocumento_generico_obbligazioniHash();
//					if (h != null && getParentModel() != null)
//						lista = (java.util.Vector)h.get(getParentModel());
//				}
//				return lista;
//			}
//		};
//	
	
}
//protected void basicEdit(it.cnr.jada.action.ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
//	try {
//		RichiestaUopBulk doc = (RichiestaUopBulk)bulk;
//		setAnnoDiCompetenza(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() == doc.getEsercizio().intValue());
//		super.basicEdit(context, doc, doInitializeForEdit);
//	} catch(Throwable e) {
//		throw new it.cnr.jada.action.BusinessProcessException(e);
//	}
//}
//protected boolean basicRiportaButtonHidden() {
//
//	RichiestaUopBulk doc = (RichiestaUopBulk)getModel();
//	return	isAnnoSolareInScrivania() ||
//			!isRiportaAvantiIndietro() ||
//			isDeleting() ||
//			isModelVoided() ||
////			(doc != null && doc.isPagata()) ||
//			!isEditing();
//}
public void create(it.cnr.jada.action.ActionContext context)
	throws	it.cnr.jada.action.BusinessProcessException {
		
	try { 
		getModel().setToBeCreated();
		setModel(
				context,
				((RichiestaUopComponentSession)createComponentSession()).creaConBulk(
																			context.getUserContext(),
																			getModel()));
	} catch(Exception e) {
		throw handleException(e);
	}
}
//public OggettoBulk createEmptyModel(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
//
//	setAnnoDiCompetenza(true);
//	return super.createEmptyModel(context);
//}
//
//public it.cnr.jada.bulk.OggettoBulk createNewBulk(
//    it.cnr.jada.action.ActionContext context)
//    throws it.cnr.jada.action.BusinessProcessException {
//	//al momento della creazione di un nuovo oggetto bulk imposta il tipo di documento
//    RichiestaUopBulk generico =
//        (RichiestaUopBulk) super.createNewBulk(context);
////    if (generico.getCd_tipo_documento_amm() == null){
////        //generico.setCd_tipo_documento_amm(generico.GENERICO_S);
////        generico.setTi_entrate_spese(generico.SPESE);
////    }
//    return generico;
//}
//public it.cnr.jada.bulk.OggettoBulk createNewSearchBulk(
//    it.cnr.jada.action.ActionContext context)
//    throws it.cnr.jada.action.BusinessProcessException {
//
//	return createNewBulk(context);
//}
//protected it.cnr.jada.util.jsp.Button[] createToolbar() {
//	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[11];
//	int i = 0;
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.bringBack");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.undoBringBack");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.print");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.riportaIndietro");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.riportaAvanti");
//
//	return toolbar;
//}
//public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
//	int crudStatus = getModel().getCrudStatus();
//	try {
//		getModel().setToBeDeleted();
//		createComponentSession().eliminaConBulk(context.getUserContext(),getModel());
//	} catch(Exception e) {
//		getModel().setCrudStatus(crudStatus);
//		throw handleException(e);
//	}
//}
//public void edit(
//	it.cnr.jada.action.ActionContext context,
//	OggettoBulk bulk,
//	boolean doInitializeForEdit) 
//	throws it.cnr.jada.action.BusinessProcessException {
//
//	setCarryingThrough(false);
//	super.edit(context, bulk, doInitializeForEdit);
//}
///**
// * Effettua una operazione di ricerca per un attributo di un modello.
// *
// * @param userContext	lo UserContext che ha generato la richiesta
// * @param filtro		modello che fa da contesto alla ricerca (il modello del FormController padre del
// * 						controller che ha scatenato la ricerca)
// * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
// *
//**/
//public it.cnr.jada.util.RemoteIterator findObbligazioni(it.cnr.jada.UserContext userContext, Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.action.BusinessProcessException {
//
//	try{
//
//		DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession)createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);
//		return sess.cercaObbligazioni(userContext, filtro);
//
//	} catch (it.cnr.jada.comp.ComponentException e) {
//		throw handleException(e);
//	} catch (java.rmi.RemoteException e) {
//		throw handleException(e);
//	}
//}
///**
// * Effettua una operazione di ricerca per un attributo di un modello.
// *
// * @param actionContext contesto dell'azione in corso
// * @param clauses Albero di clausole da utilizzare per la ricerca
// * @param bulk prototipo del modello di cui si effettua la ricerca
// * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
// * 			controller che ha scatenato la ricerca)
// * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
// *
//**/
//public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
//
//	try {
//		
//		DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession)createComponentSession("CNRDOCAMM00_EJB_DocumentoGenericoComponentSession", DocumentoGenericoComponentSession.class);
//		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
//												actionContext,
//												sess.cerca(
//														actionContext.getUserContext(),
//														clauses,
//														bulk,
//														context,
//														property));
//	} catch (it.cnr.jada.comp.ComponentException e) {
//		throw handleException(e);
//	} catch (java.rmi.RemoteException e) {
//		throw handleException(e);
//	}
//}
//public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {
//
//	return null;
//}
//public IDocumentoAmministrativoBulk getBringBackDocAmm() {
//	
//	return getDocumentoAmministrativoCorrente();
//}
//public String getColumnsetForGenericSearch() {
//
//	return "filtro_ricerca_documenti";
//}
//public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
//
//	if (isDeleting() && getParent() != null)
//		return getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
//	return (IDefferUpdateSaldi)getDocumentoAmministrativoCorrente();
//}
//public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {
//
//	if (isDeleting() && getParent() != null)
//		return ((IDefferedUpdateSaldiBP)getParent()).getDefferedUpdateSaldiParentBP();
//	return this;
//}
//public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {
//
//	return null;
////		if (deleteManager == null)
////		deleteManager = new it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk();
////	else
////		deleteManager.reset();
////	return deleteManager;
//}
///**
// * Restituisce il valore della proprietà 'dettaglio'
// *
// * @return Il valore della proprietà 'dettaglio'
// */
public final SimpleDetailCRUDController getRighe() {
	return righe;
}
///**
// * Restituisce il valore della proprietà 'dettaglioObbligazioneController'
// *
// * @return Il valore della proprietà 'dettaglioObbligazioneController'
// */
////public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglioObbligazioneController() {
////	return dettaglioObbligazioneController;
////}
//public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {
//	
//	return (IDocumentoAmministrativoBulk)getModel();
//}
//public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
////	if (getObbligazioniController() == null)
//		return null;
////	return (Obbligazione_scadenzarioBulk)getObbligazioniController().getModel();
//}
///**
// * Restituisce il valore della proprietà 'obbligazioniController'
// *
// * @return Il valore della proprietà 'obbligazioniController'
// */
////public final ObbligazioniCRUDController getObbligazioniController() {
////	return obbligazioniController;
////}
//public String getPropertyForGenericSearch() {
//
//	return null;
//}
/**
//	/**
//	 * Imposta come attivi i tab di default.
//	 *
//	 * @param context <code>ActionContext</code>
//	 */
//
//protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
//
//	super.init(config,context);
//	
//	try {
////		int solaris = RichiestaUopBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR);
//		int esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
////		setAnnoSolareInScrivania(solaris == esercizioScrivania);
//		setRibaltato(initRibaltato(context));
//		if (!isAnnoSolareInScrivania()) {
//			String cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext());
//			try {
//				DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession)createComponentSession();
//				boolean esercizioScrivaniaAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania)));
//				boolean esercizioSuccessivoAperto = session.verificaStatoEsercizio(context.getUserContext(), new EsercizioBulk(cds, new Integer(esercizioScrivania+1)));
//				setRiportaAvantiIndietro(esercizioScrivaniaAperto && esercizioSuccessivoAperto && isRibaltato());
//			} catch (Throwable t) {
//				handleException(t);
//			}
//		} else
//			setRiportaAvantiIndietro(false);
//	} catch (javax.ejb.EJBException e) {
//		setAnnoSolareInScrivania(false);
//	}
//	
//	resetTabs();
//}
//public it.cnr.jada.ejb.CRUDComponentSession initializeModelForGenericSearch(
//	it.cnr.jada.util.action.BulkBP bp,
//	it.cnr.jada.action.ActionContext context)
//	throws BusinessProcessException {
//
//	DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession)createComponentSession();
//
//	Filtro_ricerca_doc_ammVBulk filtro = (Filtro_ricerca_doc_ammVBulk)bp.getModel();
//	RichiestaUopBulk dg = (RichiestaUopBulk)filtro.getInstance();
//	
//	try {
//		RichiestaUopBulk clause = (RichiestaUopBulk)dg.clone();
////		clause.setTi_entrate_spese(dg.ENTRATE);
////		java.util.Collection tipiDocumentoEntrata = session.findTipi_doc_for_search(context.getUserContext(), clause);
////		dg.setTipi_doc_for_search(tipiDocumentoEntrata);
//	} catch (Throwable t) {
//		throw new BusinessProcessException(t.getMessage(), t);
//	}
//	
//	return (it.cnr.jada.ejb.CRUDComponentSession)session;
//}
///**
// * Insert the method's description here.
// * Creation date: (06/06/2003 16.48.19)
// * @return boolean
// */
//public boolean isAnnoDiCompetenza() {
//	return annoDiCompetenza;
//}
///**
// * Insert the method's description here.
// * Creation date: (02/07/2003 12.46.07)
// * @return boolean
// */
//public boolean isAnnoSolareInScrivania() {
//	return annoSolareInScrivania;
//}
//public boolean isAutoGenerated(){
//	return false;
//}
///**
// * Insert the method's description here.
// * Creation date: (02/07/2003 12.46.07)
// * @return boolean
// */
//public boolean isCarryingThrough() {
//	return carryingThrough;
//}
//public boolean isDeleteButtonEnabled() {
//	RichiestaUopBulk doc= (RichiestaUopBulk)CRUDRichiestaUopBP.this.getModel();
//	return super.isDeleteButtonEnabled();
////		&& (doc.getStato_cofi()!=null && doc.getStato_cofi().equals(doc.STATO_CONTABILIZZATO)) &&
////		!isModelVoided() &&
////		((isAnnoDiCompetenza() && !doc.isRiportata()) ||
////			// Gennaro Borriello - (02/11/2004 16.48.21)
////			// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
////				(!isAnnoDiCompetenza() && doc.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(doc.getRiportataInScrivania())));
//	//&& (!doc.getTi_associato_manrev().equals(doc.PARZIALMENTE_ASSOCIATO_A_MANDATO));;
//}
//public boolean isDeleting() {
//	return isDeleting;
//}
//public boolean isFreeSearchButtonHidden() {
//	return super.isFreeSearchButtonHidden() ;
//}
//public boolean isInputReadonly() {
//	RichiestaUopBulk doc= (RichiestaUopBulk) getModel();
//	return (
//		super.isInputReadonly()
//			|| isDeleting()
//			|| isModelVoided()
//			|| !isAnnoDiCompetenza()
//		//Gennaro Borriello - (03/11/2004 19.04.48)
//		// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato 
//		//	DA UN ES. PRECEDENTE a quello di scrivania.
////			|| (doc != null && (doc.isPagata() || 
////								(isAnnoDiCompetenza() && doc.isRiportata()))  
////				)          
////			|| (doc.getTipo_documento() != null
////				&& !(doc.getTipo_documento() != null && doc.getTipo_documento().getFl_utilizzo_doc_generico() != null && doc.getTipo_documento().getFl_utilizzo_doc_generico().booleanValue()))
//			)
//		&& !this.isSearching();
//}
//public boolean isManualModify() {
//    RichiestaUopBulk doc= (RichiestaUopBulk) CRUDRichiestaUopBP.this.getModel();
//    return true;
////    		doc.getTipo_documento()!=null && !doc.getTipo_documento().getDs_tipo_documento_amm().equalsIgnoreCase("TRASF_S");
//}
//public boolean isModelVoided() {
//	return false;
//	return !isSearching() && getModel() != null && ((Voidable)getModel()).isAnnullato();
//}
////public boolean isRiportaAvantiButtonEnabled() {
////
////	RichiestaUopBulk doc = (RichiestaUopBulk)getModel();
////	return	isCarryingThrough() || !doc.isRiportata();
////}
//public boolean isRiportaAvantiButtonHidden() {
//
//	return	basicRiportaButtonHidden();
//}
///**
// * Insert the method's description here.
// * Creation date: (02/07/2003 12.46.07)
// * @return boolean
// */
//public boolean isRiportaAvantiIndietro() {
//	return riportaAvantiIndietro;
//}
//public boolean isRiportaIndietroButtonEnabled() {
//
//	RichiestaUopBulk doc = (RichiestaUopBulk)getModel();
//    return isEditing() &&
//    		!isDeleting() &&
//    		!isModelVoided() && 
//    		!isDirty() &&
////    		!doc.isPagata() &&
//    		!isCarryingThrough();
//}
//public boolean isRiportaIndietroButtonHidden() {
//
//	RichiestaUopBulk doc = (RichiestaUopBulk)getModel();
//	return basicRiportaButtonHidden() ||
//			(doc != null 
////&& 			!(doc.isRiportata() || isCarryingThrough())
//			);
//}
//public boolean isSaveButtonEnabled() {
//	RichiestaUopBulk doc= (RichiestaUopBulk)CRUDRichiestaUopBP.this.getModel();
//	//return super.isSaveButtonEnabled() && isEditable() && (doc.getStato_cofi()!=null && (doc.getStato_cofi().equals(doc.STATO_PARZIALE) || doc.getStato_cofi().equals(doc.STATO_CONTABILIZZATO))) && !isModelVoided();
//	return super.isSaveButtonEnabled() &&
//			isEditable() && 
//			!isModelVoided() 
//			/* RP per consentire salvataggio delle associazioni con l'inventario
//			 * tutti i dati risultano comunque non aggiornabili
//			!doc.isPagata() && */
////&&			(    (isAnnoDiCompetenza() && !doc.isRiportata())
////			 ||  carryingThrough
////			// Consentire salvataggio 		
////			 ||(!doc.isEditable()&&doc.getCrudStatus()!=5)
////				// Gennaro Borriello - (02/11/2004 16.48.21)
////				// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.				
////			 ||(doc.COMPLETAMENTE_RIPORTATO.equals(doc.getRiportataInScrivania()) && !isAnnoDiCompetenza())
//			;
//		
//}
///**
// * Attiva oltre al normale reset il metodo di set dei tab di default.
// *
// * @param context <code>ActionContext</code>
// *
// * @see resetTabs
// */
//
//public void reset(ActionContext context) throws BusinessProcessException {
//
//	setCarryingThrough(false);
//    super.reset(context);
//    resetTabs();
//}
///**
// * Attiva oltre al normale reset il metodo di set dei tab di default.
// *
// * @param context <code>ActionContext</code>
// *
// * @see resetTabs
// */
//
//public void resetForSearch(ActionContext context) throws BusinessProcessException {
//
//	setCarryingThrough(false);
//	super.resetForSearch(context);
//    resetTabs();
//}
//	/**
//	 * Imposta come attivi i tab di default.
//	 *
//	 * @param context <code>ActionContext</code>
//	 */
//
//	public void resetTabs() {
//		setTab("tab","tabDocumentoPassivo");
//		/*
//		setTab("tabDocumentoPassivo","tabDocumentoPassivo");
//		setTab("tabDocumentoPassivoDettaglio","tabDocumentoPassivoDettaglio");
//		*/
//	}
//public void riportaAvanti(ActionContext context)
//	throws ValidationException,BusinessProcessException {
//
//	try {
//		DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession)createComponentSession();
//		RichiestaUopBulk docCarried = 
//				(RichiestaUopBulk)session.riportaAvanti(
//										context.getUserContext(),
//										(IDocumentoAmministrativoBulk)getModel(),
//										getUserConfirm());
//		setModel(context, docCarried);
//	} catch(Exception e) {
//		throw handleException(e);
//	} finally {
//		setUserConfirm(null);
//	}
//}
//public void riportaIndietro(ActionContext context)
//	throws it.cnr.jada.action.BusinessProcessException {
//	
//	if (isDirty()) {
//		setMessage("Il documento è stato modificato! Operazione annullata.");
//		return;
//	}
//	
//	rollbackUserTransaction();
//	setCarryingThrough(true);
//
//	try {
//		DocumentoGenericoComponentSession session = (DocumentoGenericoComponentSession)createComponentSession();
//		RichiestaUopBulk docCarried = (RichiestaUopBulk)session.riportaIndietro(
//											context.getUserContext(),
//											(IDocumentoAmministrativoBulk)getModel());
//		basicEdit(context, docCarried, true);
//		setDirty(true);
//		
//	} catch (Throwable t) {
//		setCarryingThrough(false);
//		rollbackUserTransaction();
//		throw handleException(t);
//	}
//}
///**
// * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
// *
// * Pre-post-conditions:
// *
// * Nome: Rollback to savePoint
// * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
// * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
// *       modifiche apportate al doc. amministrativo che ha aperto il compenso
// * @param	context			il Context che ha generato la richiesta
// * @param	savePointName	il nome del savePoint
// *
// */	
//public void rollbackToSavePoint(ActionContext context, String savePointName) throws BusinessProcessException{
//
//	try {
//
//		DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession)createComponentSession();
//		sess.rollbackToSavePoint(context.getUserContext(), savePointName);
//
//	} catch (java.rmi.RemoteException e) {
//			throw handleException(e);
//	} catch (it.cnr.jada.comp.ComponentException e) {
//			throw handleException(e);
//	}
//}
//public void salvaRiportandoAvanti(ActionContext context)
//	throws ValidationException,BusinessProcessException {
//
//	RichiestaUopBulk docClone = (RichiestaUopBulk)getModel();
//	try {
//		setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
//		completeSearchTools(context,this);
//		validate(context);
//		saveChildren(context);
//
//		update(context);
//		riportaAvanti(context);
//	} catch (BusinessProcessException e) {
//		rollbackToSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
//		//Il setModel è necessario perchè update setta il modello. se riportaAvanti fallisce il pg_ver_rec
//		//rimane disallineato.
//		setModel(context, docClone);
//		throw handleException(e);
//	}
//	if (getMessage() == null)
//		setMessage("Salvataggio e riporto all'esercizio successivo eseguito in modo corretto.");
//	
//	commitUserTransaction();
//	setCarryingThrough(false);
//	
//	try {
//		basicEdit(context,getModel(),true);
//	} catch (BusinessProcessException e) {
//		setModel(context,null);
//		setDirty(false);
//		throw e;
//	}
//}
//public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException {
//
//	super.save(context);
//	setCarryingThrough(false);
//	resetTabs();
//}
///**
// * Insert the method's description here.
// * Creation date: (06/06/2003 16.48.19)
// * @param newAnnoDiCompetenza boolean
// */
//public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
//	annoDiCompetenza = newAnnoDiCompetenza;
//}
///**
// * Insert the method's description here.
// * Creation date: (02/07/2003 12.46.07)
// * @param newAnnoSolareInScrivania boolean
// */
//public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
//	annoSolareInScrivania = newAnnoSolareInScrivania;
//}
///**
// * Insert the method's description here.
// * Creation date: (02/07/2003 12.46.07)
// * @param newCarryingThrough boolean
// */
//public void setCarryingThrough(boolean newCarryingThrough) {
//	carryingThrough = newCarryingThrough;
//}
//public void setIsDeleting(boolean newIsDeleting) {
//	isDeleting = newIsDeleting;
//}
///**
// * Insert the method's description here.
// * Creation date: (02/07/2003 12.46.07)
// * @param newRiportaAvantiIndietro boolean
// */
//public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
//	riportaAvantiIndietro = newRiportaAvantiIndietro;
//}
///**
// * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
// * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
// * anche quelli del documento amministrativo.
// *
// * Pre-post-conditions:
// *
// * Nome: Imposta savePoint
// * Pre:  Una richiesta di impostare un savepoint e' stata generata 
// * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
// *
// * @param	context			il Context che ha generato la richiesta
// * @param	savePointName	il nome del savePoint
// *
// */	
//public void setSavePoint(ActionContext context, String savePointName) throws BusinessProcessException{
//
//	try {
//
//		DocumentoGenericoComponentSession sess = (DocumentoGenericoComponentSession)createComponentSession();
//		sess.setSavePoint(context.getUserContext(), savePointName);
//
//	} catch (java.rmi.RemoteException e) {
//			throw handleException(e);
//	} catch (it.cnr.jada.comp.ComponentException e) {
//			throw handleException(e);
//	}
//}
/**
 * Imposta il valore della proprietà 'userConfirm'
 *
 * @param newUserConfirm	Il valore da assegnare a 'userConfirm'
 */
public void update(it.cnr.jada.action.ActionContext context)
	throws	it.cnr.jada.action.BusinessProcessException {
		
	try {
		getModel().setToBeUpdated();
		setModel(
				context,
				((RichiestaUopComponentSession)createComponentSession()).modificaConBulk(
																			context.getUserContext(),
																			getModel()));
		archiviaAllegati(context, null);
		archiviaAllegatiDettaglio();
	} catch(Exception e) {
		throw handleException(e);
	}
}
private void archiviaAllegatiDettaglio() throws ApplicationException, BusinessProcessException {
	RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
	for (Object oggetto : richiesta.getRigheRichiestaColl()) {
		RichiestaUopRigaBulk dettaglio = (RichiestaUopRigaBulk)oggetto;
		for (AllegatoRichiestaDettaglioBulk allegato : dettaglio.getDettaglioAllegati()) {
			if (allegato.isToBeCreated()){
				try {
					Document node = cmisService.storeSimpleDocument(allegato, 
							new FileInputStream(allegato.getFile()),
							allegato.getContentType(),
							allegato.getNome(), getCMISPathDettaglio(dettaglio, true));
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					throw handleException(e);
				} catch(CmisContentAlreadyExistsException _ex) {
					throw new ApplicationException("Il file " + allegato.getNome() +" già esiste!");
				}
			}else if (allegato.isToBeUpdated()) {
				try {
					if (allegato.getFile() != null)
//							if (!isDocumentoDettaglioProvenienteDaGemis(allegato)){
								cmisService.updateContent(allegato.getDocument(cmisService).getId(), 
										new FileInputStream(allegato.getFile()),
										allegato.getContentType());
								cmisService.updateProperties(allegato, allegato.getDocument(cmisService));
								allegato.setCrudStatus(OggettoBulk.NORMAL);
//							} else {
//								throw new ApplicationException("Aggiornamento non possibile! Documento proveniente dalla procedura Missioni");
//							}
				} catch (FileNotFoundException e) {
					throw handleException(e);
				}
			}
		}
		for (Iterator<AllegatoRichiestaDettaglioBulk> iterator = dettaglio.getDettaglioAllegati().deleteIterator(); iterator.hasNext();) {
			AllegatoRichiestaDettaglioBulk allegato = iterator.next();
			if (allegato.isToBeDeleted()){
				cmisService.deleteNode(allegato.getDocument(cmisService));
				allegato.setCrudStatus(OggettoBulk.NORMAL);
			}
		}
	}

//	if (!missione.isSalvataggioTemporaneo()){
		for (Iterator<RichiestaUopRigaBulk> iterator = richiesta.getRigheRichiestaColl().deleteIterator(); iterator.hasNext();) {
			RichiestaUopRigaBulk dettaglio = iterator.next();
			Folder folder = null;
			for (Iterator<AllegatoRichiestaDettaglioBulk> iteratorAll = dettaglio.getDettaglioAllegati().iterator(); iteratorAll.hasNext();) {
				AllegatoRichiestaDettaglioBulk allegato = iteratorAll.next();
				if (allegato.isToBeDeleted()){
					Document doc = allegato.getDocument(cmisService);
					List<Folder> list = doc.getParents();
					for (Iterator<Folder> iteratorFolder = list.iterator(); iteratorFolder.hasNext();) {
						folder = iteratorFolder.next();
					}
					cmisService.deleteNode(doc);
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				}
			}
			if (folder != null){
				cmisService.deleteNode(folder);
			}
		}
//	}
}
///**
//  * Validazione dell'obbligazione in fase di creazione di una nuova obbligazione
//  * o modifica di una già creata.
//  * Il metodo viene chiamato sul riporta dell'Obbligazione in modo da validare
//  * istantaneamente l'oggetto creato.
//  * Chi non ne ha bisogno lo lasci vuoto.
//  *
//**/
//public void validaObbligazionePerDocAmm(ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
//	return;
//}
//protected void writeToolbar(javax.servlet.jsp.JspWriter writer,it.cnr.jada.util.jsp.Button[] buttons) throws java.io.IOException,javax.servlet.ServletException {
//
//	it.cnr.jada.util.jsp.Button riportaAvantiButton = buttons[buttons.length-1];
//	riportaAvantiButton.setSeparator(isRiportaIndietroButtonHidden() && !isRiportaAvantiButtonHidden());
//	super.writeToolbar(writer, buttons);
//	writeInventarioToolbar(writer);
//}
//public boolean initRibaltato(it.cnr.jada.action.ActionContext context)  throws it.cnr.jada.action.BusinessProcessException
//{
//	try 
//	{
//		return (((RicercaDocContComponentSession)createComponentSession("CNRCHIUSURA00_EJB_RicercaDocContComponentSession", RicercaDocContComponentSession.class)).isRibaltato(context.getUserContext()));
//	} catch(Exception e) 
//	{
//		throw handleException(e);
//	} 
//}
//public boolean isRibaltato() {
//	return ribaltato;
//}
//public void setRibaltato(boolean b) {
//	ribaltato = b;
//}
//public void writeInventarioToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {
//
//	if (!isSearching() && !isDeleting()) {
//		openToolbar(writer);
//		it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createInventarioToolbar(),this);
//		closeToolbar(writer);
//	}
//}
//protected it.cnr.jada.util.jsp.Button[] createInventarioToolbar() {
//
//	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
//	int i = 0;
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.inventaria");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.associaInventario");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.inventariaPerAumento");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.beni_coll");
//	return toolbar;
//}
//public boolean isAssociaInventarioButtonEnabled() {
//	
//	 return  	(isEditing()||isInserting());
//}
//public boolean isAssociaInventarioButtonHidden() {
//	return isSearching() || isDeleting();
//}
//public boolean isInventariaButtonEnabled() {
//	
//	return	 	(isEditing()||isInserting());
//}
//public boolean isInventariaButtonHidden() {
//	return isSearching() || isDeleting();
//}
//public boolean isInventariaPerAumentoButtonEnabled() {
//	
//	return	  	(isEditing()||isInserting());
//			/*getModel() != null &&
//			!getDettaglio().getDetails().isEmpty() &&
//			!((RichiestaUopBulk)getModel()).isGenerataDaCompenso() &&
//			(isAnnoDiCompetenza())*/
//	
//}
//public void writeFormInput(javax.servlet.jsp.JspWriter jspwriter,String s,String s1,boolean flag,String s2,String s3) throws java.io.IOException {
//	RichiestaUopBulk doc=null;
//	if(getModel()!=null)
//		doc = (RichiestaUopBulk)getModel();
//	if (doc!=null &&
////		doc.isRiportataInScrivania()&&
////		!doc.isPagata()&&
//		isInputReadonly()&& 
//		s1.equals("stato_liquidazione")){ 
//		getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, "onChange=\"submitForm('doOnStatoLiquidazioneChange')\"", getInputPrefix(), getStatus(), getFieldValidationMap());
//	}
//	else
//		if (doc!=null &&
////			doc.isRiportataInScrivania()&&
////			!doc.isPagata()&& 
//			isInputReadonly()&& 
//			s1.equals("causale")){ 
//				getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, "onChange=\"submitForm('doOnCausaleChange')\"", getInputPrefix(), getStatus(), getFieldValidationMap());
//		
//	} else if (doc != null 
////			&& doc.isRiportataInScrivania() && !doc.isPagata()
//		&& isInputReadonly() && s1.equals("sospeso")) {
//	getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag,
//			s2,"" ,
//			getInputPrefix(), getStatus(), getFieldValidationMap());
//	}  
//	else
//		super.writeFormInput(jspwriter,s,s1,flag,s2,s3);
//}
@Override
protected CMISPath getCMISPath(RichiestaUopBulk allegatoParentBulk, boolean create) throws BusinessProcessException{
	return richiesteCMISService.getCMISPath(allegatoParentBulk, create);
}

protected CMISPath getCMISPathDettaglio(RichiestaUopRigaBulk dettaglioBulk, boolean create) throws BusinessProcessException{
	try {
		CMISPath cmisPath = null;
		cmisPath = SpringUtil.getBean("cmisPathRichieste",CMISPath.class);

		if (create) {
			cmisPath = richiesteCMISService.createFolderIfNotPresent(cmisPath, dettaglioBulk.getCdUnitaOperativa(), dettaglioBulk.getCdUnitaOperativa(), dettaglioBulk.getCdUnitaOperativa());
			cmisPath = richiesteCMISService.createFolderIfNotPresent(cmisPath, dettaglioBulk.getCdNumeratore(), dettaglioBulk.getCdNumeratore(), dettaglioBulk.getCdNumeratore());
			cmisPath = richiesteCMISService.createFolderIfNotPresent(cmisPath, "Anno "+dettaglioBulk.getEsercizio().toString(), "Anno "+dettaglioBulk.getEsercizio().toString(), "Anno "+dettaglioBulk.getEsercizio().toString());
			cmisPath = richiesteCMISService.createFolderRichiestaIfNotPresent(cmisPath, dettaglioBulk.getRichiestaUop());
			cmisPath = richiesteCMISService.createFolderDettaglioIfNotPresent(cmisPath, dettaglioBulk);
		} else {
			try {
				richiesteCMISService.getNodeByPath(cmisPath);
			} catch (CmisObjectNotFoundException _ex) {
				return null;
			}
		}			
		return cmisPath;
	} catch (ComponentException e) {
		throw new BusinessProcessException(e);
	}
}
@Override
protected Class<AllegatoRichiestaBulk> getAllegatoClass() {
	return AllegatoRichiestaBulk.class;
}
public RichiesteCMISService getRichiesteCMISService() {
	return richiesteCMISService;
}
public void setRichiesteCMISService(RichiesteCMISService richiesteCMISService) {
	this.richiesteCMISService = richiesteCMISService;
}
protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
	super.initialize(actioncontext);
	richiesteCMISService = SpringUtil.getBean("richiesteCMISService",
			RichiesteCMISService.class);	
}
@Override
public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk)
		throws BusinessProcessException {
	
	RichiestaUopBulk allegatoParentBulk = (RichiestaUopBulk)oggettobulk;
	try {
		ItemIterable<CmisObject> files = richiesteCMISService.getFilesRichiesta(allegatoParentBulk);
		if (files != null){
			for (CmisObject cmisObject : files) {
				if (richiesteCMISService.hasAspect(cmisObject, CMISAspect.SYS_ARCHIVED.value()))
					continue;
				if (excludeChild(cmisObject))
					continue;

				richiesteCMISService.recuperoAllegatiDettaglioRichiesta(allegatoParentBulk, cmisObject);

				if (cmisObject.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) {
					Document document = (Document) cmisObject;
					AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk) Introspector.newInstance(getAllegatoClass(), document);
					allegato.setContentType(document.getContentStreamMimeType());
					allegato.setNome(cmisObject.getName());
					allegato.setDescrizione((String)document.getPropertyValue(SiglaCMISService.PROPERTY_DESCRIPTION));
					allegato.setTitolo((String)document.getPropertyValue(SiglaCMISService.PROPERTY_TITLE));
					completeAllegato(allegato);
					allegato.setCrudStatus(OggettoBulk.NORMAL);
					allegatoParentBulk.addToArchivioAllegati(allegato);					
				}
			}
		}
	} catch (ApplicationException e) {
		throw handleException(e);
	} catch (ComponentException e) {
		throw handleException(e);
	} catch (NoSuchMethodException e) {
		throw handleException(e);
	} catch (IllegalAccessException e) {
		throw handleException(e);
	} catch (InstantiationException e) {
		throw handleException(e);
	} catch (InvocationTargetException e) {
		throw handleException(e); 
	}
	return oggettobulk;	
}
@Override
protected void completeAllegato(AllegatoRichiestaBulk allegato) throws ApplicationException {
	for (SecondaryType secondaryType : allegato.getDocument(richiesteCMISService).getSecondaryTypes()) {
		if (AllegatoRichiestaBulk.aspectNamesKeys.get(secondaryType.getId()) != null){
			allegato.setAspectName(secondaryType.getId());
			break;
		}
	}
	super.completeAllegato(allegato);
}

@Override
public String getAllegatiFormName() {
	super.getAllegatiFormName();
	return "allegatiRichiesta";
}
public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
	AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk)getCrudArchivioAllegati().getModel();
	Document node = allegato.getDocument(richiesteCMISService);
	InputStream is = richiesteCMISService.getResource(node);
	((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(node.getContentStreamLength()).intValue());
	((HttpActionContext)actioncontext).getResponse().setContentType(node.getContentStreamMimeType());
	OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
	((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
	byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
	int buflength;
	while ((buflength = is.read(buffer)) > 0) {
		os.write(buffer,0,buflength);
	}
	is.close();
	os.flush();
}
public String getNomeAllegatoDettaglio() throws ApplicationException{
	AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
	if (dettaglio!= null){
		return dettaglio.getNome();
	}
	return "";
}
public void scaricaDocumentoDettaglioCollegato(ActionContext actioncontext) throws Exception {
	AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
	if (dettaglio!= null){
		Document document = dettaglio.getDocument(richiesteCMISService);
		if (document != null){
			InputStream is = richiesteCMISService.getResource(document);
			((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(document.getContentStreamLength()).intValue());
			((HttpActionContext)actioncontext).getResponse().setContentType(document.getContentStreamMimeType());
			OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
			((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
			byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
			int buflength;
			while ((buflength = is.read(buffer)) > 0) {
				os.write(buffer,0,buflength);
			}
			is.close();
			os.flush();
		} else {
			throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
		}
	} else {
		throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
	}
}
@Override
protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
	return true;
}
protected Boolean isPossibileCancellazioneDettaglioAllegato(AllegatoGenericoBulk allegato) {
	return true;
}
public SimpleDetailCRUDController getDettaglioAllegatiController() {
	return dettaglioAllegatiController;
}
@Override
protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato){
	return true;
}
@Override
protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
	RichiestaUopBulk richiesta = (RichiestaUopBulk)allegatoParentBulk;
		super.gestioneCancellazioneAllegati(allegatoParentBulk);
}
public void gestionePostSalvataggio(it.cnr.jada.action.ActionContext context)
		throws	it.cnr.jada.action.BusinessProcessException {

	try {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel(); 
			((RichiestaUopComponentSession)createComponentSession()).gestioneStampaRichiesta(context.getUserContext(), richiesta);
	} catch(Exception e) {
		throw handleException(e);
	}
}
}
