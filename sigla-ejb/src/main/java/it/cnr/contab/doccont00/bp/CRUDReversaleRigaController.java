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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Optional;

public class CRUDReversaleRigaController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    public CRUDReversaleRigaController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    public void remove(it.cnr.jada.action.ActionContext context) throws ValidationException, it.cnr.jada.action.BusinessProcessException {
        super.remove(context);
        ((ReversaleBulk) getParentModel()).refreshImporto();

    }

    /*
    caso 1 - riga non ancora inserita nel db: se l'utente ne richiede la cancellazione viene effettivamente rimossa
             se viene richiesta la cancellazione di una fattura con associate note a debito/credito, anche questi doc. amm
             devono essere rimossi
    caso 2 - NON USATO - riga già salvata nel db: se l'utente ne richiede la cancellazione viene annullata
    */
    public OggettoBulk removeDetail(int index) {
        Reversale_rigaIBulk rigaDaCancellare = (Reversale_rigaIBulk) getDetails().get(index);
        Reversale_rigaIBulk row;
        // la riga non e' ancora stata inserita nel db --> la cancello
        if (rigaDaCancellare != null && rigaDaCancellare.isToBeCreated() && rigaDaCancellare.isFlCancellazione()) {
            int len = getDetails().size();
            for (int i = len - 1; i >= 0; i--) {
                row = (Reversale_rigaIBulk) getDetails().get(i);
                if (row.getEsercizio_accertamento().equals(rigaDaCancellare.getEsercizio_accertamento()) &&
                        row.getCd_cds().equals(rigaDaCancellare.getCd_cds()) &&
                        row.getEsercizio_ori_accertamento().equals(rigaDaCancellare.getEsercizio_ori_accertamento()) &&
                        row.getPg_accertamento().equals(rigaDaCancellare.getPg_accertamento()) &&
                        row.getPg_accertamento_scadenzario().equals(rigaDaCancellare.getPg_accertamento_scadenzario())) {
                    row = (Reversale_rigaIBulk) ((ReversaleBulk) getParentModel()).removeFromReversale_rigaColl(i);
                    row.setToBeDeleted();
                }
            }
            return rigaDaCancellare;
        } else if (
                Optional.ofNullable(getParentModel())
                        .filter(ReversaleBulk.class::isInstance)
                        .map(ReversaleBulk.class::cast)
                        .filter(reversaleBulk -> Optional.ofNullable(reversaleBulk.getStatoVarSos()).isPresent())
                        .map(reversaleBulk -> reversaleBulk.getStatoVarSos().equals(StatoVariazioneSostituzione.DA_VARIARE.value()))
                        .orElse(Boolean.FALSE)){
            rigaDaCancellare.setToBeDeleted();
            ((ReversaleBulk) getParentModel()).removeFromReversale_rigaColl(index);
            return rigaDaCancellare;
        }
        // la riga e' già stata inserita nel db e il suo stato e' != da STATO_ANNULLATO --> aggiorno lo stato
        else if (rigaDaCancellare != null && !rigaDaCancellare.isToBeCreated() && !rigaDaCancellare.getStato().equals(rigaDaCancellare.STATO_ANNULLATO)) {
            rigaDaCancellare.annulla();
            return rigaDaCancellare;
        } else
            return rigaDaCancellare;
    }

    /* Non e' possibile cancellare note a credito / debito; e' necessario cancellare la fattura attiva da cui dipendono */
    public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
        if (detail instanceof Reversale_rigaIBulk && !((Reversale_rigaIBulk) detail).isFlCancellazione()) {
            Reversale_rigaIBulk rigaDaCancellare = (Reversale_rigaIBulk) detail;
            Reversale_rigaIBulk row = null;
            int len = getDetails().size();
            Long pg_fattura_attiva = new Long(0);
            //ricerco il progressivo della fattura attiva da cui la nota di debito/credito dipende
            int index;
            for (index = len - 1; index >= 0; index--) {
                row = (Reversale_rigaIBulk) getDetails().get(index);
                if (row.getEsercizio_accertamento().equals(rigaDaCancellare.getEsercizio_accertamento()) &&
                        row.getCd_cds().equals(rigaDaCancellare.getCd_cds()) &&
                        row.getEsercizio_ori_accertamento().equals(rigaDaCancellare.getEsercizio_ori_accertamento()) &&
                        row.getPg_accertamento().equals(rigaDaCancellare.getPg_accertamento()) &&
                        row.getPg_accertamento_scadenzario().equals(rigaDaCancellare.getPg_accertamento_scadenzario()) &&
                        row.isFlCancellazione()) {
                    pg_fattura_attiva = row.getPg_doc_amm();
                    break;
                }

            }
            //se la fattura attiva non e' stata selezionata per essere cancellata segnalo l'errore
            if (row != null && !getSelection().isSelected(index))
                throw new ValidationException("Cancellazione impossibile: e' necessario rimuovere la fattura attiva " + pg_fattura_attiva + " da cui " +
                        "il documento amministrativo " + ((Reversale_rigaIBulk) detail).getPg_doc_amm() + " dipende");
        }
    }

    /**
     * Metodo per aggiungere alla toolbar del Controller un tasto necessario all'utente
     * per la ricerca rapida della riga da quadrare con il SIOPE
     *
     * @param context  Il contesto dell'azione
     * @param scadenza La scadenza dell'oggetto bulk in uso
     */
    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

        super.writeHTMLToolbar(context, reset, find, delete, false);

        if (getParentController() instanceof CRUDReversaleBP) {
            CRUDReversaleBP bp = (CRUDReversaleBP) getParentController();
            boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);
            if (bp.isSiope_attiva() && !((ReversaleBulk) getParentModel()).isSiopeTotalmenteAssociato()) {
                String command = "javascript:submitForm('doSelezionaRigaSiopeDaCompletare')";
                it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                        context,
                        isFromBootstrap ? "fa fa-fw fa-arrow-down" : "img/find16.gif",
                        !(isInputReadonly() || getDetails().isEmpty() || ((CRUDReversaleBP) getParentController()).isSearching()) ? command : null,
                        true,
                        "SIOPE - Vai a riga successiva da completare",
                        "btn-sm btn-outline-primary",
                        isFromBootstrap);
            }
            if (bp.isDaVariare()) {
                it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                        context,
                        isFromBootstrap ? "fa fa-fw fa-bolt" : "img/history16.gif",
                        "javascript:submitForm('doRicercaAccertamento')",
                        true,
                        "Cambia accertamento",
                        "btn-sm btn-outline-primary btn-title",
                        isFromBootstrap);
            }
        }
        super.closeButtonGROUPToolbar(context);
    }
}
