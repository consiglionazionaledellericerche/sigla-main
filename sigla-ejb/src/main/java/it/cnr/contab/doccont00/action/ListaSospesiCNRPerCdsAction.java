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

import it.cnr.contab.doccont00.bp.ListaSospesiCNRPerCdsBP;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * Insert the type's description here.
 * Creation date: (21/03/2003 10.02.11)
 *
 * @author: Simonetta Costa
 */
public class ListaSospesiCNRPerCdsAction extends it.cnr.jada.util.action.BulkAction {
    /**
     * ListaSospesiCNRPercds constructor comment.
     */
    public ListaSospesiCNRPerCdsAction() {
        super();
    }

    public it.cnr.jada.action.Forward doCercaSospesiCNR(it.cnr.jada.action.ActionContext context) {
        try {
            fillModel(context);
            ListaSospesiCNRPerCdsBP bp = (ListaSospesiCNRPerCdsBP) context.getBusinessProcess();
            OggettoBulk model = bp.getModel();
            model.validate();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, model);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
            } else {
                final BulkInfo bulkInfo = BulkInfo.getBulkInfo(SospesoBulk.class);
                SelezionatoreListaBP nbp = (SelezionatoreListaBP) context.createBusinessProcess("ListaSospesiCNRPerCdsSelezionatoreBP");
                nbp.setIterator(context, ri);
                nbp.setBulkInfo(bulkInfo);
                nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary("SospesiRiaccredito"));
                return context.addBusinessProcess(nbp);
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
}
