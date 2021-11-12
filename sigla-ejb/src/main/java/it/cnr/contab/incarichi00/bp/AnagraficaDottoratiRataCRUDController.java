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

package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiRateBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

/**
 * Insert the type's description here.
 * Creation date: (6/24/2002 12:04:47 PM)
 *
 * @author: Roberto Peli
 */
public class AnagraficaDottoratiRataCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    /**
     * MinicarrieraRataCRUDController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     */
    public AnagraficaDottoratiRataCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    /**
     * MinicarrieraRataCRUDController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     * @param multiSelection   boolean
     */
    public AnagraficaDottoratiRataCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
        super(name, modelClass, listPropertyName, parent, multiSelection);
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isGrowable() {

        AnagraficaDottoratiBulk carriera = (AnagraficaDottoratiBulk) getParentModel();
        return super.isGrowable() &&
                !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching();
    }

    public boolean isInputReadonly() {
        AnagraficaDottoratiRateBulk rata = (AnagraficaDottoratiRateBulk) getModel();
        return super.isInputReadonly();
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isShrinkable() {

        AnagraficaDottoratiBulk carriera = (AnagraficaDottoratiBulk) getParentModel();
        return super.isShrinkable() &&
                !((it.cnr.jada.util.action.CRUDBP) getParentController()).isSearching();
    }

    public void validate(ActionContext context, OggettoBulk model) throws ValidationException {

        //Non dovrebbe + servire: se non attiva si disabilita il controller.
        //if (!((MinicarrieraBulk)getParentModel()).isAttiva())
        //throw new ValidationException("La minicarriera non è attiva! Impossibile effettuare modifiche.");

        AnagraficaDottoratiRateBulk rata = (AnagraficaDottoratiRateBulk) model;
        if (rata.getImRata() == null)
            throw new ValidationException("Specificare l'importo della rata \"" + rata.getPgRata().longValue() + "\".");
        if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(rata.getImRata()) == 0)
            throw new ValidationException("L'importo della rata \"" + rata.getPgRata().longValue() + "\" non è valido.");

        if (rata.getDtInizioRata() == null || rata.getDtFineRata() == null)
            throw new ValidationException("Inserire le date di validità della rata \"" + rata.getPgRata().longValue() + "\".");
        if (rata.getDtScadenza() == null)
            throw new ValidationException("Inserire la data di scadenza della rata \"" + rata.getPgRata().longValue() + "\".");

        if (rata.getDtFineRata().before(rata.getDtInizioRata()))
            throw new ValidationException("Date di validità della rata \"" + rata.getPgRata().longValue() + "\" non corrette. Verificare i periodi.");

        //E' stata data una notizia falsa e tendenziosa su questo controllo. Lo commento per il momento.
        //if (rata.getDt_scadenza().before(rata.getDt_inizio_rata()))
        //throw new ValidationException("La data di scadenza non puo' essere precedente alla data inizio validità della rata \"" + rata.getPg_rata().longValue() + "\".");
        if (((AnagraficaDottoratiBulk) getParentModel()).getDtFineDottorato().before(rata.getDtFineRata()))
            throw new ValidationException("La rata \"" + rata.getPgRata().longValue() + "\" ha una data fine validità posteriore alla data fine validità della minicarriera.");

        //try {
        //((FatturaPassivaComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).validaRiga(context.getUserContext(), (Fattura_passiva_rigaBulk)model);
        //} catch (it.cnr.jada.comp.ApplicationException e) {
        //throw new ValidationException(e.getMessage());
        //} catch (Throwable e) {
        //throw new it.cnr.jada.DetailedRuntimeException(e);
        //}
    }

    public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {

        AnagraficaDottoratiRateBulk rata = (AnagraficaDottoratiRateBulk) detail;

   /**     if (!((MinicarrieraBulk) getParentModel()).isAttiva())
            throw new ValidationException("La minicarriera non è attiva! Impossibile effettuare modifiche.");

        if (rata.isAssociataACompenso())
            throw new ValidationException("La rata \"" + rata.getPg_rata().longValue() + "\" è associata a compenso. Impossibile eliminarla.");
*/
    }
    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

        AnagraficaDottoratiBulk carriera = (AnagraficaDottoratiBulk) getParentModel();
        CRUDAnagraficaDottoratiBP parentController = (CRUDAnagraficaDottoratiBP) getParentController();
        super.openButtonGROUPToolbar(context);
        it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                context,
                HttpActionContext.isFromBootstrap(context) ? "fa fa-plus-square": "img/history16.gif",
                (!(isInputReadonly() ||  parentController.isSearching()) ? "javascript:submitForm('doGeneraRate')" : null),
                true,
                "Crea rate",
                "btn-title btn-outline-primary",
                HttpActionContext.isFromBootstrap(context));
        super.writeHTMLToolbar(context, reset, find, delete, false, false);
        it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                context,
                HttpActionContext.isFromBootstrap(context) ? "fa fa-plus-circle": "img/properties16.gif",
                ((!(//isInputReadonly() ||
                        parentController.isSearching() )) ? "javascript:submitForm('doCreaCompenso')" : null),
                true,
                "Crea compenso",
                "btn-title btn-outline-primary",
                HttpActionContext.isFromBootstrap(context));
        it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                context,
                HttpActionContext.isFromBootstrap(context) ? "fa fa-external-link": "img/folderopen16.gif",
                (!parentController.isSearching() &&
                        getModel() != null) ? "javascript:submitForm('doVisualizzaCompenso')" : null,
                false,
                "Visualizza compenso",
                "btn-title btn-outline-info",
                HttpActionContext.isFromBootstrap(context));
        super.closeButtonGROUPToolbar(context);
    }
}
