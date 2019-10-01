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

import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_richiestaBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiRichiestaComponentSession;
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

public class CRUDIncarichiRichiestaBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private boolean utenteAbilitatoPubblicazioneSito;
	private boolean utenteAbilitatoFunzioniIncarichi;
	private Unita_organizzativaBulk uoSrivania;
	
	private SimpleDetailCRUDController incarichiProceduraColl = new SimpleDetailCRUDController( "IncarichiProceduraColl", Incarichi_proceduraBulk.class, "incarichi_proceduraColl", this);

	/**
	 * Primo costruttore della classe <code>CRUDIncarichiRichiestaBP</code>.
	 */
	public CRUDIncarichiRichiestaBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDIncarichiRichiestaBP</code>.
	 * @param String function
	 */
	public CRUDIncarichiRichiestaBP(String function) {
		super(function);
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
	
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);
		if (((Incarichi_richiestaBulk)oggettobulk).isRichiestaScaduta()){
			setStatus(CRUDBP.VIEW);
			setMessage("Richiesta scaduta. Non consentita la modifica.");
		}
		if (((Incarichi_richiestaBulk)oggettobulk).isRichiestaInScadenza()){
			setStatus(CRUDBP.VIEW);
			if (isUtenteAbilitatoFunzioniIncarichi())
				setMessage("Richiesta in scadenza. Inserire l'informazione sulla ricerca di personale interno o procedere con la fase successiva di richiesta attivazione contratto.");
			else
				setMessage("Richiesta in scadenza. Può essere modificata unicamente dal Direttore dell'Istituto.");
		}
		if (((Incarichi_richiestaBulk)oggettobulk).isPubblicazioneInCorso()){
			setStatus(CRUDBP.VIEW);
			setMessage("Richiesta in fase di pubblicazione. E' consentito solo l'annullamento della pubblicazione.");
		}
		else if (((Incarichi_richiestaBulk)oggettobulk).isRichiestaAnnullata()){
			setStatus(CRUDBP.VIEW);
			setMessage("Richiesta annullata. Non consentita la modifica.");
		}
		else if (((Incarichi_richiestaBulk)oggettobulk).isRichiestaCancellata()){
			setStatus(CRUDBP.VIEW);
			setMessage("Richiesta cancellata. Non consentita la modifica.");
		}
		else if (((Incarichi_richiestaBulk)oggettobulk).isRichiestaChiusa()){
			setStatus(CRUDBP.VIEW);
			setMessage("Richiesta chiusa. Non consentita la modifica.");
		}
	}

	public void pubblicaSulSito(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (isInserting())
				save(context);
			setModel(context,((IncarichiRichiestaComponentSession)createComponentSession()).pubblicaSulSito(context.getUserContext(), getModel()));
		}
		catch(Exception e) 
		{
			throw handleException(e);
		}
	}
	        
	public void annullaPubblicazioneSulSito(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		((Incarichi_richiestaBulk)getModel()).setStato(Incarichi_richiestaBulk.STATO_ANNULLATO);
		update(context);
	}

	public boolean isUtenteAbilitatoPubblicazioneSito() {
		return utenteAbilitatoPubblicazioneSito;
	}

	private void setUtenteAbilitatoPubblicazioneSito(boolean utenteAbilitatoPubblicazioneSito) {
		this.utenteAbilitatoPubblicazioneSito = utenteAbilitatoPubblicazioneSito;
	}

	public boolean isROPersonaleInterno(){
		return  ((Incarichi_richiestaBulk)getModel()).isRichiestaChiusa() ||
				!((isUoEnte() ||isUtenteAbilitatoFunzioniIncarichi()) &&
			      ((Incarichi_richiestaBulk)getModel()).isRichiestaInScadenza());
	}

	public boolean isRicercaDaChiudere(){
		return !isROPersonaleInterno() &&
			   (((Incarichi_richiestaBulk)getModel()).isPubblicazioneFinita() &&
				((Incarichi_richiestaBulk)getModel()).isRichiestaInScadenza() &&
				!((Incarichi_richiestaBulk)getModel()).isRichiestaChiusa());
	}

	public boolean isButtonRichiediContrattoEnabled(){
		Incarichi_richiestaBulk richiesta = (Incarichi_richiestaBulk)getModel();
		return ((isUoEnte() || isUtenteAbilitatoFunzioniIncarichi()) &&
				richiesta.isRichiestaInScadenza() && 
				richiesta.getNrRisorseNonTrovate()>0 &&
				richiesta.getNrContrattiAttivati().compareTo(richiesta.getNrRisorseNonTrovate())==-1);
	}

	public void create(ActionContext actioncontext) throws BusinessProcessException {
		if (getModel() instanceof Incarichi_richiestaBulk) {
			Incarichi_richiestaBulk inc = (Incarichi_richiestaBulk)getModel();
			int maxlength = this.getBulkInfo().getFieldProperty("attivita_breve").getMaxLength();
			if (inc.getAttivita_breve()==null && inc.getAttivita()!=null)
		      inc.setAttivita_breve(inc.getAttivita().substring(0,(inc.getAttivita().length()<maxlength)?inc.getAttivita().length():maxlength));
		}
		super.create(actioncontext);
	}

	public void validaChiusuraRicerca(ActionContext actioncontext) throws ValidationException {
		if (getModel() instanceof Incarichi_richiestaBulk) {
			Incarichi_richiestaBulk inc = (Incarichi_richiestaBulk)getModel();
			if (inc.isRichiestaScaduta())
				throw new ValidationException("Non è possibile chiudere una ricerca che risulta già essere scaduta.");
			if (inc.isPubblicazioneInCorso())
				throw new ValidationException("Non è possibile chiudere una ricerca che risulta essere ancora in fase di pubblicazione.");
			if (!inc.isRichiestaInScadenza())
				throw new ValidationException("Non è possibile chiudere una ricerca che si trova in uno stato diverso da quello 'In scadenza'.");
			if (inc.getNr_risorse_da_trovare()==1 && inc.getPersonale_interno()==null)
				throw new ValidationException("Non è possibile chiudere una ricerca senza indicare l'esito della ricerca di personale interno.");
			if (inc.getNr_risorse_da_trovare()>1 && 
				(inc.getNr_risorse_trovate_si()+
				 inc.getNr_risorse_trovate_no()+
				 inc.getNr_risorse_trovate_na()!=inc.getNr_risorse_da_trovare()))
				throw new ValidationException("Non è possibile chiudere una ricerca senza indicare l'esito totale della verifica di professionalità interna.");
		}
	}

	public void validaRichiestaContratto(ActionContext actioncontext) throws ValidationException {
		if (getModel() instanceof Incarichi_richiestaBulk) {
			Incarichi_richiestaBulk inc = (Incarichi_richiestaBulk)getModel();
			if (inc.isRichiestaScaduta())
				throw new ValidationException("Non è possibile chiedere l'attivazione di un contratto sulla base di una richiesta scaduta.");
			if (inc.isPubblicazioneInCorso())
				throw new ValidationException("Non è possibile chiedere l'attivazione di un contratto sulla base di una richiesta ancora in fase di pubblicazione.");
			if (!inc.isRichiestaInScadenza())
				throw new ValidationException("Non è possibile chiedere l'attivazione di un contratto sulla base di una richiesta che si trova in uno stato diverso da quello 'In scadenza'.");
			if (inc.getNr_risorse_trovate_si()==0 && inc.getNr_risorse_trovate_no()==0 && inc.getNr_risorse_trovate_na()==0)
				throw new ValidationException("Non è possibile chiedere l'attivazione di un contratto sulla base di una richiesta senza l'informazione sull'esito della ricerca di personale interno.");
			if (inc.getNr_risorse_da_trovare()==1 && inc.getPersonale_interno()==null)
				throw new ValidationException("Non è possibile chiudere una ricerca senza indicare l'esito della ricerca di personale interno.");
			if (inc.getNr_risorse_trovate_si()+
				inc.getNr_risorse_trovate_no()+
				inc.getNr_risorse_trovate_na()!=inc.getNr_risorse_da_trovare())
				throw new ValidationException("Attenzione! Il risultato dell'esito della ricerca deve essere uguale al numero delle risorse cercate.");
			if (inc.getNr_risorse_da_trovare()==1 && inc.getPersonale_interno().equals(Incarichi_richiestaBulk.PERSONALE_INTERNO_TROVATO))
				throw new ValidationException("Non è possibile chiedere l'attivazione di un contratto sulla base di una richiesta con esito positivo della ricerca di personale interno.");
		}
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
    public void completaUnitaOrganizzativa(it.cnr.jada.action.ActionContext context, Unita_organizzativaBulk uo) throws BusinessProcessException {
    	Incarichi_richiestaBulk incarico = (Incarichi_richiestaBulk)getModel();
    	String indirizzoNew="";
		try {
			if (incarico.getIndirizzo_unita_organizzativa()!=null && incarico.getIndirizzo_unita_organizzativa().equals(incarico.getSede_lavoro()))
				indirizzoNew = Utility.createUnita_organizzativaComponentSession().getIndirizzoUnitaOrganizzativa(context.getUserContext(), uo, true);
		}
		catch(Exception e) 
		{
		}
    	incarico.setUnita_organizzativa(uo);
    	if (indirizzoNew != "") {
    		incarico.setSede_lavoro(indirizzoNew);
    		incarico.setIndirizzo_unita_organizzativa(indirizzoNew);
    	}
	}
	protected Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 3];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.pubblica");
		newToolbar[ i ].setSeparator(true);
		newToolbar[ i+1 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.annullaPubblica");
		newToolbar[ i+1 ].setSeparator(true);
		newToolbar[ i+2 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.goToIncarichiProcedura");
		newToolbar[ i+2 ].setSeparator(true);
		return newToolbar;
	}
	
	public boolean isPubblicaButtonHidden()	{
		return !(isUoEnte()||isUtenteAbilitatoPubblicazioneSito()) ||
			   !(isInserting()||((Incarichi_richiestaBulk)getModel()).isRichiestaProvvisoria());
	}
	
	public boolean isAnnullaPubblicaButtonHidden() {
		return !(isUoEnte()||isUtenteAbilitatoPubblicazioneSito()) ||
		         isInserting()||
		         ((Incarichi_richiestaBulk)getModel()).isRichiestaProvvisoria()||
		         !((Incarichi_richiestaBulk)getModel()).isPubblicazioneInCorso();
	}
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled()||isRicercaDaChiudere();
	}
	public boolean isUtenteAbilitatoFunzioniIncarichi() {
		return utenteAbilitatoFunzioniIncarichi;
	}
	private void setUtenteAbilitatoFunzioniIncarichi(boolean utenteAbilitatoFunzioniIncarichi) {
		this.utenteAbilitatoFunzioniIncarichi = utenteAbilitatoFunzioniIncarichi;
	}
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_richiestaBulk){
			Incarichi_richiestaBulk model = (Incarichi_richiestaBulk)oggettobulk;
			model.caricaNrRisorseTrovateList(actioncontext);
			if (model.getNr_risorse_da_trovare()==1) {
				if (model.getNr_risorse_trovate_si()==1)
					model.setPersonale_interno(Incarichi_richiestaBulk.PERSONALE_INTERNO_TROVATO);
				else if (model.getNr_risorse_trovate_no()==1)
					model.setPersonale_interno(Incarichi_richiestaBulk.PERSONALE_INTERNO_NON_TROVATO);
				else if (model.getNr_risorse_trovate_na()==1)
					model.setPersonale_interno(Incarichi_richiestaBulk.PERSONALE_INTERNO_TROVATO_NON_ADATTO);
			}
		}
		return oggettobulk;
	}
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		if (oggettobulk instanceof Incarichi_richiestaBulk) {
			((Incarichi_richiestaBulk)oggettobulk).setNr_risorse_da_trovare(1);
			((Incarichi_richiestaBulk)oggettobulk).setNr_risorse_trovate_si(0);
			((Incarichi_richiestaBulk)oggettobulk).setNr_risorse_trovate_no(0);
			((Incarichi_richiestaBulk)oggettobulk).setNr_risorse_trovate_na(0);
		}
		return super.initializeModelForInsert(actioncontext, oggettobulk);
	}

	public SimpleDetailCRUDController getIncarichiProceduraColl() {
		return incarichiProceduraColl;
	}

	public void setIncarichiProceduraColl(SimpleDetailCRUDController incarichiProceduraColl) {
		this.incarichiProceduraColl = incarichiProceduraColl;
	}
	public boolean isApriIncarichiProceduraButtonHidden() {
		return isSearching() ||
		       getIncarichiProceduraColl()==null ||
		       getIncarichiProceduraColl().getModel() == null;
	}
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabTestata");
		super.init(config, actioncontext);
	}
}