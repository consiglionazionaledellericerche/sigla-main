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

package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiRepertorioComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.upload.UploadedFile;

import java.rmi.RemoteException;
import java.util.Iterator;

public class CRUDIncarichiRepertorioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private static final long LUNGHEZZA_MAX=0x1000000;
	private final SimpleDetailCRUDController ripartizionePerAnno = new SimpleDetailCRUDController("Incarichi_repertorio_annoColl",Incarichi_repertorio_annoBulk.class,"incarichi_repertorio_annoColl",this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_annoBulk repertorioAnno = (Incarichi_repertorio_annoBulk)oggettobulk;
			if (repertorioAnno.getEsercizio_limite()==null)
				throw new ValidationException("Valorizzare l'esercizio di imputazione finanziaria.");
			if (repertorioAnno.getIncarichi_repertorio().isIncaricoProvvisorio())
				repertorioAnno.setImporto_complessivo(repertorioAnno.getImporto_iniziale());
			super.validate(actioncontext,oggettobulk);
		}
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_annoBulk repertorioAnno = (Incarichi_repertorio_annoBulk)oggettobulk;
			if (repertorioAnno !=  null)
				if (repertorioAnno.getImporto_utilizzato() != null && 
					repertorioAnno.getImporto_utilizzato().compareTo(Utility.ZERO)!=0)
					throw new ValidationException("Eliminazione non possibile!\nL'importo relativo all'anno selezionato risulta già utilizzato.");
				if (repertorioAnno.getIncarichi_repertorio() != null && 
				    !repertorioAnno.getIncarichi_repertorio().isIncaricoProvvisorio())
					if (repertorioAnno.getImporto_iniziale()!=null && repertorioAnno.getImporto_iniziale().compareTo(Utility.ZERO)!=0) 
						throw new ValidationException("Eliminazione non possibile!\nLa richiesta di incarico è già stata pubblicata.\nE' possibile solo modificare l'importo complessivo.");
			super.validateForDelete(actioncontext, oggettobulk);
		}
	};
	private SimpleDetailCRUDController crudArchivioAllegati = new SimpleDetailCRUDController( "ArchivioAllegati", Incarichi_repertorio_archivioBulk.class, "archivioAllegati", this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			// TODO Auto-generated method stub
			Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.ArchivioAllegati.blob");

			if ( allegato.getNome_file() == null ) {
				if (file == null || file.getName().equals(""))
					throw new ValidationException("Attenzione: selezionare un File da caricare.");
			}

			if (!(file == null || file.getName().equals(""))) { 
				if (file.length() > LUNGHEZZA_MAX)
					throw new ValidationException("Attenzione: la dimensione del file è superiore alla massima consentita (10 Mb).");

				allegato.setFile(file.getFile());
				allegato.setNome_file(allegato.parseFilename(file.getName()));
				allegato.setToBeUpdated();
				getParentController().setDirty(true);
			}
			
			super.validate(actioncontext, oggettobulk);
		}
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_archivioBulk archivio = (Incarichi_repertorio_archivioBulk)oggettobulk;
			if (archivio !=  null)
				if (!archivio.isToBeCreated()) 
					if (archivio.getIncarichi_repertorio() != null)
						if (archivio.getIncarichi_repertorio().isIncaricoDefinitivo() &&
							archivio.isContratto())
							throw new ValidationException("Eliminazione non possibile!\nLa richiesta di incarico è già stata resa definitiva.");
			
			super.validateForDelete(actioncontext, oggettobulk);
		}
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			if (oggettobulk instanceof Incarichi_repertorio_archivioBulk) {
				Incarichi_repertorio_archivioBulk archivio = (Incarichi_repertorio_archivioBulk)oggettobulk;
				if (!archivio.isToBeCreated()) {
					archivio.setStato(Incarichi_repertorio_archivioBulk.STATO_ANNULLATO);
					archivio.setToBeUpdated();
					return archivio;
				}
			}
			return super.removeDetail(oggettobulk, i);
		}
	};
	
	private SimpleDetailCRUDController compensiAllegati = new SimpleDetailCRUDController( "CompensiAllegati", CompensoBulk.class, "compensiColl", ripartizionePerAnno);
	private Incarichi_proceduraBulk incaricoProceduraOrigine;
	private boolean utenteAbilitatoPubblicazioneSito;
	private boolean utenteAbilitatoFunzioniIncarichi;
	private Unita_organizzativaBulk uoSrivania;
	private boolean verificaFormatoBando = Boolean.FALSE; 
	
	/**
	 * Primo costruttore della classe <code>CRUDIncarichiRepertorioBP</code>.
	 */
	public CRUDIncarichiRepertorioBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDIncarichiRepertorioBP</code>.
	 * @param String function
	 */
	public CRUDIncarichiRepertorioBP(String function) {
		super(function);
	}
	public CRUDIncarichiRepertorioBP(String function, OggettoBulk bulk) {
		super(function);
		if (bulk instanceof Incarichi_proceduraBulk){
			setIncaricoProceduraOrigine((Incarichi_proceduraBulk)bulk);
		}
	}
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk=super.initializeModelForInsert(actioncontext, oggettobulk);
		
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)oggettobulk;

		if (getIncaricoProceduraOrigine()!=null){
			incarico.setIncarichi_procedura(getIncaricoProceduraOrigine());
			incarico.setCds(getIncaricoProceduraOrigine().getCds());
			incarico.setUnita_organizzativa(getIncaricoProceduraOrigine().getUnita_organizzativa());
			//ripulisco il dato per evitare che, se crea un nuovo incarico, riproponga il 
			//collegamento
			setIncaricoProceduraOrigine(null);
		}

		incarico.setUtenteCollegatoUoEnte(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext).getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
		incarico.setDt_registrazione(DateServices.getDt_valida(actioncontext.getUserContext()));
		return incarico;
	}
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			oggettobulk=super.initializeModelForEdit(actioncontext, oggettobulk);
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)oggettobulk;
			for ( Iterator i = incarico.getIncarichi_repertorio_annoColl().iterator(); i.hasNext(); ) {
				Incarichi_repertorio_annoBulk repAnno = (Incarichi_repertorio_annoBulk) i.next();
				repAnno.caricaAnniList(actioncontext);
			}
			if ((incarico.isIncaricoDefinitivo()) && (isUoEnte() || isUtenteAbilitatoPubblicazioneSito()))
				incarico.setDataProrogaEnableOnView(Boolean.TRUE);

			incarico.setUtenteCollegatoUoEnte(isUoEnte());
			
			return incarico;
		} catch(javax.ejb.EJBException ejbe){
			throw handleException(ejbe);
		} 
	}
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabTestata");
		setEditable(false);
		super.init(config, actioncontext);
	}
	public void findTipiRapporto(ActionContext context) throws BusinessProcessException{
		try{
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)getModel();
			if (incarico.getTerzo()!= null) {
				IncarichiRepertorioComponentSession sess = (IncarichiRepertorioComponentSession)createComponentSession();
				java.util.Collection coll = sess.findTipiRapporto(context.getUserContext(), incarico);
				incarico.setTipiRapporto(coll);

				if(coll == null || coll.isEmpty()){
					incarico.setTipo_rapporto(null);
					throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi Rapporto validi associati al contraente selezionato");
				} 
				else if (incarico.getTipo_rapporto()!=null) {
					boolean trovato=false;
					for ( Iterator i = coll.iterator(); i.hasNext(); ) {
						if (((OggettoBulk)i.next()).equalsByPrimaryKey(incarico.getTipo_rapporto())){
							trovato=true;
							break;	
						}
					}
					if (!trovato)
						incarico.setTipo_rapporto(null);
				}
			}else{
				incarico.setTipo_rapporto(null);
			}

		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public final SimpleDetailCRUDController getRipartizionePerAnno() {
		return ripartizionePerAnno;
	}

	private Incarichi_proceduraBulk getIncaricoProceduraOrigine() {
		return incaricoProceduraOrigine;
	}

	private void setIncaricoProceduraOrigine(
			Incarichi_proceduraBulk incaricoProceduraOrigine) {
		this.incaricoProceduraOrigine = incaricoProceduraOrigine;
	}
	
	public boolean isROIncaricoProcedura() {
		return getIncaricoProceduraOrigine()!=null;
	}

	public boolean isROContraente() {
		Incarichi_repertorioBulk model = (Incarichi_repertorioBulk)getModel();
		return (!isSearching() && 
			   ((model.getDt_inizio_validita()==null || model.getDt_fine_validita()==null) ||
	            (model.getTerzo() == null || model.getTerzo().getCrudStatus() == OggettoBulk.NORMAL)));
	}

	public boolean isUtenteAbilitatoPubblicazioneSito() {
		return utenteAbilitatoPubblicazioneSito;
	}

	private void setUtenteAbilitatoPubblicazioneSito(boolean utenteAbilitatoPubblicazioneSito) {
		this.utenteAbilitatoPubblicazioneSito = utenteAbilitatoPubblicazioneSito;
	}

	private Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}

	private void setUoSrivania(Unita_organizzativaBulk uoSrivania) {
		this.uoSrivania = uoSrivania;
	}

	public boolean isUoEnte(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
    }

	protected void initialize(ActionContext context) throws BusinessProcessException {
		super.initialize(context);
		try {
			setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
			setUtenteAbilitatoPubblicazioneSito(UtenteBulk.isAbilitatoPubblicazioneSito(context.getUserContext()));
			setUtenteAbilitatoFunzioniIncarichi(UtenteBulk.isAbilitatoFunzioniIncarichi(context.getUserContext()));
		} catch (ComponentException e1) {
			throw handleException(e1);
		} catch (RemoteException e1) {
			throw handleException(e1);
		}
	}

	public void completaTerzo(ActionContext context, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk contraente) throws BusinessProcessException {

		try {

			IncarichiRepertorioComponentSession component = (IncarichiRepertorioComponentSession)createComponentSession();
			Incarichi_repertorioBulk incaricoClone = component.completaTerzo(context.getUserContext(), incarico, contraente);

			setModel(context, incaricoClone);

		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}

	}
	public SimpleDetailCRUDController getCrudArchivioAllegati() {
		return crudArchivioAllegati;
	}
	public void setCrudArchivioAllegati(SimpleDetailCRUDController controller) {
		crudArchivioAllegati = controller;
	}
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
	  if (getTab("tab").equals("tabIncarichi_repertorio_allegati"))
		openForm(context,action,target,"multipart/form-data");
	  else
		super.openForm(context, action, target);
	}
	
	public void delete(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try 
		{
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getModel();
			//In fase di inserimento si permette di eliminare
			if ( incarico.isIncaricoProvvisorio() )
				super.delete(actioncontext);
			 // si tratta di uno storno
			else if ( incarico.isIncaricoDefinitivo() ) {
				if (!isUoEnte()&&!isUtenteAbilitatoFunzioniIncarichi())
					throw new it.cnr.jada.comp.ApplicationException("Eliminazione consentita solo ad utenti con l'abilitazione alle funzioni di direttore di istituto.");
				if (isIncaricoUtilizzato(actioncontext))
					chiudiIncarico(actioncontext);
				else
					stornaIncarico(actioncontext);
			}
			else // stato = STORNATA
				throw new it.cnr.jada.comp.ApplicationException("Lo stato dell'incarico non ne consente la cancellazione/storno");
			
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

	protected Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 1];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.goToIncarichiProcedura");
		newToolbar[ i ].setSeparator(true);
		return newToolbar;
	}
	public String[][] getTabs() {
		   return new String[][] {
		           { "tabIncarichi_repertorio_contratto","Contratto","/incarichi00/tab_incarichi_repertorio_contratto.jsp" },
		           { "tabIncarichi_repertorio_anno","Importo per anno","/incarichi00/tab_incarichi_repertorio_anno.jsp" },
		           { "tabIncarichi_repertorio_allegati","Allegati","/incarichi00/tab_incarichi_repertorio_allegati.jsp" }
		           };   
	}
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);
		setStatus(CRUDBP.VIEW);
	}
	public void stornaIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try 
		{
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getModel();
			if ( incarico.isIncaricoDefinitivo() )
				setModel(actioncontext, ((IncarichiRepertorioComponentSession)createComponentSession()).stornaIncaricoPubblicato(actioncontext.getUserContext(),getModel()));
			else // stato = STORNATA
				throw new BusinessProcessException( "Lo stato dell'incarico non ne consente lo storno");
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}
	
	public boolean isSalvaDefinitivoButtonHidden() {
		return !isEditing()||
			   ((Incarichi_repertorioBulk) getModel()).getIncarichi_procedura().isProceduraScaduta() ||		
		       ((Incarichi_repertorioBulk) getModel()).getIncarichi_procedura().isProceduraDefinitiva() ||
		       ((Incarichi_repertorioBulk) getModel()).isIncaricoDefinitivo();
	}

	public boolean isSalvaDefinitivoButtonEnabled() {
		return !isSalvaDefinitivoButtonHidden() &&
			   !((Incarichi_repertorioBulk) getModel()).getIncarichi_procedura().isProceduraScaduta() &&	
			   !((Incarichi_repertorioBulk) getModel()).getIncarichi_procedura().isProceduraDefinitiva() &&
		       !((Incarichi_repertorioBulk) getModel()).isIncaricoDefinitivo();
	}
	public void salvaDefinitivo(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (((Incarichi_repertorioBulk) getModel()).getIncarichi_procedura().getProcedura_amministrativa()==null ||
				!((Incarichi_repertorioBulk) getModel()).getIncarichi_procedura().getProcedura_amministrativa().getCd_proc_amm().equals("INC4")) {
				if (((Incarichi_repertorioBulk) getModel()).getContratto()==null)
					throw new it.cnr.jada.comp.ApplicationException("Allegare all'incarico un \""+Incarichi_repertorio_archivioBulk.tipo_archivioKeys.get(Incarichi_repertorio_archivioBulk.TIPO_CONTRATTO).toString()+"\".");
			}
			setModel(context,((IncarichiRepertorioComponentSession)createComponentSession()).salvaDefinitivo(context.getUserContext(), getModel()));
		}
		catch(Exception e) 
		{
			throw handleException(e);
		}
	}
	protected void resetTabs(ActionContext context) {
		setTab("tab","tabIncarichi_repertorio_contratto");
		setTab("tabIncarichiRepAnno","tabIncarichiRepAnnoImporti");
	}
	public boolean isIncaricoUtilizzato(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			/*Ricarico l'oggetto nel caso in cui qualcuno abbia, da un'altra sessione, caricato compensi sull'incarico*/
			Incarichi_repertorioBulk incDB = (Incarichi_repertorioBulk)createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), getModel());
			return incDB.getImporto_utilizzato().compareTo(Utility.ZERO)==1;
		}
		catch(Exception e) 
		{
			throw handleException(e);
		}
	}
	public void chiudiIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try 
		{
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getModel();
			if ( incarico.isIncaricoDefinitivo() )
				setModel(actioncontext, ((IncarichiRepertorioComponentSession)createComponentSession()).chiudiIncaricoPubblicato(actioncontext.getUserContext(),getModel()));
			else // stato = STORNATA
				throw new BusinessProcessException( "Lo stato dell'incarico non ne consente lo storno");
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}
	public boolean isAnnullaDefinitivoButtonHidden() {
		return !((Incarichi_repertorioBulk) getModel()).isIncaricoDefinitivo() || !isUoEnte();
	}
	public boolean isAnnullaDefinitivoButtonEnabled() {
		return !isAnnullaDefinitivoButtonHidden() &&
			   ((Incarichi_repertorioBulk) getModel()).isIncaricoDefinitivo();
	}
	public void annullaDefinitivo(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			setModel(context,((IncarichiRepertorioComponentSession)createComponentSession()).annullaDefinitivo(context.getUserContext(), getModel()));
		}
		catch(Exception e) 
		{
			throw handleException(e);
		}
	}
	public boolean isDeleteButtonEnabled() {
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getModel();
		return super.isDeleteButtonEnabled()&&
			    !incarico.getIncarichi_procedura().isProceduraScaduta()&& 
			   (!incarico.isIncaricoDefinitivo()||(isUoEnte()||isUtenteAbilitatoFunzioniIncarichi()));
	}

	public boolean isVerificaFormatoBando() {
		return verificaFormatoBando;
	}

	public void setVerificaFormatoBando(boolean verificaFormatoBando) {
		this.verificaFormatoBando = verificaFormatoBando;
	}
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		return super.initializeModelForSearch(actioncontext, oggettobulk);
	}
	public SimpleDetailCRUDController getCompensiAllegati() {
		return compensiAllegati;
	}
	public boolean isTabIncarichiRepAnnoCompensiEnabled() {
		return (getModel()!=null &&
				getRipartizionePerAnno()!=null &&
				getRipartizionePerAnno().getModel() != null &&
				!((Incarichi_repertorio_annoBulk)getRipartizionePerAnno().getModel()).getCompensiColl().isEmpty());
	}
	public boolean isUtenteAbilitatoFunzioniIncarichi() {
		return utenteAbilitatoFunzioniIncarichi;
	}
	private void setUtenteAbilitatoFunzioniIncarichi(boolean utenteAbilitatoFunzioniIncarichi) {
		this.utenteAbilitatoFunzioniIncarichi = utenteAbilitatoFunzioniIncarichi;
	}
	public boolean isApriIncarichiProceduraButtonHidden() {
		return isSearching() ||
			   getModel() == null ||
		       ((Incarichi_repertorioBulk)getModel()).getIncarichi_procedura()==null;
	}
}
