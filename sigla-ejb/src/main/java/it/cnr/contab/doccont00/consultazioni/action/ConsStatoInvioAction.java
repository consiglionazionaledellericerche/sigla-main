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

package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.doccont00.bp.AbstractFirmaDigitaleDocContBP;
import it.cnr.contab.doccont00.bp.AllegatiDocContBP;
import it.cnr.contab.doccont00.consultazioni.bp.ConsStatoInvioBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SelezionatoreListaAction;

import java.util.List;

public class ConsStatoInvioAction extends SelezionatoreListaAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConsStatoInvioAction() {
        super();
    }

    @Override
    protected Forward perform(ActionContext actioncontext, String s) {
        Forward forward = super.perform(actioncontext, s);
        BusinessProcess bp = actioncontext.getBusinessProcess();
        try {
            if (bp.getName().equals("ConsStatoInvioMandatiBP") || bp.getName().equals("ConsStatoInvioReversaliBP"))
                ((ConsStatoInvioBP) bp).setContabiliEnabled(actioncontext);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
        return forward;
    }

    @Override
    public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
        return actioncontext.findDefaultForward();
    }


    @SuppressWarnings("unchecked")
    public Forward doAllegati(ActionContext context) {
        ConsStatoInvioBP bp = (ConsStatoInvioBP)context.getBusinessProcess();
        OggettoBulk bulk = bp.getModel();
        V_cons_stato_invio vConsStatoInvio = ((V_cons_stato_invio)bulk);
        try {
            fillModel(context);
            bp.setModel(context, bulk);
            bp.setSelection(context);
            List<V_cons_stato_invio> selectedElements = bp.getSelectedElements(context);
            if (selectedElements == null || selectedElements.isEmpty() || selectedElements.size() > 1)
                throw new ApplicationException("Selezionare solo un elemento!");

            V_cons_stato_invio statoInvio = (V_cons_stato_invio) bp.getSelectedElements(context).get(0);
            V_mandato_reversaleBulk v_mandato_reversaleBulk = new V_mandato_reversaleBulk();
            v_mandato_reversaleBulk.setEsercizio(statoInvio.getEsercizio().intValue());
            v_mandato_reversaleBulk.setCd_tipo_documento_cont(statoInvio.getTipo());
            v_mandato_reversaleBulk.setCd_cds(statoInvio.getCd_cds());
            v_mandato_reversaleBulk.setPg_documento_cont(statoInvio.getProgressivo());

            AllegatiDocContBP allegatiDocContBP = (AllegatiDocContBP) context.createBusinessProcess("AllegatiMandatoBP", new Object[] {"V"});
            allegatiDocContBP.setAllegatiFormName("altro");
            allegatiDocContBP.setModel(context, allegatiDocContBP.initializeModelForEdit(context, (OggettoBulk) v_mandato_reversaleBulk));
            allegatiDocContBP.setStatus(AllegatiDocContBP.VIEW);
            return context.addBusinessProcess(allegatiDocContBP);
        } catch(Exception e) {
            return handleException(context,e);
        }
    }

}
