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
 * Created on Apr 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;


import it.cnr.contab.config00.contratto.bulk.*;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_laBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.SIGLAGroups;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.upload.UploadedFile;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.RecordFormatException;

import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.servlet.ServletException;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigAnagContrattoBP extends SimpleCRUDBP {

	private static final long serialVersionUID = 1L;

	private ContrattoBulk contratto;
	private String tipoAccesso;
	protected ContrattoService contrattoService;
	protected Date dataStipulaParametri;
	protected Boolean flagPubblicaContratto;
	private boolean attivoOrdini = false;
	private final SimpleDetailCRUDController crudDettaglio_contratto = new SimpleDetailCRUDController("Dettaglio_contratto", Dettaglio_contrattoBulk.class, "dettaglio_contratto", this){
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			ContrattoBulk contratto = ( ContrattoBulk) this.getParentModel();
			if (((Dettaglio_contrattoBulk) detail).isNonCancellabile())
				throw new ValidationException("Non è possibile cancellare dettaglio già utilizzato in un ordine.");

		}
	};

	public boolean isAttivoOrdini() {
		return attivoOrdini;
	}
	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		try {
			attivoOrdini = Utility.createConfigurazioneCnrComponentSession().isAttivoOrdini(actioncontext.getUserContext());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
		super.init(config, actioncontext);
	}

	private SimpleDetailCRUDController crudAssUO = new SimpleDetailCRUDController( "Associazione UO", Ass_contratto_uoBulk.class, "associazioneUO", this);
	private SimpleDetailCRUDController crudAssUODisponibili = new SimpleDetailCRUDController( "Associazione UO Disponibili", Unita_organizzativaBulk.class, "associazioneUODisponibili", this);
	private SimpleDetailCRUDController crudAssDitte = new SimpleDetailCRUDController( "ditte Invitate", Ass_contratto_ditteBulk.class, "ditteInvitate", this){
		public void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			((Ass_contratto_ditteBulk)oggettobulk).validate();
			super.validate(actioncontext, oggettobulk);
		};
	}; 
 
	@SuppressWarnings("serial")
	private SimpleDetailCRUDController crudArchivioAllegati = new SimpleDetailCRUDController( "ArchivioAllegati", AllegatoContrattoDocumentBulk.class, "archivioAllegati", this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.ArchivioAllegati.file");
			if ( allegato.getType() == null )
				throw new ValidationException("Attenzione: valorizzare il Tipo.");
			if ( allegato.getNome() == null ) {
				if ((file == null || file.getName().equals("")) && !allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO))
					throw new ValidationException("Attenzione: selezionare un File da caricare.");
				if (((file == null || file.getName().equals("")) && allegato.getLink() == null) && 
						allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO))
					throw new ValidationException("Attenzione: selezionare un File da caricare oppure valorizzare il Link al Progetto.");
			}else{
				if ((!allegato.isContentStreamPresent() && (allegato.getLink() == null&&allegato.getFile() == null)) && 
						allegato.getType().equals(AllegatoContrattoDocumentBulk.PROGETTO))
					throw new ValidationException("Attenzione: selezionare un File da caricare oppure valorizzare il Link al Progetto.");
			}
			
				if (!(file == null || file.getName().equals(""))) {
						allegato.setFile(file.getFile());
						allegato.setContentType(file.getContentType());
						allegato.setNome(file.getName());
				}
				if (allegato.isContentStreamPresent() )
					allegato.setToBeUpdated();
				getParentController().setDirty(true);
				super.validate(actioncontext, oggettobulk);
		}

		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			super.validateForDelete(actioncontext, oggettobulk);
		}
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			return super.removeDetail(oggettobulk, i);
		}
		public boolean isShrinkable() {
			return super.isShrinkable() && isAllegatiEnabled();
		};
		public boolean isGrowable() {
			return super.isGrowable();// && isAllegatiEnabled();			
		};
	};
	
	@SuppressWarnings("serial")
	private SimpleDetailCRUDController crudArchivioAllegatiFlusso = new SimpleDetailCRUDController( "ArchivioAllegatiFlusso", AllegatoContrattoFlussoDocumentBulk.class, "archivioAllegatiFlusso", this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {

		}

		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			throw new ValidationException("Documento non inserito in SIGLA non sono consentite cancellazioni.");
		}
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			return oggettobulk;
		}
		public boolean isShrinkable() {
			return false;
		};
		public boolean isGrowable() {
			return false;			
		};
	};
	
	public CRUDConfigAnagContrattoBP()
	{
		super();
	}

	protected void validaDitte(ActionContext actioncontext,
			Ass_contratto_ditteBulk bulk) throws ValidationException {
		bulk.validate();
		
		for (java.util.Iterator i = bulk.getContratto().getDitteInvitate().iterator();i.hasNext();) {
			Ass_contratto_ditteBulk b = (Ass_contratto_ditteBulk)i.next();
			if (!b.equals(bulk) &&
					((b.getCodice_fiscale() != null && bulk.getCodice_fiscale()!=null && b.getCodice_fiscale().compareTo(bulk.getCodice_fiscale())==0) || 
					(b.getId_fiscale()!=null && bulk.getId_fiscale()!=null && b.getId_fiscale().compareTo(bulk.getId_fiscale())==0)))			    		
	 			throw new ValidationException ("Attenzione: esistono più ditte con lo stesso codice fiscale/Id Fiscale!"); 	
			 	
		}
		if( bulk.getDenominazione_rti()!=null){
			boolean trovato=false;
			for (java.util.Iterator i = bulk.getContratto().getDitteInvitate().iterator();i.hasNext();) {
				Ass_contratto_ditteBulk b = (Ass_contratto_ditteBulk)i.next();
		
				if(!b.equals(bulk) && b.getDenominazione_rti()!=null &&
						bulk.getDenominazione_rti().compareTo(b.getDenominazione_rti())==0)
					trovato=true;
			}
			if(!trovato)
				throw new ValidationException ("Attenzione: devono esistere più ditte con la stessa denominazione rti!");
		}
		if( bulk.getRuolo()!=null){
			boolean trovato=false;
			for (java.util.Iterator i = bulk.getContratto().getDitteInvitate().iterator();i.hasNext();) {
				Ass_contratto_ditteBulk b = (Ass_contratto_ditteBulk)i.next();
		
				if(!b.equals(bulk) && b.getRuolo()!=null &&  
						bulk.getDenominazione_rti()!=null &&
						b.getDenominazione_rti()!=null &&
						bulk.getDenominazione_rti().compareTo(b.getDenominazione_rti())==0 &&
						bulk.getRuolo().compareTo(b.getRuolo())!=0)
					trovato=true;
			}
			if(!trovato)
				throw new ValidationException ("Attenzione: devono esistere ruoli diversi con la stessa denominazione rti!");
		}
	}

	public CRUDConfigAnagContrattoBP(String s)
	{
		super(s);
	}

	public CRUDConfigAnagContrattoBP(String function, ContrattoBulk contratto, String tipoAccesso) {
		super(function);
		setContratto(contratto);
		setTipoAccesso(tipoAccesso);
	}

	public boolean isPublishHidden(){
		if (isSearching() || isInserting())
			return false;
		if (getModel()!=null){
			ContrattoBulk contratto = (ContrattoBulk) getModel();
			if (contratto.isProvvisorio())
				return false;
			if (contratto.isCessato())
				return true;
		}
		return isPublishCRUDButtonHidden();
	}

	public boolean isFromFlussoAcquisti(){
		if (isSearching() || isInserting())
			return false;
		if (getModel()!=null){
			ContrattoBulk contratto = (ContrattoBulk) getModel();
			if (contratto.isFromFlussoAcquisti())
				return true;
		}
		return false;
	}

	public boolean isPublishCRUDButtonHidden(){
		/*if (isSearching())
			return true;		
		if (getModel()!=null){
			ContrattoBulk contratto = (ContrattoBulk) getModel();
			if ((contratto.isPassivo() || contratto.isAttivo_e_Passivo()) &&
					contratto.isDefinitivo() && flagPubblicaContratto.booleanValue() &&
					(!contratto.getDt_stipula().before(dataStipulaParametri)) &&
					!contratto.getFl_pubblica_contratto()
				&& (contratto.getTipo_contratto() != null && 
				    contratto.getTipo_contratto().getFl_pubblica_contratto() != null  &&
				   contratto.getTipo_contratto().getFl_pubblica_contratto().booleanValue())) 
				return false;
		}*/
		return true;
	}
	
	public boolean isAllegatiEnabled(){
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		if (isEditing() && 
				contratto != null && 
				contratto.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL){
			if (contratto.isDefinitivo()){
				if (flagPubblicaContratto.booleanValue()){
				   if (contratto.getFl_pubblica_contratto() == null || contratto.getFl_pubblica_contratto())
					   return false;
				   else			
					  return (!contratto.getDt_stipula().before(dataStipulaParametri));
				}else
					return false;
			}
		}
		return true;
	}
	
	public boolean isContrattoDefinitivo(){
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		if (contratto != null)
			return contratto.isRODefinitivo();
		return false;
	}
	
	private it.cnr.contab.config00.bulk.Parametri_cnrBulk getParametri_cnrBulk(it.cnr.jada.action.ActionContext context) throws ComponentException, RemoteException, EJBException{
		return Utility.createParametriCnrComponentSession().
			getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext()));
	}
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);
		ContrattoBulk contratto= (ContrattoBulk)getModel();
		try {
			dataStipulaParametri=getParametri_cnrBulk(context).getData_stipula_contratti();
			flagPubblicaContratto=getParametri_cnrBulk(context).getFl_pubblica_contratto();
		} catch (Exception e) {
			throw handleException(e);
		}
		if (getStatus()!=VIEW){
			if (contratto.getUnita_organizzativa() != null && contratto.getUnita_organizzativa().getCd_unita_organizzativa() != null &&
			    !contratto.getUnita_organizzativa().getCd_unita_organizzativa().equals(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()))){
					setStatus(VIEW);					
					getCrudAssUO().setEnabled(false);		
					getCrudAssUODisponibili().setEnabled(false);	    	
			    }
			else{
				getCrudAssUO().setEnabled(true);  							
				getCrudAssUODisponibili().setEnabled(true);
			}
			if (contratto!=null && contratto.isCancellatoLogicamente()) {
				setStatus(VIEW);
				getCrudAssUODisponibili().setEnabled(false);
			}			
		}else{
			getCrudAssUODisponibili().setEnabled(false);
		}
			getCrudArchivioAllegati().setEnabled(true);
			getCrudArchivioAllegatiFlusso().setEnabled(true);
	}	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUO() {
		return crudAssUO;
	}
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		openForm(context,action,target,"multipart/form-data");
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUO(SimpleDetailCRUDController controller) {
		crudAssUO = controller;
	}
	protected void resetTabs(it.cnr.jada.action.ActionContext context) {
		setTab("tab","tabTestata");
	}
	public boolean isVisualizzaDocContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doc_cont_spe()==null)
		  return false;
		else if (contratto.getTot_doc_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDocContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doc_cont_etr()==null)
		  return false;
		else if (contratto.getTot_doc_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	
	public boolean isVisualizzaDocammContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_docamm_cont_etr()==null)
		  return false;
		else if (contratto.getTot_docamm_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDocammContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_docamm_cont_spe()==null)
		  return false;
		else if (contratto.getTot_docamm_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDoccontContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doccont_cont_etr()==null)
		  return false;
		else if (contratto.getTot_doccont_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDoccontContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doccont_cont_spe()==null)
		  return false;
		else if (contratto.getTot_doccont_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaCommessaButtonEnabled(){
		return isVisualizzaDocContSpeButtonEnabled()||isVisualizzaDocContEtrButtonEnabled();
	}	
	/**
	 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
	 * @return toolbar Toolbar in uso
	 */

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[11];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.undoBringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.definitiveSave");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.publish");

		return toolbar;
	}
	/**
	 * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 *
	 * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 */
	public boolean isSalvaDefinitivoButtonEnabled() {

		return isEditing() && 
				getModel() != null && 
				getModel().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				((ContrattoBulk)getModel()).isProvvisorio();
	}
	/**
	 * Gestione del salvataggio definitivo di un contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @throws BusinessProcessException	
	 */
	public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			archiviaAllegati(context, (ContrattoBulk) getModel());
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		try {
			if (((ContrattoBulk) getModel()).getDitteInvitate()!=null && ((ContrattoBulk) getModel()).getDitteInvitate().size()!=0 )
				for (java.util.Iterator i =((ContrattoBulk) getModel()).getDitteInvitate().iterator();i.hasNext();) {
					Ass_contratto_ditteBulk b = (Ass_contratto_ditteBulk)i.next();
					validaDitte(context,b);
				}
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			ContrattoBulk contratto = comp.salvaDefinitivo(context.getUserContext(), (ContrattoBulk)getModel());
			edit(context,contratto);
		}catch(ValidationException ex){
			throw handleException(ex);	
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	@Deprecated
	public void pubblicaContratto(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		ContrattoBulk contratto = (ContrattoBulk) getModel();
		try {
			archiviaAllegati(context, contratto);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		try {
			StorageObject folder = contrattoService.getFolderContratto((ContrattoBulk)getModel());
			if (!contratto.isAllegatoContrattoPresent())
				throw handleException(new ApplicationException("Bisogna allegare il file del Contratto!"));
			contratto.setFl_pubblica_contratto(Boolean.TRUE);
			contratto.setToBeUpdated();
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			comp.modificaConBulk(context.getUserContext(), contratto);
			if (folder != null){
				contrattoService.updateProperties(contratto, folder); 
				contrattoService.addAspect(folder, "P:sigla_contratti_aspect:stato_definitivo");
				contrattoService.addConsumer(folder, SIGLAGroups.GROUP_CONTRATTI.name());
				contrattoService.setInheritedPermission(
						contrattoService.getStorageObjectByPath(contrattoService.getCMISPathFolderContratto(contratto)),
						Boolean.FALSE);
			}
			edit(context,contratto);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}

	public void controllaCancellazioneAssociazioneUo(ActionContext context,Ass_contratto_uoBulk ass_contratto_uo) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			comp.controllaCancellazioneAssociazioneUo(context.getUserContext(), ass_contratto_uo);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void initializzaUnita_Organizzativa(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			ContrattoBulk contratto = (ContrattoBulk)getModel();
			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			setModel(context, comp.initializzaUnita_Organizzativa(context.getUserContext(), (ContrattoBulk)getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUODisponibili() {
		return crudAssUODisponibili;
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUODisponibili(SimpleDetailCRUDController controller) {
		crudAssUODisponibili = controller;
	}
	
	public SimpleDetailCRUDController getCrudArchivioAllegati() {
		return crudArchivioAllegati;
	}

	public SimpleDetailCRUDController getCrudArchivioAllegatiFlusso() {
		return crudArchivioAllegatiFlusso;
	}

	public void setCrudArchivioAllegatiFlusso(SimpleDetailCRUDController crudArchivioAllegatiFlusso) {
		this.crudArchivioAllegatiFlusso = crudArchivioAllegatiFlusso;
	}

	public void setCrudArchivioAllegati(
			SimpleDetailCRUDController crudArchivioAllegati) {
		this.crudArchivioAllegati = crudArchivioAllegati;
	}

	public boolean isDeleteButtonEnabled()
	{
		return isEditable() && isEditing();
	}
	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		contrattoService = SpringUtil.getBean("contrattoService",
				ContrattoService.class);		
		super.initialize(actioncontext);
		try {
			if (Optional.ofNullable(getTipoAccesso())
					.filter(tipoAccesso -> tipoAccesso.equals("V"))
					.isPresent()) {
				ContrattoBulk contratto = getContratto();
				setModel(actioncontext, contratto);
				cerca(actioncontext);
	}
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public void cerca(ActionContext actioncontext) throws RemoteException, InstantiationException, RemoveException, BusinessProcessException
	{
		try
		{
			fillModel(actioncontext);
			OggettoBulk oggettobulk = getModel();
			RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk);
			if(remoteiterator == null || remoteiterator.countElements() == 0)
			{
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				return;
			}
			if(remoteiterator.countElements() == 1)
			{
				OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				if(oggettobulk1 != null) {
					edit(actioncontext, oggettobulk1);
				}
				return;
			}
			else {
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				//reset(actioncontext);
				setStatus(SEARCH);
			}
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}

	@Override
	public void delete(ActionContext actioncontext)
			throws BusinessProcessException {
		super.delete(actioncontext);
	}
	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		super.update(actioncontext);
		try {
		if (((ContrattoBulk) getModel()).getDitteInvitate()!=null && ((ContrattoBulk) getModel()).getDitteInvitate().size()!=0 )
			for (java.util.Iterator i =((ContrattoBulk) getModel()).getDitteInvitate().iterator();i.hasNext();) {
				Ass_contratto_ditteBulk b = (Ass_contratto_ditteBulk)i.next();
				validaDitte(actioncontext,b);
			}
		} catch (ValidationException e) {
			throw handleException(e);
		}
		try {
			archiviaAllegati(actioncontext, (ContrattoBulk) getModel());
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		if (!((ContrattoBulk) getModel()).isFromFlussoAcquisti()){
			StorageObject folder;
			try {
				folder = contrattoService.getFolderContratto((ContrattoBulk) getModel());
				if (folder != null)
					contrattoService.updateProperties(getModel(), folder);
			} catch (ApplicationException e) {
				throw handleException(e);
			}
		}
	}
	
	@Override
	public void create(ActionContext actioncontext)
			throws BusinessProcessException {
		super.create(actioncontext);
		try {
			if (((ContrattoBulk) getModel()).getDitteInvitate()!=null && ((ContrattoBulk) getModel()).getDitteInvitate().size()!=0 )
				for (java.util.Iterator i =((ContrattoBulk) getModel()).getDitteInvitate().iterator();i.hasNext();) {
					Ass_contratto_ditteBulk b = (Ass_contratto_ditteBulk)i.next();
					validaDitte(actioncontext,b);
				}
			} catch (ValidationException e) {
				throw handleException(e);
			}
		try {
			archiviaAllegati(actioncontext, (ContrattoBulk) getModel());
		} catch (ApplicationException e) {
			throw handleException(e);
		}
	}

	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		ContrattoBulk contratto = (ContrattoBulk)super.initializeModelForEdit(actioncontext, oggettobulk);
		try {
			Optional.ofNullable(contrattoService.getFolderContratto(contratto))
					.map(storageObject -> contrattoService.getChildren(storageObject.getKey()))
					.map(storageObjects -> storageObjects.stream())
					.orElse(Stream.empty())
					.filter(storageObject -> Optional.ofNullable(storageObject.getKey()).isPresent())
					.forEach(child -> {
						contratto.setAllegatoFlusso(false);
						if (contratto.isFromFlussoAcquisti()){
							AllegatoContrattoFlussoDocumentBulk allegato = AllegatoContrattoFlussoDocumentBulk.construct(child);
							Optional.ofNullable(child.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
									.map(strings -> strings.stream())
									.ifPresent(stringStream -> {
										stringStream
										.filter(s -> AllegatoContrattoFlussoDocumentBulk.ti_allegatoFlussoKeys.get(s) != null)
										.findFirst()
										.ifPresent(s -> allegato.setType(s));
										if (allegato.getType() != null){
											allegato.setContentType(child.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
											allegato.setDescrizione(child.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
											allegato.setTitolo(child.getPropertyValue(StoragePropertyNames.TITLE.value()));
											allegato.setNome(allegato.getTitolo());
											allegato.setCrudStatus(OggettoBulk.NORMAL);
											contratto.addToArchivioAllegatiFlusso(allegato);
											contratto.setAllegatoFlusso(true);
											if (!allegato.isContentStreamPresent())
												setMessage(ERROR_MESSAGE, "Attenzione l'allegato [" + allegato.getName() + "] risulta privo di contenuto!");
										}
									});
						}
						if (contratto.getAllegatoFlusso() == false){
							AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
							allegato.setContentType(child.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
							allegato.setDescrizione(child.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
							allegato.setTitolo(child.getPropertyValue(StoragePropertyNames.TITLE.value()));
							allegato.setNome(child.getPropertyValue("sigla_contratti_attachment:original_name"));
							allegato.setType(child.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));

							allegato.setLink(child.<String>getPropertyValue("sigla_contratti_aspect_link:url"));
							allegato.setCrudStatus(OggettoBulk.NORMAL);
							contratto.addToArchivioAllegati(allegato);
							if (!allegato.isContentStreamPresent())
								setMessage(ERROR_MESSAGE, "Attenzione l'allegato [" + allegato.getName() + "] risulta privo di contenuto!");
						}

					});
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		return contratto;
	}

	public String getNomeAllegato() throws ApplicationException{
		AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk)getCrudArchivioAllegati().getModel();
		return Optional.ofNullable(allegato)
				.map(AllegatoContrattoDocumentBulk::getNodeId)
				.map(key -> contrattoService.getStorageObjectBykey(key))
				.map(storageObject -> storageObject.<String>getPropertyValue(StoragePropertyNames.NAME.value()))
				.orElse(null);
	}
	
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoContrattoDocumentBulk allegato = (AllegatoContrattoDocumentBulk)getCrudArchivioAllegati().getModel();
		scarica(actioncontext, allegato.getNodeId());
	}

	public void scaricaAllegatoFlusso(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoContrattoFlussoDocumentBulk allegato = (AllegatoContrattoFlussoDocumentBulk)getCrudArchivioAllegatiFlusso().getModel();
		scarica(actioncontext, allegato.getNodeId());
	}

	private void scarica(ActionContext actioncontext, String nodeId) throws IOException {
		StorageObject storageObject = contrattoService.getStorageObjectBykey(nodeId);
		InputStream is = contrattoService.getResource(storageObject);
		((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
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

	private void archiviaAllegati(ActionContext actioncontext, ContrattoBulk contratto) throws BusinessProcessException, ApplicationException{
		try {
			crudArchivioAllegati.validate(actioncontext);
		} catch (ValidationException e1) {
			throw handleException(e1);
		}
		ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
		try {
			comp.archiviaAllegati(actioncontext.getUserContext(),contratto);
		} catch (ComponentException e) {
			throw new ApplicationException(e);
		}
		/*
		Optional.ofNullable(contrattoService.getStorageObjectByPath(contrattoService.getCMISPathFolderContratto(contratto)))
				.orElseGet(() -> {
					StorageObject parentStorageObject = contrattoService.getStorageObjectByPath(
							contrattoService.getCMISPath(contratto), true
					);
					return contrattoService.getStorageObjectBykey(
							contrattoService.createFolderIfNotPresent(parentStorageObject.getPath(),
									contratto.getCMISFolderName(), null, null,
									contratto
							));
				});

		for (Iterator<AllegatoContrattoDocumentBulk> iterator = contratto.getArchivioAllegati().deleteIterator(); iterator.hasNext();) {
			AllegatoContrattoDocumentBulk allegato = iterator.next();
			if (allegato.isToBeDeleted()){
				contrattoService.delete(allegato.getNodeId());
				allegato.setCrudStatus(OggettoBulk.NORMAL);
			}
		}
		for (AllegatoContrattoDocumentBulk allegato : contratto.getArchivioAllegati()) {
			if (allegato.isToBeCreated()){
				try {
					StorageObject storageObject = Optional.ofNullable(allegato.getFile())
							.map(file -> {
								try {
									return contrattoService.storeSimpleDocument(
											allegato,
											new FileInputStream(file),
											allegato.getContentType(),
											allegato.getDocumentName(),
											contrattoService.getCMISPath(allegato),
											true);
								} catch (FileNotFoundException e) {
									throw new StorageException(StorageException.Type.GENERIC, e);
								}
							}).orElseGet(() -> {
								return contrattoService.storeSimpleDocument(
										allegato,
										null,
										allegato.getContentType(),
										allegato.getDocumentName(), contrattoService.getCMISPath(allegato),
										true);
							});
					if (contratto.isDefinitivo() && !allegato.getType().equals(AllegatoContrattoDocumentBulk.GENERICO))
						contrattoService.costruisciAlberaturaAlternativa(allegato, storageObject);

					allegato.setCrudStatus(OggettoBulk.NORMAL);
					allegato.setNodeId(storageObject.getKey());
				} catch (StorageException e) {
					if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
						throw new ApplicationException("CMIS - File ["+allegato.getNome()+"] gia' presente. Inserimento non possibile!");
					throw handleException(e);
				}
			}else if (allegato.isToBeUpdated()) {
				try {
					if (allegato.getFile() != null)
						contrattoService.updateStream(allegato.getNodeId(),
								new FileInputStream(allegato.getFile()),
								allegato.getContentType());
					contrattoService.updateProperties(allegato, contrattoService.getStorageObjectBykey(allegato.getNodeId()));
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					throw handleException(e);
				}catch (StorageException e) {
					throw new ApplicationException("CMIS - File ["+allegato.getNome()+"] gia' presente. Inserimento non possibile!");
				}
			}
		}
		*/
	}

	public Boolean getFlagPubblicaContratto() {
		return flagPubblicaContratto;
	}

	public void setFlagPubblicaContratto(Boolean flagPubblicaContratto) {
		this.flagPubblicaContratto = flagPubblicaContratto;
	}
	public ContrattoBulk getContratto() {
		return contratto;
	}


	public void setContratto(ContrattoBulk contratto) {
		this.contratto = contratto;
	}

	public String getTipoAccesso() {
		return tipoAccesso;
	}

	public void setTipoAccesso(String tipoAccesso) {
		this.tipoAccesso = tipoAccesso;
	}

public SimpleDetailCRUDController getCrudAssDitte() {
		return crudAssDitte;
	}


	public void setCrudAssDitte(SimpleDetailCRUDController crudAssDitte) {
		this.crudAssDitte = crudAssDitte;
	}
	public void caricaDitteInvitate(ActionContext context,File file) throws BusinessProcessException, ComponentException, IOException {
		java.io.InputStream in;
		Ass_contratto_ditteBulk bulk=null;
		try {
			in = new java.io.BufferedInputStream(new FileInputStream(file),(int)file.length());
			
		  HSSFWorkbook wb =new HSSFWorkbook(in);
		  HSSFSheet s =wb.getSheet(wb.getSheetName(0));
		  HSSFRow r;
		  HSSFCell c;
		  String denominazione=new String();
		  String codice_fiscale=new String();
		  String id_fiscale=new String();
		  String ruolo=new String();
		  String denominazione_rti=new String();
		  
		  for(int i=1;i<=s.getLastRowNum();i++){
			  r=s.getRow(i);	  
			  if (r==null)
				  throw new ApplicationException("Formato file non valido!");
			  c=null;
			  denominazione=null;
			  denominazione_rti=null;
			  codice_fiscale=null;
			  id_fiscale=null;
			  ruolo=null;
			  if(r.getLastCellNum()<2 )
				  throw new ApplicationException("Formato file non valido!");
				  c = r.getCell((short)0);
				  if (c!=null && c.getCellType()==1)
					  denominazione=c.getStringCellValue();
				  else if(c!=null && c.getCellType()!=1)
					  throw new ApplicationException("Formato denominazione non valido riga:"+(i+1));	
				  c = r.getCell((short)1);
				  if (c!=null && c.getCellType()==1)
					  codice_fiscale=c.getStringCellValue();
				  else if(c!=null && (c.getCellType()!=1 && c.getCellType()!=3))
					  throw new ApplicationException("Formato codice fiscale non valido riga:"+(i+1));	
				  c = r.getCell((short)2);
				  if (c!=null && c.getCellType()==1)
					  id_fiscale=c.getStringCellValue();
				  else if(c!=null && (c.getCellType()!=1 && c.getCellType()!=3))
					  throw new ApplicationException("Formato id fiscale non valido riga:"+(i+1));	
				  c = r.getCell((short)3);
				  if (c!=null && c.getCellType()==1)
					  ruolo=c.getStringCellValue();
				  else if(c!=null && (c.getCellType()!=1 && c.getCellType()!=3))
					  throw new ApplicationException("Formato ruolo non valido riga:"+(i+1));	
				  c = r.getCell((short)4);
				  if (c!=null && (c.getCellType()!=1 && c.getCellType()!=3))
					  denominazione_rti=c.getStringCellValue();
				  else if(c!=null && c.getCellType()!=1)
					  throw new ApplicationException("Formato denominazione rti non valido riga:"+(i+1));	
				  c = r.getCell((short)5);
				  if ((denominazione!=null || codice_fiscale!=null || id_fiscale!=null ||ruolo!=null  ||denominazione_rti !=null)
					  	&& (((denominazione ==null || (denominazione!=null && !(codice_fiscale!=null  || id_fiscale!=null)))
						  &&((ruolo!=null && denominazione_rti ==null) || (ruolo==null && denominazione_rti !=null)))))
				  throw new ApplicationException("Formato file non valido!");		  
		  }  
		  for(int i=1;i<=s.getLastRowNum();i++){
			  r=s.getRow(i);	  
			  c=null;
			  denominazione=null;
			  denominazione_rti=null;
			  codice_fiscale=null;
			  id_fiscale=null;
			  ruolo=null;
				  c = r.getCell((short)0);
				  if (c !=null && c.getCellType()==1)
					  denominazione=c.getStringCellValue();
				  c = r.getCell((short)1);
				  if (c !=null && c.getCellType()==1)
					  codice_fiscale=c.getStringCellValue();
				  c = r.getCell((short)2);
				  if (c !=null && c.getCellType()==1)
					  id_fiscale=c.getStringCellValue(); 
				  c = r.getCell((short)3);
				  if (c !=null && c.getCellType()==1)
					  ruolo=c.getStringCellValue();
				  c = r.getCell((short)4);
				  if (c !=null && c.getCellType()==1)
					  denominazione_rti=c.getStringCellValue();
				  c = r.getCell((short)5);
				  if(denominazione==null )
					  break;
			 	  bulk = new Ass_contratto_ditteBulk();
				  bulk.setContratto((ContrattoBulk)getModel());
				  if(denominazione!=null)
					  bulk.setDenominazione(denominazione.trim());
				  if(codice_fiscale!=null)
					  bulk.setCodice_fiscale(codice_fiscale.trim());
				  if( id_fiscale!= null)
				     bulk.setId_fiscale(id_fiscale.trim());
				  if( ruolo!= null)	
					  bulk.setRuolo(ruolo.trim());
				  if( denominazione_rti != null)
					   bulk.setDenominazione_rti(denominazione_rti.trim());
				  if(bulk !=null)				
					  getCrudAssDitte().add(context,bulk);  
		  }
		} catch (FileNotFoundException e) {
			  throw new ApplicationException("File non trovato!");
		}
		catch (IllegalArgumentException e) {
			throw new ApplicationException("Formato file non valido!");
		}
		catch (RecordFormatException e) {
			throw new ApplicationException("Errore nella lettura del file!");
		}
	}

	public SimpleDetailCRUDController getCrudDettaglio_contratto() {
		return crudDettaglio_contratto;
	}
}