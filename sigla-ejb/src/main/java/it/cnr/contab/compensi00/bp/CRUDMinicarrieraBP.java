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

package it.cnr.contab.compensi00.bp;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.ejb.MinicarrieraComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.AbstractPrintBP;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.12.44)
 *
 * @author: Roberto Fantino
 */
public class CRUDMinicarrieraBP
        extends it.cnr.jada.util.action.SimpleCRUDBP
        implements IDefferedUpdateSaldiBP {

    public final static String SAVE_POINT_NAME = "MINICARRIERA_SP";

    private final MinicarrieraRataCRUDController rateCRUDController = new MinicarrieraRataCRUDController(
            "rateCRUDController",
            Minicarriera_rataBulk.class,
            "minicarriera_rate",
            this);

    private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;

    /**
     * CRUDCompensoBP constructor comment.
     */
    public CRUDMinicarrieraBP() {
        super();
    }

    /**
     * CRUDCompensoBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDMinicarrieraBP(String function) {
        super(function + "Tr");
    }

    /**
     * Reimplementato per mandare in stato view il controller in caso di modello in
     * stato 'non editabile' (vedi bulk)
     */

    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

        super.basicEdit(context, bulk, doInitializeForEdit);

        if (getStatus() != VIEW) {
            MinicarrieraBulk carriera = (MinicarrieraBulk) bulk;
            if (carriera != null && !carriera.isEditable()) {
                setStatus(VIEW);
                setMessage("Minicarriera in stato \"" + carriera.STATI.get(carriera.getStato()) + "\". Non consentita la modifica.");
            }
        }
    }

    /**
     * Genera le rate chiamando il metodo relativo sulla component
     */

    public void calcolaAliquotaMedia(ActionContext context)
            throws BusinessProcessException {

        try {

            MinicarrieraComponentSession comp = (MinicarrieraComponentSession) createComponentSession();
            MinicarrieraBulk carriera = comp.calcolaAliquotaMedia(context.getUserContext(), (MinicarrieraBulk) getModel());
            carriera.setAliquotaCalcolata(true);
            setModel(context, carriera);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }

    }

    /**
     * Chiama il metodo sulla component 'completa percipiente' e mette il controller
     * in stato edit sul modello clone
     */

    public void completaPercipiente(
            ActionContext context,
            MinicarrieraBulk carriera,
            V_terzo_per_compensoBulk vTerzo) throws BusinessProcessException {

        try {

            MinicarrieraComponentSession component = (MinicarrieraComponentSession) createComponentSession();
            MinicarrieraBulk carrieraClone = component.completaPercipiente(context.getUserContext(), carriera, vTerzo);

            setModel(context, carrieraClone);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }

    }

    /**
     * Reimplementato per gestire il passaggio dei parametri dei saldi per il doc contabile
     * una volta che l'utente ha deciso di eseguire lo sfondamento di cassa. Vedi action
     * metodo 'onCheckDisponibiltaDiCassaFailed'
     */

    public void create(it.cnr.jada.action.ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            getModel().setToBeCreated();
            setModel(
                    context,
                    ((MinicarrieraComponentSession) createComponentSession()).creaConBulk(
                            context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
            setUserConfirm(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Ricerca i conti disponibili e imposta nel modello il primo elemento trovato
     */

    public void findListaBanche(ActionContext context) throws BusinessProcessException {

        try {
            MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();
            if (carriera.getModalita_pagamento() != null) {
                MinicarrieraComponentSession component = (MinicarrieraComponentSession) createComponentSession();
                java.util.List coll = component.findListaBanche(context.getUserContext(), carriera);

                //	Assegno di default la prima banca tra quelle selezionate
                if (coll == null || coll.isEmpty())
                    carriera.setBanca(null);
                else
                    carriera.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk) new java.util.Vector(coll).firstElement());
            } else
                carriera.setBanca(null);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Ricerca i tipi di trattamento validi ed imposta nel modello il primo elemento
     * trovato
     */

    public void findTipiRapporto(ActionContext context) throws BusinessProcessException {

        try {
            MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();
            if (carriera.getTerzo() != null) {
                MinicarrieraComponentSession component = (MinicarrieraComponentSession) createComponentSession();
                java.util.Collection coll = component.findTipiRapporto(context.getUserContext(), carriera);
                carriera.setTipiRapporto(coll);

                if (coll == null || coll.isEmpty()) {
                    carriera.setTipo_rapporto(null);
                    throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi Rapporto validi associati al percipiente selezionato");
                }
            } else
                carriera.setTipo_rapporto(null);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Ricerca i tipi di trattamento validi ed imposta nel modello il primo elemento
     * trovato
     */

    public void findTipiTrattamento(ActionContext context) throws BusinessProcessException {

        try {
            MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();
            if (carriera.getTipo_rapporto() != null) {
                MinicarrieraComponentSession component = (MinicarrieraComponentSession) createComponentSession();
                java.util.Collection coll = component.findTipiTrattamento(context.getUserContext(), carriera);
                carriera.setTipiTrattamento(coll);
                carriera.setTipo_trattamento(null);

                //	Assegno di default il primo trattamento trovato
                if (coll == null || coll.isEmpty()) {
                    throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi Trattamento associati al Tipo di Rapporto selezionato");
                }
                //else
                //carriera.setTipo_trattamento((it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk)new java.util.Vector(coll).firstElement());
            } else
                carriera.setTipo_trattamento(null);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public void findTipiPrestazioneCompenso(ActionContext context) throws BusinessProcessException {

        try {
            MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();
            if (carriera.getTipo_rapporto() != null) {
                MinicarrieraComponentSession component = (MinicarrieraComponentSession) createComponentSession();
                java.util.Collection coll = component.findTipiPrestazioneCompenso(context.getUserContext(), carriera);
                carriera.setTipiPrestazioneCompenso(coll);

                if (coll == null || coll.isEmpty()) {
                    carriera.setTipoPrestazioneCompenso(null);
                    throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi di prestazione associati al Tipo di Rapporto selezionato");
                }
            } else
                carriera.setTipoPrestazioneCompenso(null);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Genera le rate chiamando il metodo relativo sulla component
     */

    public void generaRate(ActionContext context)
            throws BusinessProcessException {

        try {

            MinicarrieraComponentSession comp = (MinicarrieraComponentSession) createComponentSession();
            MinicarrieraBulk carriera = comp.generaRate(context.getUserContext(), (MinicarrieraBulk) getModel(), isEditing());

            setModel(context, carriera);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }

    }

    /**
     * Restituisce il modello responsabile dell'aggiornamento dei saldi
     */
    public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {

        if (getParent() != null && getParent() instanceof IDefferedUpdateSaldiBP)
            return getDefferedUpdateSaldiParentBP().getDefferedUpdateSaldiBulk();
        return (IDefferUpdateSaldi) getModel();
    }

    /**
     * Restituisce il business process del modello responsabile dell'aggiornamento dei saldi
     */

    public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

        if (getParent() != null && getParent() instanceof IDefferedUpdateSaldiBP)
            return ((IDefferedUpdateSaldiBP) getParent()).getDefferedUpdateSaldiParentBP();
        return this;
    }

    /**
     * Restituisce il controller delle rate
     */

    public final MinicarrieraRataCRUDController getRateCRUDController() {
        return rateCRUDController;
    }

    /**
     * Attributo dei dati relativi alla scelta dell'utente di proseguire con loù
     * sfondamento di cassa
     */

    public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
        return userConfirm;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/29/2002 12:59:29 PM)
     *
     * @param newUserConfirm it.cnr.contab.doccont00.core.bulk.OptionRequestParameter
     */
    public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter newUserConfirm) {
        userConfirm = newUserConfirm;
    }

    /**
     * Inizializza il controller e imposta i tab sulla prima pagina
     */

    protected void init(Config config, ActionContext context) throws BusinessProcessException {

        super.init(config, context);
        resetTabs(context);
    }

    protected void initializePrintBP(ActionContext context, AbstractPrintBP bp) {

        OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
        printbp.setReportName("/docamm/docamm/minicarriera_bds.jasper");

        MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();

        Print_spooler_paramBulk param;

        param = new Print_spooler_paramBulk();
        param.setNomeParam("cds");
        param.setValoreParam(carriera.getCd_cds());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("uo");
        param.setValoreParam(carriera.getCd_unita_organizzativa());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("esercizio");
        param.setValoreParam(carriera.getEsercizio().toString());
        param.setParamType("java.lang.Integer");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("pg");
        param.setValoreParam(carriera.getPg_minicarriera().toString());
        param.setParamType("java.lang.Long");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("esercizio_stm");
        param.setValoreParam(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString());
        param.setParamType("java.lang.Integer");
        printbp.addToPrintSpoolerParam(param);
    }

    public boolean isPrintButtonHidden() {
        return getPrintbp() == null ||
                isDirty() ||
                isBringBack() ||
                (!isViewing() && !isEditing());
    }

    /**
     * Imposta il tab sulla pagina di default
     */

    public void resetTabs(ActionContext context) {

        setTab("tab", "tabMinicarriera");
        setTab("subtab", "tabMinicarrieraPercipientePagamenti");
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/02/2002 12.56.44)
     *
     * @param userContext it.cnr.jada.UserContext
     * @param compenso    it.cnr.contab.compensi00.docs.bulk.CompensoBulk
     * @param aTerzo      it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
     * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
     */
    public void ripristinaSelezioneTipoRapporto() {

        MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();
        Tipo_rapportoBulk tipoRapporto = carriera.getTipo_rapporto();

        // ripristino la selezione del Tipo Rapporto
        if (tipoRapporto != null) {
            for (java.util.Iterator i = carriera.getTipiRapporto().iterator(); i.hasNext(); ) {
                Tipo_rapportoBulk tipoRapp = (Tipo_rapportoBulk) i.next();
                if (tipoRapp.getCd_tipo_rapporto().equals(tipoRapporto.getCd_tipo_rapporto()))
                    carriera.setTipo_rapporto(tipoRapp);
            }
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/02/2002 12.56.44)
     *
     * @param userContext it.cnr.jada.UserContext
     * @param compenso    it.cnr.contab.compensi00.docs.bulk.CompensoBulk
     * @param aTerzo      it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
     * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
     */
    public void ripristinaSelezioneTipoTrattamento() {

        MinicarrieraBulk carriera = (MinicarrieraBulk) getModel();
        Tipo_trattamentoBulk tipoTrattamento = carriera.getTipo_trattamento();

        if (tipoTrattamento == null && carriera.getCd_trattamento() != null) {
            //Caso in cui sto rinnovando o ripristinando una minicarriera
            tipoTrattamento = new Tipo_trattamentoBulk();
            tipoTrattamento.setCd_trattamento(carriera.getCd_trattamento());
        }

        // ripristino la selezione del Tipo Trattamento
        if (tipoTrattamento != null) {
            for (java.util.Iterator i = carriera.getTipiTrattamento().iterator(); i.hasNext(); ) {
                Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk) i.next();
                if (tratt.getCd_trattamento().equals(tipoTrattamento.getCd_trattamento()))
                    carriera.setTipo_trattamento(tratt);
            }
        }
    }

    /**
     * Reimplementato per gestire il passaggio dei parametri dei saldi per il doc contabile
     * una volta che l'utente ha deciso di eseguire lo sfondamento di cassa. Vedi action
     * metodo 'onCheckDisponibiltaDiCassaFailed'
     */

    public void update(ActionContext context)
            throws it.cnr.jada.action.BusinessProcessException {

        try {
            getModel().setToBeUpdated();
            setModel(
                    context,
                    ((MinicarrieraComponentSession) createComponentSession()).modificaConBulk(
                            context.getUserContext(),
                            getModel(),
                            getUserConfirm()));
            setUserConfirm(null);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Imposta il tab sulla pagina di default
     */

    public int validaPercipiente(ActionContext context, boolean checkValidita)
            throws BusinessProcessException {

        if (checkValidita)
            try {
                MinicarrieraComponentSession session = (MinicarrieraComponentSession) createComponentSession();
                return session.validaPercipiente(
                        context.getUserContext(),
                        (MinicarrieraBulk) getModel());
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw handleException(ex);
            } catch (java.rmi.RemoteException ex) {
                throw handleException(ex);
            }
        return V_terzo_per_compensoBulk.TUTTO_BENE;
    }

    public boolean isTerzoCervellone(UserContext userContext, MinicarrieraBulk carriera) throws BusinessProcessException {

        try {

            MinicarrieraComponentSession sess = (MinicarrieraComponentSession) createComponentSession();
            return sess.isTerzoCervellone(userContext, carriera);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public boolean isGestitiIncarichi(UserContext userContext) throws BusinessProcessException {
        try {
            MinicarrieraComponentSession sess = (MinicarrieraComponentSession) createComponentSession();
            return sess.isGestitiIncarichi(userContext);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    public void completaIncarico(ActionContext context, MinicarrieraBulk carriera, Incarichi_repertorioBulk incarico) throws BusinessProcessException {

        try {
            MinicarrieraComponentSession component = (MinicarrieraComponentSession) createComponentSession();
            MinicarrieraBulk carrieraClone = component.completaIncarico(context.getUserContext(), carriera, incarico);

            setModel(context, carrieraClone);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }
/*
public boolean isGestitePrestazioni(UserContext userContext) throws BusinessProcessException {
	try{
		MinicarrieraComponentSession sess = (MinicarrieraComponentSession)createComponentSession();
		return sess.isGestitePrestazioni(userContext);
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
*/
}
