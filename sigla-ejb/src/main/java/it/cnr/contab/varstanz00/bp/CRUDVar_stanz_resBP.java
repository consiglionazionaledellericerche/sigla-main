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
 * Created on Feb 16, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.ejb.RemoveException;
import javax.servlet.http.HttpSession;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDVar_stanz_resBP extends SimpleCRUDBP {
	private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita_scrivania;
	private it.cnr.contab.config00.sto.bulk.CdsBulk centro_di_spesa_scrivania;
	private Unita_organizzativaBulk uoSrivania;
	private Accertamento_modificaBulk acrMod;
    private Integer annoFromPianoEconomico;
	private Progetto_rimodulazioneBulk mainProgettoRimodulazione;
	private boolean uoRagioneria;
	private boolean supervisore=false;

	private SimpleDetailCRUDController crudAssCDR = new SimpleDetailCRUDController( "AssociazioneCDR", Ass_var_stanz_res_cdrBulk.class, "associazioneCDR", this) {
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			validaAssociazioneCDRPerCancellazione(context, (Ass_var_stanz_res_cdrBulk)detail);
		}
	};
	public void validaAssociazioneCDRPerCancellazione(ActionContext context, Ass_var_stanz_res_cdrBulk assBulk) throws ValidationException {
		/*try {
			PdGVariazioniComponentSession comp = (PdGVariazioniComponentSession)createComponentSession();
			comp.validaAssociazioneCDRPerCancellazione(context.getUserContext(), assBulk);
		} catch (Throwable e) {
			throw new ValidationException(e.getMessage());
		}*/
	}

	/**
	 * 
	 */
	public CRUDVar_stanz_resBP() {
		super();
	}

	/**
	 * @param s
	 */
	public CRUDVar_stanz_resBP(String s) {
		super(s);
	}
	
	public CRUDVar_stanz_resBP(String s, Accertamento_modificaBulk acrMod) {
		super(s);
		setAcrMod(acrMod);
	}

    public CRUDVar_stanz_resBP(String function, Progetto_rimodulazioneBulk rimodulazione) {
		super(function);
		setMainProgettoRimodulazione(rimodulazione);
    }
    
	protected void resetTabs(it.cnr.jada.action.ActionContext context) {
		setTab("tab","tabTestataVarStanzRes");
	}
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssCDR() {
		return crudAssCDR;
	}

	/**
	 * @param controller
	 */
	public void setCrudAssCDR(SimpleDetailCRUDController controller) {
		crudAssCDR = controller;
	}
	/**
	 * Gestione del salvataggio come definitiva di una variazione
	 *
	 * @param context	L'ActionContext della richiesta
	 * @throws BusinessProcessException	
	 */
	public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			edit(context,comp.salvaDefinitivo(context.getUserContext(), getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	public void validaOrigineFontiPerAnnoResiduo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		Var_stanz_resBulk var = (Var_stanz_resBulk)getModel();
		VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
		try {
			if (var.getEsercizio_res() == null)
				throw new ApplicationException("Valorizzare l'esercizio residuo.");
			comp.validaOrigineFontiPerAnnoResiduo(context.getUserContext(), var.getEsercizio_residuo(), var.getTipologia_fin());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}
	/**
	 * Gestione del salvataggio come approvata di una variazione
	 *
	 * @param context	L'ActionContext della richiesta
	 * @throws BusinessProcessException	
	 */
	public void approva(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			Var_stanz_resBulk var_stanz_res = null;
			if (isDaAccertamentoModifica()) {
				var_stanz_res = (Var_stanz_resBulk)comp.controllaApprova(context.getUserContext(), getModel());
			}
			else {
				var_stanz_res = (Var_stanz_resBulk)comp.approva(context.getUserContext(), getModel());
				var_stanz_res = (Var_stanz_resBulk)comp.esitaVariazioneBilancio(context.getUserContext(), var_stanz_res);
				edit(context,var_stanz_res);
			}
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void controllaApprova(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk) comp.controllaApprova(context.getUserContext(), getModel());
			var_stanz_res.setApprovazioneControllata(true);
			setModel(context,var_stanz_res);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	/**
	 * Gestione del salvataggio come respinta di una variazione
	 *
	 * @param context	L'ActionContext della richiesta
	 * @throws BusinessProcessException	
	 */
	public void respingi(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			edit(context,comp.respingi(context.getUserContext(), getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void statoPrecedente(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			edit(context,comp.statoPrecedente(context.getUserContext(), getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}		
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
		super.basicEdit(context, bulk, doInitializeForEdit);

		if (getStatus()!=VIEW){
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)getModel();
			if (var_stanz_res!=null && 
					(var_stanz_res.isCancellatoLogicamente() || 
					 var_stanz_res.isPropostaDefinitiva() || 
					 var_stanz_res.isRespinta())) {
				setStatus(VIEW);
			}	
		}

		try
        {
    		if (isDaAccertamentoModifica()) {
    			Var_stanz_resBulk var = (Var_stanz_resBulk)getModel();
    			if (var !=null && var.isPropostaDefinitiva() && !var.isApprovazioneControllata())
    				controllaApprova(context);
            }
        }
        catch(Throwable ex)
        {
        	setMessage(ex.getMessage());
        }
        
      
	}		
	
	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		try {
			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	        BigDecimal annoFrom = configSession.getIm01(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
	        if (Optional.ofNullable(annoFrom).isPresent())
	            setAnnoFromPianoEconomico(annoFrom.intValue());
	    } catch (Throwable e) {
	        throw new BusinessProcessException(e);
	    }
		super.init(config, actioncontext);
	}
	
	protected void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.initialize(context);
		try {
			setCentro_responsabilita_scrivania(Utility.createCdrComponentSession().cdrFromUserContext(context.getUserContext()));
			setAbilitatoModificaDescVariazioni(UtenteBulk.isAbilitatoModificaDescVariazioni(context.getUserContext()));
			setAttivaGestioneVariazioniTrasferimento(Utility.createParametriEnteComponentSession().getParametriEnte(context.getUserContext()).getFl_variazioni_trasferimento());
			setSupervisore(Utility.createUtenteComponentSession().isSupervisore(context.getUserContext()));

			String uoRagioneria = Utility.createConfigurazioneCnrComponentSession().getUoRagioneria(context.getUserContext(),CNRUserContext.getEsercizio(context.getUserContext()));
			setUoRagioneria(Optional.ofNullable(uoRagioneria).map(el->el.equals(getCentro_responsabilita_scrivania().getCd_unita_organizzativa())).orElse(Boolean.FALSE));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
		
		setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));

		try {
			if (isDaAccertamentoModifica() && getAccMod().getVariazione()!=null) {
				if (getAccMod().getVariazione().getPg_variazione()!=null) {
					setModel(context, getAccMod().getVariazione());
					cerca(context);
				}
			}
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
		Var_stanz_resBulk var = (Var_stanz_resBulk)super.initializeModelForInsert(context,bulk);
		if (isDaAccertamentoModifica()) {
			var.setTipologia(Var_stanz_resBulk.TIPOLOGIA_ECO);
			var.setAccMod(getAccMod());
		}
		var.setProgettoRimodulazione(getMainProgettoRimodulazione());
		var.setAnnoFromPianoEconomico(this.getAnnoFromPianoEconomico());
		return var;
	}
	public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
		Var_stanz_resBulk var = (Var_stanz_resBulk)super.initializeModelForEdit(context,bulk);
		if (isDaAccertamentoModifica())
			var.setAccMod(getAccMod());
		Optional.ofNullable(var)
				.filter(Var_stanz_resBulk.class::isInstance)
				.map(Var_stanz_resBulk.class::cast)
				.ifPresent(el->{
					el.setMapMotivazioneVariazione(Optional.ofNullable(el.getTiMotivazioneVariazione()).orElse(Pdg_variazioneBulk.MOTIVAZIONE_GENERICO));	
					el.setStorageMatricola(el.getIdMatricola());
					el.setAnnoFromPianoEconomico(this.getAnnoFromPianoEconomico());
					el.setProgettoRimodulatoForSearch(
							Optional.ofNullable(el.getProgettoRimodulazione()).flatMap(rim->Optional.ofNullable(rim.getProgetto()))
									.orElse(null));
				});
		return var;
	}
	/**
	 * Verifica che il CDR della variazione PDG sia uguale al CDR di scrivania 
	 */
	public boolean isCdrScrivania() {
		if (getStatus() == SEARCH)
		  return true;
		try{
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)getModel();
			if(var_stanz_res.getCentroDiResponsabilita().equalsByPrimaryKey(getCentro_responsabilita_scrivania()))
			  return true;
			return false;
		}catch(NullPointerException ex){
			return false;
		}catch(java.lang.ArrayIndexOutOfBoundsException ex){
			return false;
		}
	}	
	public boolean isAnnullabile() {
		if (getStatus() == SEARCH)
		  return true;
		return isApprovaButtonEnabled();
	}
	public boolean isSaveButtonEnabled()
	{
		Var_stanz_resBulk varStanzRes = (Var_stanz_resBulk)getModel();
		if (isCdrScrivania() || isUoEnte()) {
			if ((varStanzRes.isApprovata() || varStanzRes.isApprovazioneControllata()) &&
				varStanzRes.isMotivazioneVariazioneBandoPersonale() && varStanzRes.getStorageMatricola()==null)
				return true;
			else if (!isAbilitatoModificaDescVariazioni() && varStanzRes.isApprovata())
				return false;
			else
				return super.isSaveButtonEnabled();
		}
		return false;
	}
	
	public boolean isDeleteButtonEnabled()
	{
		return super.isDeleteButtonEnabled() && (isCdrScrivania() || isUoEnte()) && !((Var_stanz_resBulk)getModel()).isApprovata();
	}
	/**
	 * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 * Il bottone di SalvaDefinitivo è disponibile solo se:
	 * - la proposta è provvisoria
	 * - il CDR è di 1è Livello
	 *
	 * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 */
	public boolean isSalvaDefinitivoButtonEnabled() {
		try{
			return (isSaveButtonEnabled()||(super.isSaveButtonEnabled()&&((Var_stanz_resBulk)getModel()).isPropostaProvvisoria()))&& 
					((Var_stanz_resBulk)getModel()).isPropostaProvvisoria() && 
					((Var_stanz_resBulk)getModel()).isNotNew() &&
					(controllaCdrDaAccMod() || isUoArea()|| isUoSac())&&
					controllaBP() &&
					((Var_stanz_resBulk)getModel()).getCentroDiResponsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds());
		}catch(NullPointerException e){
			return false;
		}			
	}
	private boolean controllaBP() {
		return (!isDirty() || !isDaAccertamentoModifica());
	}

	public boolean isStatoPrecedenteButtonEnabled() {
		try{
			final Var_stanz_resBulk varStanzResBulk = (Var_stanz_resBulk) getModel();
			return (isSaveButtonEnabled()|| varStanzResBulk.isPropostaDefinitiva() || varStanzResBulk.isAnnullata()) &&
					varStanzResBulk.isNotNew() &&
					varStanzResBulk.getCentroDiResponsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds());
		} catch (NullPointerException e){
			return false;
		}
		
	}
	
	/**
	 * Restituisce il valore della proprietà 'approvaButtonEnabled'
	 * Il bottone di Approva è disponibile solo se:
	 * - è attivo il bottone di salvataggio
	 * - la proposta di variazione PDG è definitiva
	 * - la UO che sta effettuando l'operazione è di tipo ENTE
	 *
	 * @return Il valore della proprietà 'approvaButtonEnabled'
	 */
	public boolean isApprovaButtonEnabled() {
		try{
			return ((Var_stanz_resBulk)getModel()).isPropostaDefinitiva() && 
			   ((isUoEnte()&& ((Var_stanz_resBulk)getModel()).isEnteAbilitatoAdApprovare())||
				((controllaCdrDaAccMod() || isUoArea())&&
				((Var_stanz_resBulk)getModel()).getCentroDiResponsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds())&&
				((Var_stanz_resBulk)getModel()).isCdsAbilitatoAdApprovare()));
		}catch(NullPointerException e){
			return false;
		}
			
	}
	/**
	 * ritorna true se proviene dalla mappa degli accertamenti modifica, nel senso
	 * che la variazione è legata ad una modifica ad accertamento residuo, in
	 * tal caso non è necessario che il cdr sia di primo livello
	 * @return
	 */
	public boolean controllaCdrDaAccMod() {
		try{
			return (getCentro_responsabilita_scrivania().getLivello().intValue() == 1 || isDaAccertamentoModifica());
		}catch(NullPointerException e){
			return false;
		}
			
	}
	public boolean isAssestatoResiduoButtonHidden() {
		Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)getModel();
		if (!(isEditable() && isEditing() && !isDirty()) || var_stanz_res.isApprovata())
		   return true;
		if (var_stanz_res == null)
		  return true;
		if (var_stanz_res.getTipologia() == null)
		  return true;
		//Se non ho selezionato alcun CDR associato allora  
		if (getCrudAssCDR().getSelection().getFocus() ==-1){
			if (var_stanz_res.getCentroDiResponsabilita().equalsByPrimaryKey(getCentro_responsabilita_scrivania()))
			  return false;
			else
			  return true;
		}else{
			Ass_var_stanz_res_cdrBulk ass_var_stanz_res_cdr = (Ass_var_stanz_res_cdrBulk)(var_stanz_res.getAssociazioneCDR().get(getCrudAssCDR().getSelection().getFocus()));
			boolean isCdrDiScrivania = ass_var_stanz_res_cdr.getCentro_di_responsabilita().equalsByPrimaryKey(getCentro_responsabilita_scrivania());
			boolean tipoView =  var_stanz_res.getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO)?
				controllaCdrDaAccMod()? 
				!ass_var_stanz_res_cdr.getCentro_di_responsabilita().getCd_cds().equalsIgnoreCase(getCentro_responsabilita_scrivania().getCd_cds())
			  :!isCdrDiScrivania			
			:!isCdrDiScrivania;
			return tipoView;			
		}
	}
	public boolean isUoEnte(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
	}
	public boolean isUoSac(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0);
	}
	public boolean isUoArea(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_AREA)==0);
	}
	
	/**
	 * 
	 * Restituisce il valore della proprietà 'nonApprovaButtonEnabled'
	 * Il bottone di NonApprova è disponibile solo se:
	 * - è attivo il bottone di salvataggio
	 * - la proposta di variazione PDG è definitiva
	 * - la UO che sta effettuando l'operazione è di tipo ENTE
	 *
	 * @return Il valore della proprietà 'nonApprovaButtonEnabled'
	 */
	public boolean isNonApprovaButtonEnabled() {
		return isApprovaButtonEnabled();
	}
	
	/**
	 * @return
	 */
	public CdsBulk getCentro_di_spesa_scrivania() {
		return centro_di_spesa_scrivania;
	}

	/**
	 * @return
	 */
	public CdrBulk getCentro_responsabilita_scrivania() {
		return centro_responsabilita_scrivania;
	}

	/**
	 * @return
	 */
	public Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}

	/**
	 * @param bulk
	 */
	public void setCentro_di_spesa_scrivania(CdsBulk bulk) {
		centro_di_spesa_scrivania = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setCentro_responsabilita_scrivania(CdrBulk bulk) {
		centro_responsabilita_scrivania = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setUoSrivania(Unita_organizzativaBulk bulk) {
		uoSrivania = bulk;
	}
	/**
	 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
	 * @return toolbar Toolbar in uso
	 */
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[15];
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
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.statoPrecedente");	
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.approva");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.nonApprova");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.assestatoResiduo");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.annullaApprovazione");
		return toolbar;
	}

	public Accertamento_modificaBulk getAccMod() {
		return acrMod;
	}

	public void setAcrMod(Accertamento_modificaBulk acrMod) {
		this.acrMod = acrMod;
	}

	/**
	 * se true la variazione è stata richiamata da una modifica
	 * all'accertamento residuo, in tal caso il processo deve essere
	 * transazionale in modo da creare una modifica e una variazione
	 * in contemporanea
	 * 
	 * @return
	 */
	public boolean isDaAccertamentoModifica() {
		if (getAccMod()!=null)
			return true;
		return false;
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
	public boolean isBringbackButtonEnabled() {
		return super.isBringbackButtonEnabled() || (isDaAccertamentoModifica() && isViewing());
	}
	public boolean isUndoBringBackButtonEnabled() {
		return super.isUndoBringBackButtonEnabled() || (isDaAccertamentoModifica() && isViewing());
	}
	public boolean isNonApprovaButtonHidden() {
		if (isDaAccertamentoModifica())
			return true;
		return false;
	}
	public boolean isStatoPrecedenteButtonHidden() {
		if (isDaAccertamentoModifica())
			return isSalvaDefinitivoButtonEnabled();
		return false;
	}
	public boolean isApprovaButtonHidden() {
		if (isDaAccertamentoModifica())
			return true;
		return false;
	}

	public boolean isAnnullaApprovazioneButtonHidden() {
		return !isSupervisore() || isDaAccertamentoModifica() ||
				Optional.ofNullable(this.getModel())
						.filter(Var_stanz_resBulk.class::isInstance)
						.map(Var_stanz_resBulk.class::cast)
						.map(el->!el.isApprovata())
						.orElse(Boolean.TRUE) ||
				(isUoEnte() && !((Var_stanz_resBulk)getModel()).isEnteAbilitatoAdApprovare()) ||
				(!isUoEnte() && !getCentro_responsabilita_scrivania().getLivello().equals(1)) ||
				(!isUoEnte() && Optional.ofNullable(this.getModel())
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.filter(el->el.getCentroDiResponsabilita()!=null)
					.filter(el->el.getCentroDiResponsabilita().getCd_cds()!=null)
					.filter(el->el.getCentroDiResponsabilita().getCd_cds().equals(getCentro_responsabilita_scrivania().getCd_cds()))
					.map(el->!el.isCdsAbilitatoAdApprovare())
					.orElse(Boolean.TRUE));
	}

	public boolean isROTipologia(){
    	return isDaAccertamentoModifica();
	}    
	
	private boolean abilitatoModificaDescVariazioni;
	
	public boolean isAbilitatoModificaDescVariazioni() {
		return abilitatoModificaDescVariazioni;
	}
	public void setAbilitatoModificaDescVariazioni(boolean abilitatoModificaDescVariazioni) {
		this.abilitatoModificaDescVariazioni = abilitatoModificaDescVariazioni;
	}	

	private boolean attivaGestioneVariazioniTrasferimento;
	
	public boolean isAttivaGestioneVariazioniTrasferimento() {
		return attivaGestioneVariazioniTrasferimento;
	}

	private void setAttivaGestioneVariazioniTrasferimento(boolean attivaGestioneVariazioniTrasferimento) {
		this.attivaGestioneVariazioniTrasferimento = attivaGestioneVariazioniTrasferimento;
	}

	public boolean isVariazioneFromLiquidazioneIvaDaModificare(ActionContext context, Var_stanz_resBulk variazione) throws BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			return comp.isVariazioneFromLiquidazioneIvaDaModificare(context.getUserContext(), variazione);
		} catch (Throwable e) {
			throw new BusinessProcessException(e.getMessage());
		}
	}

	public void aggiornaMotivazioneVariazione(ActionContext context) throws BusinessProcessException{
		Var_stanz_resBulk var = (Var_stanz_resBulk)this.getModel();
		var.setTiMotivazioneVariazione(Pdg_variazioneBulk.MOTIVAZIONE_GENERICO.equals(var.getMapMotivazioneVariazione())
											?null
											:var.getMapMotivazioneVariazione());

		if (!this.isSearching()) {
			if (var.isMotivazioneVariazioneBandoPersonale())
				var.setIdMatricola(null);
			else if (var.isMotivazioneVariazioneProrogaPersonale() || var.isMotivazioneVariazioneAltreSpesePersonale())
				var.setIdBando(null);
			else {
				var.setIdMatricola(null);
				var.setIdBando(null);
			}
		}
	}
	
	@Override
	public void validate(ActionContext actioncontext) throws ValidationException {
		if (this.isAttivaGestioneVariazioniTrasferimento()) 
			Optional.ofNullable(getModel())
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.filter(el->!Var_stanz_resBulk.TIPOLOGIA_STO.equals(el.getTipologia()) || el.getMapMotivazioneVariazione()!=null)
					.orElseThrow(()->new ValidationException("Occorre indicare la motivazione per cui viene effettuata la variazione."));
		super.validate(actioncontext);
	}
	
	public Progetto_rimodulazioneBulk getMainProgettoRimodulazione() {
		return mainProgettoRimodulazione;
	}
	
	private void setMainProgettoRimodulazione(Progetto_rimodulazioneBulk mainProgettoRimodulazione) {
		this.mainProgettoRimodulazione = mainProgettoRimodulazione;
	}

    public void findAndSetRimodulazione(ActionContext actioncontext, ProgettoBulk progetto) throws BusinessProcessException {
    	try {
    		if (Optional.ofNullable(progetto).isPresent()) {
	    		List<Progetto_rimodulazioneBulk> list = new BulkList<Progetto_rimodulazioneBulk>(this.createComponentSession().find(actioncontext.getUserContext(), ProgettoBulk.class, "findRimodulazioni", progetto.getPg_progetto()));
	    		((Var_stanz_resBulk)this.getModel()).setProgettoRimodulazione(list.stream().filter(Progetto_rimodulazioneBulk::isStatoValidato).findFirst().orElse(null));
    	    }
    	} catch (Throwable e) {
	        throw handleException(e);
	    }
    }
    
    protected Integer getAnnoFromPianoEconomico() {
        return annoFromPianoEconomico;
    }

    public void setAnnoFromPianoEconomico(Integer annoFromPianoEconomico) {
        this.annoFromPianoEconomico = annoFromPianoEconomico;
    }
    
    public String[][] getTabs(HttpSession session) {
        TreeMap<Integer, String[]> pages = new TreeMap<Integer, String[]>();
        int i = 0;
        
        pages.put(i++, new String[]{"tabTestataVarStanzRes", "Testata", "/pdg01/tab_var_stanz_res_testata.jsp"});
        pages.put(i++, new String[]{"tabCDR", "CDR abilitati a concorrervi", "/pdg01/tab_ass_var_stanz_res_cdr.jsp"});
        
        if (Optional.ofNullable(this.getAnnoFromPianoEconomico())
        			.filter(el->el.compareTo(CNRUserContext.getEsercizio(HttpActionContext.getUserContext(session)))<=0)
        			.isPresent())
        	pages.put(i++, new String[]{"tabRimodulazione", "Rimodulazione Progetto", "/pdg01/tab_var_stanz_res_rimodulazione.jsp"});

        String[][] tabs = new String[i][3];

        for (int j = 0; j < i; j++)
            tabs[j] = new String[]{pages.get(j)[0], pages.get(j)[1], pages.get(j)[2]};
        return tabs;
    }

    public boolean isUoRagioneria() {
		return uoRagioneria;
	}

	private void setUoRagioneria(boolean uoRagioneria) {
		this.uoRagioneria = uoRagioneria;
	}

	public boolean isSupervisore() {
		return supervisore;
	}

	public void setSupervisore(boolean supervisore) {
		this.supervisore = supervisore;
	}

	public void annullaApprovazione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {
			if (!isSupervisore())
				throw new ApplicationException("Operazione consentita solo ad utente di tipo Supervisore. Aggiornamento non possibile!");
			VariazioniStanziamentoResiduoComponentSession comp = (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			edit(context,comp.annullaApprovazione(context.getUserContext(), getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
}
