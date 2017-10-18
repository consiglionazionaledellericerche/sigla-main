/*
 * Created on Feb 1, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.spring.storage.SiglaStorageService;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
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
	Date dataVisibilitaStatoResiduo;
	boolean isStatoModificabile = true;

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

	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		super.initialize(actioncontext);
		Configurazione_cnrComponentSession confCNR = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		try {
			String mesegiorno = confCNR.getVal01(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext()), 
					"DATA", "RIACCERTAMENTO_RESIDUI", "STATO");
			if (mesegiorno != null)
				dataVisibilitaStatoResiduo = new SimpleDateFormat("dd/MM/yyyy").parse(mesegiorno + "/" + CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} catch (ParseException e) {
			throw new BusinessProcessException(e);		
		}		
	}

	public boolean isStatoResiduoVisibile() {
		if (dataVisibilitaStatoResiduo == null)
			return false;
		boolean statoVisible = EJBCommonServices.getServerDate().after(dataVisibilitaStatoResiduo);
		if (statoVisible) {
			if (this.isSearching())
				return true;
			else if (this.getModel()!=null && this.getModel() instanceof ObbligazioneResBulk) {
				if (((ObbligazioneResBulk)this.getModel()).getStatoResiduo()!=null)
					return true;
			}
		}
		return statoVisible;
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
		setStatoModificabile(true);//((AccertamentoResiduoBulk)getModel()).getStato() == null;
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
			if (getTab( "tab" )!=null && 
				((getTab( "tab" ).equalsIgnoreCase("tabScadenzario") || getTab( "tab" ).equalsIgnoreCase("tabObbligazione")) && 
				 isScadenzaModificabile() && !((ObbligazioneBulk)getModel()).isDocRiportato()) ||
				(getTab( "tab" ).equalsIgnoreCase("tabAllegati") && isStatoResiduoVisibile())) 
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

	@Override
	protected String getStorePath(ObbligazioneBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegatoParentBulk.getUnita_organizzativa())
						.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
						.orElse(""),
				"Riaccertamento dei residui passivi",
				Optional.ofNullable(allegatoParentBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				allegatoParentBulk.getCd_uo_origine() + "-" + allegatoParentBulk.getEsercizio_originale() + allegatoParentBulk.getPg_obbligazione()
		).stream().collect(
				Collectors.joining(SiglaStorageService.SUFFIX)
		);
	}

	public String [][] getTabs() {
		TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
		int i=0;

		pages.put(i++, new String[]{ "tabObbligazione","Impegni","/doccont00/tab_obbligazione.jsp" });
		pages.put(i++, new String[]{ "tabImputazioneFin","Imputazione Finanziaria","/doccont00/tab_imputazione_fin_obbligazione.jsp" });
		pages.put(i++, new String[]{ "tabScadenzario","Scadenzario","/doccont00/tab_scadenzario_obbligazione.jsp" });
		if (isStatoResiduoVisibile())
			pages.put(i++, new String[]{ "tabAllegati","Allegati","/util00/tab_allegati.jsp" });

		pages.put(i++, new String[]{ "tabCdrCapitoli","Cdr","/doccont00/tab_cdr_capitoli.jsp" });
		
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++)
			tabs[j]=new String[]{pages.get(j)[0],pages.get(j)[1],pages.get(j)[2]};
		return tabs;
	}

	private void setStatoModificabile(boolean isStatoModificabile) {
		this.isStatoModificabile = isStatoModificabile;
	}
	
	public boolean isStatoModificabile() {
		return isStatoModificabile;
	}
}
