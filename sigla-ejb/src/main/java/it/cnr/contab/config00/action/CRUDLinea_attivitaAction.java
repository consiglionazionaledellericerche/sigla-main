package it.cnr.contab.config00.action;

import it.cnr.contab.config00.bp.CRUDWorkpackageBP;
import it.cnr.contab.config00.ejb.Linea_attivitaComponentSession;
import it.cnr.contab.config00.latt.bulk.Insieme_laBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent01.bulk.Pdg_programmaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FieldProperty;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDLinea_attivitaAction extends it.cnr.jada.util.action.CRUDAction {
    //Dimensione massima ammessa per il File
    private static final long lunghezzaMax = 0x1000000;

    public CRUDLinea_attivitaAction() {
        super();
    }

    /**
     * Gestisce una richiesta di ricerca del searchtool "centro_responsabilita"
     *
     * @param context        L'ActionContext della richiesta
     * @param linea_attivita L'OggettoBulk padre del searchtool
     * @param cdr            L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBlankSearchCentro_responsabilita(ActionContext context, WorkpackageBulk linea_attivita) {
        try {
            linea_attivita.setCentro_responsabilita(new CdrBulk());
            linea_attivita.setInsieme_la(null);
            linea_attivita.setPdgMissione(null);
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            bp.setModel(context,
                    ((Linea_attivitaComponentSession) bp.createComponentSession()).inizializzaNaturaPerInsieme(
                            context.getUserContext(),
                            linea_attivita));
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di azzeramento del searchtool "tipo_linea_attivita"
     *
     * @param context        L'ActionContext della richiesta
     * @param linea_attivita L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doBlankSearchTipo_linea_attivita(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita) {
        linea_attivita.setTipo_linea_attivita(new it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk());
        linea_attivita.setCentro_responsabilita(null);
        return context.findDefaultForward();
    }

    /**
     * Gestisce una richiesta di ricerca del searchtool "centro_responsabilita"
     *
     * @param context        L'ActionContext della richiesta
     * @param linea_attivita L'OggettoBulk padre del searchtool
     * @param cdr            L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackSearchCentro_responsabilita(ActionContext context, WorkpackageBulk linea_attivita, CdrBulk cdr) {
        try {
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            if (cdr != null)
                if (linea_attivita.getCentro_responsabilita() == null ||
                        linea_attivita.getCentro_responsabilita().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL ||
                        !cdr.getCd_centro_responsabilita().equals(linea_attivita.getCd_centro_responsabilita())) {
                    linea_attivita.setCentro_responsabilita(cdr);
                    // (11/06/2002 15.14.46) CNRADM
                    // Modificato per azzerare l'insieme al cambiamento del cdr.
                    linea_attivita.setInsieme_la(null);
                    bp.setModel(context,
                            ((Linea_attivitaComponentSession) bp.createComponentSession()).inizializzaNaturaPerInsieme(
                                    context.getUserContext(),
                                    linea_attivita));
                }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di ricerca del searchtool "insieme_la"
     *
     * @param context        L'ActionContext della richiesta
     * @param linea_attivita L'OggettoBulk padre del searchtool
     * @param insieme_la     L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackSearchInsieme_la(ActionContext context, WorkpackageBulk linea_attivita, Insieme_laBulk insieme_la) {
        try {
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            linea_attivita.setInsieme_la(insieme_la);
            if (insieme_la != null)
                bp.setModel(context,
                        ((Linea_attivitaComponentSession) bp.createComponentSession()).inizializzaNaturaPerInsieme(
                                context.getUserContext(),
                                linea_attivita));
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doCambiaGestione(ActionContext context) {
        try {
            fillModel(context);
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            bp.setModel(context,
                    ((Linea_attivitaComponentSession) bp.createComponentSession()).inizializzaNature(
                            context.getUserContext(),
                            (WorkpackageBulk) bp.getModel()));
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di ricerca del searchtool "tipo_linea_attivita"
     *
     * @param context             L'ActionContext della richiesta
     * @param linea_attivita      L'OggettoBulk padre del searchtool
     * @param tipo_linea_attivita L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     */
    public it.cnr.jada.action.Forward doBringBackSearchTipo_linea_attivita(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita, it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk tipo_linea_attivita) {
        linea_attivita.setTipo_linea_attivita(tipo_linea_attivita);
        linea_attivita.setCentro_responsabilita(null);

        // Passo e funzione natura ereditati da tipologia di linea di attività

        if (tipo_linea_attivita != null) {
            linea_attivita.setFunzione(tipo_linea_attivita.getFunzione());
            linea_attivita.setNatura(tipo_linea_attivita.getNatura());
        }
        return context.findDefaultForward();
    }

    public Forward doFreeSearchFind_nodo_padre(ActionContext context) {
        try {
            ProgettoBulk progetto = new ProgettoBulk();
            progetto.setProgettopadre(new ProgettoBulk());
            return freeSearch(context, getFormField(context, "main.find_nodo_padre"), progetto);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doFreeSearchFind_nodo_padre_2015(ActionContext context) {
        try {
            CRUDWorkpackageBP bpLinea = (CRUDWorkpackageBP) context.getBusinessProcess();
            WorkpackageBulk gae = (WorkpackageBulk) bpLinea.getModel();
            if (gae.getProgetto2016() != null && gae.getProgetto2016().getPg_progetto() != null &&
                    (gae.getPdgProgramma() == null || gae.getPdgProgramma().getCd_programma() == null)) {
                setErrorMessage(context, "Attenzione: non risulta valorizzato il programma nonostante la presenza del progetto. Aprire una segnalazione HelpDesk!");
                return context.findDefaultForward();
            }
            ProgettoBulk progetto = new ProgettoBulk();
            progetto.setProgettopadre(new ProgettoBulk());
            return freeSearch(context, getFormField(context, "main.find_nodo_padre_2015"), progetto);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doFreeSearchFind_nodo_padre_2016(ActionContext context) {
        try {
            CRUDWorkpackageBP bpLinea = (CRUDWorkpackageBP) context.getBusinessProcess();
            WorkpackageBulk gae = (WorkpackageBulk) bpLinea.getModel();
            if (gae.getEsercizio_fine() != null && gae.getEsercizio_fine().compareTo(Integer.valueOf(2016)) == -1) {
                setErrorMessage(context, "Attenzione: il GAE termina prima dell''anno 2016. Inserimento non possibile!");
                return context.findDefaultForward();
            }
            if (gae.getModulo2015() != null && gae.getModulo2015().getPg_progetto() != null &&
                    (gae.getPdgProgramma() == null || gae.getPdgProgramma().getCd_programma() == null)) {
                setErrorMessage(context, "Attenzione: non risulta valorizzato il programma nonostante la presenza del modulo di attività. Aprire una segnalazione HelpDesk!");
                return context.findDefaultForward();
            }
            ProgettoBulk progetto = new ProgettoBulk();
            progetto.setProgettopadre(new ProgettoBulk());
            return freeSearch(context, getFormField(context, "main.find_nodo_padre_2016"), progetto);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doSearchFind_nodo_padre_2016(ActionContext context) {
        try {
            CRUDWorkpackageBP bpLinea = (CRUDWorkpackageBP) context.getBusinessProcess();
            WorkpackageBulk gae = (WorkpackageBulk) bpLinea.getModel();
            if (gae.getEsercizio_fine() != null && gae.getEsercizio_fine().compareTo(Integer.valueOf(2016)) == -1) {
                setErrorMessage(context, "Attenzione: il GAE termina prima dell''anno 2016. Inserimento non possibile!");
                return context.findDefaultForward();
            }
            if (gae.getModulo2015() != null && gae.getModulo2015().getPg_progetto() != null &&
                    (gae.getPdgProgramma() == null || gae.getPdgProgramma().getCd_programma() == null)) {
                setErrorMessage(context, "Attenzione: non risulta valorizzato il programma nonostante la presenza del modulo di attività. Aprire una segnalazione HelpDesk!");
                return context.findDefaultForward();
            }
            return search(context, getFormField(context, "main.find_nodo_padre_2016"), "nuovoPdg");
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doSearchFind_nodo_padre_2015(ActionContext context) {
        try {
            CRUDWorkpackageBP bpLinea = (CRUDWorkpackageBP) context.getBusinessProcess();
            WorkpackageBulk gae = (WorkpackageBulk) bpLinea.getModel();
            if (gae.getProgetto2016() != null && gae.getProgetto2016().getPg_progetto() != null &&
                    (gae.getPdgProgramma() == null || gae.getPdgProgramma().getCd_programma() == null)) {
                setErrorMessage(context, "Attenzione: non risulta valorizzato il programma nonostante la presenza del progetto. Aprire una segnalazione HelpDesk!");
                return context.findDefaultForward();
            }
            return search(context, getFormField(context, "main.find_nodo_padre_2015"), null);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public it.cnr.jada.action.Forward doBlankSearchFind_nodo_padre(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita) {
        linea_attivita.setProgetto(new ProgettoBulk());
        linea_attivita.setPdgProgramma(null);
        return context.findDefaultForward();
    }

    public it.cnr.jada.action.Forward doBlankSearchFind_nodo_padre_2015(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita) {
        linea_attivita.setModulo2015(new ProgettoBulk());
        if (linea_attivita.getProgetto2016() == null || linea_attivita.getProgetto2016().getCd_progetto() == null)
            linea_attivita.setPdgProgramma(null);
        return context.findDefaultForward();
    }

    public it.cnr.jada.action.Forward doBlankSearchFind_nodo_padre_2016(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita) {
        linea_attivita.setProgetto2016(new ProgettoBulk());
        if (linea_attivita.getModulo2015() == null || linea_attivita.getModulo2015().getCd_progetto() == null)
            linea_attivita.setPdgProgramma(null);
        return context.findDefaultForward();
    }

    /**
     * E' stata generata la richiesta di cercare un Progetto che sia padre del Progetto
     * che si sta creando.
     * Il metodo antepone alla descrizione specificata dall'utente, quella del Progetto selezionato
     * come padre.
     * In caso di modifica di una Progetto esistente sul DB, il sistema controlla che il Progetto
     * selezionato dall'utente non sia la stesso che sta modificando.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/

    public it.cnr.jada.action.Forward doBringBackSearchFind_nodo_padre(ActionContext context, WorkpackageBulk linea, ProgettoBulk progetto) throws java.rmi.RemoteException {
        try {
            // valore di default nel caso non fose valorizzato
            String columnDescription = "Codice Modulo di Attività";
            // nome del campo nel file xml
            final String propName = "cd_progetto";
            FieldProperty property = BulkInfo.getBulkInfo(linea.getClass()).getFieldProperty(propName);
            if (property != null)
                columnDescription = property.getLabel();


            if (progetto != null) {
                if (progetto.getLivello() == null || !progetto.getLivello().equals(new Integer("3"))) {
                    setErrorMessage(context, "Attenzione: il valore immesso in " + columnDescription + " non è valido!");
                    return context.findDefaultForward();
                }
            }
            linea.setProgetto(progetto);
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doSearchPdgMissione(ActionContext context) {
        WorkpackageBulk linea_attivita = (WorkpackageBulk) getBusinessProcess(context).getModel();
        if (linea_attivita == null || linea_attivita.getCentro_responsabilita() == null || linea_attivita.getCentro_responsabilita().getUnita_padre() == null ||
                linea_attivita.getCentro_responsabilita().getUnita_padre().getCd_tipo_unita() == null)
            throw new MessageToUser("Ricerca Missione non possibile! Centro di Responsabilità non selezionato e/o errore nell'individuazione del tipo di CDR!");
        return search(context, getFormField(context, "main.pdgMissione"), null);
    }

    public it.cnr.jada.action.Forward doBringBackSearchFind_nodo_padre_2015(ActionContext context, WorkpackageBulk linea, ProgettoBulk progetto) throws java.rmi.RemoteException {
        try {
            // valore di default nel caso non fose valorizzato
            String columnDescription = "Codice Modulo di Attività";
            // nome del campo nel file xml
            final String propName = "cd_modulo2015";
            FieldProperty property = BulkInfo.getBulkInfo(linea.getClass()).getFieldProperty(propName);
            if (property != null)
                columnDescription = property.getLabel();

            if (progetto != null) {
                if (progetto.getLivello() == null || !progetto.getLivello().equals(new Integer("3"))) {
                    setErrorMessage(context, "Attenzione: il valore immesso in " + columnDescription + " non è valido!");
                    return context.findDefaultForward();
                }
            }
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            if (linea != null && linea.getPdgProgramma() != null && linea.getPdgProgramma().getCd_programma() != null &&
                    progetto != null && progetto.getProgettopadre() != null && progetto.getProgettopadre().getProgettopadre() != null &&
                    !linea.getPdgProgramma().getCd_programma().equals(progetto.getProgettopadre().getProgettopadre().getCd_dipartimento())) {
                setErrorMessage(context, "Attenzione: il modulo di attivita', appartenente al dipartimento con codice " + progetto.getProgettopadre().getProgettopadre().getCd_dipartimento() +
                        " non è coerente con il programma indicato sulla GAE!");
                return context.findDefaultForward();
            } else if (linea != null && (linea.getPdgProgramma() == null || linea.getPdgProgramma().getCd_programma() == null) &&
                    progetto != null && progetto.getProgettopadre() != null && progetto.getProgettopadre().getProgettopadre() != null &&
                    progetto.getProgettopadre().getProgettopadre().getCd_dipartimento() != null) {
                linea.setPdgProgramma((Pdg_programmaBulk) bp.createComponentSession().findByPrimaryKey(context.getUserContext(), new Pdg_programmaBulk(progetto.getProgettopadre().getProgettopadre().getCd_dipartimento())));
            }
            linea.setModulo2015(progetto);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public it.cnr.jada.action.Forward doBringBackSearchFind_nodo_padre_2016(ActionContext context, WorkpackageBulk linea, ProgettoBulk progetto) throws java.rmi.RemoteException {
        try {
            // valore di default nel caso non fose valorizzato
            String columnDescription = "Codice Progetto";
            // nome del campo nel file xml
            final String propName = "cd_progetto2016";
            FieldProperty property = BulkInfo.getBulkInfo(linea.getClass()).getFieldProperty(propName);
            if (property != null)
                columnDescription = property.getLabel();

            if (progetto != null) {
                if (progetto.getLivello() == null || !progetto.getLivello().equals(new Integer("2"))) {
                    setErrorMessage(context, "Attenzione: il valore immesso in " + columnDescription + " non è valido!");
                    return context.findDefaultForward();
                }
            }
            CRUDWorkpackageBP bp = (CRUDWorkpackageBP) context.getBusinessProcess();
            if (progetto != null) {
                progetto.setProgettopadre((ProgettoBulk) bp.createComponentSession().findByPrimaryKey(context.getUserContext(),
                        new ProgettoBulk(progetto.getEsercizio(), progetto.getPg_progetto_padre(), progetto.getTipo_fase())));
                progetto.setOtherField((Progetto_other_fieldBulk) bp.createComponentSession().findByPrimaryKey(context.getUserContext(),
                        new Progetto_other_fieldBulk(progetto.getPg_progetto())));
            }
            if (linea != null && linea.getPdgProgramma() != null && linea.getPdgProgramma().getCd_programma() != null &&
                    progetto != null && progetto.getProgettopadre() != null &&
                    !linea.getPdgProgramma().getCd_programma().equals(progetto.getProgettopadre().getCd_programma())) {
                setErrorMessage(context, "Attenzione: il progetto, appartenente al dipartimento con codice " + progetto.getProgettopadre().getCd_dipartimento() +
                        " non è coerente con il programma indicato sulla GAE!");
                return context.findDefaultForward();
            } else if (linea != null && (linea.getPdgProgramma() == null || linea.getPdgProgramma().getCd_programma() == null) &&
                    progetto != null && progetto.getProgettopadre() != null && progetto.getProgettopadre().getPdgProgramma() != null) {
                linea.setPdgProgramma((Pdg_programmaBulk) bp.createComponentSession().findByPrimaryKey(context.getUserContext(), progetto.getProgettopadre().getPdgProgramma()));
            }
            linea.setProgetto2016(progetto);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}
