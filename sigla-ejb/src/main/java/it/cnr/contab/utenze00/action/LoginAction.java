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

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.utente00.nav.comp.GestioneLoginComponent;
import it.cnr.contab.utente00.nav.comp.UtenteLdapNuovoException;
import it.cnr.contab.utente00.nav.comp.UtenteMultiploException;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bp.GestioneUtenteBP;
import it.cnr.contab.utenze00.bp.LoginBP;
import it.cnr.contab.utenze00.bp.SelezionaCdsBP;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.action.FormBP;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.IDToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (17/04/2003 15.45.23)
 *
 * @author: Simonetta Costa
 */
public class LoginAction extends it.cnr.jada.util.action.BulkAction {
    public static final String NULL = "null";
    public static final String BOOTSTRAP = "bootstrap";
    private static final Logger log = LoggerFactory.getLogger(LoginAction.class);

    /**
     * LoginAction constructor comment.
     */
    public LoginAction() {
        super();
    }

    /**
     * Restituisce il valore della proprietà  'componentSession'
     *
     * @return Il valore della proprietà  'componentSession'
     * @throws EJBException    Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public static GestioneLoginComponentSession getComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession", GestioneLoginComponentSession.class);
    }

    /**
     * Gestisce la richiesta di cambiamento della password dell'utente
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws java.text.ParseException
     */
    public Forward doAssegnaPassword(ActionContext context) throws java.text.ParseException {
        try {
            LoginBP bp = (LoginBP) context.getBusinessProcess();
            fillModel(context);
            CNRUserInfo ui = bp.getUserInfo();
            try {
                ui.validaNuovaPassword();
                ui.getUtente().setUser(ui.getUtente().getCd_utente());
                UtenteBulk utente = getComponentSession().cambiaPassword(context.getUserContext(), ui.getUtente(), ui.getNuovaPassword().toUpperCase());
                ui.setUtente(utente);
            } finally {
                ui.setNuovaPassword(null);
                ui.setConfermaPassword(null);
            }
            return initializeWorkspace(context);
        } catch (ValidationException e) {
            setErrorMessage(context, e.getMessage());
            return context.findForward("cambio_password");
        } catch (java.text.ParseException e) {
            setErrorMessage(context, "Errore di formattazione");
            return context.findForward("cambio_password");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la richiesta di navigazione alla form di cambiamento della password
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws java.text.ParseException
     */
    public Forward doCambiaPassword(ActionContext context) throws java.text.ParseException {
        try {
            Forward forward = doLogin(context, GestioneLoginComponent.VALIDA_FASE_INIZIALE);
            if (forward == null) {
                LoginBP bp = (LoginBP) context.getBusinessProcessRoot(true);
                CNRUserInfo ui = bp.getUserInfo();
                // se l'utente è di tipo LDAP
                if (ui.getUtente().isAutenticatoLdap())
                    forward = context.findForward("cambio_password_ldap");
                    // se l'utente è di tipo SIGLA
                else
                    forward = context.findForward("cambio_password");
            }
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doDefault(ActionContext context) {
        try {
            LoginBP loginbp = (LoginBP) context.getBusinessProcessRoot(true);
            if (Optional.ofNullable(loginbp)
                    .map(LoginBP::getParentRoot)
                    .map(BusinessProcess::isBootstrap)
                    .orElse(Boolean.FALSE)) {
                context.invalidateSession();
                return context.findForward("logout");
            }
            return doInitializeWorkspace(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doDefaultNG(ActionContext context) {
        try {
            return doInitializeWorkspace(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doInitializeWorkspace(ActionContext context) throws BusinessProcessException, ParseException, ComponentException, RemoteException {
        try {
            LoginBP loginbp = (LoginBP) context.getBusinessProcessRoot(true);
            GestioneUtenteBP bp = (GestioneUtenteBP) context.getBusinessProcess("/GestioneUtenteBP");
            context.setBusinessProcess(bp);
            for (Enumeration en = bp.getChildren(); en.hasMoreElements(); ) {
                BusinessProcess bpc = (BusinessProcess) en.nextElement();
                if (bpc instanceof SelezionaCdsBP) {
                    bp.closeAllChildren();
                    CNRUserInfo userInfo = (CNRUserInfo) context.getUserInfo();
                    if (userInfo.getUnita_organizzativa() == null &&
                            !userInfo.getUtente().isUtenteAmministratore() &&
                            !userInfo.getUtente().isSuperutente())
                        bp.cercaCds(context);
                    break;
                } else {
                    bp.closeAllChildren();
                    CNRUserInfo userInfo = (CNRUserInfo) context.getUserInfo();
                    if (userInfo.getUnita_organizzativa() == null &&
                            !userInfo.getUtente().isUtenteAmministratore() &&
                            !userInfo.getUtente().isSuperutente())
                        bp.cercaUnitaOrganizzative(context);
                    break;
                }
            }
        } catch (NoSuchBusinessProcessException _ex) {
            final Forward forward = doLogin(context, GestioneLoginComponent.VALIDA_FASE_INIZIALE);
            if (forward == null)
                return initializeWorkspace(context);
            return forward;
        }
        return context.findForward("home");
    }

    public Forward doLoginIniziale(ActionContext context) throws java.text.ParseException {
        try {
            LoginBP bp = (LoginBP) context.getBusinessProcessRoot(true);
            CNRUserInfo ui = bp.getUserInfo();
            ui.setPassword(null);
            return context.findForward("login");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doEntraUtenteMultiplo(ActionContext context) throws java.text.ParseException {
        try {
            //initializeLdap(context);
            Forward forward = doLogin(context, GestioneLoginComponent.VALIDA_FASE_INIZIALE_UTENTE_MULTIPLO);
            if (forward == null)
                forward = initializeWorkspace(context);

            Optional.ofNullable(context)
                    .filter(HttpActionContext.class::isInstance)
                    .map(HttpActionContext.class::cast)
                    .flatMap(httpActionContext -> Optional.ofNullable(httpActionContext.getParameter("main.utente_multiplo")))
                    .ifPresent(s -> log.warn("Utente Multiplo User: {}", s));

            Optional.ofNullable(context.getBusinessProcess())
                    .filter(FormBP.class::isInstance)
                    .map(FormBP.class::cast)
                    .flatMap(formBP -> Optional.ofNullable(formBP.getMessage()))
                    .ifPresent(s -> log.warn("Utente Multiplo message: {}", s));
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * L'utente è stato riconosciuto come un nuovo utente per autenticazione con LDAP
     * e quindi è stata richiesta la sua user id e la sua password LDAP, in modo da
     * potere associare utente SIGLA con utente LDAP
     *
     * @param context
     * @return
     * @throws java.text.ParseException
     */
    public Forward doEntraLdap(ActionContext context) throws java.text.ParseException {
        try {
            Forward forward = doLogin(context, GestioneLoginComponent.VALIDA_NUOVO_UTENTE_LDAP);
            if (forward == null)
                forward = initializeWorkspace(context);
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * L'utente sta tentando di entrare senza autenticazione LDAP
     * nonostante per lui sia obbligatorio, verifichiamo che ciò sia ancora possibile
     *
     * @param context
     * @return
     * @throws java.text.ParseException
     */
    public Forward doAnnullaLdap(ActionContext context) throws java.text.ParseException {
        try {
            Forward forward = doLogin(context, GestioneLoginComponent.VALIDA_NUOVO_UTENTE_LDAP_ANNULLA);
            if (forward == null)
                forward = initializeWorkspace(context);
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private void fillContextFromRequest(ActionContext context) {
        HttpActionContext httpActionContext = (HttpActionContext) context;
        if (Optional.ofNullable(httpActionContext.getParameter("context.esercizio")).isPresent()) {
            doSelezionaContesto(context,
                    Optional.ofNullable(httpActionContext.getParameter("context.esercizio"))
                            .map(x -> Integer.valueOf(x)).orElse(null),
                    Optional.ofNullable(httpActionContext.getParameter("context.cds"))
                            .map(x -> x).orElse(null),
                    Optional.ofNullable(httpActionContext.getParameter("context.uo"))
                            .map(x -> x).orElse(null),
                    Optional.ofNullable(httpActionContext.getParameter("context.cdr"))
                            .map(x -> x).orElse(null));
        }
    }

    /**
     * Gestisce il login all'applicazione
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     * @throws java.text.ParseException
     */
    public Forward doEntra(ActionContext context) throws java.text.ParseException {
        try {
            Forward forward = doLogin(context, GestioneLoginComponent.VALIDA_FASE_INIZIALE);
            if (forward == null)
                forward = initializeWorkspace(context);
            return forward;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private Forward doLogin(ActionContext context, int faseValidazione) throws java.text.ParseException {
        boolean utentiMultipliFound = false;
        CNRUserInfo ui = null;
        final Optional<Principal> principalOptional = Optional.ofNullable(context)
                .filter(HttpActionContext.class::isInstance)
                .map(HttpActionContext.class::cast)
                .map(HttpActionContext::getRequest)
                .flatMap(request -> Optional.ofNullable(request.getUserPrincipal()));
        if (!principalOptional.isPresent())
            return context.findDefaultForward();
        try {
            if (context.getUserContext() == null)
                context.setUserContext(new CNRUserContext("LOGIN", context.getSessionId(), null, null, null, null));
            LoginBP bp = (LoginBP) context.getBusinessProcessRoot(true);

            ui = bp.getUserInfo();
            bp.fillModel(context);
            context.setUserInfo(ui);

            if (ui.getDescrizioneSessione() != null)
                context.setTracingSessionDescription(ui.getDescrizioneSessione());
            if (ui.getDescrizioneSessione() == null && context.isRequestTracingUser())
                return context.findForward("traced_login");

            UtenteBulk utente = new UtenteBulk();
            utente.setCd_utente(principalOptional.get().getName().toUpperCase());
            if (ui.getLdap_userid() != null) {
                utente.setCd_utente_uid(ui.getLdap_userid());
            }
            final Optional<IDToken> idToken = principalOptional
                    .filter(KeycloakPrincipal.class::isInstance)
                    .map(KeycloakPrincipal.class::cast)
                    .map(KeycloakPrincipal::getKeycloakSecurityContext)
                    .map(keycloakSecurityContext -> {
                        return Optional.ofNullable(keycloakSecurityContext.getIdToken())
                                .orElse(keycloakSecurityContext.getToken());
                    });
            if (idToken.isPresent()) {
                utente.setCd_utente(idToken.get().getPreferredUsername());
                utente.setCd_utente_uid(idToken.get().getPreferredUsername());
            }
            utente.setUtente_multiplo(ui.getUtente_multiplo());
            ui.setUtente(utente);
            try {
                utente = getComponentSession().validaUtente(context.getUserContext(), utente, faseValidazione);
            } catch (UtenteLdapNuovoException e) {
                return context.findForward("login_ldap");
            } catch (UtenteMultiploException e) {
                try {
                    utentiMultipliFound = true;
                    bp.utentiMultipli(context.getUserContext(), utente);
                    return context.findForward("login_multiplo");
                } catch (Exception e2) {
                    ui.setPassword(null);
                    throw e2;
                }
            }

            if (utente == null) {
                setErrorMessage(context, "Nome utente o password sbagliati.");
                return context.findDefaultForward();
            }
            ui.setUserid(utente.getCd_utente());
            if (utente.isAutenticazioneLdap())
                ui.setLdap_userid(utente.getCd_utente_uid());
            ui.setUtente(utente);

            StringBuffer infoUser = new StringBuffer();
            infoUser.append("LogIn User:" + ui.getUtente().getCd_utente());
            if (ui.getUtente().getCd_utente_uid() != null)
                infoUser.append(" LogIn Ldap User:" + ui.getUtente().getCd_utente_uid());
            infoUser.append(" RemoteHost:" + ((HttpActionContext) context).getRequest().getRemoteAddr());
            log.warn(infoUser.toString());
            if (utente.getDt_ultima_var_password() == null && !utente.isAutenticatoLdap())
                return context.findForward("primo_login");
            return null;
        } catch (it.cnr.contab.utente00.nav.comp.PasswordScadutaException e) {
            setErrorMessage(context, "Password scaduta da più di sei mesi.");
            // se l'utente è di tipo LDAP
            if (ui.getUtente().isAutenticatoLdap())
                return context.findForward("password_scaduta_ldap");
                // se l'utente è di tipo SIGLA
            else
                return context.findForward("password_scaduta");
        } catch (it.cnr.contab.utente00.nav.comp.PasswordLdapScadutaException e) {
            setErrorMessage(context, "Password scaduta da più di tre mesi.");
            return context.findForward("password_scaduta_ldap");
        } catch (it.cnr.contab.utente00.nav.comp.UtenteNonValidoException e) {
            setErrorMessage(context, "Utente non più valido o con data di validità scaduta. Contattare l'amministratore utenti di SIGLA");
            return context.findDefaultForward();
        } catch (it.cnr.contab.utente00.nav.comp.UtenteInDisusoException e) {
            setErrorMessage(context, "Utente non utilizzato da più di sei mesi.");
            return context.findDefaultForward();
        } catch (it.cnr.contab.utente00.nav.comp.UtenteLdapException e) {
            setErrorMessage(context, "Utente non più valido. Utilizzare l'utente di accesso ufficiale di tipo \"nome.cognome\"");
            return context.findDefaultForward();
        } catch (it.cnr.contab.utente00.nav.comp.UtenteLdapNonUtenteSiglaException e) {
            setErrorMessage(context, "Utente valido ma che non possiede nessun profilo/abilitazione in SIGLA. Contattare l'amministratore delle utenze Sigla dell'Istituto.");
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public CRUDComponentSession createCRUDComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (CRUDComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
    }

    public Forward doModificaPassword(ActionContext context) throws java.text.ParseException {
        try {
            LoginBP bp = (LoginBP) context.getBusinessProcess();
            fillModel(context);
            CNRUserInfo ui = bp.getUserInfo();
            try {
                ui.validaNuovaPassword();
                ui.getUtente().setUser(ui.getUtente().getCd_utente());
                UtenteBulk utente = getComponentSession().cambiaPassword(context.getUserContext(), ui.getUtente(), ui.getNuovaPassword().toUpperCase());
                ui.setUtente(utente);
            } finally {
                ui.setNuovaPassword(null);
                ui.setConfermaPassword(null);
            }
            return initializeWorkspace(context);
        } catch (ValidationException e) {
            setErrorMessage(context, e.getMessage());
            return context.findForward("cambio_password");
        } catch (java.text.ParseException e) {
            setErrorMessage(context, "Errore di formattazione");
            return context.findForward("cambio_password");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doResetBulkInfos(ActionContext context) {
        try {
            doDefault(context);
            it.cnr.jada.bulk.BulkInfo.resetBulkInfos();
            it.cnr.jada.util.Introspector.resetPropertiesCache();
            if (context instanceof it.cnr.jada.action.HttpActionContext)
                ((it.cnr.jada.action.HttpActionContext) context).resetActionMappings();
            it.cnr.jada.util.Config.getHandler().reset();
            java.beans.Introspector.flushCaches();
            it.cnr.jada.ejb.AdminSession ejbadmin = (it.cnr.jada.ejb.AdminSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_AdminSession", it.cnr.jada.ejb.AdminSession.class);
            ejbadmin.resetPersistentInfos();
            return context.findForward("logout");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    private Forward initializeWorkspace(ActionContext context) throws java.text.ParseException, javax.ejb.EJBException, java.rmi.RemoteException, it.cnr.jada.comp.ComponentException, BusinessProcessException {
        CNRUserInfo ui = (CNRUserInfo) context.getUserInfo();
        LoginBP loginBP = (LoginBP) context.getBusinessProcess();

        UtenteBulk utente = ui.getUtente();
        context.setUserContext(new CNRUserContext(
                loginBP.getUserInfo().getUtente().getCd_utente(),
                context.getSessionId(),
                null,
                null,
                null,
                null));
        Integer[] esercizi = getComponentSession().listaEserciziPerUtente(context.getUserContext(), utente);
        int annoInCorso = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        if (esercizi.length == 0 && !utente.isSuperutente()) {
            context.closeBusinessProcess();
            setErrorMessage(context, "L'utente non ha accessi all'applicazione.");
            return context.findDefaultForward();
        }

        Integer esercizio = esercizi.length == 0 ? null : esercizi[esercizi.length - 1];
        for (int i = 0; i < esercizi.length; i++)
            if (esercizi[i].intValue() == annoInCorso) {
                esercizio = esercizi[i];
                break;
            }
        ui.setEsercizi(esercizi);
        ui.setEsercizio(esercizio);
        /*
         * Recupero i parametri CNR
         * @autor Marco Spasiano 29/11/2004
         */
        Parametri_cnrBulk paramBulk = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession")).getParametriCnr(context.getUserContext(), esercizio);
        if (paramBulk == null) {
            context.closeBusinessProcess();
            setErrorMessage(context, "Parametri CNR non presenti per l'anno: " + esercizio);
            return context.findDefaultForward();
        }
        ui.setCd_tipo_rapporto(paramBulk.getCd_tipo_rapporto());
        fillContextFromRequest(context);

        GestioneUtenteBP bp = (GestioneUtenteBP) context.createBusinessProcess("GestioneUtenteBP");
        context.addBusinessProcess(bp);
        bp.setUserInfo(ui);
        Parametri_enteBulk parametriEnte = ((Parametri_enteComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession")).getParametriEnte(context.getUserContext());
        ui.setLogo(parametriEnte.checkLogo());
        ui.setTipo_db(parametriEnte.getTipo_db());

        if (utente.isSuperutente() || utente.isUtenteAmministratore()) {
            CNRUserContext userContext = new CNRUserContext(
                    utente.getCd_utente(),
                    context.getSessionId(),
                    esercizio,
                    null,
                    null,
                    null);
            userContext.getAttributes().put(BOOTSTRAP,
                    Optional.ofNullable(context.getUserContext().getAttributes().get(BOOTSTRAP))
                            .map(Boolean.class::cast)
                            .orElse(Boolean.FALSE)
            );
            // Testo se l'utente puè² entrare con l'anno in corso.
            try {
                getComponentSession().registerUser(userContext, context.getApplicationId());
                //	Remmato Marco Spasiano 28/02/2006 per problema di sessioni attive
                //UnregisterUser.registerUnregisterUser((HttpActionContext)context);
            } catch (it.cnr.jada.comp.ApplicationException e) {
                // Se l'anno in corso è lockato cerco il primo esercizio disponibile.
                esercizio = null;
                for (int i = 0; i < esercizi.length; i++)
                    try {
                        userContext = new CNRUserContext(
                                utente.getCd_utente(),
                                context.getSessionId(),
                                esercizi[i],
                                null,
                                null,
                                null);
                        getComponentSession().registerUser(userContext, context.getApplicationId());
                        esercizio = esercizi[i];
                        break;
                    } catch (it.cnr.jada.comp.ApplicationException ex) {
                        continue;
                    }
                ui.setEsercizio(esercizio);
                if (esercizio == null) {
                    ((LoginBP) context.getBusinessProcessRoot(true)).setErrorMessage("Tutti gli esercizi disponibili sono temporaneamente bloccati.");
                    return context.findDefaultForward();
                }
                //	Remmato Marco Spasiano 28/02/2006 per problema di sessioni attive
                //UnregisterUser.registerUnregisterUser((HttpActionContext)context);
            }

            context.setUserContext(userContext);
            bp.setRadiceAlbero_main(context, getComponentSession().generaAlberoPerUtente(context.getUserContext(), utente, null, null, (short) 0));
            return context.findForward("desktop");
        }
        if (utente.isSupervisore())
            bp.cercaCds(context);
        else
            bp.cercaUnitaOrganizzative(context);
        return context.findForward("desktop");
    }

    public Forward doSelezionaContesto(ActionContext context, Integer esercizio) {
        return doSelezionaContesto(context, esercizio, null, null, null);
    }

    /**
     * Gestisce l'azione di selezione di un contesto
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doSelezionaContesto(ActionContext context, Integer esercizio, String cds, String uo, String cdr) {
        try {
            LoginBP bp = (LoginBP) context.getBusinessProcess();
            CNRUserInfo ui = Optional.ofNullable(context.getUserInfo())
                    .filter(CNRUserInfo.class::isInstance)
                    .map(CNRUserInfo.class::cast)
                    .orElseThrow(() -> new NoSuchSessionException());
            ui.setEsercizio(esercizio);
            CNRUserContext userContext = new CNRUserContext(
                    ui.getUtente().getCd_utente(),
                    context.getSessionId(),
                    ui.getEsercizio(),
                    Optional.ofNullable(uo).filter(x -> !x.equalsIgnoreCase(NULL))
                            .orElse(CNRUserContext.getCd_unita_organizzativa(context.getUserContext())),
                    Optional.ofNullable(cds).filter(x -> !x.equalsIgnoreCase(NULL))
                            .orElse(CNRUserContext.getCd_cds(context.getUserContext())),
                    Optional.ofNullable(cdr).filter(x -> !x.equalsIgnoreCase(NULL))
                            .orElse(CNRUserContext.getCd_cdr(context.getUserContext())));

            ui.setUnita_organizzativa(Optional.ofNullable(uo)
                    .filter(x -> !x.equalsIgnoreCase(NULL))
                    .map(x -> new Unita_organizzativaBulk(x))
                    .map(unita_organizzativaBulk -> {
                        try {
                            return Utility.createUnita_organizzativaComponentSession().findByPrimaryKey(userContext, unita_organizzativaBulk);
                        } catch (ComponentException | RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Unita_organizzativaBulk.class::isInstance)
                    .map(Unita_organizzativaBulk.class::cast)
                    .orElse(null));

            final CdrBulk cdrBulk1 = Optional.ofNullable(cdr)
                    .filter(x -> !x.equalsIgnoreCase(NULL))
                    .map(x -> new CdrBulk(x))
                    .map(cdrBulk -> {
                        try {
                            return Utility.createUnita_organizzativaComponentSession().findByPrimaryKey(userContext, cdrBulk);
                        } catch (ComponentException | RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(CdrBulk.class::isInstance)
                    .map(CdrBulk.class::cast)
                    .orElseGet(() -> {
                        if (Optional.ofNullable(ui)
                                .flatMap(cnrUserInfo -> Optional.ofNullable(cnrUserInfo.getUnita_organizzativa()))
                                .flatMap(unita_organizzativaBulk -> Optional.ofNullable(unita_organizzativaBulk.getCd_unita_organizzativa())).isPresent()) {
                            try {
                                return createCRUDComponentSession().find(
                                                context.getUserContext(),
                                                V_struttura_organizzativaBulk.class,
                                                "findCDRCollegatiUO",
                                                ui.getUnita_organizzativa(),
                                                ui.getEsercizio()
                                        ).stream()
                                        .filter(CdrBulk.class::isInstance)
                                        .map(CdrBulk.class::cast)
                                        .findAny()
                                        .orElse(null);
                            } catch (RemoteException | ComponentException e) {
                                log.error("CANNOT FIND CDR from UO: {}", ui.getUnita_organizzativa().getCd_unita_organizzativa(), e);
                            }
                        }
                        return null;
                    });
            ui.setCdr(cdrBulk1);
            final String cdCdR = Optional.ofNullable(userContext.getCd_cdr())
                    .orElseGet(() ->
                            Optional.ofNullable(cdrBulk1)
                                    .flatMap(cdrBulk -> Optional.ofNullable(cdrBulk.getCd_centro_responsabilita()))
                                    .orElse(null)
                    );
            if (Optional.ofNullable(cdCdR).isPresent())
                userContext.setCd_cdr(cdCdR);
            userContext.getAttributes().put(BOOTSTRAP, true);
            bp.setBootstrap(true);
            context.setUserContext(userContext);
            return context.findDefaultForward();
        } catch (NoSuchSessionException _ex) {
            return context.findForward("sessionExpired");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}