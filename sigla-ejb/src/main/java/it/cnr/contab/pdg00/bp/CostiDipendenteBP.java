package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.pdg00.ejb.CostiDipendenteComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelectionIterator;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import javax.ejb.EJBException;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * Business Process per la gestione dei Costi dei Dipendenti
 */

public class CostiDipendenteBP extends BulkBP {
    // Controller che gestisce la lista delle matricole; contiene un elenco
    // di <code>V_cdp_matricolaBulk</code>
    private final SimpleDetailCRUDController costiDipendenti = new SimpleDetailCRUDController("costiDipendenti", V_cdp_matricolaBulk.class, "costi_dipendenti", this);

    private boolean costiRipartiti;
    private boolean costiDefinitivi;
    private boolean pdgPrevisionaleEnabled;

    // Controller che gestisce la lista dei costi scaricati su linea di attività
    private final SimpleDetailCRUDController costiScaricati = new SimpleDetailCRUDController("costiScaricati", Ass_cdp_laBulk.class, "costiScaricati", costiDipendenti) {
        public boolean isGrowable() {
            if (isInputReadonly()) return false;
            if (isCostiRipartiti()) return false;
            if (isCostiDefinitivi()) return false;
            V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk) getParentModel();
            if (cdp == null) return false;
            if (cdp.isProvenienzaInterna()) return true;
            return cdp.getCostoCaricato() != null && cdp.getCostoCaricato().isAccettato();
        }

        public boolean isShrinkable() {
            if (isInputReadonly()) return false;
            if (isCostiRipartiti()) return false;
            if (isCostiDefinitivi()) return false;
            V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk) getParentModel();
            return cdp != null;
        }

        public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
            if (((Ass_cdp_laBulk) detail).isNonCancellabile())
                throw new ValidationException("Non è possibile cancellare una ripartizione scaricata.");
        }

        @Override
        public void writeHTMLToolbar(
                javax.servlet.jsp.PageContext context,
                boolean reset,
                boolean find,
                boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

            if (!((CostiDipendenteBP) this.getParentController().getParentController()).isRipartizioneCostiModificabile()) {
                reset = Boolean.FALSE;
                delete = Boolean.FALSE;
            }

            super.writeHTMLToolbar(context, reset, find, delete, closedToolbar);
        }
    };

    // Controller che gestisce la lista dei costi scaricati verso altra UO
    private final SimpleDetailCRUDController costiScaricatiAltraUO = new SimpleDetailCRUDController("costiScaricatiAltraUO", Ass_cdp_uoBulk.class, "costiScaricatiAltraUO", costiDipendenti) {
        public boolean isGrowable() {
            if (isInputReadonly()) return false;
            if (isCostiRipartiti()) return false;
            if (isCostiDefinitivi()) return false;
            V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk) getParentModel();
            return cdp != null && cdp.isProvenienzaInterna() && (getModel() == null || !((Ass_cdp_uoBulk) getModel()).isAccettato());
        }

        public boolean isShrinkable() {
            if (isInputReadonly()) return false;
            if (isCostiRipartiti()) return false;
            if (isCostiDefinitivi()) return false;
            V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk) getParentModel();
            return cdp != null && cdp.isProvenienzaInterna() && (getModel() == null || !((Ass_cdp_uoBulk) getModel()).isAccettato());
        }

        public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
            Ass_cdp_uoBulk assCdpUo = (Ass_cdp_uoBulk) oggettobulk;
            if (assCdpUo != null && !assCdpUo.isToBeCreated()) {
                if (isInputReadonly() || isCostiRipartiti() || isCostiDefinitivi())
                    throw new ValidationException("Eliminazione non possibile!\nI costi del personale risultano essere " + (assCdpUo.getMese() == 0 ? "scaricati nel piano di gestione." : "definitivi."));
                else if (assCdpUo.isAccettato())
                    throw new ValidationException("Eliminazione non possibile!\nLa matricola risulta gia'' essere stata accettata dalla UO " + assCdpUo.getCd_unita_organizzativa() + ".");
            }
            super.validateForDelete(actioncontext, oggettobulk);
        }

        @Override
        public void writeHTMLToolbar(
                javax.servlet.jsp.PageContext context,
                boolean reset,
                boolean find,
                boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

            if (!((CostiDipendenteBP) this.getParentController().getParentController()).isRipartizioneCostiModificabile()) {
                reset = Boolean.FALSE;
                delete = Boolean.FALSE;
            }

            super.writeHTMLToolbar(context, reset, find, delete, false);

            if (isGrowable()) {
                String command = "javascript:submitForm('doFreeSearch(main.unita_organizzativa_scarico)')";
                it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                        context,
                        "img/freesearch16.gif",
                        reset ? command : null,
                        true, "Ricerca guidata",
                        HttpActionContext.isFromBootstrap(context));
            }
            super.closeButtonGROUPToolbar(context);
        }
    };

    // Il mese a cui si riferiscono i cdp
    private int mese;

    public CostiDipendenteBP() {
        super();
    }

    public CostiDipendenteBP(String function) {
        super(function);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param function La funzione con cui è stato creato il BusinessProcess
     * @param mese
     */
    public CostiDipendenteBP(String function, Integer mese) {
        super(function);
        this.mese = mese.intValue();
    }

    /**
     * Crea il riferimento alla componente CNRPDG00_EJB_CostiDipendenteComponentSession
     *
     * @return Remote interface della componente
     * @throws EJBException    Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
     * @throws RemoteException Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
     */
    public CostiDipendenteComponentSession createComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
        return (CostiDipendenteComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_CostiDipendenteComponentSession", CostiDipendenteComponentSession.class);
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[5];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.copiaMesePrecedente");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.residui");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.salvaDefinitivo");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "buttons.annullaDefinitivo");

        return toolbar;
    }

    public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, OggettoBulk bulk, OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
        try {
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext, createComponentSession().cerca(actionContext.getUserContext(), clauses, bulk, context, property));
        } catch (Exception e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'costi_dipendente'
     *
     * @return Il valore della proprietà 'costi_dipendente'
     */
    public Costi_dipendenteVBulk getCosti_dipendente() {
        return (Costi_dipendenteVBulk) getModel();
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'costiDipendenti'
     *
     * @return Il valore della proprietà 'costiDipendenti'
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCostiDipendenti() {
        return costiDipendenti;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'costiScaricati'
     *
     * @return Il valore della proprietà 'costiScaricati'
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCostiScaricati() {
        return costiScaricati;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'costiScaricatiAltraUO'
     *
     * @return Il valore della proprietà 'costiScaricatiAltraUO'
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCostiScaricatiAltraUO() {
        return costiScaricatiAltraUO;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'mese'
     *
     * @return Il valore della proprietà 'mese'
     */
    public int getMese() {
        return mese;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'mese'
     *
     * @param newMese Il valore da assegnare a 'mese'
     */
    public void setMese(int newMese) {
        mese = newMese;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'ubicazioniTree'
     *
     * @return Il valore della proprietà 'ubicazioniTree'
     */
    public it.cnr.jada.util.RemoteBulkTree getUbicazioniTree() {
        return new it.cnr.jada.util.RemoteBulkTree() {
            public it.cnr.jada.util.RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
                //			return createComponentSession().;
                return null;
            }

            public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
                return null;
            }

            public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
                return false;
            }
        };
    }

    protected void init(Config config, ActionContext context) throws BusinessProcessException {
        super.init(config, context);
        try {
            costiScaricati.setReadonly(false);
            costiScaricatiAltraUO.setReadonly(false);
            setModel(context, createComponentSession().caricaCosti_dipendente(context.getUserContext(), mese));
            if (getCosti_dipendente().isMensile()) {
                setCostiRipartiti(Boolean.FALSE);
                setCostiDefinitivi(createComponentSession().isCostiDipendenteDefinitivi(context.getUserContext(), mese, CNRUserContext.getCd_unita_organizzativa(context.getUserContext())));
            } else {
                setCostiRipartiti(createComponentSession().isCostiDipendenteRipartiti(context.getUserContext()));
                setCostiDefinitivi(Boolean.FALSE);
            }

            setPdgPrevisionaleEnabled(createComponentSession().isPdgPrevisionaleEnabled(context.getUserContext()));
            //refresh(context);
            setTab("tab", "tabCostiScaricati");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isInputReadonly() {
        return super.isInputReadonly() || !isEditable();
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'saveButtonEnabled'
     *
     * @return Il valore della proprietà 'saveButtonEnabled'
     */
    public boolean isResiduiButtonEnabled() {
        return !isInputReadonly() &&
                (((Costi_dipendenteVBulk) getModel()) == null || !((Costi_dipendenteVBulk) getModel()).isMensile() || !isCostiDefinitivi());
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'saveButtonEnabled'
     *
     * @return Il valore della proprietà 'saveButtonEnabled'
     */
    public boolean isSaveButtonEnabled() {
        return !isInputReadonly() &&
                getCostiDipendenti().getModel() != null &&
                (!((Costi_dipendenteVBulk) getModel()).isMensile() || !isCostiDefinitivi());
    }

    public boolean isCostiRipartiti() {
        return costiRipartiti;
    }

    public void setCostiRipartiti(boolean b) {
        costiRipartiti = b;
    }

    public boolean isCostiDefinitivi() {
        return costiDefinitivi;
    }

    public void setCostiDefinitivi(boolean costiDefinitivi) {
        this.costiDefinitivi = costiDefinitivi;
    }

    public boolean isSalvaDefinitivoButtonHidden() {
        return !getCosti_dipendente().isMensile() || isCostiDefinitivi();
    }

    public boolean isAnnullaDefinitivoButtonHidden() {
        return !getCosti_dipendente().isMensile() || !isCostiDefinitivi();
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'saveButtonEnabled'
     *
     * @return Il valore della proprietà 'saveButtonEnabled'
     */
    public boolean isSalvaDefinitivoButtonEnabled() {
        return true;
    }

    public boolean isCopiaMesePrecedenteButtonEnabled() {
        return getCosti_dipendente().isMensile() &&
                isResiduiButtonEnabled();
    }

    public void copiaMesePrecedente(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.action.BusinessProcessException {
        try {
            int contaRipartizioni = 0;
            if (mese > 1 && !createComponentSession().isCostiDipendenteDefinitivi(context.getUserContext(), mese - 1, CNRUserContext.getCd_unita_organizzativa(context.getUserContext())))
                setMessage("Operazione non effettuata. La ripartizione dei costi del mese precedente a quello corrente non risulta definitiva.");
            else {
                for (Iterator i = this.getCostiDipendenti().getSelectedModels(context).iterator(); i.hasNext(); ) {
                    V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk) i.next();

                    if (cdp.isProvenienzaInterna() && cdp.getCostiScaricati().isEmpty() && cdp.getCostiScaricatiAltraUO().isEmpty()) {
                        V_cdp_matricolaBulk cdpNew = (V_cdp_matricolaBulk) createComponentSession().generaDaUltimaRipartizione(context.getUserContext(), cdp);

                        int oldIndex = this.getCosti_dipendente().getCosti_dipendenti().indexOf(cdp);
                        this.getCosti_dipendente().getCosti_dipendenti().remove(cdp);
                        this.getCosti_dipendente().getCosti_dipendenti().add(oldIndex, cdpNew);

                        // Se siamo nei costi stipendiali mensili è possibile che la componente
                        // generi in automatico una ripartizione sulla base di quelle dei mesi
                        // precedenti. L'utente deve necessariamente salvarla e viene mostato
                        // un messaggio esplicativo
                        setDirty(Boolean.TRUE);
                        contaRipartizioni++;
                    }
                }
                getCostiDipendenti().getSelection().clear();
                getCostiDipendenti().resync(context);
                if (contaRipartizioni > 0)
                    setMessage("Operazione effettuata. Sono state aggiornate " + contaRipartizioni + " matricole.");
                else
                    setMessage("Operazione effettuata. Non è stata aggiornata nessuna matricola.");
            }
        } catch (javax.ejb.EJBException e) {
            throw new it.cnr.jada.action.ActionPerformingError(e);
        } catch (java.rmi.RemoteException e) {
            throw new it.cnr.jada.action.ActionPerformingError(e);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public void copiaRipartizione(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.action.BusinessProcessException {
        // 05/09/2003
        // Aggiunto controllo sulla chiusura dell'esercizio

        try {
            if (((CostiDipendenteComponentSession) createComponentSession()).isEsercizioChiuso(userContext))
                throw new ApplicationException("Funzione non disponibile ad esercizio chiuso.");

            String matricola_rip = null, matricola_nac = null, matricola_ind = null, matricola_rap13 = null;
            int matricoleRipartite = 0;
            V_cdp_matricolaBulk matricola_src = (V_cdp_matricolaBulk) this.getCostiDipendenti().getModel();

            if (matricola_src == null)
                return;
            else if (!matricola_src.isProvenienzaInterna() &&
                    (matricola_src.getStato_carico() == null || !matricola_src.getStato_carico().equals(Ass_cdp_uoBulk.STATO_ACCETTATO)))
                throw new ApplicationException("Funzione non disponibile in presenza di selezione di una matricola scaricata da altra UO e non ancora accettata.");
				
/*
if (matricola_src.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_DETERMINATO) && !matricola_src.getFl_rapporto13()) {
				for (SelectionIterator i = getCostiDipendenti().getSelection().iterator();i.hasNext();) {
					V_cdp_matricolaBulk matricola_dest = (V_cdp_matricolaBulk)getCostiDipendenti().getDetails().get(i.nextIndex());
					if (matricola_dest.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_INDETERMINATO))
						throw new ApplicationException("Funzione non disponibile in presenza di selezione contemporanea di dipendenti a tempo deteminato ed indeterminato.");
					else if (matricola_dest.getFl_rapporto13())
						throw new it.cnr.jada.action.MessageToUser("Funzione non disponibile in presenza di selezione della matricola "+matricola_dest.getId_matricola()+".");
				}
			}
*/
            if (!getCostiDipendenti().getSelection().isEmpty()) {
                for (SelectionIterator i = getCostiDipendenti().getSelection().iterator(); i.hasNext(); ) {
                    V_cdp_matricolaBulk matricola_dest = (V_cdp_matricolaBulk) getCostiDipendenti().getDetails().get(i.nextIndex());
                    if (matricola_dest != matricola_src) {
                        // Lock della matricola
                        ((CostiDipendenteComponentSession) createComponentSession()).lockMatricola(userContext, matricola_dest.getId_matricola(), matricola_dest.getMese());

                        if (!matricola_dest.getCostiScaricati().isEmpty() || !matricola_dest.getCostiScaricatiAltraUO().isEmpty())
                            matricola_rip = (matricola_rip != null ? matricola_rip + ", " : "") + matricola_dest.getId_matricola();
                        else if (!matricola_dest.isProvenienzaInterna() &&
                                (matricola_dest.getStato_carico() == null || !matricola_dest.getStato_carico().equals(Ass_cdp_uoBulk.STATO_ACCETTATO)))
                            matricola_nac = (matricola_nac != null ? matricola_nac + ", " : "") + matricola_dest.getId_matricola();
                        else if ((matricola_src.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_DETERMINATO) && !matricola_src.getFl_rapporto13()) &&
                                matricola_dest.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_INDETERMINATO))
                            matricola_ind = (matricola_ind != null ? matricola_ind + ", " : "") + matricola_dest.getId_matricola();
                        else if ((matricola_src.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_DETERMINATO) && !matricola_src.getFl_rapporto13()) &&
                                matricola_dest.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_DETERMINATO) && matricola_dest.getFl_rapporto13())
                            matricola_rap13 = (matricola_rap13 != null ? matricola_rap13 + ", " : "") + matricola_dest.getId_matricola();
                        else {
                            for (java.util.Iterator<Ass_cdp_laBulk> y = matricola_src.getCostiScaricati().iterator(); y.hasNext(); ) {
                                Ass_cdp_laBulk ass_cdp_la = y.next();

                                Ass_cdp_laBulk newAssCpdLa = new Ass_cdp_laBulk();
                                newAssCpdLa.setEsercizio(matricola_dest.getEsercizio());
                                newAssCpdLa.setId_matricola(matricola_dest.getId_matricola());
                                newAssCpdLa.setLinea_attivita(ass_cdp_la.getLinea_attivita());
                                if (getCosti_dipendente().isMensile())
                                    newAssCpdLa.setStato(Ass_cdp_laBulk.STATO_SCARICATO_PROVVISORIO);
                                else
                                    newAssCpdLa.setStato(Ass_cdp_laBulk.STATO_NON_SCARICATO);
                                newAssCpdLa.setPrc_la_a1(ass_cdp_la.getPrc_la_a1());
                                newAssCpdLa.setPrc_la_a2(ass_cdp_la.getPrc_la_a2());
                                newAssCpdLa.setPrc_la_a3(ass_cdp_la.getPrc_la_a3());
                               // newAssCpdLa.setFl_dip_altra_uo(ass_cdp_la.getFl_dip_altra_uo());
                                newAssCpdLa.setFl_dip_altra_uo(new Boolean(matricola_dest.isProvenienzaCaricato()));
                                newAssCpdLa.setUser(userContext.getUser());
                                newAssCpdLa.setToBeCreated();

                                matricola_dest.addToCostiScaricati(newAssCpdLa);
                            }

                            for (java.util.Iterator<Ass_cdp_uoBulk> y = matricola_src.getCostiScaricatiAltraUO().iterator(); y.hasNext(); ) {
                                Ass_cdp_uoBulk ass_cdp_uo = y.next();

                                Ass_cdp_uoBulk newAssCpdUo = new Ass_cdp_uoBulk();
                                newAssCpdUo.setEsercizio(matricola_dest.getEsercizio());
                                newAssCpdUo.setId_matricola(matricola_dest.getId_matricola());
                                newAssCpdUo.setUnita_organizzativa(ass_cdp_uo.getUnita_organizzativa());
                                newAssCpdUo.setStato(Ass_cdp_uoBulk.STATO_INIZIALE);
                                newAssCpdUo.setPrc_uo_a1(ass_cdp_uo.getPrc_uo_a1());
                                newAssCpdUo.setPrc_uo_a2(ass_cdp_uo.getPrc_uo_a2());
                                newAssCpdUo.setPrc_uo_a3(ass_cdp_uo.getPrc_uo_a3());

                                newAssCpdUo.setUser(userContext.getUser());
                                newAssCpdUo.setToBeCreated();

                                matricola_dest.addToCostiScaricatiAltraUO(newAssCpdUo);
                            }
                            matricoleRipartite++;
                        }
                    }
                }
            }
            StringBuffer message = new StringBuffer();
            if (matricola_rip != null) {
                if (matricola_rip.contains(","))
                    message.append("Le matricole " + matricola_rip + " non sono state aggiornate in quanto già ripartite in precedenza.");
                else
                    message.append("La matricola " + matricola_rip + " non è stata aggiornata in quanto già ripartita in precedenza.");
            }
            if (matricola_nac != null) {
                if (message.length() != 0)
                    message.append("\n");
                if (matricola_nac.contains(","))
                    message.append("Le matricole " + matricola_nac + " non sono state aggiornate in quanto scaricate da altra UO e non ancora accettate.");
                else
                    message.append("La matricola " + matricola_nac + " non è stata aggiornata in quanto scaricata da altra UO e non ancora accettata.");
            }
            if (matricola_ind != null) {
                if (message.length() != 0)
                    message.append("\n");
                if (matricola_ind.contains(","))
                    message.append("Le matricole " + matricola_ind + " non sono state aggiornate in quanto a tempo indeterminato. Non è possibile copiare da una matricola a tempo determinato.");
                else
                    message.append("La matricola " + matricola_ind + " non è stata aggiornata in quanto a tempo indeterminato. Non è possibile copiare da una matricola a tempo determinato.");
            }
            if (matricola_rap13 != null) {
                if (message.length() != 0)
                    message.append("\n");
                if (matricola_rap13.contains(","))
                    message.append("Le matricole " + matricola_rap13 + " non sono state aggiornate. Non è possibile copiare da una matricola a tempo determinato.");
                else
                    message.append("La matricola " + matricola_rap13 + " non è stata aggiornata. Non è possibile copiare da una matricola a tempo determinato.");
            }
            if (message.length() == 0)
                message.append("Sono state aggiornate " + matricoleRipartite + " matricole in modo corretto.");
            else
                message.append("\n\n Sono state aggiornate " + matricoleRipartite + " matricole in modo corretto.");

            setMessage(message.toString());
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isRipartizioneCostiModificabile() {
        if (!this.getCosti_dipendente().isMensile()) {
            if (!this.isPdgPrevisionaleEnabled() || this.isCostiRipartiti())
                return false;
        }
        return true;
    }

    public boolean isPdgPrevisionaleEnabled() {
        return pdgPrevisionaleEnabled;
    }

    public void setPdgPrevisionaleEnabled(boolean pdgPrevisionaleEnabled) {
        this.pdgPrevisionaleEnabled = pdgPrevisionaleEnabled;
    }

    public void ripartizioneResidui(it.cnr.jada.UserContext userContext, java.util.Collection linee_attivita) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.action.BusinessProcessException {
        try {
            // 05/09/2003
            // Aggiunto controllo sulla chiusura dell'esercizio
            if (((CostiDipendenteComponentSession) createComponentSession()).isEsercizioChiuso(userContext))
                throw new ApplicationException("Funzione non disponibile ad esercizio chiuso.");

            if (linee_attivita != null && !linee_attivita.isEmpty()) {
                int countTi = 0, countTd = 0, countRap3 = 0;
                StringBuffer matrRap3 = new StringBuffer();
                for (SelectionIterator i = getCostiDipendenti().getSelection().iterator(); i.hasNext(); ) {
                    V_cdp_matricolaBulk matricola_dest = (V_cdp_matricolaBulk) getCostiDipendenti().getDetails().get(i.nextIndex());
                    if (matricola_dest.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_INDETERMINATO))
                        countTi++;
                    else {
                        countTd++;
                        if (matricola_dest.getFl_rapporto13()) {
                            countRap3++;
                            if (matrRap3.length() > 0) matrRap3.append(", ");
                            matrRap3.append(matricola_dest.getId_matricola());
                        }
                    }
                }
                if (countTi > 0 && countTd > 0)
                    throw new ApplicationException("Funzione non disponibile in presenza di selezione contemporanea di dipendenti a tempo deteminato ed indeterminato.");
                else if (countRap3 > 0)
                    throw new it.cnr.jada.action.MessageToUser("Funzione non disponibile per le matricole " + matrRap3 + ".");

                if (getCostiDipendenti().getSelection().size() == 0) {
                    ripartizioneResidui(userContext, (V_cdp_matricolaBulk) getCostiDipendenti().getModel(), linee_attivita);
                    setMessage(WARNING_MESSAGE, "Ripartizione dei residui effettuata correttamente per la matricola " + ((V_cdp_matricolaBulk) getCostiDipendenti().getModel()).getId_matricola() + ".");
                } else {
                    it.cnr.jada.comp.ApplicationException lastException = null;
                    int exceptionCount = 0;
                    int size = getCostiDipendenti().getSelection().size();

                    for (SelectionIterator i = getCostiDipendenti().getSelection().iterator(); i.hasNext(); ) {
                        V_cdp_matricolaBulk matricola_dest = (V_cdp_matricolaBulk) getCostiDipendenti().getDetails().get(i.nextIndex());
                        try {
                            ripartizioneResidui(userContext, matricola_dest, linee_attivita);
                            i.remove();
                        } catch (it.cnr.jada.comp.ApplicationException e) {
                            exceptionCount++;
                            lastException = e;
                        }
                    }
                    if (exceptionCount == 1)
                        setMessage("Non è stato possibile ripartire i residui per la matricola selezionata.\n" + lastException.getMessage());
                    else if (exceptionCount > 1)
                        setMessage("Non è stato possibile ripartire i residui " + exceptionCount + " matricole; le matricole correttamente ripartite sono state deselezionate.");
                    else
                        setMessage(WARNING_MESSAGE, "Ripartizione dei residui effettuata correttamente per " + size + " matricole.");
                }
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    private void ripartizioneResidui(it.cnr.jada.UserContext userContext, V_cdp_matricolaBulk matricola, java.util.Collection linee_attivita) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.action.BusinessProcessException {
        try {
            // Lock della matricola
            ((CostiDipendenteComponentSession) createComponentSession()).lockMatricola(userContext, matricola.getId_matricola(), matricola.getMese());

            // Cerco la riga di ass_cdp_uo da cui proviene la matricola
            // Se non esiste vuol dire che la matricola appartiene alla UO dell'utente
            if (!matricola.isProvenienzaInterna() &&
                    (matricola.getStato_carico() == null || !matricola.getStato_carico().equals(Ass_cdp_uoBulk.STATO_ACCETTATO)))
                throw new ApplicationException("Per poter effettuare lo scarico di una matricola proveniente da un'altra unità organizzativa è necessario prima accettare la contrattazione.");

            // Costanti
            final java.math.BigDecimal BD_100 = java.math.BigDecimal.valueOf(100);
            final java.math.BigDecimal BD_LATT_S = java.math.BigDecimal.valueOf(linee_attivita.size());

            // Calcolo del residuo per ogni anno
            java.math.BigDecimal prc_a1 = BD_100.subtract(matricola.getTotale_prc_ripartito_a1());
            java.math.BigDecimal prc_a2 = BD_100.subtract(matricola.getTotale_prc_ripartito_a2());
            java.math.BigDecimal prc_a3 = BD_100.subtract(matricola.getTotale_prc_ripartito_a3());

            // Calcolo della percentuale ripartita per ogni anno
            prc_a1 = prc_a1.divide(BD_LATT_S, 2, java.math.BigDecimal.ROUND_HALF_UP);
            prc_a2 = prc_a2.divide(BD_LATT_S, 2, java.math.BigDecimal.ROUND_HALF_UP);
            prc_a3 = prc_a3.divide(BD_LATT_S, 2, java.math.BigDecimal.ROUND_HALF_UP);

            if ((matricola.getMese() == 0 && (prc_a1.compareTo(BigDecimal.ZERO) != 0 ||
                    prc_a2.compareTo(BigDecimal.ZERO) != 0 ||
                    prc_a3.compareTo(BigDecimal.ZERO) != 0)) ||
                    (matricola.getMese() != 0 && (prc_a1.compareTo(BigDecimal.ZERO) != 0))) {

                // Creazione/modifica delle Ass_cdp_la
                for (java.util.Iterator i = linee_attivita.iterator(); i.hasNext(); ) {
                    it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) i.next();

                    Ass_cdp_laBulk newAssCdpLa = new Ass_cdp_laBulk(linea.getCd_centro_responsabilita(),
                            linea.getCd_linea_attivita(),
                            matricola.getEsercizio(),
                            matricola.getId_matricola(),
                            matricola.getMese());

                    if (matricola.getCostiScaricati().containsByPrimaryKey(newAssCdpLa)) {
                        newAssCdpLa = (Ass_cdp_laBulk) matricola.getCostiScaricati().get(matricola.getCostiScaricati().indexOfByPrimaryKey(newAssCdpLa));

                        // Se esiste aggiungo alla percentuale già scaricata la percentuale residua ripartita
                        if (i.hasNext()) {
                            newAssCdpLa.setPrc_la_a1(Utility.nvl(newAssCdpLa.getPrc_la_a1()).add(prc_a1));
                            if (matricola.getMese() == 0) {
                                newAssCdpLa.setPrc_la_a2(Utility.nvl(newAssCdpLa.getPrc_la_a2()).add(prc_a2));
                                newAssCdpLa.setPrc_la_a3(Utility.nvl(newAssCdpLa.getPrc_la_a3()).add(prc_a3));
                            }
                        } else {
                            newAssCdpLa.setPrc_la_a1(Utility.nvl(newAssCdpLa.getPrc_la_a1()).add(BD_100.subtract(matricola.getTotale_prc_ripartito_a1())));
                            if (matricola.getMese() == 0) {
                                newAssCdpLa.setPrc_la_a2(Utility.nvl(newAssCdpLa.getPrc_la_a2()).add(BD_100.subtract(matricola.getTotale_prc_ripartito_a2())));
                                newAssCdpLa.setPrc_la_a3(Utility.nvl(newAssCdpLa.getPrc_la_a3()).add(BD_100.subtract(matricola.getTotale_prc_ripartito_a3())));
                            }
                        }
                        newAssCdpLa.setToBeUpdated();
                    } else {
                        // Se non esiste creo una nuova ass_cdp_la con la
                        // percentuale residua ripartita
                        if (getCosti_dipendente().isMensile())
                            newAssCdpLa.setStato(Ass_cdp_laBulk.STATO_SCARICATO_PROVVISORIO);
                        else
                            newAssCdpLa.setStato(Ass_cdp_laBulk.STATO_NON_SCARICATO);

                        if (i.hasNext()) {
                            newAssCdpLa.setPrc_la_a1(prc_a1);
                            if (matricola.getMese() == 0) {
                                newAssCdpLa.setPrc_la_a2(prc_a2);
                                newAssCdpLa.setPrc_la_a3(prc_a3);
                            } else {
                                newAssCdpLa.setPrc_la_a2(BigDecimal.ZERO);
                                newAssCdpLa.setPrc_la_a3(BigDecimal.ZERO);
                            }
                        } else {
                            newAssCdpLa.setPrc_la_a1(BD_100.subtract(matricola.getTotale_prc_ripartito_a1()));
                            if (matricola.getMese() == 0) {
                                newAssCdpLa.setPrc_la_a2(BD_100.subtract(matricola.getTotale_prc_ripartito_a2()));
                                newAssCdpLa.setPrc_la_a3(BD_100.subtract(matricola.getTotale_prc_ripartito_a3()));
                            } else {
                                newAssCdpLa.setPrc_la_a2(BigDecimal.ZERO);
                                newAssCdpLa.setPrc_la_a3(BigDecimal.ZERO);
                            }
                        }

                        newAssCdpLa.setGiorni_la_a1(BigDecimal.ZERO);
                        newAssCdpLa.setGiorni_la_a2(BigDecimal.ZERO);
                        newAssCdpLa.setGiorni_la_a3(BigDecimal.ZERO);
                        newAssCdpLa.setFl_dip_altra_uo(new Boolean(matricola.isProvenienzaCaricato()));

                        newAssCdpLa.setUser(userContext.getUser());
                        newAssCdpLa.setToBeCreated();

                        matricola.addToCostiScaricati(newAssCdpLa);
                    }
                }
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
}
