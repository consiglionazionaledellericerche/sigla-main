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

/**
 *
 */
package it.cnr.contab.config00.action;

import it.cnr.contab.config00.ejb.CDRComponentSession;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bp.CostiDipendenteBP;
import it.cnr.contab.prevent01.action.CRUDPdGAggregatoModuloAction;
import it.cnr.contab.prevent01.bp.CRUDPdGAggregatoModuloBP;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.ejb.PdgAggregatoModuloComponentSession;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoKey;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.progettiric00.enumeration.StatoProgetto;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bp.GestioneUtenteBP;
import it.cnr.contab.utenze00.bp.LoginBP;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteComuneBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.OptionBP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author mspasiano
 *
 */
public class MacroAction extends BulkAction {
    private transient final static Logger logger = LoggerFactory.getLogger(MacroAction.class);

    public MacroAction() {
        super();
    }

    public static GestioneLoginComponentSession getComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession", GestioneLoginComponentSession.class);
    }

    public static CDRComponentSession getCdrComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (CDRComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", CDRComponentSession.class);
    }

    public static Unita_organizzativaComponentSession getUOComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (Unita_organizzativaComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Unita_organizzativaComponentSession", Unita_organizzativaComponentSession.class);
    }

    public static PdgAggregatoModuloComponentSession getPdgModuloComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (PdgAggregatoModuloComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_PdgAggregatoModuloComponentSession", PdgAggregatoModuloComponentSession.class);
    }

    public static ProgettoRicercaPadreComponentSession getProgettoRicercaPadreComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (ProgettoRicercaPadreComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession", ProgettoRicercaPadreComponentSession.class);
    }

    public Forward doDefault(ActionContext actioncontext) {
        try {
            actioncontext.invalidateSession();
            String cd_utente, pwd, cd_uo = null;
            Integer pg_modulo = null;
            ProgettoBulk modulo = null;
            Integer esercizio;
            String costi_personale = "N";
            String mode = "M";
            try {
                esercizio = new Integer(((HttpActionContext) actioncontext).getParameter("esercizio"));
            } catch (NullPointerException e) {
                esercizio = new Integer(2006);
            } catch (NumberFormatException e) {
                esercizio = new Integer(2006);
            }
            try {
                costi_personale = ((HttpActionContext) actioncontext).getParameter("costi_personale");
            } catch (NullPointerException e) {
                costi_personale = "N";
            }
            if (costi_personale == null)
                costi_personale = "N";
            try {
                mode = ((HttpActionContext) actioncontext).getParameter("mode");
            } catch (NullPointerException e) {
                mode = "M";
            }
            if (mode == null)
                mode = "M";
            actioncontext.setUserContext(new CNRUserContext("LOGIN", actioncontext.getSessionId(), esercizio, null, null, null));
            try {
                cd_utente = ((HttpActionContext) actioncontext).getParameter("cd_utente").toUpperCase();
                pwd = ((HttpActionContext) actioncontext).getParameter("pwd");
            } catch (NullPointerException e) {
                setErrorMessage(actioncontext, "Parametri non corretti.");
                return actioncontext.findDefaultForward();
            }
            //Parametri Opzionali
			/*try{
				cd_uo = ((HttpActionContext)actioncontext).getParameter("cd_uo").toUpperCase();
			}catch(NullPointerException e){}
			catch(NumberFormatException e){
				setErrorMessage(actioncontext,"Parametri non corretti.");
				return actioncontext.findDefaultForward();
			}*/
            try {
                if (((HttpActionContext) actioncontext).getParameter("pg_modulo") != null)
                    pg_modulo = new Integer(((HttpActionContext) actioncontext).getParameter("pg_modulo"));
            } catch (NullPointerException e) {
            } catch (NumberFormatException e) {
                setErrorMessage(actioncontext, "Parametri non corretti.");
                return actioncontext.findDefaultForward();
            }

            Unita_organizzativaBulk uo = null;
            LoginBP loginbp = (LoginBP) actioncontext.getBusinessProcessRoot(true);
            CNRUserInfo ui = loginbp.getUserInfo();
            UtenteBulk utente = new UtenteBulk();
            utente.setCd_utente(cd_utente);
            utente.setPassword(pwd);
            utente.setLdap_password(pwd);
            ui.setUtente(utente);

            Integer[] esercizi = getComponentSession().listaEserciziPerUtente(actioncontext.getUserContext(), utente);
            int annoInCorso = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            ui.setEsercizi(esercizi);

            utente = getComponentSession().validaUtente(actioncontext.getUserContext(), utente);
            if (utente == null || !(utente instanceof UtenteComuneBulk)) {
                setErrorMessage(actioncontext, "Nome utente o password sbagliati.");
                return actioncontext.findDefaultForward();
            }

            if (cd_uo != null)
                uo = (Unita_organizzativaBulk) getUOComponentSession().findUOByCodice(actioncontext.getUserContext(), cd_uo);
            if (uo == null)
                uo = ((UtenteComuneBulk) utente).getCdr().getUnita_padre();

            ui.setUtente(utente);
            ui.setUserid(utente.getCd_utente());
            ui.setUnita_organizzativa(uo);
            ui.setEsercizio(esercizio);

            GestioneUtenteBP bp = (GestioneUtenteBP) actioncontext.createBusinessProcess("GestioneUtenteBP");
            actioncontext.addBusinessProcess(bp);
            bp.setUserInfo(ui);
            CNRUserContext userContext = new CNRUserContext(
                    utente.getCd_utente(),
                    actioncontext.getSessionId(),
                    esercizio,
                    ui.getUnita_organizzativa().getCd_unita_organizzativa(),
                    ui.getUnita_organizzativa().getCd_cds(),
                    ui.getCdr().getCd_centro_responsabilita());
            actioncontext.setUserContext(userContext);
            actioncontext.setUserInfo(ui);
            HttpServletRequest request = ((HttpActionContext) actioncontext).getRequest();
            try {
                request.logout();
                request.login(cd_utente, pwd);
            } catch (ServletException e) {
                setErrorMessage(actioncontext, "Nome utente o password sbagliati.");
                return actioncontext.findDefaultForward();
            }
            //Se il mode è H allora va in sola visualizzazione altrimenti in modifica
            mode = mode.equals("H") ? "V" : "M";
            if (costi_personale.equals("N")) {
                Object[] parametri;
                if (pg_modulo != null)
                    parametri = new Object[]{mode, pg_modulo};
                else
                    parametri = new Object[]{mode};
                CRUDPdGAggregatoModuloBP newbp = (CRUDPdGAggregatoModuloBP) actioncontext.getUserInfo().createBusinessProcess(
                        actioncontext,
                        "CRUDPdGAggregatoModuloBP", parametri);
                if (mode.equals("M")) {
                    newbp.setStatus(CRUDPdGAggregatoModuloBP.EDIT);
                    newbp.setEditable(true);
                }
                if (pg_modulo != null && mode.equals("M") && newbp.isModuloInseribile()) {
					logger.info("PDGP -> Progressivo modulo {}", pg_modulo);
                    Pdg_moduloBulk pdg_modulo = new Pdg_moduloBulk(CNRUserContext.getEsercizio(actioncontext.getUserContext()), ui.getCdr().getCd_centro_responsabilita(), pg_modulo);
                    Progetto_sipBulk progetto_sipBulk = new Progetto_sipBulk(esercizio, pg_modulo, ProgettoBulk.TIPO_FASE_PREVISIONE);
                    progetto_sipBulk = Optional.ofNullable(progetto_sipBulk)
                            .map(progetto -> {
                                try {
                                    return Optional.ofNullable(getProgettoRicercaPadreComponentSession().findByPrimaryKey(userContext, progetto))
                                            .filter(Progetto_sipBulk.class::isInstance)
                                            .map(Progetto_sipBulk.class::cast)
                                            .orElse(null);
                                } catch (ComponentException | RemoteException e) {
                                    throw new DetailedRuntimeException(e);
                                }
                            })
                            .orElse(null);
					logger.info("PDGP -> Trovato progetto con progressivo {}", Optional.ofNullable(progetto_sipBulk).map(ProgettoKey::getPg_progetto).orElse(null));
					logger.info("PDGP -> Trovato progetto con stato {}", Optional.ofNullable(progetto_sipBulk)
							.flatMap(progetto_sipBulk1 -> Optional.ofNullable(progetto_sipBulk1.getOtherField()))
							.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getStato()))
							.orElse(null)
					);
                    if (!Optional.ofNullable(progetto_sipBulk.getOtherField())
                            .flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getStato()))
                            .filter(stato -> Arrays.asList(StatoProgetto.STATO_NEGOZIAZIONE.value(), StatoProgetto.STATO_APPROVATO.value()).indexOf(stato) != -1).isPresent()) {
                        if (!progetto_sipBulk.getCd_unita_organizzativa().equals(CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext()))) {
                            setErrorMessage(actioncontext, "Attenzione: il progetto non ha uno stato utile alla previsione! Deve essere completatato dalla UO responsabile!");
                            return actioncontext.findDefaultForward();
                        } else {
                            newbp.setPgModulo(pg_modulo);
                            pdg_modulo.setStato(Pdg_moduloBulk.STATO_AC);
                            pdg_modulo.setToBeCreated();
                            ((CdrBulk) newbp.getModel()).addToDettagli(pdg_modulo);
                            newbp.evidenziaModulo(actioncontext);
                            actioncontext.addBusinessProcess(newbp);
                            return openConfirm(actioncontext, "Attenzione: il progetto non ha uno stato utile alla previsione! Vuoi completare le informazioni mancanti?",
                                    OptionBP.CONFIRM_YES_NO, "doConfermaCompletaProgetto");
                        }

                    }
                    if (!Optional.ofNullable(progetto_sipBulk.getOtherField())
                            .flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()))
                            .filter(tipoFinanziamentoBulk -> tipoFinanziamentoBulk.getFlPrevEntSpesa() || tipoFinanziamentoBulk.getFlRipCostiPers()).isPresent()) {
                        setErrorMessage(actioncontext, "Attenzione: per il progetto non è consentita la previsione!");
                        return actioncontext.findDefaultForward();
                    }
                    try {
                        pdg_modulo = getPdgModuloComponentSession().findAndInsertBulkForMacro(actioncontext.getUserContext(), pdg_modulo);
                        if (pdg_modulo != null) {
                            newbp.setModel(actioncontext, newbp.initializeModelForEdit(actioncontext, newbp.getModel()));
                            newbp.setPgModulo(pg_modulo);
                            newbp.evidenziaModulo(actioncontext);
                        }
                    } catch (ApplicationException e) {
                        setErrorMessage(actioncontext, e.getMessage());
                        return actioncontext.findDefaultForward();
                    }
                }
                actioncontext.addBusinessProcess(newbp);
            } else if (costi_personale.equals("Y")) {
                aggiornaGECO(actioncontext);
                CostiDipendenteBP newbp = (CostiDipendenteBP) actioncontext.getUserInfo().createBusinessProcess(
                        actioncontext,
                        "CostiDipendenteBP",
                        new Object[]{
                                mode
                        }
                );
                actioncontext.addBusinessProcess(newbp);
            }
            return actioncontext.findDefaultForward();
        } catch (it.cnr.contab.utente00.nav.comp.PasswordScadutaException e) {
            setErrorMessage(actioncontext, "Password scaduta da più di sei mesi.");
            return actioncontext.findForward("password_scaduta");
        } catch (it.cnr.contab.utente00.nav.comp.UtenteNonValidoException e) {
            setErrorMessage(actioncontext, "Utente non più valido");
            return actioncontext.findDefaultForward();
        } catch (it.cnr.contab.utente00.nav.comp.UtenteInDisusoException e) {
            setErrorMessage(actioncontext, "Utente non utilizzato da più di sei mesi.");
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doConfermaCompletaProgetto(ActionContext context, int opt) throws RemoteException, BusinessProcessException {
        CRUDPdGAggregatoModuloBP bp = (CRUDPdGAggregatoModuloBP) context.getBusinessProcess();
        if (opt == OptionBP.YES_BUTTON) {
            CRUDPdGAggregatoModuloAction crudPdGAggregatoModuloAction = new CRUDPdGAggregatoModuloAction();
            crudPdGAggregatoModuloAction.doCRUD(context, "main.Dettagli.searchtool_progetto_liv2");
            TestataProgettiRicercaBP testataProgettiRicercaBP = Optional.ofNullable(context.getBusinessProcess())
                    .filter(TestataProgettiRicercaBP.class::isInstance)
                    .map(TestataProgettiRicercaBP.class::cast)
                    .orElse(null);
            if (testataProgettiRicercaBP != null) {
                ProgettoBulk progettoBulk = new ProgettoBulk(CNRUserContext.getEsercizio(context.getUserContext()), bp.getPgModulo(), ProgettoBulk.TIPO_FASE_PREVISIONE);
                progettoBulk = Optional.ofNullable(progettoBulk)
                        .map(progetto -> {
                            try {
                                return Optional.ofNullable(getProgettoRicercaPadreComponentSession().findByPrimaryKey(context.getUserContext(), progetto))
                                        .filter(ProgettoBulk.class::isInstance)
                                        .map(ProgettoBulk.class::cast)
                                        .orElse(null);
                            } catch (ComponentException | RemoteException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        })
                        .orElse(null);
                if (Optional.ofNullable(progettoBulk).isPresent()) {
                    testataProgettiRicercaBP.basicEdit(context, progettoBulk, true);
                }
            }
        }
        return context.findDefaultForward();
    }

    public void aggiornaGECO(ActionContext context) {
        try {
            getProgettoRicercaPadreComponentSession().aggiornaGECO(context.getUserContext());
        } catch (Exception e) {
            String text = "Errore interno del Server Utente:" + CNRUserContext.getUser(context.getUserContext()) + " UO:" + CNRUserContext.getCd_unita_organizzativa(context.getUserContext());
            SendMail.sendErrorMail(text, e.toString());
        }
    }
}
