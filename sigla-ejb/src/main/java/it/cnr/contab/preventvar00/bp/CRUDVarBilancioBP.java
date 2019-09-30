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

package it.cnr.contab.preventvar00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.prevent00.bulk.V_assestato_voceBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancio_detBulk;
import it.cnr.contab.preventvar00.ejb.VarBilancioComponentSession;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.PrintSpoolerBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Business Process di gestione delle variazioni di bilancio preventivo
 */

public class CRUDVarBilancioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
    private final SimpleDetailCRUDController dettagliCRUDController = new SimpleDetailCRUDController("dettagliCRUDController", Var_bilancio_detBulk.class, "dettagli", this, false) {
        protected void validate(ActionContext context, OggettoBulk bulk) throws ValidationException {
            validaDettaglioVariazioneDiBilancio(context, bulk);
        }
    };
    private String competenza_residui;

    public CRUDVarBilancioBP() {
        super();
    }

    public CRUDVarBilancioBP(String function) {
        super(function);
    }

    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

        super.basicEdit(context, bulk, doInitializeForEdit);

        if (getStatus() != VIEW) {
            Var_bilancioBulk varBil = (Var_bilancioBulk) bulk;
            if (varBil != null && varBil.isDefinitiva()) {
                setStatus(VIEW);
                setMessage("Variazione di bilancio Definitiva. Non è possibile modificarla");
            }
            if (varBil != null && varBil.isCancellatoLogicamente()) {
                setStatus(VIEW);
                setMessage("Variazione di bilancio Annullata. Non è possibile modificarla");
            }
        }
    }

    /**
     * Metodo utilizzato per creare una toolbar applicativa personalizzata.
     *
     * @return toolbar Toolbar in uso
     */

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {

        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[10];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.definitiveSave");

        return toolbar;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'dettagliCRUDController'
     *
     * @return Il valore della proprietà 'dettagliCRUDController'
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettagliCRUDController() {
        return dettagliCRUDController;
    }

    public void verificoUnitaENTE(ActionContext context) throws it.cnr.jada.comp.ApplicationException {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        if (unita_organizzativa.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) != 0)
            throw new it.cnr.jada.comp.ApplicationException("Funzione non consentita!\nAccessibile solo all'ENTE!");
    }

    public void init(Config config, ActionContext context) throws BusinessProcessException {

        try {
            super.init(config, context);
            setCompetenza_residui(config.getInitParameter("comp_res"));
            Var_bilancioBulk varBil = (Var_bilancioBulk) getModel();
            it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk bil = varBil.getBilancio();
            verificoUnitaENTE(context);
            if (bil != null && bil.getStato().compareTo(it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk.STATO_C) != 0 &&
                    getCompetenza_residui() != null && getCompetenza_residui().equalsIgnoreCase("C"))
                throw new it.cnr.jada.comp.ApplicationException("Il bilancio di previsione non e' stato approvato");

        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    /**
     * Inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri
     */
    protected void initializePrintBP(AbstractPrintBP bp) {
        OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
        //report: "variazioni_bilancio.rpt"
        printbp.setReportName("/preventivo/preventivo/variazioni_bilancio.jasper");
        Var_bilancioBulk var_bilancio = (Var_bilancioBulk) getModel();

        // impostazone dell'ufficio di competenza della stampa, utile all'invio della PEC
        printbp.initCdServizioPEC(PrintSpoolerBP.PEC_BILANCIO);
        // impostazone della descrizione del documento, utile all'invio della PEC
        printbp.initDsOggettoPEC("Variazione di Bilancio " + var_bilancio.getEsercizio() + "/" + var_bilancio.getCd_cds() + "/" + var_bilancio.getPg_variazione() + ", " + var_bilancio.getDs_variazione());
        printbp.initDsNumregPEC(var_bilancio.getPg_variazione() + "-" + var_bilancio.getEsercizio());

        Print_spooler_paramBulk param;
        param = new Print_spooler_paramBulk();
        param.setNomeParam("esercizio");
        param.setValoreParam(var_bilancio.getEsercizio().toString());
        param.setParamType("java.lang.Integer");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("cds");
        param.setValoreParam(var_bilancio.getCd_cds());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("ti_appartenenza");
        param.setValoreParam(var_bilancio.getTi_appartenenza());
        param.setParamType("java.lang.String");
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("pg_variazione");
        param.setValoreParam(var_bilancio.getPg_variazione().toString());
        param.setParamType("java.lang.Long");
        printbp.addToPrintSpoolerParam(param);

    }

    public boolean isDeleteButtonEnabled() {
        return (super.isDeleteButtonEnabled() && !((Var_bilancioBulk) getModel()).isDefinitiva());
    }

    /**
     * Abilito il bottone di Stampa solo se il Model è stato riempito
     */
    public boolean isPrintButtonHidden() {
        return super.isPrintButtonHidden() || isInserting() || isSearching();
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
     *
     * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
     */
    public boolean isSalvaDefinitivoButtonEnabled() {

        return isEditing() && !isDirty() &&
                getModel() != null &&
                getModel().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
                !((Var_bilancioBulk) getModel()).isDefinitiva();
    }

    /**
     * Gestione del salvataggio definitivo di una variazione di bilancio preventivo
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

        try {

            VarBilancioComponentSession comp = (VarBilancioComponentSession) createComponentSession();
            Var_bilancioBulk varBilancio = comp.salvaDefinitivo(context.getUserContext(), (Var_bilancioBulk) getModel());

            edit(context, varBilancio);
//		save(context);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
//	}catch(ValidationException ex){
//		throw handleException(ex);
        }
    }

    /**
     * Nell'eliminazione della viariazione viene impostato solo lo stato ad A e
     * richiamati i controlli in eliminaVariazione
     **/

    public void delete(ActionContext actioncontext) throws BusinessProcessException {

        try {

            VarBilancioComponentSession comp = (VarBilancioComponentSession) createComponentSession();
            ((Var_bilancioBulk) getModel()).setToBeUpdated();
            ((Var_bilancioBulk) getModel()).cancellaLogicamente();
            Var_bilancioBulk varBilancio = comp.eliminaVariazione(actioncontext.getUserContext(), getModel());

//		OggettoBulk varBilancio = comp.modificaConBulk(actioncontext.getUserContext(), getModel());
            setMessage("Annullamento effettuato.");
            edit(actioncontext, varBilancio);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        } catch (PersistencyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Gestione validazione del dettaglio di una variazione di bilancio
     * La voce e l'importo sono obbligatori
     *
     * @param context L'ActionContext della richiesta
     * @param bulk    dettaglio variazione
     * @throws ValidationException
     */
    public void validaDettaglioVariazioneDiBilancio(ActionContext context, OggettoBulk bulk) throws ValidationException {

        Var_bilancioBulk varBilancio = (Var_bilancioBulk) getModel();
        Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk) bulk;

        varBilDett.validate();

        if (varBilancio.hasVoceDuplicata(varBilDett, varBilDett.getVoceFSaldi())) {
            varBilDett.setVoceFSaldi(new V_assestato_voceBulk());
            varBilDett.setTipoGestione(varBilancio.getTipoGestione());
            throw new ValidationException("Esiste già un dettaglio con la voce selezionata!");
        }
    }

    public boolean isUoEnte(ActionContext context) {
        Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
        if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
            return true;
        return false;
    }

    public String getCompetenza_residui() {
        return competenza_residui;
    }

    public void setCompetenza_residui(String competenza_residui) {
        this.competenza_residui = competenza_residui;
    }

}
