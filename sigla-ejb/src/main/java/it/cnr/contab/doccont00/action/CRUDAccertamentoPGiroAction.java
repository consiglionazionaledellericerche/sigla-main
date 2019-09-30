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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Azione che gestisce le richieste relative alla Gestione dell'
 * Accertamento Partita di Giro.
 */
public class CRUDAccertamentoPGiroAction extends CRUDAbstractAccertamentoAction {
    public CRUDAccertamentoPGiroAction() {
        super();
    }

    public Forward doBlankSearchFind_debitore(ActionContext context, AccertamentoBulk accertamento) {
        try {
            accertamento.setDebitore(new TerzoBulk());
            accertamento.getDebitore().setAnagrafico(new AnagraficoBulk());

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la validazione di nuovo terzo creato
     *
     * @param context      <code>ActionContext</code> in uso.
     * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code>
     * @param terzo        Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
     * @return <code>Forward</code>
     */
    public Forward doBringBackCRUDCrea_debitore(ActionContext context, AccertamentoBulk accertamento, TerzoBulk terzo) {
        try {
            if (terzo != null) {
                accertamento.validateTerzo(terzo);
                accertamento.setDebitore(terzo);
            }
            return context.findDefaultForward();
        } catch (ValidationException e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione di un'unità organizzativa
     */
    public Forward doCambiaUnitaOrganizzativa(ActionContext context) {
        try {
            fillModel(context);
            SimpleCRUDBP bp = (SimpleCRUDBP) getBusinessProcess(context);
            AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk) bp.getModel();
            accert_pgiro.setCapitolo(new it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);

            CRUDBP bp = getBusinessProcess(context);
            if (!bp.isEditing()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                bp.delete(context);
                bp.setMessage("Annullamento effettuato");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}
