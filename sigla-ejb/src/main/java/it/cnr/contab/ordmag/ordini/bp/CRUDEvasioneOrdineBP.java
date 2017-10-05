package it.cnr.contab.ordmag.ordini.bp;

import java.util.List;

import it.cnr.contab.docamm00.bp.ObbligazioniCRUDController;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.contab.ordmag.ordini.service.OrdineAcqCMISService;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDEvasioneOrdineBP extends SimpleCRUDBP {

	private final SimpleDetailCRUDController dettaglioObbligazioneController;
	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean carryingThrough = false;
	protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
	private boolean isDeleting = false;
	public boolean isInputReadonly() 
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		if(ordine == null)
			return super.isInputReadonly();
		return 	super.isInputReadonly() || (ordine.getStato() != null && !ordine.isStatoInserito() && !ordine.isStatoInApprovazione()) ;
	}

	public OrdineAcqBulk creaOrdineDaRichieste(ActionContext context, List<RichiestaUopBulk> lista) throws BusinessProcessException{
		try {
			OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
			OrdineAcqComponentSession comp = (OrdineAcqComponentSession)createComponentSession();
			
			setModel(context, ordine);
			
			return ordine;
		}catch(Throwable ex){
			throw handleException(ex);
		}

	}

	private final SimpleDetailCRUDController righe= new OrdineAcqRigaCRUDController("Righe", OrdineAcqRigaBulk.class, "righeOrdineColl", this){
		public void validateForDelete(ActionContext context, OggettoBulk oggetto) throws ValidationException 
		{
			OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)oggetto;
			if (riga.getDspObbligazioneScadenzario() != null && riga.getDspObbligazioneScadenzario().getPg_obbligazione() != null){
				throw new ValidationException( "Impossibile cancellare una riga associata ad impegni");
			}
		}

		@Override
		public OggettoBulk removeDetail(int i) {
			List list = getDetails();
			OrdineAcqRigaBulk dettaglio =(OrdineAcqRigaBulk)list.get(i);
			for (int k=0;k<dettaglio.getRigheConsegnaColl().size();k++) {
				dettaglio.removeFromRigheConsegnaColl(k);
			}
			return super.removeDetail(i);
		}

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int index = super.addDetail(oggettobulk);
			return index;
		}

	};

	private final SimpleDetailCRUDController consegne = new SimpleDetailCRUDController("Consegne",OrdineAcqConsegnaBulk.class,"righeConsegnaColl",righe){

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)oggettobulk;
			consegna.setTipoConsegna(consegna.getOrdineAcqRiga().getTipoConsegnaDefault());
			int index = super.addDetail(oggettobulk);
			return index;
		}
		
	};

	private final ObbligazioniCRUDController obbligazioniController = new ObbligazioniCRUDController(
			"Obbligazioni",
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class,
			"ordineObbligazioniHash", this) {

				@Override
				public boolean isGrowable() {
					return false;
				}
		
		
	};

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getConsegne() {
		return consegne;
	}
	
	private OrdineAcqCMISService ordineAcqCMISService;

	public CRUDEvasioneOrdineBP() {
		this(OrdineAcqConsegnaBulk.class);
	}
	protected void setTab() {
		setTab("tab","tabOrdineAcq");
		setTab("tabOrdineAcqDettaglio","tabOrdineDettaglio");
	}

	public CRUDEvasioneOrdineBP(Class dettObbligazioniControllerClass) {
		super("Tr");

		setTab();
		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", dettObbligazioniControllerClass,
				"ordineObbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				OrdineAcqBulk ordine = (OrdineAcqBulk) CRUDEvasioneOrdineBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (ordine != null) {
					java.util.Hashtable h = ordine
							.getOrdineObbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			@Override
			public boolean isGrowable() {
				return false;
				
//				return super.isGrowable()
//						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
//								.getParentController()).isSearching();
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
	public CRUDEvasioneOrdineBP(String function)
			throws BusinessProcessException {
		super(function + "Tr");

		setTab();
		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", OrdineAcqConsegnaBulk.class,
				"ordineObbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				OrdineAcqBulk ordine = (OrdineAcqBulk) CRUDEvasioneOrdineBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (ordine != null) {
					java.util.Hashtable h = ordine
							.getOrdineObbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			@Override
			public boolean isGrowable() {
				return false;
				
//				return super.isGrowable()
//						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
//								.getParentController()).isSearching();
			}


			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};

	}

	
	
	
	public void create(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try { 
			getModel().setToBeCreated();
			setModel(
					context,
					((OrdineAcqComponentSession)createComponentSession()).creaConBulk(
							context.getUserContext(),
							getModel()));
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public final SimpleDetailCRUDController getRighe() {
		return righe;
	}
	public boolean isStampaOrdineButtonHidden() {

		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		return (ordine == null || ordine.getNumero() == null);
	}

	public boolean isSalvaDefinitivoButtonHidden() {

		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		return (ordine == null || ordine.getNumero() == null || !ordine.isStatoAllaFirma());
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[7];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");

		return toolbar;
	}
	public boolean areBottoniObbligazioneAbilitati()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();

		return 	ordine != null && 
				!isSearching() && 
				!isViewing() ;
	}
	public boolean isBottoneObbligazioneAggiornaManualeAbilitato()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();

		return(	ordine != null && !isSearching() && 
				!isViewing() );
	}
	public ObbligazioniCRUDController getObbligazioniController() {
		return obbligazioniController;
	}
	public void setDeleteManager(it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager) {
		this.deleteManager = deleteManager;
	}
	public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
		return userConfirm;
	}
	public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm) {
		this.userConfirm = userConfirm;
	}
	public boolean isCarryingThrough() {
		return carryingThrough;
	}
	public void setCarryingThrough(boolean carryingThrough) {
		this.carryingThrough = carryingThrough;
	}
	public SimpleDetailCRUDController getDettaglioObbligazioneController() {
		return dettaglioObbligazioneController;
	}
	public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		int crudStatus = getModel().getCrudStatus();
		try 
		{
			OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();
			if ( !ordine.isStatoInserito()){
				throw new BusinessProcessException( "Non è possibile cancellare un ordine in stato diverso da inserito");
			} else {
				ordine = ((OrdineAcqComponentSession)createComponentSession()).cancellaOrdine(context.getUserContext(),(OrdineAcqBulk)getModel());
				setModel( context, ordine );
				setMessage("Cancellazione effettuata");			
			}
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

}
