package it.cnr.contab.docamm00.bp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.docamm00.docs.bulk.Consuntivo_rigaVBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.docamm00.docs.bulk.Voidable;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAllegatiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.SecondaryType;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

/**
 * Gestisce le catene di elementi correlate con la fattura passiva in uso.
 */
public abstract class CRUDFatturaPassivaBP extends AllegatiCRUDBP<AllegatoFatturaBulk, Fattura_passivaBulk> implements
		IDocumentoAmministrativoBP, IGenericSearchDocAmmBP, VoidableBP,
		IDefferedUpdateSaldiBP, FatturaPassivaElettronicaBP{

	private final SimpleDetailCRUDController crudRiferimentiBanca = new SimpleDetailCRUDController(
			"RifBanca", Fattura_passiva_rigaBulk.class, "riferimenti_bancari",
			this);
	private final SimpleDetailCRUDController consuntivoController = new SimpleDetailCRUDController(
			"Consuntivo", Consuntivo_rigaVBulk.class,
			"fattura_passiva_consuntivoColl", this);
	private final ObbligazioniCRUDController obbligazioniController = new ObbligazioniCRUDController(
			"Obbligazioni",
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class,
			"fattura_passiva_obbligazioniHash", this);
	private final SimpleDetailCRUDController dettaglioObbligazioneController;
	private final FatturaPassivaRigaIntrastatCRUDController dettaglioIntrastatController = new FatturaPassivaRigaIntrastatCRUDController(
			"Intrastat", Fattura_passiva_intraBulk.class,
			"fattura_passiva_intrastatColl", this);
	private final SimpleDetailCRUDController crudDocEleAllegatiColl = new SimpleDetailCRUDController(
			"RifDocEleAllegatiColl", DocumentoEleAllegatiBulk.class,
			"docEleAllegatiColl", this);

	protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
	private boolean isDeleting = false;
	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean annoDiCompetenza = true;
	private boolean annoSolareInScrivania = true;
	private boolean riportaAvantiIndietro = false;
	private boolean carryingThrough = false;
	private boolean ribaltato;
	private boolean isDetailDoubling = false;
	
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

		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", dettObbligazioniControllerClass,
				"fattura_passiva_obbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				Fattura_passivaBulk fattura = (Fattura_passivaBulk) CRUDFatturaPassivaBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (fattura != null) {
					java.util.Hashtable h = fattura
							.getFattura_passiva_obbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			public boolean isGrowable() {

				return super.isGrowable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}

			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};
	}

	/**
	 * CRUDAnagraficaBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public CRUDFatturaPassivaBP(String function,
			Class dettObbligazioniControllerClass)
			throws BusinessProcessException {
		super(function + "Tr");

		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", dettObbligazioniControllerClass,
				"fattura_passiva_obbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				Fattura_passivaBulk fattura = (Fattura_passivaBulk) CRUDFatturaPassivaBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (fattura != null) {
					java.util.Hashtable h = fattura
							.getFattura_passiva_obbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			public boolean isGrowable() {

				return super.isGrowable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}

			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};
	}

	protected void basicEdit(it.cnr.jada.action.ActionContext context,
			OggettoBulk bulk, boolean doInitializeForEdit)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			Fattura_passivaBulk fp = (Fattura_passivaBulk) bulk;
			setAnnoDiCompetenza(it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(context.getUserContext()).intValue() == fp
					.getEsercizio().intValue());
			super.basicEdit(context, bulk, doInitializeForEdit);
		} catch (Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}

	protected boolean basicRiportaButtonHidden() {

		Fattura_passivaBulk fp = (Fattura_passivaBulk) getModel();
		return isAnnoSolareInScrivania() || !isRiportaAvantiIndietro()
				|| isDeleting() || isModelVoided()
				|| (fp != null && (fp.isPagata() || fp.isCongelata()))
				|| !isEditing();
	}

	public void create(it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {

		try {
			getModel().setToBeCreated();
			setModel(context,
					((FatturaPassivaComponentSession) createComponentSession())
							.creaConBulk(context.getUserContext(), getModel(),
									getUserConfirm()));
		} catch (Exception e) {
			throw handleException(e);
		} finally {
			setUserConfirm(null);
		}
	}

	public OggettoBulk createEmptyModel(it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {

		setAnnoDiCompetenza(true);
		return super.createEmptyModel(context);
	}

	public void delete(ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try {
			getModel().setToBeDeleted();
			createComponentSession().eliminaConBulk(context.getUserContext(),
					getModel());
		} catch (Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

	public void edit(it.cnr.jada.action.ActionContext context,
			OggettoBulk bulk, boolean doInitializeForEdit)
			throws it.cnr.jada.action.BusinessProcessException {

		setCarryingThrough(false);
		super.edit(context, bulk, doInitializeForEdit);
	}

	/**
	 * Effettua una operazione di ricerca per un attributo di un modello.
	 *
	 * @param userContext
	 *            lo userContext che ha generato la richiesta
	 * @param filtro
	 *            modello che fa da contesto alla ricerca (il modello del
	 *            FormController padre del controller che ha scatenato la
	 *            ricerca)
	 * @return un RemoteIterator sul risultato della ricerca o null se la
	 *         ricerca non ha ottenuto nessun risultato
	 *
	 **/
	public it.cnr.jada.util.RemoteIterator findObbligazioni(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession fpcs = (it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession) createComponentSession();
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
	 * @param actionContext
	 *            contesto dell'azione in corso
	 * @param clauses
	 *            Albero di clausole da utilizzare per la ricerca
	 * @param bulk
	 *            prototipo del modello di cui si effettua la ricerca
	 * @param context
	 *            modello che fa da contesto alla ricerca (il modello del
	 *            FormController padre del controller che ha scatenato la
	 *            ricerca)
	 * @return un RemoteIterator sul risultato della ricerca o null se la
	 *         ricerca non ha ottenuto nessun risultato
	 *
	 **/
	public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(
			it.cnr.jada.action.ActionContext actionContext,
			it.cnr.jada.persistency.sql.CompoundFindClause clauses,
			it.cnr.jada.bulk.OggettoBulk bulk,
			it.cnr.jada.bulk.OggettoBulk context, java.lang.String property)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession fpcs = (it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession) createComponentSession();
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
					actionContext, fpcs.cerca(actionContext.getUserContext(),
							clauses, bulk, context, property));
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	/**
	 * Insert the method's description here. Creation date: (10/18/2001 12:50:31
	 * PM)
	 * 
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
	 * Insert the method's description here. Creation date: (9/12/2001 10:35:52
	 * AM)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final SimpleDetailCRUDController getConsuntivoController() {
		return consuntivoController;
	}

	/**
	 * Insert the method's description here. Creation date: (09/07/2001
	 * 14:55:11)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final SimpleDetailCRUDController getCrudRiferimentiBanca() {
		return crudRiferimentiBanca;
	}

	public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {

		if (isDeleting() && getParent() != null)
			return getDefferedUpdateSaldiParentBP()
					.getDefferedUpdateSaldiBulk();
		return (IDefferUpdateSaldi) getDocumentoAmministrativoCorrente();
	}

	public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

		if (isDeleting() && getParent() != null)
			return ((IDefferedUpdateSaldiBP) getParent())
					.getDefferedUpdateSaldiParentBP();
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
	 * Insert the method's description here. Creation date: (09/07/2001
	 * 14:55:11)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public abstract FatturaPassivaRigaCRUDController getDettaglio();

	/**
	 * Insert the method's description here. Creation date: (4/3/2002 11:11:03
	 * AM)
	 * 
	 * @return 
	 *         it.cnr.contab.docamm00.bp.FatturaPassivaRigaIntrastatCRUDController
	 */
	public final FatturaPassivaRigaIntrastatCRUDController getDettaglioIntrastatController() {
		return dettaglioIntrastatController;
	}

	/**
	 * Insert the method's description here. Creation date: (10/18/2001 12:50:31
	 * PM)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglioObbligazioneController() {
		return dettaglioObbligazioneController;
	}

	/**
	 * Insert the method's description here. Creation date: (9/12/2001 10:35:52
	 * AM)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {

		return (IDocumentoAmministrativoBulk) getModel();
	}

	/**
	 * Insert the method's description here. Creation date: (10/18/2001 12:50:31
	 * PM)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
		if (getObbligazioniController() == null)
			return null;
		return (Obbligazione_scadenzarioBulk) getObbligazioniController()
				.getModel();
	}

	/**
	 * Insert the method's description here. Creation date: (10/18/2001 12:50:31
	 * PM)
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getObbligazioniController() {
		return obbligazioniController;
	}

	public String getPropertyForGenericSearch() {

		return "fornitore";
	}

	/**
	 * Insert the method's description here. Creation date: (5/29/2002 12:59:29
	 * PM)
	 * 
	 * @return it.cnr.contab.doccont00.core.bulk.OptionRequestParameter
	 */
	public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
		return userConfirm;
	}

	/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context
	 *            <code>ActionContext</code>
	 */

	protected void init(it.cnr.jada.action.Config config,
			it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {

		super.init(config, context);

		try {
			int solaris = Fattura_passivaBulk.getDateCalendar(
					it.cnr.jada.util.ejb.EJBCommonServices.getServerDate())
					.get(java.util.Calendar.YEAR);
			int esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(context.getUserContext()).intValue();
			setAnnoSolareInScrivania(solaris == esercizioScrivania);
			setRibaltato(initRibaltato(context));
			if (!isAnnoSolareInScrivania()) {
				String cds = it.cnr.contab.utenze00.bp.CNRUserContext
						.getCd_cds(context.getUserContext());
				try {
					FatturaPassivaComponentSession session = (FatturaPassivaComponentSession) createComponentSession();
					boolean esercizioScrivaniaAperto = session
							.verificaStatoEsercizio(context.getUserContext(),
									new EsercizioBulk(cds, new Integer(
											esercizioScrivania)));
					boolean esercizioSuccessivoAperto = session
							.verificaStatoEsercizio(context.getUserContext(),
									new EsercizioBulk(cds, new Integer(
											esercizioScrivania + 1)));
					setRiportaAvantiIndietro(esercizioScrivaniaAperto
							&& esercizioSuccessivoAperto && isRibaltato());
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

	public OggettoBulk initializeModelForEdit(ActionContext context,
			OggettoBulk bulk) throws BusinessProcessException {

		try {
			if (bulk != null) {
				Fattura_passivaBulk fp = (Fattura_passivaBulk) bulk;
				fp.setDettagliCancellati(new java.util.Vector());
				fp.setDocumentiContabiliCancellati(new java.util.Vector());
			}
			FatturaPassivaComponentSession h = (FatturaPassivaComponentSession) createComponentSession();
			if (isEditing()) {
				if (h.hasFatturaPassivaARowNotInventoried(
						context.getUserContext(),
						(Fattura_passivaBulk) getModel())) {
					setErrorMessage("Attenzione: sebbene il salvataggio sia stato effettuato correttamente, si ricorda che alcuni beni devono ancora essere inventariati!");
				} else if (getDettaglio().isInventoriedChildDeleted()) {
					getDettaglio().setInventoriedChildDeleted(false);
					setErrorMessage("Attenzione: sebbene il salvataggio sia stato effettuato correttamente, si ricorda che sono stati eliminati beni inventariati. Provvedere all'aggiornamento dell'inventario!");
				}
			}
			return super.initializeModelForEdit(context, bulk);
		} catch (Throwable e) {
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
	 * Insert the method's description here. Creation date: (06/06/2003
	 * 15.53.41)
	 * 
	 * @return boolean
	 */
	public boolean isAnnoDiCompetenza() {
		return annoDiCompetenza;
	}

	/**
	 * Insert the method's description here. Creation date: (24/06/2003
	 * 17.06.48)
	 * 
	 * @return boolean
	 */
	public boolean isAnnoSolareInScrivania() {
		return annoSolareInScrivania;
	}

	public boolean isAssociaInventarioButtonEnabled() {

		return (isEditing() || isInserting()) && getModel() != null
				&& !getDettaglio().getDetails().isEmpty()
				&& !((Fattura_passivaBulk) getModel()).isGenerataDaCompenso()
				&& (isAnnoDiCompetenza());
	}

	public boolean isAssociaInventarioButtonHidden() {

		return isSearching() || isDeleting();
	}

	public boolean isFatturaElettronicaButtonEnabled() {
		return (isEditing() || isInserting()) && getModel() != null
				&& ((Fattura_passivaBulk) getModel()).getDocumentoEleTestata() != null;
	}

	public abstract boolean isAutoGenerated();

	public boolean isBringbackButtonEnabled() {
		return super.isBringbackButtonEnabled() || isDeleting();
	}

	public boolean isBringbackButtonHidden() {
		return super.isBringbackButtonHidden() || !isDeleting();
	}

	/**
	 * Insert the method's description here. Creation date: (25/06/2003 9.38.09)
	 * 
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
				!fp.isElettronica() &&
				!fp.isGenerataDaCompenso() &&
				((isAnnoDiCompetenza() && !fp.isRiportata()) ||
					// Gennaro Borriello - (02/11/2004 16.48.21)
					// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
					(!isAnnoDiCompetenza() && fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania())));
	}

	/**
	 * Insert the method's description here. Creation date: (2/7/2002 2:40:22
	 * PM)
	 * 
	 * @return boolean
	 */
	public boolean isDeleting() {
		return isDeleting;
	}
	public boolean isInputReadonly() {
		Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
		if (Optional.ofNullable(getTab("tab")).filter(x -> x.equals("tabAllegati")).isPresent())
			return false;		
		return super.isInputReadonly() || isDeleting() || isModelVoided()|| (!isAnnoDiCompetenza() && isEditing()) ||
				     (fp != null && ((fp.isPagata() || 
					 ((isAnnoDiCompetenza() && fp.isRiportata())) ||
					 fp.isCongelata()) && !isSearching())) ||
					 fp.getCompenso() != null /*&& !isInserting()*/;
	}

	public boolean isInputReadonlyDoc1210() {
		return super.isInputReadonly();
	}
	
	public boolean isInventariaButtonEnabled() {

		return (isEditing() || isInserting()) && getModel() != null
				&& !getDettaglio().getDetails().isEmpty()
				&& !((Fattura_passivaBulk) getModel()).isGenerataDaCompenso()
				&& (isAnnoDiCompetenza());
	}

	public boolean isInventariaButtonHidden() {

		return isSearching() || isDeleting();
	}

	public boolean isInventariaPerAumentoButtonEnabled() {

		return (isEditing() || isInserting()) && getModel() != null
				&& !getDettaglio().getDetails().isEmpty()
				&& !((Fattura_passivaBulk) getModel()).isGenerataDaCompenso()
				&& (isAnnoDiCompetenza());
	}

	public boolean isInventariaPerAumentoButtonHidden() {

		return isSearching() || isDeleting();
	}

	public boolean isBeni_collButtonEnabled() {
		Fattura_passivaBulk fattura = (Fattura_passivaBulk) getModel();
		if (fattura.getHa_beniColl() == null)
			return false;
		else
			return /* isInserting() && */
			getModel() != null
					&& !getDettaglio().getDetails().isEmpty()
					&& !((Fattura_passivaBulk) getModel())
							.isGenerataDaCompenso()
					&& (fattura.getHa_beniColl().booleanValue());
	}

	public boolean isBeni_collButtonHidden() {
		return isSearching() || isDeleting();
	}

	public boolean isManualModify() {
		return !((Fattura_passivaBulk) getModel()).isCongelata();
	}

	/**
	 * Insert the method's description here. Creation date: (04/06/2001
	 * 11:45:16)
	 * 
	 * @return boolean
	 */
	public boolean isModelVoided() {
		return !isSearching() && getModel() != null
				&& ((Voidable) getModel()).isAnnullato();
	}

	public boolean isNewButtonEnabled() {
		Fattura_passivaBulk fp = (Fattura_passivaBulk) getModel();
		return super.isNewButtonEnabled()
				&& ((fp != null
						&& fp.getEsercizio() != null
						&& Fattura_passivaBulk.getDateCalendar(null).get(
								java.util.Calendar.YEAR) <= fp.getEsercizio()
								.intValue() && isAnnoDiCompetenza()) || !isAnnoDiCompetenza());
	}

	public boolean isRiportaAvantiButtonEnabled() {

		Fattura_passivaBulk fp = (Fattura_passivaBulk) getModel();
		return isCarryingThrough() || !fp.isRiportata();
	}

	public boolean isRiportaAvantiButtonHidden() {

		return basicRiportaButtonHidden();
	}

	/**
	 * Insert the method's description here. Creation date: (24/06/2003
	 * 17.28.47)
	 * 
	 * @return boolean
	 */
	public boolean isRiportaAvantiIndietro() {
		return riportaAvantiIndietro;
	}

	public boolean isRiportaIndietroButtonEnabled() {

		Fattura_passivaBulk fp = (Fattura_passivaBulk) getModel();
		return isEditing() && !isDeleting() && !isModelVoided() && !isDirty()
				&& !fp.isPagata() && !isCarryingThrough();
	}

	public boolean isRiportaIndietroButtonHidden() {

		Fattura_passivaBulk fp = (Fattura_passivaBulk) getModel();
		return basicRiportaButtonHidden()
				|| (fp != null && !(fp.isRiportata() || isCarryingThrough()));
	}

	public boolean isSaveButtonEnabled() {
	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	if (Optional.ofNullable(getTab("tab")).filter(x -> x.equals("tabAllegati")).isPresent())
		return true;	
	return super.isSaveButtonEnabled() && 
			!isModelVoided() &&
			//commentato per consentire modifiche fatt elettr
			//!fp.isGenerataDaCompenso() &&
			/* RP per consentire salvataggio delle associazioni con l'inventario
			 * tutti i dati risultano comunque non aggiornabili
			 !fp.isPagata() && */
			!fp.isCongelata() &&
				// Gennaro Borriello - (02/11/2004 16.48.21)
				// Fix sul controllo dello "Stato Riportato":
				((!fp.isRiportata() && isAnnoDiCompetenza())
						|| isCarryingThrough() || isDetailDoubleable() || (!isAnnoDiCompetenza() && fp.COMPLETAMENTE_RIPORTATO
						.equals(fp.getRiportataInScrivania())));
	}

	/**
	 * Abilito il bottone di cancellazione documento solo se non ho scadenze in
	 * fase di modifica/inserimento
	 */

	public boolean isUndoBringBackButtonEnabled() {

		return super.isUndoBringBackButtonEnabled() || isDeleting()
				|| isViewing();
	}

	/**
	 * Abilito il bottone di cancellazione documento solo se non ho scadenze in
	 * fase di modifica/inserimento
	 */

	public boolean isUndoBringBackButtonHidden() {

		return super.isUndoBringBackButtonHidden() || !isDeleting();
	}

	/**
	 * Attiva oltre al normale reset il metodo di set dei tab di default.
	 *
	 * @param context
	 *            <code>ActionContext</code>
	 *
	 * @see resetTabs
	 */

	public void reset(ActionContext context) throws BusinessProcessException {

		if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(
				context.getUserContext()).intValue() != Fattura_passivaBulk
				.getDateCalendar(null).get(java.util.Calendar.YEAR))
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
	 * @param context
	 *            <code>ActionContext</code>
	 *
	 * @see resetTabs
	 */

	public void resetForSearch(ActionContext context)
			throws BusinessProcessException {
		setCarryingThrough(false);
		super.resetForSearch(context);
		resetTabs();
	}

	/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context
	 *            <code>ActionContext</code>
	 */

	public void resetTabs() {
		setTab("tab", "tabFatturaPassiva");
	}

	public void riportaAvanti(ActionContext context)
			throws ValidationException, BusinessProcessException {

		try {
			FatturaPassivaComponentSession session = (FatturaPassivaComponentSession) createComponentSession();
			Fattura_passivaBulk fpCarried = (Fattura_passivaBulk) session
					.riportaAvanti(context.getUserContext(),
							(IDocumentoAmministrativoBulk) getModel(),
							getUserConfirm());
			setModel(context, fpCarried);
		} catch (Exception e) {
			throw handleException(e);
		} finally {
			setUserConfirm(null);
		}
	}

	public void riportaIndietro(ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {

		if (isDirty()) {
			setMessage("Il documento � stato modificato! Operazione annullata.");
			return;
		}

		rollbackUserTransaction();
		setCarryingThrough(true);

		try {
			FatturaPassivaComponentSession session = (FatturaPassivaComponentSession) createComponentSession();
			Fattura_passivaBulk fpCarried = (Fattura_passivaBulk) session
					.riportaIndietro(context.getUserContext(),
							(IDocumentoAmministrativoBulk) getModel());
			basicEdit(context, fpCarried, true);
			setDirty(true);

		} catch (Throwable t) {
			setCarryingThrough(false);
			rollbackUserTransaction();
			throw handleException(t);
		}
	}

	/**
	 * Annulla le modifiche apportate al compenso e ritorna al savepoint
	 * impostato in precedenza
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Rollback to savePoint Pre: Una richiesta di annullare tutte le
	 * modifiche apportate e di ritornare al savepoint e' stata generata Post:
	 * Tutte le modifiche effettuate sul compenso vengono annullate, mentre
	 * rimangono valide le modifiche apportate al doc. amministrativo che ha
	 * aperto il compenso
	 * 
	 * @param context
	 *            il Context che ha generato la richiesta
	 * @param savePointName
	 *            il nome del savePoint
	 *
	 */
	public void rollbackToSavePoint(ActionContext context, String savePointName)
			throws BusinessProcessException {

		try {

			FatturaPassivaComponentSession sess = (FatturaPassivaComponentSession) createComponentSession();
			sess.rollbackToSavePoint(context.getUserContext(), savePointName);

		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		}
	}

	public void salvaRiportandoAvanti(ActionContext context)
			throws ValidationException, BusinessProcessException {

		Fattura_passivaBulk fpClone = (Fattura_passivaBulk) getModel();
		try {
			setSavePoint(context, IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
			completeSearchTools(context, this);
			validate(context);
			saveChildren(context);

			update(context);
			riportaAvanti(context);
		} catch (BusinessProcessException e) {
			rollbackToSavePoint(context,
					IDocumentoAmministrativoBulk.RIPORTA_AVANTI);
			// Il setModel � necessario perch� update setta il modello. se
			// riportaAvanti fallisce il pg_ver_rec
			// rimane disallineato.
			setModel(context, fpClone);
			throw handleException(e);
		}

		if (getMessage() == null)
			setMessage("Salvataggio e riporto all'esercizio successivo eseguito in modo corretto.");

		commitUserTransaction();
		setCarryingThrough(false);

		try {
			basicEdit(context, getModel(), true);
		} catch (BusinessProcessException e) {
			setModel(context, null);
			setDirty(false);
			throw e;
		}
	}

	public void save(ActionContext context) throws ValidationException,
			BusinessProcessException {

		super.save(context);
		setCarryingThrough(false);
	}

	/**
	 * Insert the method's description here. Creation date: (06/06/2003
	 * 15.53.41)
	 * 
	 * @param newIsAnnoDiCompetenza
	 *            boolean
	 */
	public void setAnnoDiCompetenza(boolean newAnnoDiCompetenza) {
		annoDiCompetenza = newAnnoDiCompetenza;
	}

	/**
	 * Insert the method's description here. Creation date: (24/06/2003
	 * 17.06.48)
	 * 
	 * @param newAnnoSolareInScrivania
	 *            boolean
	 */
	public void setAnnoSolareInScrivania(boolean newAnnoSolareInScrivania) {
		annoSolareInScrivania = newAnnoSolareInScrivania;
	}

	/**
	 * Insert the method's description here. Creation date: (25/06/2003 9.38.09)
	 * 
	 * @param newCarryingThrough
	 *            boolean
	 */
	public void setCarryingThrough(boolean newCarryingThrough) {
		carryingThrough = newCarryingThrough;
	}

	/**
	 * Insert the method's description here. Creation date: (2/7/2002 2:40:22
	 * PM)
	 * 
	 * @param newIsDeleting
	 *            boolean
	 */
	public void setIsDeleting(boolean newIsDeleting) {
		isDeleting = newIsDeleting;
	}

	/**
	 * Insert the method's description here. Creation date: (24/06/2003
	 * 17.28.47)
	 * 
	 * @param newRiportaAvantiIndietro
	 *            boolean
	 */
	public void setRiportaAvantiIndietro(boolean newRiportaAvantiIndietro) {
		riportaAvantiIndietro = newRiportaAvantiIndietro;
	}

	/**
	 * Imposta un savepoint che consente di salvare le modifiche apportate al
	 * doc. amministrativo fino a quel momento in modo che se gli aggiornamenti
	 * apportati al compenso non venissero confermati (rollback), comunque non
	 * verrebbero persi anche quelli del documento amministrativo.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Imposta savePoint Pre: Una richiesta di impostare un savepoint e'
	 * stata generata Post: Un savepoint e' stato impostato in modo che le
	 * modifiche apportate al doc. amministrativo vengono consolidate
	 *
	 * @param context
	 *            il Context che ha generato la richiesta
	 * @param savePointName
	 *            il nome del savePoint
	 *
	 */
	public void setSavePoint(ActionContext context, String savePointName)
			throws BusinessProcessException {

		try {

			FatturaPassivaComponentSession sess = (FatturaPassivaComponentSession) createComponentSession();
			sess.setSavePoint(context.getUserContext(), savePointName);

		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		}
	}

	/**
	 * Insert the method's description here. Creation date: (5/29/2002 12:59:29
	 * PM)
	 * 
	 * @param newUserConfirm
	 *            it.cnr.contab.doccont00.core.bulk.OptionRequestParameter
	 */
	public void setUserConfirm(
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter newUserConfirm) {
		userConfirm = newUserConfirm;
	}

	public void update(ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {

		try {
			archiviaAllegati(context, null);			
			getModel().setToBeUpdated();
			setModel(context,
					((FatturaPassivaComponentSession) createComponentSession())
							.modificaConBulk(context.getUserContext(),
									getModel(), getUserConfirm()));
		} catch (Exception e) {
			throw handleException(e);
		} finally {
			setUserConfirm(null);
		}
	}

	/**
	 * Validazione dell'obbligazione in fase di creazione di una nuova
	 * obbligazione o modifica di una gi� creata. Il metodo viene chiamato sul
	 * riporta dell'Obbligazione in modo da validare istantaneamente l'oggetto
	 * creato. Chi non ne ha bisogno lo lasci vuoto.
	 *
	 **/
	public void validaObbligazionePerDocAmm(ActionContext context,
			it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {
		return;
	}

	protected void writeToolbar(javax.servlet.jsp.JspWriter writer,
			it.cnr.jada.util.jsp.Button[] buttons) throws java.io.IOException,
			javax.servlet.ServletException {

		it.cnr.jada.util.jsp.Button riportaAvantiButton = buttons[buttons.length - 1];
		riportaAvantiButton.setSeparator(isRiportaIndietroButtonHidden()
				&& !isRiportaAvantiButtonHidden());
		super.writeToolbar(writer, buttons);
	}

	public boolean initRibaltato(it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			return (((RicercaDocContComponentSession) createComponentSession(
					"CNRCHIUSURA00_EJB_RicercaDocContComponentSession",
					RicercaDocContComponentSession.class)).isRibaltato(context
					.getUserContext()));
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public boolean isRibaltato() {
		return ribaltato;
	}

	public void setRibaltato(boolean b) {
		ribaltato = b;
	}

	public boolean isDetailDoubling() {
		return isDetailDoubling;
	}

	public void setDetailDoubling(boolean isDetailDoubling) {
		this.isDetailDoubling = isDetailDoubling;
	}

	private Fattura_passiva_rigaIBulk copyByRigaDocumento(
			ActionContext context, Fattura_passiva_rigaIBulk nuovoDettaglio,
			Fattura_passiva_rigaIBulk origine) {
		// associo la riga creata al documento
		origine.setUser(context.getUserContext().getUser());
		if (origine.getVoce_iva() != null)
			nuovoDettaglio.setVoce_iva((Voce_ivaBulk) origine.getVoce_iva()
					.clone());
		// nuovoDettaglio.setEsercizio( origine.getEsercizio());
		// nuovoDettaglio.setCd_cds( origine.getCd_cds());
		// nuovoDettaglio.setCd_unita_organizzativa(
		// origine.getCd_unita_organizzativa());
		nuovoDettaglio.setStato_cofi(origine.getStato_cofi());
		nuovoDettaglio.setDt_da_competenza_coge(origine
				.getDt_da_competenza_coge());
		nuovoDettaglio.setDt_a_competenza_coge(origine
				.getDt_a_competenza_coge());
		nuovoDettaglio.setDs_riga_fattura(origine.getDs_riga_fattura());
		nuovoDettaglio.setTi_associato_manrev(origine.getTi_associato_manrev());
		nuovoDettaglio.setBene_servizio(origine.getBene_servizio());
		nuovoDettaglio.setInventariato(origine.isInventariato());
		nuovoDettaglio.setTrovato(origine.getTrovato());
		nuovoDettaglio.setToBeCreated();
		return nuovoDettaglio;
	}

	public void sdoppiaDettaglioInAutomatico(ActionContext context)
			throws ValidationException, BusinessProcessException {
		try {
			it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = it.cnr.contab.doccont00.bp.CRUDVirtualObbligazioneBP
					.getVirtualComponentSession(context, true);
			FatturaPassivaComponentSession session = (FatturaPassivaComponentSession) createComponentSession();
			Fattura_passivaBulk documento = (Fattura_passivaBulk) getModel();
			Fattura_passiva_rigaIBulk dettaglioSelezionato = (Fattura_passiva_rigaIBulk) getDettaglio()
					.getModel();
			Obbligazione_scadenzarioBulk scadenzaNuova = null;

			if (dettaglioSelezionato == null)
				return;
			if (documento.getStato_cofi() != null
					&& documento.getStato_cofi().equals(documento.STATO_PAGATO))
				setMessage("Non � possibile sdoppiare righe in un documento pagato.");
			if (dettaglioSelezionato.getIm_riga_sdoppia() == null
					|| dettaglioSelezionato.getIm_riga_sdoppia().equals(
							Utility.ZERO)
					|| dettaglioSelezionato.getIm_riga_sdoppia().compareTo(
							dettaglioSelezionato.getSaldo()) != -1) {
				setMessage("Il nuovo importo della riga da sdoppiare deve essere positivo ed inferiore "
						+ "al saldo originario.");
				return;
			}

			Obbligazione_scadenzarioBulk scadenzaVecchia = dettaglioSelezionato
					.getObbligazione_scadenziario();

			BigDecimal newImportoRigaVecchia = dettaglioSelezionato
					.getIm_riga_sdoppia().add(
							dettaglioSelezionato.getIm_diponibile_nc()
									.subtract(dettaglioSelezionato.getSaldo()));
			BigDecimal newImportoRigaNuova = dettaglioSelezionato.getSaldo()
					.subtract(dettaglioSelezionato.getIm_riga_sdoppia());

			BigDecimal newPrezzoRigaVecchia = newImportoRigaVecchia.divide(
					documento.getCambio(), 2, BigDecimal.ROUND_HALF_UP).divide(
					dettaglioSelezionato.getQuantita().multiply(
							dettaglioSelezionato.getVoce_iva().getPercentuale()
									.divide(new BigDecimal(100))
									.add(new java.math.BigDecimal(1))), 2,
					BigDecimal.ROUND_HALF_UP);
			BigDecimal newPrezzoRigaNuova = dettaglioSelezionato
					.getPrezzo_unitario().subtract(newPrezzoRigaVecchia);
			BigDecimal oldImportoIvaVecchia = BigDecimal.ZERO;
			BigDecimal tot_imp = BigDecimal.ZERO;
		
		if(dettaglioSelezionato.getVoce_iva().getFl_autofattura()||documento.quadraturaInDeroga()){
				oldImportoIvaVecchia = dettaglioSelezionato.getIm_iva();
				tot_imp = newPrezzoRigaVecchia.multiply(documento.getCambio())
						.multiply(dettaglioSelezionato.getQuantita())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else
				tot_imp = dettaglioSelezionato.getIm_riga_sdoppia();
			if (dettaglioSelezionato.getObbligazione_scadenziario() != null) {
				scadenzaNuova = (Obbligazione_scadenzarioBulk) h
						.sdoppiaScadenzaInAutomatico(
								context.getUserContext(),
								scadenzaVecchia,
								scadenzaVecchia
										.getIm_scadenza()
										.subtract(
												dettaglioSelezionato
														.getSaldo()
														.subtract(
																oldImportoIvaVecchia))
										.add(tot_imp));

				// ricarico obbligazione e recupero i riferimenti alle scadenze
				ObbligazioneBulk obbligazione = (ObbligazioneBulk) h
						.inizializzaBulkPerModifica(context.getUserContext(),
								scadenzaNuova.getObbligazione());

				if (!obbligazione.getObbligazione_scadenzarioColl()
						.containsByPrimaryKey(scadenzaVecchia)
						|| !obbligazione.getObbligazione_scadenzarioColl()
								.containsByPrimaryKey(scadenzaNuova))
					throw new ValidationException(
							"Errore nello sdoppiamento della scadenza dell'impegno.");

				scadenzaVecchia = (Obbligazione_scadenzarioBulk) obbligazione
						.getObbligazione_scadenzarioColl().get(
								obbligazione.getObbligazione_scadenzarioColl()
										.indexOfByPrimaryKey(scadenzaVecchia));
				scadenzaNuova = (Obbligazione_scadenzarioBulk) obbligazione
						.getObbligazione_scadenzarioColl().get(
								obbligazione.getObbligazione_scadenzarioColl()
										.indexOfByPrimaryKey(scadenzaNuova));
			}

			// creo la nuova riga di dettaglio e la associo al documento
			Fattura_passiva_rigaIBulk nuovoDettaglio = new Fattura_passiva_rigaIBulk();

			getDettaglio().addDetail(nuovoDettaglio);

			nuovoDettaglio = copyByRigaDocumento(context, nuovoDettaglio,
					dettaglioSelezionato);
			nuovoDettaglio.setQuantita(dettaglioSelezionato.getQuantita());
			nuovoDettaglio.setPrezzo_unitario(newPrezzoRigaNuova);

			nuovoDettaglio.calcolaCampiDiRiga();
			// setto im_diponibile prime per la verifica e dopo
			nuovoDettaglio.setIm_diponibile_nc(nuovoDettaglio.getSaldo());
			if (nuovoDettaglio.getIm_diponibile_nc().compareTo(
					newImportoRigaNuova) != 0) {
				nuovoDettaglio.setIm_iva(nuovoDettaglio.getIm_iva().add(
						newImportoRigaNuova.subtract(nuovoDettaglio
								.getIm_diponibile_nc())));
				nuovoDettaglio.setIm_totale_divisa(newImportoRigaNuova
						.subtract(nuovoDettaglio.getIm_iva()));
				nuovoDettaglio.setFl_iva_forzata(Boolean.TRUE);
				nuovoDettaglio.calcolaCampiDiRiga();
			}
			nuovoDettaglio.setIm_diponibile_nc(nuovoDettaglio.getSaldo());

			// Aggiorno la vecchia riga di dettaglio ed in particolare l'importo
			// della riga da sdoppiare
			// del doc amministrativo
			BigDecimal oldImpTotaleDivisa = dettaglioSelezionato
					.getIm_totale_divisa();

			dettaglioSelezionato.setPrezzo_unitario(newPrezzoRigaVecchia);
			dettaglioSelezionato.calcolaCampiDiRiga();
			// setto im_diponibile prime per la verifica e dopo
			dettaglioSelezionato.setIm_diponibile_nc(dettaglioSelezionato
					.getSaldo());
			if (dettaglioSelezionato.getIm_diponibile_nc().compareTo(
					newImportoRigaVecchia) != 0) {
				dettaglioSelezionato.setIm_iva(dettaglioSelezionato.getIm_iva()
						.add(newImportoRigaVecchia
								.subtract(dettaglioSelezionato
										.getIm_diponibile_nc())));
				dettaglioSelezionato.setIm_totale_divisa(newImportoRigaVecchia
						.subtract(dettaglioSelezionato.getIm_iva()));
				dettaglioSelezionato.setFl_iva_forzata(Boolean.TRUE);
				dettaglioSelezionato.calcolaCampiDiRiga();
			}

			dettaglioSelezionato.setIm_diponibile_nc(dettaglioSelezionato
					.getSaldo());

			dettaglioSelezionato.setToBeUpdated();

			if (scadenzaVecchia != null) {
				for (Iterator i = documento.getFattura_passiva_dettColl()
						.iterator(); i.hasNext();) {
					Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) i
							.next();
					if (riga.getObbligazione_scadenziario() != null
							&& riga.getObbligazione_scadenziario()
									.equalsByPrimaryKey(scadenzaVecchia)) {
						riga.setObbligazione_scadenziario(scadenzaVecchia);
						documento.addToDefferredSaldi(scadenzaVecchia
								.getObbligazione(), scadenzaVecchia
								.getObbligazione().getSaldiInfo());
					}
				}
			}
			if (scadenzaNuova != null) {
				BulkList selectedModels = new BulkList();
				selectedModels.add(nuovoDettaglio);
				documento = session.contabilizzaDettagliSelezionati(
						context.getUserContext(), documento, selectedModels,
						scadenzaNuova);
				documento.addToFattura_passiva_obbligazioniHash(scadenzaNuova,
						nuovoDettaglio);
				documento.addToDefferredSaldi(scadenzaNuova.getObbligazione(),
						scadenzaNuova.getObbligazione().getSaldiInfo());
				// Sdoppia associazione inventario in automatico
				if (nuovoDettaglio.isInventariato()) {

					// r.p. Prendo il progressivo dalla fattura_passivaBulk
					// perch� viene aggiornato
					BuonoCaricoScaricoComponentSession r = (it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
							.createEJB(
									"CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
									it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession.class);
					Ass_inv_bene_fatturaBulk newAss = r.sdoppiaAssociazioneFor(
							context.getUserContext(),
							(Fattura_passiva_rigaBulk) dettaglioSelezionato,
							(Fattura_passiva_rigaBulk) nuovoDettaglio);
					documento.addToAssociazioniInventarioHash(newAss,
							nuovoDettaglio);
				}
			}

			documento = (Fattura_passivaBulk) session.rebuildDocumento(
					context.getUserContext(), documento);

			getObbligazioniController().getSelection().clear();
			getObbligazioniController().setModelIndex(context, -1);
			getObbligazioniController().setModelIndex(
					context,
					it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(
							getObbligazioniController().getDetails(),
							dettaglioSelezionato));

			documento.setDetailDoubled(true);

			setModel(context, documento);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Boolean individua le condizioni per cui � possibile sdoppiare i dettagli
	 * del documento
	 * 
	 * false: - se annullato - se eliminato - se interamente incassato - se,
	 * indipendentemente dall'anno, � stata riportata all'esercizio successivo -
	 * se non di anno corrente e non riportata all'esercizio successivo
	 *
	 * @return Returns the isDetailDoubleable.
	 */
	public boolean isDetailDoubleable() {
		if (getModel() instanceof Fattura_passiva_IBulk) {
			Fattura_passiva_IBulk fattura = (Fattura_passiva_IBulk) getModel();
			return (!super.isInputReadonly()
					&& !isModelVoided()
					&& !isDeleting()
					&& !(fattura != null && fattura.getStato_cofi() != null && fattura
							.isPagata()) && !(fattura.isRiportata())
			// !(!isAnnoDiCompetenza() && !fattura.isRiportataInScrivania()) &&
			// !(fattura.getTipo_documento()!=null &&
			// !fattura.getTipo_documento().getFl_utilizzo_doc_generico().booleanValue())
			)
					&& !this.isSearching()
					&& !fattura.hasAddebiti()
					&& !fattura.hasStorni();
		}
		return false;
	}

	public void ricercaDatiTrovato(ActionContext context) throws Exception {
		FatturaPassivaComponentSession h;
		Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk) getDettaglio()
				.getModel();
		try {
			h = (FatturaPassivaComponentSession) createComponentSession();
			TrovatoBulk trovato = h.ricercaDatiTrovatoValido(
					context.getUserContext(), riga.getPg_trovato());
			riga.setTrovato(trovato);
		} catch (java.rmi.RemoteException e) {
			riga.setTrovato(new TrovatoBulk());
			handleException(e);
		} catch (BusinessProcessException e) {
			riga.setTrovato(new TrovatoBulk());
			handleException(e);
		} catch (Exception e) {
			riga.setTrovato(new TrovatoBulk());
			throw e;
		}
	}

	private java.sql.Timestamp getDataInizioObbligoRegistroUnico(
			it.cnr.jada.action.ActionContext context)
			throws BusinessProcessException {
		try {
			return Utility.createConfigurazioneCnrComponentSession().getDt01(
					context.getUserContext(), new Integer(0), null,
					"REGISTRO_UNICO_FATPAS", "DATA_INIZIO");
		} catch (Exception e) {
			throw handleException(e);
		}
	}

/* spostata nel component perch� chiamata nell'inizializza
private java.sql.Timestamp getDataInizioFatturazioneElettronica(it.cnr.jada.action.ActionContext context) throws BusinessProcessException {
	try{
	return  Utility.createConfigurazioneCnrComponentSession().getDt01(context.getUserContext(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()), null, Configurazione_cnrBulk.PK_FATTURAZIONE_ELETTRONICA, Configurazione_cnrBulk.SK_PASSIVA);
	} catch(Exception e) {
		throw handleException(e);
	}
}
*/
@Override
public void validate(ActionContext actioncontext)
		throws ValidationException {

	
	Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
	try {
		fp.setDataInizioObbligoRegistroUnico(getDataInizioObbligoRegistroUnico(actioncontext));
		//fp.setDataInizioFatturaElettronica(getDataInizioFatturazioneElettronica(actioncontext));
	} catch (BusinessProcessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	fp.validate();
	super.validate(actioncontext);
}

public void valorizzaInfoDocEle(ActionContext context, Fattura_passivaBulk fp) throws BusinessProcessException {

	try {

		FatturaPassivaComponentSession comp = (FatturaPassivaComponentSession)createComponentSession();
		fp = comp.valorizzaInfoDocEle(context.getUserContext(), fp);

		setModel(context, fp);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}


	/**
	 * Il metodo � stato sovrascritto per consentire all'utente di modificare lo
	 * stato della liquidazione quando il documento non risulta essere
	 * modificabile
	 * 
	 */
	public void writeFormInput(javax.servlet.jsp.JspWriter jspwriter, String s,
			String s1, boolean flag, String s2, String s3)
			throws java.io.IOException {
		Fattura_passivaBulk fp = null;
		if (getModel() != null)
			fp = (Fattura_passivaBulk) getModel();
		if (fp != null && fp.isRiportataInScrivania() && !fp.isPagata()
				&& isInputReadonly() && s1.equals("stato_liquidazione")) {
			getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag,
					s2,
					"onChange=\"submitForm('doOnStatoLiquidazioneChange')\"",
					getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
		} else if (fp != null && fp.isRiportataInScrivania() && !fp.isPagata()
				&& isInputReadonly() && s1.equals("causale")) {
			getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag,
					s2, "onChange=\"submitForm('doOnCausaleChange')\"",
					getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
		} else if (fp != null && fp.isRiportataInScrivania() && !fp.isPagata()
				&& isInputReadonly() && s1.equals("sospeso")) {
			getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag,
					s2,"" ,
					getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
		}  
		else
			super.writeFormInput(jspwriter, s, s1, flag, s2, s3);
	}

	public SimpleDetailCRUDController getCrudDocEleAllegatiColl() {
		return crudDocEleAllegatiColl;
	}

	public String getNomeFileAllegato() {
		DocumentoEleAllegatiBulk allegato = (DocumentoEleAllegatiBulk)getCrudDocEleAllegatiColl().getModel();
		if (allegato != null && allegato.getCmisNodeRef() != null)
			return allegato.getNomeAttachment();
		return null;
	}
	
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		SiglaCMISService cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);	
		DocumentoEleAllegatiBulk allegato = (DocumentoEleAllegatiBulk)getCrudDocEleAllegatiColl().getModel();
		Document document = (Document) cmisService.getNodeByNodeRef(allegato.getCmisNodeRef());
		InputStream is = cmisService.getResource(document);
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
	}
	
	public void scaricaFatturaHtml(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
		SiglaCMISService cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);
		Fattura_passivaBulk fattura_passivaBulk = (Fattura_passivaBulk) getModel();
    	StringBuffer query = new StringBuffer("select cmis:objectId from sigla_fatture:fatture_passive");
    	query.append(" where sigla_fatture:identificativoSdI = ");
    	query.append(fattura_passivaBulk.getDocumentoEleTestata().getIdentificativoSdi());
    	ItemIterable<QueryResult> results = cmisService.search(query);
    	for (QueryResult queryResult : results) {
			Folder fatturaFolder = (Folder) cmisService.getNodeByNodeRef((String)queryResult.getPropertyValueById(PropertyIds.OBJECT_ID));
			ItemIterable<CmisObject> files = fatturaFolder.getChildren();
			for (CmisObject cmisObject : files) {
				if (cmisObject.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues().contains("P:sigla_fatture_attachment:trasmissione_fattura")){													
					TransformerFactory tFactory = TransformerFactory.newInstance();							
					Source xslDoc = null;
					if (fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione().getFormatoTrasmissione().equals("FPA12")){
						xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.2.xsl"));
					} else if (fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione().getFormatoTrasmissione().equals("SDI11")){
						xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.1.xsl"));
					} else {
						throw new ApplicationException("Il formato trasmissione indicato da SDI non rientra tra i formati attesi");
					}
				
					Source xmlDoc = new StreamSource(((Document)cmisObject).getContentStream().getStream());
					HttpServletResponse response = ((HttpActionContext)actioncontext).getResponse();
					OutputStream os = response.getOutputStream();
					response.setContentType("text/html");
					Transformer trasform = tFactory.newTransformer(xslDoc);
					trasform.transform(xmlDoc, new StreamResult(os));
					os.flush();
				}
			}
		}
	}
	
	public String[][] getTabs() {
		TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
		int i=0;

		pages.put(i++, new String[]{ "tabFatturaPassiva","Testata","/docamm00/tab_fattura_passiva.jsp" });
		pages.put(i++, new String[]{ "tabFornitore","Fornitore","/docamm00/tab_fornitore.jsp" });		
		pages.put(i++, new String[]{ "tabFatturaPassivaDettaglio","Dettaglio","/docamm00/tab_fattura_passiva_dettaglio.jsp" }); 
		pages.put(i++, new String[]{ "tabFatturaPassivaConsuntivo","Consuntivo","/docamm00/tab_fattura_passiva_consuntivo.jsp" });

		if (this.getModel() instanceof Nota_di_creditoBulk) {
			Nota_di_creditoBulk ndc = (Nota_di_creditoBulk)this.getModel();
			java.util.Hashtable obbligazioni = ndc.getFattura_passiva_obbligazioniHash();
			java.util.Hashtable accertamenti = ndc.getAccertamentiHash();
			boolean hasObbligazioni = !(obbligazioni == null || obbligazioni.isEmpty());
			boolean hasAccertamenti = !(accertamenti == null || accertamenti.isEmpty());
			if (hasObbligazioni || !hasAccertamenti)
				pages.put(i++, new String[]{ "tabFatturaPassivaObbligazioni","Storni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" });
			if (hasAccertamenti || !hasObbligazioni)
				pages.put(i++, new String[]{ "tabFatturaPassivaAccertamenti","Accertamenti","/docamm00/tab_fattura_passiva_accertamenti.jsp" });
		} else if (this.getModel() instanceof Nota_di_debitoBulk) {
			pages.put(i++, new String[]{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" });
		} else if (this.getModel() instanceof Fattura_passiva_IBulk) { 
			pages.put(i++, new String[]{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" });
			pages.put(i++, new String[]{ "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_lettera_pagam_estero.jsp" });

			Fattura_passiva_IBulk fatturaPassiva=(Fattura_passiva_IBulk)this.getModel();
			if (!(fatturaPassiva.isCommerciale() && fatturaPassiva.getTi_bene_servizio() != null && 
					Bene_servizioBulk.BENE.equalsIgnoreCase(fatturaPassiva.getTi_bene_servizio()) && 
					fatturaPassiva.getFl_intra_ue() && fatturaPassiva.getFl_merce_extra_ue()!=null && fatturaPassiva.getFl_merce_extra_ue())) {
    			pages.put(i++, new String[]{ "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" });
    		}
		} else {
			pages.put(i++, new String[]{ "tabFatturaPassivaObbligazioni","Impegni","/docamm00/tab_fattura_passiva_obbligazioni.jsp" });
			pages.put(i++, new String[]{ "tabLetteraPagamentoEstero","Documento 1210","/docamm00/tab_lettera_pagam_estero.jsp" });
			pages.put(i++, new String[]{ "tabFatturaPassivaIntrastat","Intrastat","/docamm00/tab_fattura_passiva_intrastat.jsp" });
		}
		if (((Fattura_passivaBulk)this.getModel()).getDocumentoEleTestata() != null) {
			pages.put(i++, new String[]{ "tabEleAllegati","Allegati Ricevuti","/docamm00/tab_fatt_ele_allegati.jsp" });		
			pages.put(i++, new String[]{ "tabAllegati","Allegati Aggiunti","/util00/tab_allegati.jsp" });			
		}

		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++)
			tabs[j]=new String[]{pages.get(j)[0],pages.get(j)[1],pages.get(j)[2]};
		return tabs;
	}
	
	@Override
	protected CMISPath getCMISPath(Fattura_passivaBulk fattura_passivaBulk, boolean create) {
		if (fattura_passivaBulk != null && fattura_passivaBulk.getDocumentoEleTestata() != null && fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione() != null && 
				fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione().getCmisNodeRef() != null) {
			try {
				return CMISPath.construct(
						((Folder)SpringUtil.getBean("cmisService", SiglaCMISService.class).getNodeByNodeRef(fattura_passivaBulk.getDocumentoEleTestata().getDocumentoEleTrasmissione().getCmisNodeRef())).getPath()
				);
			} catch (ApplicationException|CmisObjectNotFoundException e) {
				return null;
			}
		}
		return null;
	}

	@Override
	protected Class<AllegatoFatturaBulk> getAllegatoClass() {
		return AllegatoFatturaBulk.class;
	}	
	
	@Override
	protected boolean excludeChild(CmisObject cmisObject) {		
		for (Object obj : crudDocEleAllegatiColl.getDetails()) {
			DocumentoEleAllegatiBulk documentoEleAllegatiBulk = (DocumentoEleAllegatiBulk)obj;
			if (documentoEleAllegatiBulk.getCmisNodeRef().equalsIgnoreCase(cmisObject.getId()))
				return true;
		}
		if (cmisObject.getType().getId().equalsIgnoreCase("D:sigla_fatture_attachment:document"))
			return true;
		return super.excludeChild(cmisObject);
	}
	
	@Override
	protected void completeAllegato(AllegatoFatturaBulk allegato) throws ApplicationException {
		for (SecondaryType secondaryType : allegato.getDocument(cmisService).getSecondaryTypes()) {
			if (AllegatoFatturaBulk.aspectNamesKeys.get(secondaryType.getId()) != null){
				allegato.setAspectName(secondaryType.getId());
				break;
			}
		}
		super.completeAllegato(allegato);
	}
	
	public boolean isRequiredSplitPayment(ActionContext actioncontext, Timestamp dt_registrazione) throws BusinessProcessException {
		try {
			return Utility.createFatturaPassivaComponentSession().isAttivoSplitPayment(actioncontext.getUserContext(), dt_registrazione);
		} catch (Exception e) {
			throw handleException(e);
		}			
	}
	
	//variabile inizializzata in fase di caricamento Nota da fattura elettronica 
	//utilizzata per ritornare sulla fattura elettronica 
	public boolean fromFatturaElettronica = Boolean.FALSE;

	public void setFromFatturaElettronica(boolean fromFatturaElettronica) {
		this.fromFatturaElettronica = fromFatturaElettronica;
	}
	
	public boolean isFromFatturaElettronica() {
		return fromFatturaElettronica;
	}
}