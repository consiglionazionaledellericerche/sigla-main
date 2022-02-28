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

package it.cnr.contab.utenze00.action;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.utente00.nav.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

import java.security.Principal;
import java.util.Optional;

/**
 * Action utilizzata per la gestione utenze.
 */
public class GestioneUtenteAction extends it.cnr.jada.util.action.BulkAction {
	public GestioneUtenteAction() {
		super();
	}
	/**
	 * Gestisce l'invalidazione della sessione utente (logout)
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doApriListaMessaggi(ActionContext context) {
		try {
			String server_url = null;
			if (context instanceof HttpActionContext)
				server_url = it.cnr.jada.util.jsp.JSPUtils.getBaseUrl(((HttpActionContext)context).getRequest());
			it.cnr.contab.messaggio00.bp.ListaMessaggiBP bp = (it.cnr.contab.messaggio00.bp.ListaMessaggiBP)context.createBusinessProcess("ListaMessaggiBP",new Object[] { "V",server_url });
			getComponentSession().notificaMessaggi(context.getUserContext(),server_url);
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la richiesta di espansione di un nodo del menu applicativo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param cd_nodo codice del nodo da aprire	
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doApriMenu(ActionContext context,String cd_nodo) {
		try {
			GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess();
			if (Optional.ofNullable(bp)
					.map(GestioneUtenteBP::getParentRoot)
					.map(BusinessProcess::isBootstrap)
					.orElse(Boolean.FALSE)) {
				context.invalidateSession();
				return context.findForward("logout");
			}
			Albero_mainBulk nodo = bp.getNodoAlbero_main(cd_nodo);
			if (nodo == null)
				bp.addNodoAlbero_main(
					nodo = getComponentSession().generaAlberoPerUtente(
						context.getUserContext(),
						bp.getUserInfo().getUtente(),
						bp.getUserInfo().getUnita_organizzativa() == null ?
							null :
							bp.getUserInfo().getUnita_organizzativa().getCd_unita_organizzativa(),
						cd_nodo,
						(short)0));
			bp.espandiNodo(nodo);
			return context.findForward("menu_tree");
		}catch(NullPointerException e){
			setMessage(context, 0, "Selezionare l'Unità  Organizzativa");
			return context.findDefaultForward();
		}catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la richiesta di cambiamento dell'UO di scrivania
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doCambiaUnitaOrganizzativa(ActionContext context) {
		try {
			if (isCurrentBPDirty(context)) {
				it.cnr.jada.util.action.OptionBP optionbp = openContinuePrompt(context,"doConfermaCambiaUnitaOrganizzativa");
				return optionbp;
			}
			CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
			UtenteBulk utente = ui.getUtente();
			if (utente.isSupervisore())
				return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaCds(context);
			else
				return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaUnitaOrganizzative(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la richiesta di cambiamento dell'UO di scrivania
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doListaUnitaOrganizzativa(ActionContext context) {
		try {
			if (isCurrentBPDirty(context)) {
				it.cnr.jada.util.action.OptionBP optionbp = openContinuePrompt(context,"doConfermaListaUnitaOrganizzativa");
				return optionbp;
			}
			CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
			UtenteBulk utente = ui.getUtente();
			return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).listaUnitaOrganizzative(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la richiesta di cambiamento dell'UO di scrivania
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doCambiaCdr(ActionContext context) {
		try {
			if (isCurrentBPDirty(context)) {
				it.cnr.jada.util.action.OptionBP optionbp = openContinuePrompt(context,"doConfermaCambiaCdr");
				return optionbp;
			}
			CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
			if (ui.getUnita_organizzativa()==null) {
				SelezionatoreListaBP bp = (SelezionatoreListaBP)context.getBusinessProcess();
				bp.setErrorMessage("Selezionare l'Unitè  organizzativa");
				return context.findDefaultForward();
			}
			UtenteBulk utente = ui.getUtente();
			return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaCdr(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la richiesta di chiusura della gerarchia di un nodo dell'albero applicativo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param cd_nodo codice del nodo da chiudere
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doChiudiMenu(ActionContext context,String cd_nodo) {
		try {
			GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess();
			bp.chiudiNodoEspanso(cd_nodo);
			return context.findForward("menu_tree");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doCloseAll(ActionContext context) {
		try {
			GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
			context.setBusinessProcess(bp);
			bp.closeAllChildren(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doCloseForm(ActionContext context) {
		return context.findForward("sessionExpired");
	}
	/**
	 * Gestisce la conferma della richiesta di cambiamento dell'UO di scrivania
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param option	
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doConfermaCambiaUnitaOrganizzativa(ActionContext context,int option) {
		try {
			if (option == it.cnr.jada.util.action.OptionBP.NO_BUTTON)
				return context.findDefaultForward();
			CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
			UtenteBulk utente = ui.getUtente();
			if (utente.isSupervisore())
				return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaCds(context);
			else
				return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaUnitaOrganizzative(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la conferma della richiesta di cambiamento dell'UO di scrivania
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param option	
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doConfermaListaUnitaOrganizzativa(ActionContext context,int option) {
		try {
			if (option == it.cnr.jada.util.action.OptionBP.NO_BUTTON)
				return context.findDefaultForward();
			CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
			UtenteBulk utente = ui.getUtente();
			return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaUnitaOrganizzative(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doConfermaCambiaCdr(ActionContext context,int option) {
		try {
			if (option == it.cnr.jada.util.action.OptionBP.NO_BUTTON)
				return context.findDefaultForward();
			CNRUserInfo ui = (CNRUserInfo)context.getUserInfo();
			UtenteBulk utente = ui.getUtente();
			return ((GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP")).cercaCdr(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la conferma di selezione effettuata su un nodo dell'albero applicativo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param optionbp	
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConfermaSelezioneMenu(it.cnr.jada.action.ActionContext context,it.cnr.jada.util.action.OptionBP optionbp) {
		try {
			it.cnr.contab.utenze00.bp.GestioneUtenteBP bp = (it.cnr.contab.utenze00.bp.GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
			if (optionbp.getOption() == OptionBP.NO_BUTTON)
				return context.findDefaultForward();
			bp.closeAllChildren(context);
			it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = bp.getUserInfo().getUnita_organizzativa();			
			it.cnr.contab.utenze00.bulk.Albero_mainBulk nodo = getComponentSession().validaNodoPerUtente(context.getUserContext(),bp.getUserInfo().getUtente(),uo == null ? null : uo.getCd_unita_organizzativa(), (String)optionbp.getAttribute("cd_nodo"));
			return startNodo(context,bp,nodo);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doControllaMessaggi(ActionContext context,String cd_nodo) {
		try {
			it.cnr.contab.utenze00.bp.GestioneUtenteBP bp = (it.cnr.contab.utenze00.bp.GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
			return context.findForward("mailbar");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doDefault(ActionContext context) {
		try {
			if (context.getBusinessProcessRoot(false) == null)
				return context.findDefaultForward();
			GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
			if (bp == null)
				return context.findDefaultForward();
			context.setBusinessProcess(bp);
			bp.closeAllChildren();
			CNRUserInfo userInfo = (CNRUserInfo)context.getUserInfo();
			if (userInfo.getUnita_organizzativa() == null)
				getBusinessProcess(context).cercaUnitaOrganizzative(context);
			return context.findForward("desktop");
		} catch(NoSuchBusinessProcessException e) {
			return context.getBusinessProcessRoot(true);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce l'invalidazione della sessione utente (logout)
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doLogout(ActionContext context) {
		doCloseAll(context);
		context.invalidateSession();
		final Optional<KeycloakPrincipal> principalOptional = Optional.ofNullable(context)
				.filter(HttpActionContext.class::isInstance)
				.map(HttpActionContext.class::cast)
				.map(HttpActionContext::getRequest)
				.flatMap(request -> Optional.ofNullable(request.getUserPrincipal()))
				.filter(KeycloakPrincipal.class::isInstance)
				.map(KeycloakPrincipal.class::cast);
		if (principalOptional.isPresent()) {
			Optional.ofNullable(principalOptional.get().getKeycloakSecurityContext())
					.filter(RefreshableKeycloakSecurityContext.class::isInstance)
					.map(RefreshableKeycloakSecurityContext.class::cast)
					.ifPresent(rKSC -> {
						rKSC.logout(rKSC.getDeployment());
					});
		}
		return context.findForward("logout");
	}
	/**
	 * Gestisce l'azione di selezione di un esercizio tra quelli disponibili
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSelezionaEsercizio(ActionContext context) {
		try {
			GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess();
	
			// salvo l'esercizio corrente
			Integer esercizio = bp.getUserInfo().getEsercizio();
			bp.getUserInfo().fillFromActionContext(context,null,it.cnr.jada.util.action.FormController.EDIT,bp.getFieldValidationMap());
	
			CNRUserContext userContext = new CNRUserContext(
				bp.getUserInfo().getUtente().getCd_utente(),
				context.getSessionId(),
				bp.getUserInfo().getEsercizio(),
				CNRUserContext.getCd_unita_organizzativa(context.getUserContext()),
				CNRUserContext.getCd_cds(context.getUserContext()),
				CNRUserContext.getCd_cdr(context.getUserContext()));
	
			// Se il nuovo esercizio è bloccato ripristino l'esercizio corrente
			// e informo l'utente.
			try {			
				LoginAction.getComponentSession().registerUser(userContext,context.getApplicationId());
				// Remmato Marco Spasiano 28/02/2006 per problema di sessioni attive
				//UnregisterUser.registerUnregisterUser((HttpActionContext)context);
			} catch(it.cnr.jada.comp.ApplicationException e) {
				bp.getUserInfo().setEsercizio(esercizio);
				bp.setErrorMessage(e.getMessage());
				return context.findForward("desktop");
			}
			
			if (!bp.getUserInfo().getUtente().isUtenteComune()) {
				context.setUserContext(userContext);
				return context.findForward("desktop");
			}
			return getBusinessProcess(context).cercaUnitaOrganizzative(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce le azioni di controllo e validazione della richiesta di apertura dall'albero main di una certa funzione applicativa
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param cd_nodo codice del nodo su cui è stata effettuata la richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSelezionaMenu(ActionContext context,String cd_nodo) {
		it.cnr.contab.utenze00.bp.GestioneUtenteBP bp = null;
		try {
			bp = Optional.ofNullable(context.getBusinessProcess("/GestioneUtenteBP"))
					.filter(GestioneUtenteBP.class::isInstance)
					.map(GestioneUtenteBP.class::cast)
					.orElseThrow(() -> new NoSuchBusinessProcessException());
			if (isCurrentBPDirty(context)) {
				it.cnr.jada.util.action.OptionBP optionbp = openContinuePrompt(context,"doConfermaSelezioneMenu");
				optionbp.addAttribute("cd_nodo", cd_nodo);
				return optionbp;
			}
			it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = bp.getUserInfo().getUnita_organizzativa();
			bp.closeAllChildren(context);
			it.cnr.contab.utenze00.bulk.Albero_mainBulk nodo = getComponentSession().validaNodoPerUtente(context.getUserContext(),bp.getUserInfo().getUtente(),uo == null ? null : uo.getCd_unita_organizzativa(),cd_nodo);
			if (nodo == null) return context.findDefaultForward();
			return startNodo(context,bp,nodo);
		} catch (NoSuchBusinessProcessException _ex){
			return context.findForward("sessionExpired");
		} catch (Throwable e) {
		     if (bp.getParentRoot().isBootstrap()) {
		        bp.setErrorMessage(e.getMessage());
                ((HttpActionContext)context).getRequest()
                        .setAttribute(it.cnr.jada.action.BusinessProcess.class.getName(), bp);
            }
			return handleException(context,e);
		}
	}

	public Forward doCollapseAll(ActionContext context) {
		it.cnr.contab.utenze00.bp.GestioneUtenteBP bp = (it.cnr.contab.utenze00.bp.GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
		bp.collapseAllNodi();
		return context.findForward("menu_tree");
	}
	
	/**
	 * Gestisce l'azione di costruzione della gerarchia applicativa (albero main) in funzione delle abilitazioni (accessi)
	 * e unitè  organizzativa di scrivania selezionata dall'utente
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doSelezionaUnitaOrganizzativa(ActionContext context) {
		try {
			GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
			HookForward hook = (HookForward)context.getCaller();
			it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)hook.getParameter("focusedElement");
			bp.setUserInfo((CNRUserInfo)hook.getParameter("userInfo"));
			context.setUserInfo(bp.getUserInfo());
			if (uo != null)
				bp.getUserInfo().setUnita_organizzativa(uo);
			context.setUserContext(new CNRUserContext(
				bp.getUserInfo().getUtente().getCd_utente(),
				context.getSessionId(),
				bp.getUserInfo().getEsercizio(),
				bp.getUserInfo().getUnita_organizzativa().getCd_unita_organizzativa(),
				bp.getUserInfo().getUnita_organizzativa().getCd_unita_padre(),
				bp.getUserInfo().getCdr().getCd_centro_responsabilita()));
			bp.setRadiceAlbero_main(context, getComponentSession().generaAlberoPerUtente(context.getUserContext(),bp.getUserInfo().getUtente(),uo.getCd_unita_organizzativa(),null,(short)0));
			return context.findForward("desktop");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public GestioneUtenteBP getBusinessProcess(ActionContext context) {
		return (GestioneUtenteBP)context.getBusinessProcess();
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà  'componentSession'
	 *
	 * @return Il valore della proprietà  'componentSession'
	 * @throws javax.ejb.EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
	 * @throws java.rmi.RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
	 */
	public static GestioneLoginComponentSession getComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class);
	}
	public Forward handleException(ActionContext context, Throwable ex) {
		try {
			throw ex;
		} catch(ValidationException e) {
			setErrorMessage(context,e.getMessage());
			return context.findDefaultForward();
		} catch(java.text.ParseException e) {
			setErrorMessage(context,"Errore di formattazione");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return super.handleException(context,e);
		}
	}
	private boolean isCurrentBPDirty(ActionContext context) {
		it.cnr.jada.action.BusinessProcess currentbp = context.getBusinessProcess();
		try {
			if (currentbp instanceof it.cnr.jada.util.action.FormController) {
				it.cnr.jada.util.action.FormController formbp = (it.cnr.jada.util.action.FormController)currentbp;
				formbp.fillModel(context);
			}
		} catch(it.cnr.jada.bulk.FillException e) {
		}
		boolean dirty = false;
		while(currentbp != null && !dirty) {
			if (currentbp instanceof it.cnr.jada.util.action.FormController) {
				it.cnr.jada.util.action.FormController formbp = (it.cnr.jada.util.action.FormController)currentbp;
				dirty = dirty || formbp.isDirty();
			}
			currentbp = currentbp.getParent();
		}
		return dirty;
	}
	public boolean isThreadsafe(ActionContext context) {
		return true;
	}
	/**
	 * Gestisce l'inizializzazione del business process legato ad un nodo dell'albero applicativo
	 *
	 * @param context	L'ActionContext della richiesta
	 * @param bp business process di gestione delle utenze	
	 * @param nodo nodo in processo	
	 * @return Il Forward alla pagina di risposta
	 */
	protected it.cnr.jada.action.Forward startNodo(it.cnr.jada.action.ActionContext context,it.cnr.contab.utenze00.bp.GestioneUtenteBP bp,it.cnr.contab.utenze00.bulk.Albero_mainBulk nodo) {
		BusinessProcess currentbp = context.getBusinessProcess();
		try {
			context.setBusinessProcess(bp);
			it.cnr.jada.action.BusinessProcess newbp;
			if (nodo.getTi_funzione() != null)
				newbp = context.createBusinessProcess(nodo.getBusiness_process(),new Object[] { nodo.getTi_funzione() });
			else
				newbp = context.createBusinessProcess(nodo.getBusiness_process());
			if (newbp == null) return context.findDefaultForward();
			context.addBusinessProcess(newbp);
			newbp.initBusinessProcess(context);
			return context.findDefaultForward();
		} catch(it.cnr.jada.action.BusinessProcessException e) {
			if (currentbp.getParent() != null)
				context.setBusinessProcess(currentbp);
			return handleException(context,e);
		} catch(Throwable e) {
			if (currentbp.getParent() != null)
				context.setBusinessProcess(currentbp);
			return handleException(context,e);
		}
	}
	
	public Forward doCallPreferiti(ActionContext context, String businessProcessName, String tiFunzione){
		GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
		Object[] params = new Object[]{};
		try {
			if (!tiFunzione.equalsIgnoreCase("C")){
				params = new Object[]{tiFunzione};
			}				
			BusinessProcess newbp = bp.getUserInfo().createBusinessProcess(context, businessProcessName, params);
			return context.addBusinessProcess(newbp);
		} catch (BusinessProcessException e) {
			return handleException(context,e);
		}
	}
}
