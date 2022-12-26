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

/*
 * Created on Feb 1, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.bp;

import java.math.BigDecimal;
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
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
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
		setStatoModificabile(getModel() instanceof ObbligazioneResBulk && ((ObbligazioneResBulk)getModel()).getStatoResiduo() == null);
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
	public boolean isEditScadenzaButtonEnabled() {
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
			setStatusAndEditableMap(VIEW);
			if (getTab( "tab" )!=null) {
				if (getTab( "tab" ).equalsIgnoreCase("tabScadenzario") || getTab( "tab" ).equalsIgnoreCase("tabObbligazione")) {
					if ( isScadenzaModificabile() && !((ObbligazioneBulk)getModel()).isDocRiportato())
						setStatusAndEditableMap(EDIT);
				} else if (getTab( "tab" ).equalsIgnoreCase("tabAllegati")) {
					if (!isROStato())
						setStatusAndEditableMap(EDIT);
					else if (!isStatoModificabile) {
						setStatusAndEditableMap(EDIT);
						getArchivioAllegati().setShrinkable(false);
						getArchivioAllegati().setGrowable(true);		
						getArchivioAllegati().setReadonlyOnEdit(true);
					}
				}
			}
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
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public String [][] getTabs() {
		TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
		int i=0;

		pages.put(i++, new String[]{ "tabObbligazione","Impegni","/doccont00/tab_obbligazione.jsp" });
		if (!this.isSearching()) {
			pages.put(i++, new String[]{"tabImputazioneFin", "Imputazione Finanziaria", "/doccont00/tab_imputazione_fin_obbligazione.jsp"});
			pages.put(i++, new String[]{"tabScadenzario", "Scadenzario", "/doccont00/tab_scadenzario_obbligazione.jsp"});
			pages.put(i++, new String[]{"tabAllegati", "Allegati", "/util00/tab_allegati.jsp"});
			pages.put(i++, new String[]{"tabCdrCapitoli", "Cdr", "/doccont00/tab_cdr_capitoli.jsp"});
		}
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

	public boolean isStatoVisibile() {
		if (dataVisibilitaStatoResiduo == null)
			return false;
		boolean statoVisible = EJBCommonServices.getServerDate().after(dataVisibilitaStatoResiduo);
		if (statoVisible) {
			if (this.isSearching())
				return true;
			else if (this.getModel()!=null && this.getModel() instanceof ObbligazioneResBulk) {
				if (((ObbligazioneResBulk)this.getModel()).getStatoResiduo()!=null)
					return true;
				if (((ObbligazioneResBulk)this.getModel()).getImportoNonPagato().compareTo(BigDecimal.ZERO)==0)
					return false;
			}
		}
		return statoVisible;
	}

	public boolean isROStato() {
		boolean roStato = isROImporto();
		if (getModel()!=null && !isStatoModificabile)
			roStato = true;
		return roStato;
	}

	@Override
	public void validate(ActionContext context) throws ValidationException {
		super.validate(context);
		if (getModel() != null) {
			ObbligazioneResBulk obb = ((ObbligazioneResBulk)getModel());
			if (obb.isLiquidabile() || obb.isNonLiquidabile())
				obb.getArchivioAllegati().stream()
					.map(AllegatoObbligazioneBulk.class::cast)
					.filter(AllegatoObbligazioneBulk::isTipoAllegatoGenerico)
					.filter(e->e.isNew() || e.getEsercizioDiAppartenenza().equals(CNRUserContext.getEsercizio(context.getUserContext())))
					.findAny()
					.orElseThrow(()->new ValidationException("Inserire almeno un 'Allegato Generico' per l'esercizio "+CNRUserContext.getEsercizio(context.getUserContext())+"!"));
		}
	}

	/**
	 * Inizializza il modello per la modifica.
	 * @param context Il contesto dell'azione
	 * @param bulk L'oggetto bulk in uso
	 * @return Oggetto Bulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
		try {
			ObbligazioneBulk oggettobulk = (ObbligazioneBulk)super.initializeModelForEdit(context, bulk);
			oggettobulk = initializeModelForEditAllegatiEqualsObbligazioni(context, oggettobulk);
			return oggettobulk;
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}

	//Questo metodo recupera gli allegati sulle obbligazioni simili e li fa vedere tutte sull'obbligazione aperta
	private ObbligazioneBulk initializeModelForEditAllegatiEqualsObbligazioni(ActionContext actioncontext, ObbligazioneBulk oggettobulk) throws BusinessProcessException {
		BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<>();
		try	{
			RemoteIterator ri = this.find(actioncontext, new CompoundFindClause(), new ObbligazioneBulk(), oggettobulk, "allEqualsObbligazioni");
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
			while (ri.hasMoreElements()) {
				ObbligazioneBulk currObbligazione = (ObbligazioneBulk) ri.nextElement();
				if (currObbligazione.getEsercizio().compareTo(oggettobulk.getEsercizio())<0) {
					CRUDObbligazioneBP obbligazioneBP=null;
					if (currObbligazione.isCompetenza())
						obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)actioncontext.getUserInfo().createBusinessProcess(actioncontext,"CRUDObbligazioneBP",new Object[] { "MRSWTh" });
					else if (currObbligazione.isObbligazioneResiduoImproprio())
						obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)actioncontext.getUserInfo().createBusinessProcess(actioncontext,"CRUDObbligazioneResImpropriaBP",new Object[] { "MRSWTh" });
					else
						obbligazioneBP = this;
					currObbligazione = (ObbligazioneBulk)obbligazioneBP.initializeModelForEditAllegati(actioncontext, currObbligazione);
					for (AllegatoGenericoBulk allegatoGenericoBulk : currObbligazione.getArchivioAllegati())
						((AllegatoObbligazioneBulk)allegatoGenericoBulk).setEsercizioDiAppartenenza(currObbligazione.getEsercizio());
					archivioAllegati.addAll(currObbligazione.getArchivioAllegati());
				}
			}
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}

		((AllegatoParentBulk)oggettobulk).getArchivioAllegati().addAll(archivioAllegati);
		return oggettobulk;
	}

	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		if (allegato instanceof AllegatoObbligazioneBulk && this.getModel()!=null && !allegato.isToBeCreated()) {
			if (isROStato() || ((AllegatoObbligazioneBulk)allegato).getEsercizioDiAppartenenza()==null || this.getModel()==null ||
				!((AllegatoObbligazioneBulk)allegato).getEsercizioDiAppartenenza().equals(((ObbligazioneBulk)this.getModel()).getEsercizio()))
				return false;
		}
		return super.isPossibileCancellazione(allegato);
	}

	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
		if (allegato instanceof AllegatoObbligazioneBulk && this.getModel()!=null) {
			if (isROStato() || ((AllegatoObbligazioneBulk)allegato).getEsercizioDiAppartenenza()==null || this.getModel()==null ||
				!((AllegatoObbligazioneBulk)allegato).getEsercizioDiAppartenenza().equals(((ObbligazioneBulk)this.getModel()).getEsercizio()))
				return false;
		}
		return super.isPossibileModifica(allegato);
	}

	protected void addChildDetail(OggettoBulk oggettobulk) {
		if (this.isStatoResiduoVisibile())
			((AllegatoObbligazioneBulk)oggettobulk).setTipoAllegatiAllKeys();
		else
			((AllegatoObbligazioneBulk)oggettobulk).setTipoAllegatiSenzaRiaccertamentoKeys();
	}
}
