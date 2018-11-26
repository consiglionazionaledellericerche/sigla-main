package it.cnr.contab.utenze00.bp;

/**
 * Business Process che gestisce l'attività di Gestione Utente Comune e Gestione Template di Utente: in particolare
 * gestisce i quattro dettagli relativi agli Accessi/Ruoli gia' assegnati all'Utente e agli Accessi/Ruoli ancora disponibili
 */


import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.rmi.RemoteException;
import java.util.Optional;

public class CRUDUtenzaBP extends SimpleCRUDBP {
    private final SimpleDetailCRUDController crudAccessi = new SimpleDetailCRUDController("Accessi", AccessoBulk.class, "accessi", this);
    private final SimpleDetailCRUDController crudRuoli_disponibili = new SimpleDetailCRUDController("Ruoli_disponibili", RuoloBulk.class, "ruoli_disponibili", this);
    private final SimpleDetailCRUDController crudRuoli = new SimpleDetailCRUDController("Ruoli", RuoloBulk.class, "ruoli", this);
    private final SimpleDetailCRUDController crudUtente_indirizzi_mail = new SimpleDetailCRUDController("Utente_indirizzi_mail", Utente_indirizzi_mailBulk.class, "utente_indirizzi_mail", this);
    private CompoundFindClause compoundfindclauseAccessiDisponibili = null;
    private final SimpleDetailCRUDController crudAccessi_disponibili = new SimpleDetailCRUDController("Accessi_disponibili", AccessoBulk.class, "accessi_disponibili", this) {
        public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause) {
            compoundfindclauseAccessiDisponibili = compoundfindclause;
            CRUDUtenzaBP bp = (CRUDUtenzaBP) actioncontext.getBusinessProcess();
            UtenteTemplateBulk utente = (UtenteTemplateBulk) bp.getModel();
            utente.resetAccessi();
            try {
                bp.setModel(actioncontext, ((UtenteComponentSession) createComponentSession()).
                        cercaAccessi(actioncontext.getUserContext(), utente, utente.getUnita_org_per_accesso(), compoundfindclause));

            } catch (BusinessProcessException e) {
                handleException(e);
            } catch (ComponentException e) {
                handleException(e);
            } catch (RemoteException e) {
                handleException(e);
            }
            super.setFilter(actioncontext, compoundfindclause);
        }

        ;

        public boolean isFiltered() {
            return compoundfindclauseAccessiDisponibili != null;
        }

        ;
    };

    public CRUDUtenzaBP() throws BusinessProcessException {
        super();
        setTab("tab", "tabUtenza");
    }

    public CRUDUtenzaBP(String function) throws BusinessProcessException {
        super(function);
        setTab("tab", "tabUtenza");
    }

    /**
     * Reindirizza sul Component associato a questo BP la ricerca degli accessi gia' assegnati e degli accessi
     * ancora disponibili per un Utente ed una Unita Organizzativa
     * @param context contesto dell'azione
     */

    public void cercaAccessi(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {

            UtenteTemplateBulk utente = (UtenteTemplateBulk) getModel();
            utente.resetAccessi();
            setModel(context, ((UtenteComponentSession) createComponentSession()).cercaAccessi(context.getUserContext(), utente, utente.getUnita_org_per_accesso(), compoundfindclauseAccessiDisponibili));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Reindirizza sul Component associato a questo BP la ricerca dei ruoli gia' assegnati e dei ruoli
     * ancora disponibili per un Utente ed una Unita' Organizzativa
     * @param context contesto dell'azione
     */

    public void cercaRuoli(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            UtenteTemplateBulk utente = (UtenteTemplateBulk) getModel();
            utente.resetRuoli();
            setModel(context, ((UtenteComponentSession) createComponentSession()).cercaRuoli(context.getUserContext(), utente, utente.getUnita_org_per_ruolo()));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Restituisce il Controller che gestisce il dettaglio degli Accessi gia' assegnati un Utente
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudAccessi() {
        return crudAccessi;
    }

    /**
     * Restituisce il Controller che gestisce il dettaglio degli Accessi ancora disponibili per un Utente
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudAccessi_disponibili() {
        return crudAccessi_disponibili;
    }

    /**
     * Restituisce il Controller che gestisce il dettaglio dei Ruoli gia' assegnati ad un Utente
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudRuoli() {
        return crudRuoli;
    }

    /**
     * Restituisce il Controller che gestisce il dettaglio dei Ruoli ancora disponibili per un Utente
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudRuoli_disponibili() {
        return crudRuoli_disponibili;
    }

    /**
     * Gestisce un comando di stampa degli Utenti
     */
    protected void initializePrintBP(AbstractPrintBP pbp) {
        UtenteBulk aUtente = (UtenteBulk) getModel();
        if (aUtente.getCd_utente() == null)
            throw new MessageToUser("Nessun utente specificato");

        it.cnr.contab.reports.bp.ReportPrintBP printbp = (it.cnr.contab.reports.bp.ReportPrintBP) pbp;

        printbp.setReportName("/configurazione/utenze/utente.jasper");
        Print_spooler_paramBulk param;
        param = new Print_spooler_paramBulk();
        param.setNomeParam("utente");
        param.setValoreParam(aUtente.getCd_utente());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

    }

    /**
     * Esegue il reset degli accessi visualizzati
     */

    public void resetAccessi(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            UtenteTemplateBulk utente = (UtenteTemplateBulk) getModel();
            utente.setUnita_org_per_accesso(new Unita_organizzativaBulk());
            utente.resetAccessi();
            setModel(context, utente);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue il reset della password
     */

    public void resetPassword(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            UtenteBulk utente = (UtenteTemplateBulk) getModel();
            setModel(context, ((UtenteComponentSession) createComponentSession()).resetPassword(context.getUserContext(), utente));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue il reset dei ruoli visualizzati
     */

    public void resetRuoli(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            UtenteTemplateBulk utente = (UtenteTemplateBulk) getModel();
            utente.setUnita_org_per_ruolo(new Unita_organizzativaBulk());
            utente.resetRuoli();
            setModel(context, utente);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /* Metodo per riportare il fuoco sul tab iniziale */
    protected void resetTabs(ActionContext context) {
        setTab("tab", "tabUtenza");
    }

    public SimpleDetailCRUDController getCrudUtente_indirizzi_mail() {
        return crudUtente_indirizzi_mail;
    }

    public boolean isCdrConfiguratoreAll(UserContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            return ((UtenteComponentSession) createComponentSession()).isCdrConfiguratoreAll(context).booleanValue();
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public Boolean isAutenticazioneLdap(UserContext uc) {
        Parametri_enteBulk par = null;
        try {
            par = ((Parametri_enteComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession", Parametri_enteComponentSession.class)).getParametriEnte(uc);
        } catch (ComponentException e) {
        } catch (RemoteException e) {
        }
        if (par != null)
            return par.getFl_autenticazione_ldap();
        return null;
    }

    public boolean isUtenteAbilitatoLdap(UserContext uc) throws it.cnr.jada.action.BusinessProcessException {
        UtenteBulk utente = (UtenteBulk) getModel();
        if (utente.getCd_utente_uid() == null)
            throw new MessageToUser("Codice utente ufficiale non valorizzato!");

        try {
            return ((GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession", GestioneLoginComponentSession.class)).isUtenteAbilitatoLdap(uc, utente.getCd_utente_uid(), true);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public void cambiaAbilitazioneUtente(UserContext uc, boolean abilita) throws it.cnr.jada.action.BusinessProcessException {
        UtenteBulk utente = (UtenteBulk) getModel();
        if (utente.getCd_utente_uid() == null)
            throw new MessageToUser("Codice utente ufficiale non valorizzato!");

        try {
            ((GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession", GestioneLoginComponentSession.class)).cambiaAbilitazioneUtente(uc, utente.getCd_utente_uid(), abilita);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    @Override
    public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        Optional.ofNullable(getModel())
                .filter(UtenteTemplateBulk.class::isInstance)
                .map(UtenteTemplateBulk.class::cast)
                .ifPresent(utenteTemplateBulk -> {
                    utenteTemplateBulk.setNome(
                            Optional.ofNullable(utenteTemplateBulk.getNome())
                                .orElse(utenteTemplateBulk.getCd_utente()));
                    utenteTemplateBulk.setCognome(
                            Optional.ofNullable(utenteTemplateBulk.getCognome())
                                    .orElse(utenteTemplateBulk.getCd_utente()));
                });
        super.save(actioncontext);
    }
    public void resetInutilizzo( ActionContext context ) throws it.cnr.jada.action.BusinessProcessException {
    	try 
    	{
    		UtenteBulk utente = (UtenteBulk)getModel();
    		utente.setDt_ultimo_accesso(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
    		setModel(context,((UtenteComponentSession)createComponentSession()).modificaConBulk(context.getUserContext(),utente));
    		save(context); 
    	} catch(Exception e) {
    		throw handleException(e);
    	}
    }
}
