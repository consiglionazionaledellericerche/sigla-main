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
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Optional;

import javax.ejb.RemoveException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.ejb.PdgAggregatoModuloComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.TipoFinanziamentoBulk;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.UserInfo;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdGAggregatoModuloBP extends it.cnr.jada.util.action.SimpleCRUDBP implements SearchProvider {
	private Parametri_cnrBulk parametriCnr;
	private Parametri_enteBulk parametriEnte;
	private Unita_organizzativaBulk uoScrivania;
	private CdrBulk cdrFromUserContext;
	private Integer pgModulo;
	private Pdg_esercizioBulk cdrPdGP;
	private boolean isUtenteDirettore;
	private Progetto_sipBulk progettoForUpdate;
	private Integer annoFromPianoEconomico;
	
	private SimpleDetailCRUDController crudDettagli = new SimpleDetailCRUDController( "Dettagli", Pdg_moduloBulk.class, "dettagli", this, false) {

		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			validaCancellazionePdgModulo(context, (Pdg_moduloBulk)detail);
		}

		public boolean isFiltered()
		{
			return false;
		}
		public boolean isReadonly()
		{
			return super.isReadonly() && !isUtenteAbilitato() && !isCdrPdGPUtilizzabile();
		}
		public boolean isGrowable()
		{	// r.p. 05/06/2014 Equiparato al controllo che viene da fatto da PDGP! aggiunto isModuloInseribile()
			return super.isGrowable() && isUtenteAbilitato() && isCdrPdGPUtilizzabile() && isModuloInseribile();	
		}
		public boolean isShrinkable()
		{
			return super.isShrinkable() && isUtenteAbilitato() && isCdrPdGPUtilizzabile();	
		}
	};

	public CRUDPdGAggregatoModuloBP() {
		super();
	}

	public CRUDPdGAggregatoModuloBP(String function) {
		super(function);
	}

	public CRUDPdGAggregatoModuloBP(String function, Integer pg_modulo) {
		setPgModulo(pg_modulo);
		new CRUDPdGAggregatoModuloBP(function);
	}
	public static ProgettoRicercaPadreComponentSession getProgettoRicercaPadreComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (ProgettoRicercaPadreComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession",ProgettoRicercaPadreComponentSession.class);
	}	
	public void aggiornaGECO(ActionContext context){
		try {
			getProgettoRicercaPadreComponentSession().aggiornaGECO(context.getUserContext());
		} catch (Exception e) {
			String text = "Errore interno del Server Utente:"+CNRUserContext.getUser(context.getUserContext())+" UO:"+CNRUserContext.getCd_unita_organizzativa(context.getUserContext());
			SendMail.sendErrorMail(text,e.toString());
		}
	}
	protected void initialize(ActionContext context) throws BusinessProcessException {
		try {
			setCdrFromUserContext(((PdgAggregatoModuloComponentSession)createComponentSession()).cdrFromUserContext(context.getUserContext()));
		} catch(Exception e) {
			throw handleException(e);
		} 
		super.initialize(context);
		setUoScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
		try {
			setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
			if (UtenteBulk.isAbilitatoApprovazioneBilancio(context.getUserContext()))
				setUtenteDirettore(true);
			else
				setUtenteDirettore(false);

			setParametriEnte(Utility.createParametriEnteComponentSession().getParametriEnte(context.getUserContext())); 
			if (getParametriEnte().getFl_informix())
				aggiornaGECO(context);
			
			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	   		BigDecimal annoFrom = configSession.getIm01(context.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
	   		if (Optional.ofNullable(annoFrom).isPresent())
	   			setAnnoFromPianoEconomico(annoFrom.intValue());
		} catch (ComponentException e1) {
			throw handleException(e1);
		} catch (RemoteException e1) {
			throw handleException(e1);
		}
		try {
			cerca(context);
		} catch(Exception e) {
			throw handleException(e);
		}
		caricaCdrPdGP(context);
		crudDettagli.setMultiSelection(true);
		crudDettagli.setOrderBy(context, "progetto.cd_progetto", it.cnr.jada.util.OrderConstants.ORDER_ASC);
		evidenziaModulo(context);
		if (((CdrBulk)getModel()).getCd_centro_responsabilita()!=null) {
			if (!isCdrPdGPUtilizzabile()) {
				setStatus(VIEW);
				setEditable(false);
				setErrorMessage("Lo stato del PdGP - CDR per il CdR "+((CdrBulk)getModel()).getCd_centro_responsabilita()+" risulta non impostato oppure\nè chiusa la fase previsionale per l'esercizio "+CNRUserContext.getEsercizio(context.getUserContext())+". Non consentita la modifica.");
			}
		}
	}
	public void evidenziaModulo(ActionContext context) throws BusinessProcessException{
		//Mi posiziono sul Modulo richiesto dalla procedura esterna
		if (getPgModulo() != null){
			try {
				int index = 0;
				boolean trovato = false;
				ProgettoBulk modulo = new ProgettoBulk(((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio(),getPgModulo(),ProgettoBulk.TIPO_FASE_PREVISIONE);
				Selection selCopy = getCrudDettagli().getSelection();
				for(java.util.Iterator i = ((CdrBulk)getModel()).getDettagli().iterator(); i.hasNext() && !trovato;){
					Pdg_moduloBulk dettaglio = (Pdg_moduloBulk)i.next();
					if (dettaglio.getProgetto().equalsByPrimaryKey(modulo)){
						selCopy.setFocus(index);
						trovato = true;
					}
					index++;
				}				
				getCrudDettagli().setSelection(context,selCopy);
			}catch (ValidationException e) {
				throw handleException(e);
			}			 
		}
	}
    protected Button[] createToolbar()
	{
		Button abutton[] = new Button[13];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
		//abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
		//abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");

		if (this.getParametriCnr()!=null && this.getParametriCnr().getFl_nuovo_pdg())
			abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.inserisciProgetti");
		else
			abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.inserisciModuli");
		abutton[i-1].setSeparator(true);

		it.cnr.jada.util.jsp.Button button = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.consultazionePianoRiparto");
		button.setSeparator(true);
		abutton[i++] = button;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.consultazioneLimitiSpesa");
		abutton[i-1].setSeparator(true);
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.scaricaCostiPersonale");
		abutton[i-1].setSeparator(true);
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.annullaScaricaCostiPersonale");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.statoCdRPdGP");
		abutton[i-1].setSeparator(true);
		return abutton;
	}

	public boolean isModuliButtonHidden()
	{
		return false;
	}
	
	public boolean isScaricaDipHidden()
	{
		return false;
	}

	public boolean isAnnullaScaricaDipHidden()
	{
		return false;
	}

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagli() {
		return crudDettagli;
	}

	/**
	 * Stabilisce se i campi Stato e Cambia Stato sono abilitati 
	 *  
	 * @return 	true - campi Stato e Cambia Stato in sola lettura
	 * 			false - campi Stato e Cambia Stato utilizzabili
	 */
	public boolean isROStato() {
		if (!isEditable() || (getStatus()!= INSERT && getStatus()!= EDIT) || getCrudDettagli().getModel()==null || getCrudDettagli().isReadonly() ||
			(getCrudDettagli().getModel()!=null && ((Pdg_moduloBulk)getCrudDettagli().getModel()).getStatiKeys().size()<2))
			return true;
		else
			return false;
	}

	/**
	 * Stabilisce se l'utente è abilitato ad effettuare inserimenti 
	 * e cancellazioni delle testate dei moduli, in base al fatto 
	 * che il CdR in scrivania sia di primo livello per l'utente
	 *  
	 * @return 	true - utente abilitato
	 * 			false - utente non abilitato
	 */
	public boolean isUtenteAbilitato() {
		CdrBulk cdr = (CdrBulk)getModel();
		if (getCdrFromUserContext().getCd_centro_responsabilita().equals(cdr.getCd_centro_responsabilita()))
			return true;
		else
			return false;
	}

	/**
	 * Stabilisce se è utilizzabile il CdR preimpostato in mappa
	 * dato che deve essere precaricata la tabella pdg_esercizio
	 * con l'anno di esercizio del PdG e con tale CdR 
	 *  
	 * @return 	true - CdR preimpostato utilizzabile
	 * 			false - CdR preimpostato non utilizzabile
	 */
	public boolean isCdrPdGPUtilizzabile() {
		if (getCdrPdGP()!= null &&
		  	!getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_APPROVAZIONE_CDR) &&
			/*!getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR) &&*/
			!getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_CHIUSURA_GESTIONALE_CDR))
			return true;
		else
			return false;
	}

	public boolean isModuloInseribile() {
		return getCdrPdGP()!= null && getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_CDR);
	}
	
	/**
	 * Stabilisce se i pulsanti della contrattazione devono impostare i BP chiamati
	 * in sola visualizzazione o in inserimento/modifica in base al valore di stato
	 * del record su pdg_modulo
	 *  
	 * @return 	true - BP chiamati in sola visualizzazione
	 * 			false - BP chiamati in inserimento/modifica
	 */
	public boolean isROContrattazioni() {
		Pdg_moduloBulk det = (Pdg_moduloBulk) getCrudDettagli().getModel();
		if (det!=null &&
			(det.getStato().equals(Pdg_moduloBulk.STATO_AC) || det.getStato().equals(Pdg_moduloBulk.STATO_AD)))
			return false;
		return true;
	}
	
	public boolean isROModuloEntrate() {
		// se lo stato del CdR è in esame dal centro il 999.000 può entrare in
		// modifica sulla mappa delle entrate dato che ci sono dei campi editabili
		if (getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_IN_ESAME_CDR) && isUoEnte())
			return false;
			
		Pdg_moduloBulk det = (Pdg_moduloBulk) getCrudDettagli().getModel();
		if (det!=null &&
			(det.getStato().equals(Pdg_moduloBulk.STATO_AC) || det.getStato().equals(Pdg_moduloBulk.STATO_AD)))
			return false;
		return true;
	}

	/**
	 * @return
	 */
	public CdrBulk getCdrFromUserContext() {
		return cdrFromUserContext;
	}

	/**
	 * @param bulk
	 */
	public void setCdrFromUserContext(CdrBulk bulk) {
		cdrFromUserContext = bulk;
	}

	public void cerca(ActionContext actioncontext)
		throws RemoteException, InstantiationException, RemoveException, BusinessProcessException
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
				reset(actioncontext);
				setStatus(SEARCH);
			}
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}
	/**
	 * @return
	 */
	public Integer getPgModulo() {
		return pgModulo;
	}

	/**
	 * @param integer
	 */
	public void setPgModulo(Integer integer) {
		pgModulo = integer;
	}

	public boolean isModuliButtonEnabled() {
		if (!isEditable())
			return false;
		else
			// r.p. 05/06/2014 Equiparato al controllo che viene da fatto da PDGP!
			//if(isGestionaleAccessibile()) 
			if (isModuloInseribile())
				return true;
			return false;
	}
	
	public boolean isScaricaDipEnabled() {
		if (!isEditable())
			return false;
		else
			return true;
	}
	public boolean isAnnullaScaricaDipEnabled() {
		if (!isEditable())
			return false;
		else
			return true;
	}
	public Pdg_esercizioBulk getCdrPdGP() {
		return cdrPdGP;
	}

	public void setCdrPdGP(Pdg_esercizioBulk cdrPdGP) {
		this.cdrPdGP=cdrPdGP;
	}

	/**
	 * Carica il record Pdg_esercizio con il CdR impostato in mappa
	 * e l'esercizio in scrivania, utilizzato per verificare l'abilitazione
	 * all'utilizzo della mappa PdG Preliminare
	 * 
	 */
	public void caricaCdrPdGP(ActionContext actioncontext) throws BusinessProcessException {
		try
		{
			CdrBulk cdr = (CdrBulk)getModel();
			setCdrPdGP((Pdg_esercizioBulk) ((PdgAggregatoModuloComponentSession)createComponentSession()).getCdrPdGP(actioncontext.getUserContext(),cdr));
		} catch(Exception e) {
			throw handleException(e);
		} 
	}

	public void validaCancellazionePdgModulo(ActionContext context, Pdg_moduloBulk pdg_modulo) throws ValidationException {
		try {
			PdgAggregatoModuloComponentSession comp = (PdgAggregatoModuloComponentSession)createComponentSession();
			comp.validaCancellazionePdgModulo(context.getUserContext(), pdg_modulo);
		} catch (Throwable e) {
			throw new ValidationException(e.getMessage());
		}
	}

	public boolean isStatoCdRPdGPButtonHidden() throws ComponentException, RemoteException {
		return !isUtenteDirettore();
	}

	public boolean isStatoCdRPdGPButtonEnabled() {
		return true;
	}

	/**
	 * @return
	 */
	public boolean isUtenteDirettore() {
		return isUtenteDirettore;
	}

	/**
	 * @param b
	 */
	public void setUtenteDirettore(boolean b) {
		isUtenteDirettore = b;
	}

	public Unita_organizzativaBulk getUoScrivania() {
		return uoScrivania;
	}

	public void setUoScrivania(Unita_organizzativaBulk bulk) {
		uoScrivania = bulk;
	}

	public boolean isUoEnte(){
		return (getUoScrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
	}
	
	/**
	 * Stabilisce se sono accessibili i tasti di accesso al gestionale
	 *  
	 * @return 	true - tasti gestionale accessibili
	 * 			false - tasti gestionale non accessibili
	 */
	public boolean isGestionaleAccessibile() {
		if (getCdrPdGP()!= null &&
			(getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR) ||
			 getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_CHIUSURA_GESTIONALE_CDR)))
			return true;
		else
			return false;
	}

	/**
	 * Stabilisce se si può operare (modificare) il gestionale
	 *  
	 * @return 	true - gestionale operabile
	 * 			false - gestionale non operabile
	 */
	public boolean isGestionaleOperabile() {
		if (getCdrPdGP()!= null &&
			getCdrPdGP().getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR) &&
			getCrudDettagli().getModel()!=null && 
			((Pdg_moduloBulk)getCrudDettagli().getModel()).getStato().equals(Pdg_moduloBulk.STATO_AG))
			return true;
		else
			return false;
	}

	private void setParametriCnr(Parametri_cnrBulk parametriCnr) {
		this.parametriCnr = parametriCnr;
	}
	
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}
	
	public void setParametriEnte(Parametri_enteBulk parametriEnte) {
		this.parametriEnte = parametriEnte;
	}
	public Parametri_enteBulk getParametriEnte() {
		return parametriEnte;
	}

    public Progetto_sipBulk getProgettoForUpdate() {
        return progettoForUpdate;
    }

    public CRUDPdGAggregatoModuloBP setProgettoForUpdate(Progetto_sipBulk progettoForUpdate) {
        this.progettoForUpdate = progettoForUpdate;
        return this;
    }

    public String getHintProgetto(UserInfo userInfo) {
    	Optional<TipoFinanziamentoBulk> optTipoFinanziamento = 
    			Optional.ofNullable(getCrudDettagli().getModel())
													.filter(Pdg_moduloBulk.class::isInstance)
													.map(Pdg_moduloBulk.class::cast)
													.flatMap(pdg_moduloBulk -> Optional.ofNullable(pdg_moduloBulk.getProgetto()))
													.flatMap(progetto_sipBulk -> Optional.ofNullable(progetto_sipBulk.getOtherField()))
													.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()));

		//Richiesta ufficio bilancio del 09/2023 di consentire a tutte le UO di caricare la previsione di spesa... non solo alla UO Coordinatrice
    	boolean isUoCoordinatrice = Boolean.TRUE;

		return optTipoFinanziamento
				.map(tipoFinanziamentoBulk -> "Tipologia di Finanziamento: "
						.concat(tipoFinanziamentoBulk.getDescrizione())
						.concat("<BR>")
						.concat("Previsione Entrata ").concat(tipoFinanziamentoBulk.getFlPrevEntSpesa() ? " consentita" : " non consentita")
						.concat("<BR>")
						.concat("Previsione Spesa ").concat(tipoFinanziamentoBulk.getFlPrevEntSpesa() ? (isUoCoordinatrice?" consentita":"") : " consentita solo per le spese accentrate")
                        .concat("<BR>")
                        .concat("Ripartizione Costi del Personale ").concat(tipoFinanziamentoBulk.getFlRipCostiPers() ? " consentita" : " non consentita")
				)
				.orElse("")
				.concat(!isUoCoordinatrice?
						Optional.ofNullable(getCrudDettagli().getModel())
								.filter(Pdg_moduloBulk.class::isInstance)
								.map(Pdg_moduloBulk.class::cast)
								.flatMap(pdg_moduloBulk -> Optional.ofNullable(pdg_moduloBulk.getProgetto()))
								.map(progetto_sipBulk -> 
									optTipoFinanziamento.filter(TipoFinanziamentoBulk::getFlPrevEntSpesa).isPresent()?
										"<BR>Previsione entrata/spesa decentrata consentita solo alla Uo Coordinatrice "+progetto_sipBulk.getCd_unita_organizzativa():
										"<BR>Uo Coordinatrice "+progetto_sipBulk.getCd_unita_organizzativa())
								.orElse(""):"");
	}

    public boolean isPrevEntrataEnable(UserInfo userInfo) {
		return isPrevEntSpesaEnable(userInfo, Elemento_voceHome.GESTIONE_ENTRATE);
	}

    public boolean isPrevSpesaEnable(UserInfo userInfo) {
		return isPrevEntSpesaEnable(userInfo, Elemento_voceHome.GESTIONE_SPESE);
	}

    private boolean isPrevEntSpesaEnable(UserInfo userInfo, String tipo) {
		return Optional.ofNullable(getCrudDettagli().getModel())
				.filter(Pdg_moduloBulk.class::isInstance)
				.map(Pdg_moduloBulk.class::cast)
				.flatMap(pdg_moduloBulk -> Optional.ofNullable(pdg_moduloBulk.getProgetto()))
				.map(progetto_sipBulk->
					Optional.ofNullable(this.getAnnoFromPianoEconomico())
							.map(el->el.compareTo(Optional.ofNullable(userInfo)
									.filter(CNRUserInfo.class::isInstance)
			                        .map(CNRUserInfo.class::cast)
			                        .map(CNRUserInfo::getEsercizio)
			                        .orElse(null))>0)
							.orElse(Boolean.TRUE) ||
					Optional.ofNullable(progetto_sipBulk.getOtherField())
							.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()))
							.map(tipoFinanziamentoBulk -> tipo.equals(Elemento_voceHome.GESTIONE_SPESE) ||
														  tipoFinanziamentoBulk.getFlPrevEntSpesa())
							.orElse(Boolean.FALSE))
				.orElse(Boolean.FALSE) ||
                Optional.ofNullable(userInfo)
                        .filter(CNRUserInfo.class::isInstance)
                        .map(CNRUserInfo.class::cast)
                        .map(CNRUserInfo::getUnita_organizzativa)
                        .map(Unita_organizzativaBulk::isUoEnte)
                        .orElse(Boolean.FALSE);
	}

	@Override
	public SearchProvider getSearchProvider(OggettoBulk oggettobulk, String s) {
		if(Optional.ofNullable(oggettobulk)
				.filter(Progetto_sipBulk.class::isInstance).isPresent())
			return this;
		return super.getSearchProvider(oggettobulk, s);
	}

	@Override
	public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
		Pdg_moduloBulk pdg_modulo = new Pdg_moduloBulk();
		CdrBulk cdr = (CdrBulk) getModel();
		BulkList dettagli = cdr.getDettagli();

		Iterator itr = dettagli.iterator();
		while(itr.hasNext()) {
			compoundfindclause.addClause("AND","pg_progetto", SQLBuilder.NOT_EQUALS,((Pdg_moduloBulk)itr.next()).getPg_progetto());
		}

		Progetto_sipBulk progetto = new Progetto_sipBulk();
		return find(actioncontext, compoundfindclause, progetto, pdg_modulo, "progetto");
	}
	
	private Integer getAnnoFromPianoEconomico() {
		return annoFromPianoEconomico;
	}
	
	public void setAnnoFromPianoEconomico(Integer annoFromPianoEconomico) {
		this.annoFromPianoEconomico = annoFromPianoEconomico;
	}
}
