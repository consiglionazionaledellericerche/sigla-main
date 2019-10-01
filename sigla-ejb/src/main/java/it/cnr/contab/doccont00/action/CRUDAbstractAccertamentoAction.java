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

import it.cnr.contab.doccont00.bp.CRUDAccertamentoBP;
import it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (03/07/2003 16.14.44)
 *
 * @author: Simonetta Costa
 */
public abstract class CRUDAbstractAccertamentoAction extends it.cnr.jada.util.action.CRUDAction {
    /**
     * CRUDAbstractObbligazioneAction constructor comment.
     */
    public CRUDAbstractAccertamentoAction() {
        super();
    }

    public Forward doRiportaAvanti(ActionContext context) throws java.rmi.RemoteException {

        CRUDVirtualAccertamentoBP bp = (CRUDVirtualAccertamentoBP) context.getBusinessProcess();

        try {
            fillModel(context);
            bp.riportaAvanti(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doRiportaIndietro(ActionContext context) throws java.rmi.RemoteException {

        CRUDVirtualAccertamentoBP bp = (CRUDVirtualAccertamentoBP) context.getBusinessProcess();

        try {
            fillModel(context);
            bp.riportaIndietro(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     *
     * @param context	L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doValidaDataRegistrazione(ActionContext context) {
        CRUDVirtualAccertamentoBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDVirtualAccertamentoBP.class::isInstance)
                .map(CRUDVirtualAccertamentoBP.class::cast)
                .orElse(null);
        if (Optional.ofNullable(bp).isPresent()) {
            AccertamentoBulk accertamento = Optional.ofNullable(bp.getModel())
                    .filter(AccertamentoBulk.class::isInstance)
                    .map(AccertamentoBulk.class::cast)
                    .orElse(null);
            if (Optional.ofNullable(accertamento).isPresent()) {
                try {
                    java.sql.Timestamp dataVecchia = accertamento.getDt_registrazione();
                    fillModel(context);
                    if(accertamento.getDt_registrazione() == null)
                        return context.findDefaultForward();
                    if(!accertamento.validaDataRegistrazione()) {
                        accertamento.setDt_registrazione(dataVecchia);
                        setMessage(context,0, "Data non valida! Esistono delle scadenze con data anteriore a quella dell'accertamento.");
                    }
                    return context.findDefaultForward();
                } catch(Throwable e){
                    return handleException(context,e);
                }
            }
        }
        return context.findDefaultForward();
    }
}
